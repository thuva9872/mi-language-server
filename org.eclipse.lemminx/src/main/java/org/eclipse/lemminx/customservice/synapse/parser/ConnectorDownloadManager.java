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

package org.eclipse.lemminx.customservice.synapse.parser;

import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorHolder;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.Connector;
import org.eclipse.lemminx.customservice.synapse.mediator.TryOutConstants;
import org.eclipse.lemminx.customservice.synapse.utils.Constant;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import static org.eclipse.lemminx.customservice.synapse.parser.pom.PomParser.getPomDetails;

public class ConnectorDownloadManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectorDownloadManager.class.getName());

    public static String downloadConnectors(String projectPath) {

        String projectId = new File(projectPath).getName() + "_" + Utils.getHash(projectPath);
        File directory = Path.of(System.getProperty(Constant.USER_HOME), Constant.WSO2_MI, Constant.CONNECTORS,
                projectId).toFile();
        File downloadDirectory = Path.of(directory.getAbsolutePath(), Constant.DOWNLOADED).toFile();
        File extractDirectory = Path.of(directory.getAbsolutePath(), Constant.EXTRACTED).toFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!extractDirectory.exists()) {
            extractDirectory.mkdirs();
        }
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }

        OverviewPageDetailsResponse pomDetailsResponse = new OverviewPageDetailsResponse();
        getPomDetails(projectPath, pomDetailsResponse);
        List<DependencyDetails> dependencies = pomDetailsResponse.getDependenciesDetails().getConnectorDependencies();
        deleteRemovedConnectors(downloadDirectory, dependencies, projectPath);
        List<String> failedDependencies = new ArrayList<>();
        for (DependencyDetails dependency : dependencies) {
            try {
                File connector = Path.of(downloadDirectory.getAbsolutePath(),
                        dependency.getArtifact() + "-" + dependency.getVersion() + Constant.ZIP_EXTENSION).toFile();
                File existingArtifact = null;
                if (connector.exists() && connector.isFile()) {
                    LOGGER.log(Level.INFO, "Dependency already downloaded: " + connector.getName());
                } else if ((existingArtifact = getDependencyFromLocalRepo(dependency.getGroupId(),
                        dependency.getArtifact(), dependency.getVersion())) != null ) {
                    LOGGER.log(Level.INFO, "Copying dependency from local repository: " + connector.getName());
                    copyFile(existingArtifact, downloadDirectory);
                } else {
                    LOGGER.log(Level.INFO, "Downloading dependency: " + connector.getName());
                    Utils.downloadConnector(dependency.getGroupId(), dependency.getArtifact(),
                            dependency.getVersion(), downloadDirectory);
                }
            } catch (Exception e) {
                String failedDependency = dependency.getGroupId() + "-" + dependency.getArtifact() + "-" + dependency.getVersion();
                LOGGER.log(Level.WARNING, "Error occurred while downloading dependency " + failedDependency + ": " + e.getMessage());
                failedDependencies.add(failedDependency);
            }
        }
        if (!failedDependencies.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Some connectors were not downloaded: " + String.join(", ", failedDependencies));
            return "Some connectors were not downloaded: " + String.join(", ", failedDependencies);
        }
        return "Success";
    }

    private static void deleteRemovedConnectors(File downloadDirectory, List<DependencyDetails> dependencies,
                                                String projectPath) {

        List<String> existingConnectors =
                dependencies.stream().map(dependency -> dependency.getArtifact() + "-" + dependency.getVersion())
                        .collect(Collectors.toList());
        File[] files = downloadDirectory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (isConnectorRemoved(file, existingConnectors)) {
                try {
                    Files.delete(file.toPath());
                    removeFromProjectIfUsingOldCARPlugin(projectPath, file.getName());
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error occurred while deleting removed connector: " + file.getName());
                }
            }
        }
    }

    private static void removeFromProjectIfUsingOldCARPlugin(String projectPath, String name) throws IOException {

        if (!Utils.isOlderCARPlugin(projectPath)) {
            return;
        }
        File connectorInProject =
                Path.of(projectPath).resolve(TryOutConstants.PROJECT_CONNECTOR_PATH).resolve(name).toFile();
        if (connectorInProject.exists()) {
            Files.delete(connectorInProject.toPath());
        }
    }

    private static boolean isConnectorRemoved(File file, List<String> existingConnectors) {

        return file.isFile() && !existingConnectors.contains(file.getName().replace(Constant.ZIP_EXTENSION, ""));
    }

    private static File getDependencyFromLocalRepo(String groupId, String artifactId, String version) {

        String localMavenRepo = Path.of(System.getProperty(Constant.USER_HOME),  Constant.M2,
                Constant.REPOSITORY).toString();
        String artifactPath = Path.of(localMavenRepo, groupId.replace(".", File.separator), artifactId,
                version, artifactId + "-" + version + Constant.ZIP_EXTENSION).toString();
        File artifactFile = new File(artifactPath);
        if(artifactFile.exists()) {
            LOGGER.log(Level.INFO, "Dependency found in the local repository: " + artifactId);
            return artifactFile;
        } else {
            LOGGER.log(Level.INFO, "Dependency not found in the local repository: " + artifactId);
            return null;
        }
    }

    private static void copyFile(File source, File destinationFolder) throws IOException {

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File destinationFile = Path.of(destinationFolder.getAbsolutePath(), source.getName()).toFile();
        try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred while copying dependency from local repository: " + e.getMessage());
            throw e;
        }
    }

     /**
      * Downloads the driver JAR for a specific connector and connection type by parsing the descriptor.yml file
      */
     public static String downloadDriverForConnector(String projectPath, String connectorName, String connectionType) {
         try {
 
             ConnectorHolder connectorHolder;
             connectorHolder = ConnectorHolder.getInstance();
             Connector connector = connectorHolder.getConnector(connectorName);
 
             String projectId = new File(projectPath).getName() + "_" + Utils.getHash(projectPath);
             File connectorsDirectory = Path.of(System.getProperty(Constant.USER_HOME), Constant.WSO2_MI,
                     Constant.CONNECTORS, projectId, Constant.EXTRACTED).toFile();
 
             if (!connectorsDirectory.exists() || !connectorsDirectory.isDirectory()) {
                 LOGGER.log(Level.SEVERE,
                         "Connectors directory does not exist: " + connectorsDirectory.getAbsolutePath());
                 return null;
             }
 
             String connectorPath = connector.getExtractedConnectorPath();
             File connectorDirectory = Path.of(connectorPath).toFile();
             if (!connectorDirectory.exists() || !connectorDirectory.isDirectory()) {
                 LOGGER.log(Level.SEVERE, "Connector directory does not exist: " + connectorDirectory.getAbsolutePath());
                 return null;
             }
 
             // Read descriptor.yml from the connector folder
             File descriptorFile = new File(connectorDirectory, Constant.DESCRIPTOR_FILE);
             if (!descriptorFile.exists()) {
                 LOGGER.log(Level.SEVERE, "descriptor.yml not found in connector: " + connectorName);
                 return null;
             }
 
             Map<String, Object> descriptorData;
             try (InputStream inputStream = new FileInputStream(descriptorFile)) {
                 ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
                 descriptorData = yamlMapper.readValue(inputStream, Map.class);
             } catch (IOException e) {
                 LOGGER.log(Level.SEVERE, "Error reading descriptor.yml: " + e.getMessage());
                 return null;
             }
 
             // Find the driver info that matches the connection type
             Map<String, Object> driverInfo = findDriverForConnectionType(descriptorData, connectionType);
             if (driverInfo == null) {
                 LOGGER.log(Level.SEVERE, "No driver found for connection type: " + connectionType);
                 return null;
             }
 
             // Extract driver coordinates
             String groupId = (String) driverInfo.get(Constant.GROUP_ID_KEY);
             String artifactId = (String) driverInfo.get(Constant.ARTIFACT_ID_KEY);
             String version = (String) driverInfo.get(Constant.VERSION_KEY);
 
             if (groupId == null || artifactId == null || version == null) {
                 LOGGER.log(Level.SEVERE, "Invalid driver coordinates in descriptor");
                 return null;
             }
 
             // Create temp drivers directory if it doesn't exist
             File driversDirectory = Path.of(System.getProperty(Constant.USER_HOME), Constant.WSO2_MI,
                     Constant.CONNECTORS, projectId, Constant.DRIVERS).toFile();
             if (!driversDirectory.exists()) {
                 driversDirectory.mkdirs();
             }
 
             // Check if driver already exists
             File driverFile = new File(driversDirectory, artifactId + "-" + version + Constant.JAR_EXTENSION);
             if (driverFile.exists()) {
                 LOGGER.log(Level.INFO, "Driver already exists: " + driverFile.getAbsolutePath());
                 return driverFile.getAbsolutePath();
             }
 
             // Try to find the driver in local Maven repository first
             File localDriverFile = getDriverFromLocalRepo(groupId, artifactId, version);
             if (localDriverFile != null) {
                 copyFile(localDriverFile, driversDirectory);
                 return new File(driversDirectory, localDriverFile.getName()).getAbsolutePath();
             }
 
             // Download the driver from Maven repository
             String downloadedPath = downloadDriverFromMaven(groupId, artifactId, version, driversDirectory);
             return downloadedPath;

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException occurred while downloading driver: " + e.getMessage());
                return null;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while downloading driver: " + e.getMessage());
                return null;
            }
     }
 
     /**
      * Finds driver info for the specified connection type from the descriptor data
      */
     private static Map<String, Object> findDriverForConnectionType(Map<String, Object> descriptorData,
             String connectionType) {
         try {
 
             Object dependenciesObj = descriptorData.get("dependencies");
             if (dependenciesObj != null && dependenciesObj instanceof List) {
                 List<Map<String, Object>> dependencies = (List<Map<String, Object>>) dependenciesObj;
                 Map<String, Object> exactMatch = null;
 
                 for (Map<String, Object> dependency : dependencies) {
                     Object depConnType = dependency.get("connectionType");
                     if (depConnType != null && connectionType.equalsIgnoreCase(depConnType.toString())) {
                         exactMatch = dependency;
                         break;
                     }
                 }
 
                 if (exactMatch != null) {
                     return exactMatch;
                 }
             }
 
             LOGGER.log(Level.WARNING, "No driver found for connection type: " + connectionType);
             return null;
         } catch (Exception e) {
             LOGGER.log(Level.SEVERE, "Error finding driver for connection type: " + e.getMessage());
             return null;
         }
     }
 
     /**
      * Gets driver JAR from local Maven repository
      */
     private static File getDriverFromLocalRepo(String groupId, String artifactId, String version) {
         String localMavenRepo = Path.of(System.getProperty(Constant.USER_HOME), Constant.M2,
                 Constant.REPOSITORY).toString();
         String artifactPath = Path.of(localMavenRepo, groupId.replace(".", File.separator), artifactId,
                 version, artifactId + "-" + version + ".jar").toString();
         File artifactFile = new File(artifactPath);
 
         if (artifactFile.exists()) {
             LOGGER.log(Level.INFO, "Driver found in local repository: " + artifactId);
             return artifactFile;
         } else {
             LOGGER.log(Level.INFO, "Driver not found in local repository: " + artifactId);
             return null;
         }
     }
 
     /**
      * Downloads the driver JAR from Maven repository
     * @throws IOException 
      */
     private static String downloadDriverFromMaven(String groupId, String artifactId, String version,
             File targetDirectory) throws IOException {
         if (!targetDirectory.exists()) {
             targetDirectory.mkdirs();
         }

         String url = String.format("https://maven.wso2.org/nexus/content/groups/public/%s/%s/%s/%s-%s.jar",
                 groupId.replace(".", "/"), artifactId, version, artifactId, version);
         File targetFile = new File(targetDirectory, artifactId + "-" + version + Constant.JAR_EXTENSION);

         LOGGER.log(Level.INFO, "Downloading driver from: " + url);

         try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
             byte[] dataBuffer = new byte[1024];
             int bytesRead;
             while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                 fileOutputStream.write(dataBuffer, 0, bytesRead);
             }
             LOGGER.log(Level.INFO, "Driver downloaded successfully: " + targetFile.getAbsolutePath());
             return targetFile.getAbsolutePath();
         } catch (IOException e) {
             LOGGER.log(Level.SEVERE, "Error occurred while downloading driver: " + e.getMessage());
             throw e;
         }
     }
}
