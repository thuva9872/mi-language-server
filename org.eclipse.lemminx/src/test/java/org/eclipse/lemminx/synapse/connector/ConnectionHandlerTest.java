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

import com.google.gson.JsonObject;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectionHandler;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorHolder;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorReader;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.Connector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionHandlerTest {

    ConnectionHandler connectionHandler;

    @BeforeAll
    public void setUp() {

        connectionHandler = new ConnectionHandler();
        String connectorPath = Objects.requireNonNull(
                this.getClass().getResource("/synapse/connectors/extracted/mi-connector-http-0.1.8")).getPath();
        ConnectorReader connectorReader = new ConnectorReader();
        Connector connection =
                connectorReader.readConnector(connectorPath, null);
        ConnectorHolder.getInstance().addConnector(connection);
        connectionHandler.init(ConnectorHolder.getInstance());
    }

    @Test
    public void testGetConnectionSchemaFromValidName() throws IOException {

        JsonObject connection1Schema = connectionHandler.getConnectionUiSchema("http", "http");
        JsonObject connection2Schema = connectionHandler.getConnectionUiSchema("http", "https");

        assertValidConnectionSchema(connection1Schema, "HTTP");
        assertValidConnectionSchema(connection2Schema, "HTTPS");
    }

    @Test
    public void testGetConnectionSchemaFromInvalidName() throws IOException {

        JsonObject connectionSchema = connectionHandler.getConnectionUiSchema("invalid", "invalid");
        assertNull(connectionSchema);
    }

    @Test
    public void testGetConnectionSchemaForValidConnectionFile() throws IOException {

        String connectionPath = Objects.requireNonNull(this.getClass().getResource("/synapse/connector.test/" +
                        "test_project/src/main/wso2mi/artifacts/local-entries/HttpsCon.xml"))
                .getPath();
        JsonObject connectionSchema = connectionHandler.getConnectionUiSchema(connectionPath);
        assertValidConnectionSchema(connectionSchema, "HTTPS");
    }

    @Test
    public void testGetConnectionSchemaForInvalidConnectionFile() throws IOException {

        String connectionPath = Objects.requireNonNull(this.getClass().getResource("/synapse/connector.test/" +
                        "test_project/src/main/wso2mi/artifacts/local-entries/testLocalEntry.xml"))
                .getPath();
        JsonObject connectionSchema = connectionHandler.getConnectionUiSchema(connectionPath);
        assertNull(connectionSchema);
    }

    @Test
    public void testGetConnectionSchemaForMissingConnector() throws IOException {

        String connectionPath = Objects.requireNonNull(this.getClass().getResource("/synapse/connector.test/" +
                        "test_project/src/main/wso2mi/artifacts/local-entries/invalidConnectorConnection.xml"))
                .getPath();
        JsonObject connectionSchema = connectionHandler.getConnectionUiSchema(connectionPath);
        assertNull(connectionSchema);
    }

    @Test
    public void testGetConnectionSchemaForWrongConnectionType() throws IOException {

        String connectionPath = Objects.requireNonNull(this.getClass().getResource("/synapse/connector.test/" +
                "test_project/src/main/wso2mi/artifacts/local-entries/invalidConnectionType.xml")).getPath();
        JsonObject connectionSchema = connectionHandler.getConnectionUiSchema(connectionPath);
        assertNull(connectionSchema);
    }

    private void assertValidConnectionSchema(JsonObject connectionSchema, String expectedName) {

        assertNotNull(connectionSchema);
        assertNotNull(connectionSchema.get("connectionName"));
        assertEquals(expectedName, connectionSchema.get("connectionName").getAsString());
    }
}
