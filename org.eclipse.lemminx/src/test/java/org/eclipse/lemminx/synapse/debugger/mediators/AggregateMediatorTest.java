/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.eclipse.lemminx.synapse.debugger.mediators;

import org.eclipse.lemminx.customservice.synapse.debugger.entity.Breakpoint;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class AggregateMediatorTest extends AbstractMediatorDebuggerTest {

    @Override
    protected String getTestResourceName() {

        return "aggregateMediator.xml";
    }

    @Test
    public void testAggregateMediator() throws Exception {

        Breakpoint breakpoint = new Breakpoint(23, 4);
        testDebugInfo(List.of(breakpoint), List.of("1"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(28, 12), new Breakpoint(33, 4)));
    }

    @Test
    public void testMediatorInOnCompleteSequence() throws Exception {

        Breakpoint breakpoint = new Breakpoint(28, 12);
        testDebugInfo(List.of(breakpoint), List.of("1 0"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(33, 4)));
    }

    @Test
    public void testAfterAggregateMediator() throws Exception {

        Breakpoint breakpoint = new Breakpoint(33, 4);
        testDebugInfo(List.of(breakpoint), List.of("2"));
        testStepOverInfo(breakpoint, Collections.emptyList());
    }
}
