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

package org.eclipse.lemminx.synapse;

import org.eclipse.lemminx.customservice.synapse.utils.Constant;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestUtils {

    public static String getResourceFilePath(String resourcePath) throws URISyntaxException {

        URL url = Objects.requireNonNull(TestUtils.class.getResource(resourcePath));
        return Paths.get(url.toURI()).toAbsolutePath().toString();
    }

    public static void extractConnectorZips(Path extractFolder, String resourcePath) throws Exception {

        String connectorZipFolder = getResourceFilePath(resourcePath);
        File connectorZipFolderFile = new File(connectorZipFolder);
        File[] connectorZipFiles = connectorZipFolderFile.listFiles();
        assert connectorZipFiles != null;
        for (File zip : connectorZipFiles) {
            String zipName = zip.getName();
            zipName = zipName.substring(0, zipName.lastIndexOf(Constant.DOT));
            Utils.extractZip(zip, extractFolder.resolve(zipName).toFile());
        }
    }
}
