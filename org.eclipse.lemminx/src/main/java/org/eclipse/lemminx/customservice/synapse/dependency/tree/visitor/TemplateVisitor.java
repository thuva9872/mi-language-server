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

package org.eclipse.lemminx.customservice.synapse.dependency.tree.visitor;

import org.eclipse.lemminx.customservice.synapse.dependency.tree.DependencyLookUp;
import org.eclipse.lemminx.customservice.synapse.dependency.tree.DependencyVisitorUtils;
import org.eclipse.lemminx.customservice.synapse.dependency.tree.pojo.Dependency;
import org.eclipse.lemminx.customservice.synapse.dependency.tree.pojo.DependencyTree;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.template.Template;

import java.util.List;

public class TemplateVisitor extends AbstractDependencyVisitor {

    public TemplateVisitor(DependencyTree dependencyTree, String projectPath) {

        super(dependencyTree, projectPath, new DependencyLookUp());
    }

    public TemplateVisitor(String projectPath, DependencyLookUp dependencyLookUp) {

        super(new DependencyTree(), projectPath, dependencyLookUp);
    }

    @Override
    public void visit(STNode node) {

        Template template = (Template) node;

        if (template.getEndpoint() != null) {
            Dependency dependency =
                    DependencyVisitorUtils.visitEndpoint(template.getEndpoint(), projectPath, dependencyLookUp);
            addDependency(dependency);
        } else if (template.getSequence() != null) {
            List<Dependency> dependencies =
                    DependencyVisitorUtils.visitMediators(template.getSequence().getMediatorList(), projectPath,
                            dependencyLookUp);
            addDependencies(dependencies);
        }
    }
}
