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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.customservice.synapse.dataService.QueryGenRequestParams;
import org.eclipse.lemminx.customservice.synapse.db.DBConnectionTester;

public class DynamicFieldsHandler {
    private final DatabaseService databaseService;
    private static final Logger log = Logger.getLogger(DynamicFieldsHandler.class.getName());

    private static final String DB_CONNECTOR = "dbconnector";
    private static final String FIELD_TABLE = "table";
    private static final String FIELD_COLUMNS = "columns";
    private static final String PARAM_DB_URL = "dbUrl";
    private static final String PARAM_DB_USER = "dbUser";
    private static final String PARAM_DB_PASSWORD = "dbPassword";
    private static final String PARAM_DRIVER_CLASS = "driverClass";

    private static final String OP_SELECT = "select";
    private static final String OP_DELETE = "delete";
    private static final String OP_QUERY = "query";
    private static final String OP_UPDATE = "update";
    private static final String OP_INSERT = "insert";
    private static final String OP_EXECUTE_QUERY = "executeQuery";
    private static final String OP_CALL = "call";
    private static final String OP_STORED_PROCEDURE = "storedProcedure";

    public DynamicFieldsHandler() {
        this.databaseService = new DatabaseService();
    }

    public GetDynamicFieldsResponse handleDynamicFieldsRequest(GetDynamicFieldsRequest request) {
        GetDynamicFieldsResponse response = new GetDynamicFieldsResponse();
        Map<String, List<DynamicField>> fields = new HashMap<>();
        response.setFields(fields);

        if (DB_CONNECTOR.equals(request.getConnectorName())) {
            log.log(Level.INFO, "Handling dynamic fields request for database connector");

            String url = getParameterValue(request, PARAM_DB_URL);
            String username = getParameterValue(request, PARAM_DB_USER);
            String password = getParameterValue(request, PARAM_DB_PASSWORD);
            String className = getParameterValue(request, PARAM_DRIVER_CLASS);
            String operationName = request.getOperationName();
            String fieldName = request.getFieldName();
            String selectedValue = request.getSelectedValue();

            if (url == null || username == null || password == null) {
                log.log(Level.WARNING, "Missing essential database connection parameters (URL, User, Password).");
                return response;
            }

            try {
                Connection connection = DBConnectionTester.getConnection(url, username, password, className);

                if (connection == null) {
                    log.log(Level.SEVERE, "Failed to establish database connection.");
                    return response;
                }

                if (FIELD_TABLE.equals(fieldName) && selectedValue != null) {
                    List<DynamicField> dynamicData = null;
                    switch (operationName) {
                        case OP_SELECT:
                        case OP_DELETE:
                        case OP_QUERY:
                        case OP_UPDATE:
                        case OP_INSERT:
                        case OP_EXECUTE_QUERY:
                            boolean markNull = !(OP_SELECT.equals(operationName) || OP_DELETE.equals(operationName));
                            dynamicData = databaseService.getTableColumns(url, username, password, selectedValue,
                                    fieldName, markNull);
                            break;
                        case OP_CALL:
                        case OP_STORED_PROCEDURE:
                            dynamicData = databaseService.getStoredProcedureParameters(url, username, password,
                                    selectedValue, fieldName);
                            break;
                        default:
                            log.log(Level.INFO, "Operation not supported for dynamic fields: " + operationName);
                            break;
                    }
                    if (dynamicData != null) {
                        fields.put(FIELD_COLUMNS, dynamicData);
                    }
                } else {
                    if (!FIELD_TABLE.equals(fieldName)) {
                        log.log(Level.FINE, "Dynamic fields requested for field other than 'table': " + fieldName);
                    }
                    if (selectedValue == null && FIELD_TABLE.equals(fieldName)) {
                        log.log(Level.WARNING, "Selected value (table/procedure name) is null for field 'table'.");
                    }
                }

            } catch (Exception e) {
                log.log(Level.SEVERE, "Error processing dynamic fields request", e);
            }

        } else {
            log.log(Level.FINE, "Request is not for the database connector: " + request.getConnectorName());
        }

        return response;
    }

    public List<String> getStoredProcedures(QueryGenRequestParams requestParams) {
        List<String> procedures = new ArrayList<>();
        String url = requestParams.getUrl();
        String username = requestParams.getUsername();
        String password = requestParams.getPassword();

        if (url != null && username != null && password != null) {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                DatabaseMetaData metaData = conn.getMetaData();
                try (ResultSet rs = metaData.getProcedures(null, null, null)) {
                    while (rs.next()) {
                        String procedureName = rs.getString("PROCEDURE_NAME");
                        procedures.add(procedureName);
                    }
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Error retrieving stored procedures", e);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error establishing connection for retrieving stored procedures", e);
            }
        } else {
            log.log(Level.WARNING, "Missing connection parameters for retrieving stored procedures.");
        }
        return procedures;
    }

    private String getParameterValue(GetDynamicFieldsRequest request, String paramName) {
        if (request.getConnection() != null && request.getConnection().getParameters() != null) {
            return request.getConnection().getParameters().stream()
                    .filter(param -> paramName.equals(param.getName()))
                    .findFirst()
                    .map(param -> param.getValue())
                    .orElse(null);
        }
        return null;
    }
}