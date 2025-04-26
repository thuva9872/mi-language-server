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
package org.eclipse.lemminx.customservice.synapse.driver;

import org.eclipse.lemminx.customservice.synapse.dataService.DynamicClassLoader;
import org.eclipse.lemminx.customservice.synapse.utils.Constant;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for loading JDBC drivers into the class loader.
 */
public class DriverLoader {

    private static final Logger log = Logger.getLogger(DriverLoader.class.getName());

    /**
     * Loads all temporary driver JARs found in the project's driver directory into
     * the class loader.
     *
     * @param projectUri The URI of the project.
     */
    public static void loadTempDrivers(String projectUri) {
        try {
            String projectId = new File(projectUri).getName() + "_" + Utils.getHash(projectUri);
            File driversDirectory = Path.of(System.getProperty(Constant.USER_HOME), Constant.WSO2_MI,
                    Constant.CONNECTORS, projectId, Constant.DRIVERS).toFile();
            if (driversDirectory.exists()) {
                DynamicClassLoader.updateClassLoader(new File(driversDirectory.getAbsolutePath()));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while updating class loader for DB drivers.", e);
        }
    }

    /**
     * Loads a specific driver JAR file into the class loader.
     *
     * @param driverJarPath The absolute path to the driver JAR file.
     */
    public static void loadDriverJar(String driverJarPath) {
        try {
            File driverJarFile = Path.of(driverJarPath).toFile();
            if (driverJarFile.exists() && driverJarFile.isFile()) {
                DynamicClassLoader.updateJarInClassLoader(driverJarFile, true);
            } else {
                log.log(Level.WARNING, "Driver JAR file not found or is not a file: " + driverJarPath);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while updating class loader for DB driver: " + driverJarPath, e);
        }
    }
}
