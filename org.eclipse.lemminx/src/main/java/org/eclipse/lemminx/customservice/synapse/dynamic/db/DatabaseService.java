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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.lemminx.utils.StringUtils;

public class DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());

    private static final String FIELD_TYPE_ATTRIBUTE = "attribute";
    private static final String DYN_PARAM_PREFIX = "dyn_param_";
    private static final String IS_NULLABLE_COLUMN = "IS_NULLABLE";
    private static final String NULLABLE_NO = "NO";
    private static final String BOOLEAN_TRUE = "true";
    private static final String BOOLEAN_FALSE = "false";
    private static final String COLUMN_NAME_COLUMN = "COLUMN_NAME";
    private static final String TYPE_NAME_COLUMN = "TYPE_NAME";

    /**
     * @param connectionUrl
     * @param username
     * @param password
     * @param table
     * @param fieldName
     * @param markNull
     * @return List of DynamicField objects representing the columns of the table
     */
    public List<DynamicField> getTableColumns(String connectionUrl, String username, String password, String table,
            String fieldName, boolean markNull) {
        List<DynamicField> fields = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet columns = metaData.getColumns(null, null, table, null)) {
                while (columns.next()) {
                    DynamicField field = new DynamicField();
                    DynamicFieldValue value = new DynamicFieldValue();

                    String columnName = columns.getString(COLUMN_NAME_COLUMN);
                    String dataType = columns.getString(TYPE_NAME_COLUMN);
                    String inputType = mapSqlTypeToInputType(dataType);
                    String xmlSafeColumnName = toXmlSafeName(columnName);

                    field.setType(FIELD_TYPE_ATTRIBUTE);
                    value.setName(DYN_PARAM_PREFIX + fieldName + "_" + dataType + "_" + xmlSafeColumnName);
                    value.setDisplayName(columnName);
                    value.setInputType(inputType);

                    // Determine if the column is required
                    boolean isRequiredBasedOnDb = columns.getString(IS_NULLABLE_COLUMN).equals(NULLABLE_NO);
                    value.setRequired(markNull && isRequiredBasedOnDb ? BOOLEAN_TRUE : BOOLEAN_FALSE);

                    value.setHelpTip("Column type: " + dataType);
                    value.setPlaceholder("Enter " + columnName);
                    value.setDefaultValue("");

                    field.setValue(value);
                    fields.add(field);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting table columns for table: " + table, e);
        }

        return fields;
    }

    // getStoredProcedureParameters
    public List<DynamicField> getStoredProcedureParameters(String connectionUrl, String username, String password,
            String procedureName, String fieldName) {
        List<DynamicField> fields = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet parameters = metaData.getProcedureColumns(null, null, procedureName, null)) {
                while (parameters.next()) {
                    // Skip return value parameter if present (often the first one without a name)
                    String parameterName = parameters.getString(COLUMN_NAME_COLUMN);
                    if (StringUtils.isEmpty(parameterName)) {
                        continue;
                    }

                    DynamicField field = new DynamicField();
                    DynamicFieldValue value = new DynamicFieldValue();

                    String dataType = parameters.getString(TYPE_NAME_COLUMN);
                    String inputType = mapSqlTypeToInputType(dataType);
                    String xmlSafeParameterName = toXmlSafeName(parameterName);

                    field.setType(FIELD_TYPE_ATTRIBUTE);
                    value.setName(DYN_PARAM_PREFIX + fieldName + "_" + dataType + "_" + xmlSafeParameterName);
                    value.setDisplayName(parameterName);
                    value.setInputType(inputType);

                    value.setRequired(
                            parameters.getInt("COLUMN_TYPE") == DatabaseMetaData.procedureColumnIn ? BOOLEAN_TRUE
                                    : BOOLEAN_FALSE);
                    value.setHelpTip("Parameter type: " + dataType);
                    value.setPlaceholder("Enter " + parameterName);
                    value.setDefaultValue("");

                    field.setValue(value);
                    fields.add(field);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting stored procedure parameters for procedure: " + procedureName, e);
        }

        return fields;
    }

    /**
     * Converts a database identifier (column/parameter name) to a safe
     * string suitable for XML tag names.
     *
     * @param name The original database identifier.
     * @return An XML-safe version of the name.
     */
    private String toXmlSafeName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }

        return name.replaceAll("[^a-zA-Z0-9]", "");
    }

    private String mapSqlTypeToInputType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
                return "stringOrExpression";
            case "INT":
            case "BIGINT":
            case "DECIMAL":
            case "NUMERIC":
                return "stringOrExpression";
            case "DATE":
                return "stringOrExpression";
            case "BOOLEAN":
                return "stringOrExpression";
            default:
                return "stringOrExpression";
        }
    }
}
