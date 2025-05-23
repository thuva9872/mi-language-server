/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     WSO2 LLC - support for WSO2 Micro Integrator Configuration
 */

package org.eclipse.lemminx.synapse.connector.downloader;

import org.eclipse.lemminx.customservice.synapse.parser.ConnectorDownloadManager;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

public class ConnectorDownloadManagerTest {

    private ConnectorDownloadManager connectorDownloadManager;
    private static MockedStatic<Utils> utilsMock;

    @BeforeEach
    void setUp() {
        connectorDownloadManager = new ConnectorDownloadManager();
        utilsMock = mockStatic(Utils.class);
    }

    @Test
    void downloadConnectorsWithValidDependencies() {
        String path = ConnectorDownloadManagerTest.class.getResource("/synapse/pom.parser/test_pom_parser").getPath();
        String projectPath = new File(path).getAbsolutePath();
        utilsMock.when(() -> Utils.downloadConnector(any(), any(), any(), any(), any())).thenAnswer(invocationOnMock -> { return null; });
        String result = connectorDownloadManager.downloadConnectors(projectPath);
        utilsMock.close();

        assertEquals("Success", result);
    }

    @Test
    void downloadConnectorsWithInvalidDependencies() {
        String path = ConnectorDownloadManagerTest.class.getResource("/synapse/pom.parser/test_pom_parser").getPath();
        String projectPath = new File(path).getAbsolutePath();
        utilsMock.when(() -> Utils.downloadConnector(any(), any(), any(), any(), any())).thenThrow(new IOException());
        String result = connectorDownloadManager.downloadConnectors(projectPath);
        utilsMock.close();

        assertTrue(result.contains("Some connectors were not downloaded:"));
    }
}
