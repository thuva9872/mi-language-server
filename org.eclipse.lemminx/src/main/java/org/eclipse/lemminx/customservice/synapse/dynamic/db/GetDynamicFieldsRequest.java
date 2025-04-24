/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.eclipse.lemminx.customservice.synapse.dynamic.db;

import java.util.List;

public class GetDynamicFieldsRequest {
    private String connectorName;
    private String operationName;
    private String fieldName;
    private String selectedValue;
    private Connection connection;

    public static class Connection {
        private String name;
        private String path;
        private String connectionType;
        private List<Parameter> parameters;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getConnectionType() { return connectionType; }
        public void setConnectionType(String connectionType) { this.connectionType = connectionType; }
        public List<Parameter> getParameters() { return parameters; }
        public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
    }

    public static class Parameter {
        private String name;
        private boolean isExpression;
        private String value;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isExpression() { return isExpression; }
        public void setExpression(boolean expression) { isExpression = expression; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    public String getConnectorName() { return connectorName; }
    public void setConnectorName(String connectorName) { this.connectorName = connectorName; }
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getSelectedValue() { return selectedValue; }
    public void setSelectedValue(String selectedValue) { this.selectedValue = selectedValue; }
    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }
}