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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.filter.throttle;

public class ControlAccessType implements AccessType {

    private int maximumCount;
    private int unitTime;
    private int prohibitTimePeriod;

    public int getMaximumCount() {

        return maximumCount;
    }

    public void setMaximumCount(int maximumCount) {

        this.maximumCount = maximumCount;
    }

    public int getUnitTime() {

        return unitTime;
    }

    public void setUnitTime(int unitTime) {

        this.unitTime = unitTime;
    }

    public int getProhibitTimePeriod() {

        return prohibitTimePeriod;
    }

    public void setProhibitTimePeriod(int prohibitTimePeriod) {

        this.prohibitTimePeriod = prohibitTimePeriod;
    }
}
