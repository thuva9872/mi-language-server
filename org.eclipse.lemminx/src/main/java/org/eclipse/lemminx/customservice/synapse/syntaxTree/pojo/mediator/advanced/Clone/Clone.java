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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.advanced.Clone;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.Mediator;

public class Clone extends Mediator {

    CloneTarget[] target;
    String id;
    boolean continueParent;
    boolean sequential;
    String description;

    public Clone() {
        setDisplayName("Clone");
    }

    public CloneTarget[] getTarget() {

        return target;
    }

    public void setTarget(CloneTarget[] target) {

        this.target = target;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public boolean isContinueParent() {

        return continueParent;
    }

    public void setContinueParent(boolean continueParent) {

        this.continueParent = continueParent;
    }

    public boolean isSequential() {

        return sequential;
    }

    public void setSequential(boolean sequential) {

        this.sequential = sequential;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}
