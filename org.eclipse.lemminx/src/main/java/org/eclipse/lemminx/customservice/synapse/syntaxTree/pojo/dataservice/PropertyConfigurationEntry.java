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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.dataservice;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;

public class PropertyConfigurationEntry extends STNode {

    STNode username;
    STNode password;
    String request;

    public STNode getUsername() {

        return username;
    }

    public void setUsername(STNode username) {

        this.username = username;
    }

    public STNode getPassword() {

        return password;
    }

    public void setPassword(STNode password) {

        this.password = password;
    }

    public String getRequest() {

        return request;
    }

    public void setRequest(String request) {

        this.request = request;
    }
}
