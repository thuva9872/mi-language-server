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

package org.eclipse.lemminx.customservice.synapse.directoryTree.node;

import java.util.ArrayList;
import java.util.List;

public class Resource {

    private RegistryResource registry;
    private List<Node> connectors;
    private List<Node> metadata;
    private FolderNode newResources;

    public Resource() {

        registry = new RegistryResource();
        connectors = new ArrayList<>();
        metadata = new ArrayList<>();
    }

    public FolderNode getNewResources() {

        return newResources;
    }

    public void setNewResources(FolderNode newResources) {

        this.newResources = newResources;
    }

    public void addConnector(Node connector) {

        connectors.add(connector);
    }

    public void addMetadata(Node meta) {

        metadata.add(meta);
    }

    public RegistryResource getRegistry() {

        return registry;
    }
}
