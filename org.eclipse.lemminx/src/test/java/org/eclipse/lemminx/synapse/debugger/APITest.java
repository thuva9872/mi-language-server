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

package org.eclipse.lemminx.synapse.debugger;

import org.eclipse.lemminx.customservice.synapse.debugger.entity.Breakpoint;
import org.eclipse.lemminx.customservice.synapse.debugger.entity.debuginfo.ApiDebugInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITest extends AbstractDebuggerTest {

    @Override
    protected String getResourcePath() {

        return "/synapse/debugger/api.xml";
    }

    @Test
    public void test_URLResource() {

        Breakpoint breakpoint = new Breakpoint(22, 12);
        ApiDebugInfo debugInfo = (ApiDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("/postUrl", debugInfo.getUrlMapping());
        assertEquals("POST", debugInfo.getMethod());
        assertEquals("api_inseq", debugInfo.getSequenceType());
    }

    @Test
    public void test_URIResource() {

        Breakpoint breakpoint = new Breakpoint(48, 12);
        ApiDebugInfo debugInfo = (ApiDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("/postUri/{path1}?query1={query1}", debugInfo.getUriTemplate());
        assertEquals("POST", debugInfo.getMethod());
        assertEquals("api_inseq", debugInfo.getSequenceType());
    }

    @Test
    public void testMultipleBreakpoint_APIInSequence() throws Exception {

        Breakpoint breakpoint1 = new Breakpoint(22, 12);
        Breakpoint breakpoint2 = new Breakpoint(25, 12);
        testDebugInfo(List.of(breakpoint1, breakpoint2), List.of("0", "1"));
    }

    @Test
    public void test_APIOutSequence() {

        Breakpoint breakpoint = new Breakpoint(30, 12);

        ApiDebugInfo debugInfo = (ApiDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("api_outseq", debugInfo.getSequenceType());
        assertEquals("POST", debugInfo.getMethod());
        assertEquals("/postUrl", debugInfo.getUrlMapping());
        assertEquals("api_outseq", debugInfo.getSequenceType());

        testStepOverInfo(breakpoint, List.of(new Breakpoint(33, 12)));
    }

    @Test
    public void testMultipleBreakpoint_APIOutSequence() throws Exception {

        Breakpoint breakpoint1 = new Breakpoint(30, 12);
        Breakpoint breakpoint2 = new Breakpoint(33, 12);
        testDebugInfo(List.of(breakpoint1, breakpoint2), List.of("0", "1"));
    }

    @Test
    public void test_APIFaultSequence() throws Exception {

        Breakpoint breakpoint = new Breakpoint(38, 12);
        ApiDebugInfo debugInfo = (ApiDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("api_faultseq", debugInfo.getSequenceType());
        assertEquals("POST", debugInfo.getMethod());
        assertEquals("/postUrl", debugInfo.getUrlMapping());
        assertEquals("api_faultseq", debugInfo.getSequenceType());

        testStepOverInfo(breakpoint, List.of(new Breakpoint(41, 12)));
    }

    @Test
    public void testMultipleBreakpoint_APIFaultSequence() throws Exception {

        Breakpoint breakpoint1 = new Breakpoint(38, 12);
        Breakpoint breakpoint2 = new Breakpoint(41, 12);
        testDebugInfo(List.of(breakpoint1, breakpoint2), List.of("0", "1"));
    }
}
