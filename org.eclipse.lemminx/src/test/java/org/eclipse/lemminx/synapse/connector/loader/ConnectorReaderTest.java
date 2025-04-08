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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorReader;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.Connector;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.ConnectorAction;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectorReaderTest {

    @Test
    public void testValidConnector() {

        String connectorPath = Objects.requireNonNull(
                this.getClass().getResource("/synapse/connectors/mi-connector-http-0.1.8")).getPath();
        ConnectorReader connectorReader = new ConnectorReader();
        Connector connector = connectorReader.readConnector(connectorPath, StringUtils.EMPTY);
        assertNotNull(connector);
        assertEquals("http", connector.getName());
        assertEquals("0.1.8", connector.getVersion());
        assertEquals(8, connector.getActions().size());
        assertEquals("HTTP", connector.getDisplayName());
        assertEquals(2, connector.getConnectionUiSchema().size());
        assertOperation(connector.getAction("get"));

    }

    private void assertOperation(ConnectorAction get) {

        assertNotNull(get);
        assertEquals("get", get.getName());
        assertEquals("GET", get.getDisplayName());
        assertEquals("http.get", get.getTag());
        assertEquals(19, get.getParameters().size());
        assertEquals(2, get.getAllowedConnectionTypes().size());
        assertTrue(get.getAllowedConnectionTypes().contains("HTTP"));
        assertTrue(get.getAllowedConnectionTypes().contains("HTTPS"));
    }

    @Test
    public void testInvalidConnector() {

        String connectorPath = Objects.requireNonNull(
                this.getClass().getResource("/synapse/connectors/invalid-connector-0.1.0")).getPath();
        ConnectorReader connectorReader = new ConnectorReader();
        Connector connector = connectorReader.readConnector(connectorPath, StringUtils.EMPTY);
        assertNull(connector);
    }
}
