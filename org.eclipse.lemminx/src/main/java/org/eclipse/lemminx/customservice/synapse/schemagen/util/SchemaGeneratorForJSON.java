/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     WSO2 LLC - support for WSO2 Micro Integrator Configuration
 */

package org.eclipse.lemminx.customservice.synapse.schemagen.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SchemaGeneratorForJSON extends AbstractSchemaGenerator implements ISchemaGenerator {

	@Override
	public String getSchemaResourcePath(String filePath, FileType type, String delimiter) throws IOException {
		String entireFileText = FileUtils.readFileToString(new File(filePath));
		return  getSchemaContent(entireFileText, type, null);
	}

	@Override
	public String getSchemaContent(String fileText, FileType type, String delimiter) throws IOException {
		SchemaBuilderWithNamepaces sb = new SchemaBuilderWithNamepaces();
		String jsonSchema = sb.createSchema(fileText, type);
		return  jsonSchema;
	}

}
