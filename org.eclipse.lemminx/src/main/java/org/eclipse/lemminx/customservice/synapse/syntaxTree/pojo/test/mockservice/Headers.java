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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.test.mockservice;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;

import java.util.ArrayList;
import java.util.List;

public class Headers extends STNode {

    List<Header> headers;

    public Headers() {

        this.headers = new ArrayList<>();
    }

    public List<Header> getHeaders() {

        return headers;
    }

    public void setHeaders(List<Header> headers) {

        this.headers = headers;
    }

    public void addHeader(Header header) {

        headers.add(header);
    }
}
