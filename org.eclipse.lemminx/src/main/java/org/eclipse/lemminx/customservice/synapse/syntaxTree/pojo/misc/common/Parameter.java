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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.misc.common;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;

public class Parameter extends STNode {

    Object otherAttributes;
    String content;
    String name;
    String key;
    boolean locked;

    public Object getOtherAttributes() {

        return otherAttributes;
    }

    public void setOtherAttributes(Object otherAttributes) {

        this.otherAttributes = otherAttributes;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public boolean isLocked() {

        return locked;
    }

    public void setLocked(boolean locked) {

        this.locked = locked;
    }
}
