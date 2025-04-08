/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.eclipse.lemminx.synapse.connector.loader;

import org.eclipse.lemminx.customservice.SynapseLanguageClientAPI;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorHolder;
import org.eclipse.lemminx.customservice.synapse.connectors.NewProjectConnectorLoader;
import org.eclipse.lemminx.customservice.synapse.inbound.conector.InboundConnectorHolder;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class MockConnectorLoader extends NewProjectConnectorLoader {

    private Path tempPath;

    public MockConnectorLoader(SynapseLanguageClientAPI languageClient, ConnectorHolder connectorHolder,
                               InboundConnectorHolder inboundConnectorHolder, Path testPath) {

        super(languageClient, connectorHolder, inboundConnectorHolder);
        this.tempPath = testPath;
    }

    @Override
    protected void copyToProjectIfNeeded(List<File> connectorZips) {

        // This method is intentionally left empty for testing purposes
    }

    @Override
    protected File getConnectorExtractFolder() {

        File file = tempPath.resolve("extracted").toFile();
        System.out.println("MockConnectorLoader getConnectorExtractFolder: " + file.getAbsolutePath());
        return file;
    }

    @Override
    protected void setConnectorsZipFolderPath(String projectRoot) {

        String connectorZipPath = tempPath.resolve("connectors").toString();
        connectorsZipFolderPath.add(connectorZipPath);
    }
}
