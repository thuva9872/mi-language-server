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
import org.eclipse.lemminx.customservice.synapse.dependency.tree.pojo.DependencyTree;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.MessageStore;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.misc.common.Parameter;

public class MessageStoreVisitor extends AbstractDependencyVisitor {

    private static final String FAIL_OVER_MESSAGE_PARAMETER = "store.failover.message.store.name";

    public MessageStoreVisitor(DependencyTree dependencyTree, String projectPath) {

        super(dependencyTree, projectPath, new DependencyLookUp());
    }

    public MessageStoreVisitor(String projectPath, DependencyLookUp dependencyLookUp) {

        super(new DependencyTree(), projectPath, dependencyLookUp);
    }

    @Override
    public void visit(STNode node) {

        MessageStore messageStore = (MessageStore) node;
        Parameter[] parameters = messageStore.getParameter();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                if (FAIL_OVER_MESSAGE_PARAMETER.equalsIgnoreCase(parameter.getName())) {
                    addDependency(DependencyVisitorUtils.visitMessageStore(parameter.getContent(), projectPath,
                            dependencyLookUp));
                }
            }
        }
    }
}
