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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.serializer.mediator.data;

import org.apache.axiom.om.OMElement;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.Mediator;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.advanced.DbMediator.DBReport;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.advanced.DbMediator.DbMediator;

public class DBReportMediatorSerializer extends DBMediatorSerializer {

    @Override
    protected OMElement serializeSpecificMediator(Mediator m) {

        DbMediator dbReport = (DbMediator) m;
        OMElement dbReportElt = serializeDBMediator(dbReport);
        dbReportElt.setLocalName("dbreport");

        return dbReportElt;
    }

    @Override
    public String getMediatorClassName() {

        return DBReport.class.getName();
    }
}
