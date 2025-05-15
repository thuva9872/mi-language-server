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

package org.eclipse.lemminx.customservice.synapse.dependency.tree;

import org.eclipse.lemminx.customservice.synapse.dependency.tree.pojo.Dependency;

import java.util.HashMap;
import java.util.Map;

public class DependencyLookUp {

    private final Map<String, Dependency> dependencyMap;

    public DependencyLookUp() {

        dependencyMap = new HashMap<>();
    }

    public void addDependency(String path, Dependency dependency) {

        if (path != null && dependency != null) {
            dependencyMap.put(path, dependency);
        }
    }

    public Dependency getDependency(String path) {

        return dependencyMap.get(path);
    }
}
