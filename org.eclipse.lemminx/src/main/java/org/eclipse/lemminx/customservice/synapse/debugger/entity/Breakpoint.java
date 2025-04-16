/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.eclipse.lemminx.customservice.synapse.debugger.entity;

import java.util.Objects;

public class Breakpoint {

    int line;
    Integer column;

    public Breakpoint(int line) {

        this.line = line;
    }

    public Breakpoint(int line, Integer column) {

        this.line = line;
        this.column = column;
    }

    public int getLine() {

        return line;
    }

    public void setLine(int line) {

        this.line = line;
    }

    public Integer getColumn() {

        return column;
    }

    public void setColumn(Integer column) {

        this.column = column;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breakpoint that = (Breakpoint) o;
        return line == that.line && Objects.equals(column, that.column);
    }

    @Override
    public int hashCode() {

        return Objects.hash(line, column);
    }

    @Override
    public String toString() {

        return "Breakpoint{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }
}
