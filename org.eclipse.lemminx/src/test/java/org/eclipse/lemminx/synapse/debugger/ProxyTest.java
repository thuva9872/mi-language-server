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
import org.eclipse.lemminx.customservice.synapse.debugger.entity.debuginfo.ProxyDebugInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyTest extends AbstractDebuggerTest {

    @Override
    protected String getResourcePath() {

        return "/synapse/debugger/proxy.xml";
    }

    @Test
    public void test_ProxyInSequence() {

        Breakpoint breakpoint = new Breakpoint(21, 12);
        ProxyDebugInfo debugInfo = (ProxyDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("debugger", debugInfo.getProxyKey());
        assertEquals("proxy_inseq", debugInfo.getSequenceType());

        testStepOverInfo(breakpoint, List.of(new Breakpoint(24, 12)));
    }

    @Test
    public void testMultipleBreakpoint_ProxyInSequence() throws IOException {

        Breakpoint breakpoint1 = new Breakpoint(21, 12);
        Breakpoint breakpoint2 = new Breakpoint(24, 12);
        testDebugInfo(List.of(breakpoint1, breakpoint2), List.of("0", "1"));
    }

    @Test
    public void test_ProxyFaultSequence() {

        Breakpoint breakpoint = new Breakpoint(29, 12);
        ProxyDebugInfo debugInfo = (ProxyDebugInfo) getDebugInfo(List.of(breakpoint)).get(0);
        assertEquals("0", debugInfo.getMediatorPosition());
        assertEquals("debugger", debugInfo.getProxyKey());
        assertEquals("proxy_faultseq", debugInfo.getSequenceType());

        testStepOverInfo(breakpoint, List.of(new Breakpoint(32, 12)));
    }

    @Test
    public void testMultipleBreakpoint_ProxyOutSequence() throws IOException {

        Breakpoint breakpoint1 = new Breakpoint(29, 12);
        Breakpoint breakpoint2 = new Breakpoint(32, 12);
        testDebugInfo(List.of(breakpoint1, breakpoint2), List.of("0", "1"));
    }
}
