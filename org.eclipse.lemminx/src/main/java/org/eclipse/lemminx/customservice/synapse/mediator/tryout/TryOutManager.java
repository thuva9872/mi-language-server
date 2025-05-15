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

package org.eclipse.lemminx.customservice.synapse.mediator.tryout;

import org.eclipse.lemminx.customservice.synapse.connectors.ConnectionTester;
import org.eclipse.lemminx.customservice.synapse.connectors.ConnectorHolder;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.TestConnectionRequest;
import org.eclipse.lemminx.customservice.synapse.connectors.entity.TestConnectionResponse;
import org.eclipse.lemminx.customservice.synapse.mediator.schema.generate.ServerLessTryoutHandler;
import org.eclipse.lemminx.customservice.synapse.mediator.tryout.pojo.MediatorTryoutInfo;
import org.eclipse.lemminx.customservice.synapse.mediator.tryout.pojo.MediatorTryoutRequest;

public class TryOutManager {

    private TryOutHandler tryOutHandler;
    private IsolatedTryOutHandler isolatedTryOutHandler;
    private ServerLessTryoutHandler serverLessTryoutHandler;
    private ConnectionTester connectionTester;

    public TryOutManager(String projectRoot, String miServerPath, ConnectorHolder connectorHolder) {

        tryOutHandler = new TryOutHandler(projectRoot, miServerPath);
        isolatedTryOutHandler = new IsolatedTryOutHandler(tryOutHandler, projectRoot);
        serverLessTryoutHandler = new ServerLessTryoutHandler(projectRoot);
        connectionTester = new ConnectionTester(projectRoot, tryOutHandler, connectorHolder);
    }

    public final MediatorTryoutInfo tryout(MediatorTryoutRequest request) {

        if (request.isIsolatedTryout()) {
            return isolatedTryOutHandler.tryOut(request);
        } else {
            return tryOutHandler.handle(request);
        }
    }

    public final MediatorTryoutInfo getInputOutputSchema(MediatorTryoutRequest request) {

        return serverLessTryoutHandler.handle(request);
    }

    public final TestConnectionResponse testConnectorConnection(TestConnectionRequest request) {

        return connectionTester.testConnection(request);
    }

    public boolean shutdown() {

        tryOutHandler.reset();
        CAPPCacheManager.shutdown();
        return tryOutHandler.shutDown();
    }
}
