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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.api;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;

public class APIHandlersHandler extends STNode {

    APIHandlersHandlerProperty[] property;
    String clazz;

    public APIHandlersHandlerProperty[] getProperty() {

        return property;
    }

    public void setProperty(APIHandlersHandlerProperty[] property) {

        this.property = property;
    }

    public String getClazz() {

        return clazz;
    }

    public void setClazz(String clazz) {

        this.clazz = clazz;
    }
}
