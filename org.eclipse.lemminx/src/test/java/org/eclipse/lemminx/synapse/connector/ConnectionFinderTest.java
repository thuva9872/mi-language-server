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

package org.eclipse.lemminx.synapse.connector;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectionFinder;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorHolder;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorReader;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.Connections;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.Connector;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Map;

import static org.eclipse.lemminx.synapse.TestUtils.getResourceFilePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionFinderTest {

    private ConnectorHolder connectorHolder;

    @BeforeEach
    public void setUp() throws URISyntaxException {

        connectorHolder = ConnectorHolder.getInstance();

        ConnectorReader connectorReader = new ConnectorReader();
        String connectorPath = getResourceFilePath("/synapse/connectors/extracted/mi-connector-http-0.1.8");
        Connector connector = connectorReader.readConnector(connectorPath, StringUtils.EMPTY);
        connectorHolder.addConnector(connector);
    }

    @Test
    public void testConnectorConnection() throws URISyntaxException {

        String projectPath = getResourceFilePath("/synapse/connector/test_project");
        Either<Connections, Map<String, Connections>> connectionsMapEither =
                ConnectionFinder.findConnections(projectPath, "http", connectorHolder, false);
        assertNotNull(connectionsMapEither);
        Connections connections = connectionsMapEither.getLeft();
        assertNotNull(connections);
        assertEquals(2, connections.getConnections().size());
    }
}
