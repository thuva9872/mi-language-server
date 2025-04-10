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

import java.util.ArrayList;
import java.util.List;

public class ForeachMediatorTest extends AbstractMediatorDebuggerTest {

    @Override
    protected String getTestResourceName() {

        return "foreachMediator.xml";
    }

    @Test
    public void testForeachMediator() throws Exception {

        Breakpoint breakpoint = new Breakpoint(23, 4);
        testDebugInfo(List.of(breakpoint), List.of("1"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(25, 12)));
    }

    @Test
    public void testMediatorInForeachSequence() throws Exception {

        Breakpoint breakpoint = new Breakpoint(25, 12);
        testDebugInfo(List.of(breakpoint), List.of("1 0"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(28, 12)));
    }

    @Test
    public void testNestedForeachInForeachSequence() throws Exception {

        Breakpoint breakpoint = new Breakpoint(28, 12);
        testDebugInfo(List.of(breakpoint), List.of("1 1"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(30, 20)));
    }

    @Test
    public void testForeachSequenceOfNestedForeach() throws Exception {

        Breakpoint breakpoint = new Breakpoint(30, 20);
        testDebugInfo(List.of(breakpoint), List.of("1 1 0"));
        testStepOverInfo(breakpoint, List.of(new Breakpoint(37, 4)));
    }

    @Test
    public void testAllBreakpointAtOnce() throws Exception {

        List<Breakpoint> breakpoints = new ArrayList<>();
        List<String> expected = new ArrayList<>();

        breakpoints.add(new Breakpoint(23, 4));
        expected.add("1");

        breakpoints.add(new Breakpoint(25, 12));
        expected.add("1 0");

        breakpoints.add(new Breakpoint(28, 12));
        expected.add("1 1");

        breakpoints.add(new Breakpoint(30, 20));
        expected.add("1 1 0");

        testDebugInfo(breakpoints, expected);
    }
}
