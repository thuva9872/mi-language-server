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

import org.eclipse.lemminx.customservice.synapse.debugger.DebuggerHelper;
import org.eclipse.lemminx.customservice.synapse.debugger.entity.Breakpoint;
import org.eclipse.lemminx.customservice.synapse.debugger.entity.StepOverInfo;
import org.eclipse.lemminx.customservice.synapse.debugger.entity.debuginfo.IDebugInfo;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.SyntaxTreeGenerator;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.synapse.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractDebuggerTest {

    protected STNode testStNode;

    protected abstract String getResourcePath();

    @BeforeAll
    public void init() throws Exception {

        String testResourcePath = getResourcePath();
        String resourceFilePath = TestUtils.getResourceFilePath(testResourcePath);
        DOMDocument document = Utils.getDOMDocumentFromPath(resourceFilePath);
        this.testStNode = SyntaxTreeGenerator.buildTree(document.getDocumentElement());
    }

    public List<IDebugInfo> getDebugInfo(List<Breakpoint> breakpoints) {

        DebuggerHelper debuggerHelper = new DebuggerHelper(testStNode);
        return debuggerHelper.generateDebugInfo(breakpoints);
    }

    public void testDebugInfo(List<Breakpoint> breakpoints, List<String> expectedMediatorPositions) throws
            IOException {

        List<IDebugInfo> debugInfos = getDebugInfo(breakpoints);

        int i = 0;
        for (IDebugInfo debugInfo : debugInfos) {
            String actualMediatorPosition = debugInfo.getMediatorPosition();
            String expectedMediatorPosition = expectedMediatorPositions.get(i++);

            assertEquals(expectedMediatorPosition, actualMediatorPosition);
        }
    }

    public void testStepOverInfo(Breakpoint breakpoint, List<Breakpoint> expectedStepOverBreakpoints) {

        DebuggerHelper debuggerHelper = new DebuggerHelper(testStNode);
        StepOverInfo stepOverInfo = debuggerHelper.getStepOverBreakpoints(breakpoint);

        assertEquals(expectedStepOverBreakpoints.size(), stepOverInfo.size());
        assertEquals(expectedStepOverBreakpoints, stepOverInfo.getStepOverBreakpoints());
    }
}
