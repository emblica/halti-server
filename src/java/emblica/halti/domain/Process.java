/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications for Halti usage made by Emblica Ltd.
 *
 */
package emblica.halti.domain;


import java.util.List;

public class Process {

    private Service service;
    private int moveCost;

    // Order is equal to resourceList so resource.getIndex() can be used
    private List<ProcessRequirement> processRequirementList;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }

    public List<ProcessRequirement> getProcessRequirementList() {
        return processRequirementList;
    }

    public void setProcessRequirementList(List<ProcessRequirement> processRequirementList) {
        this.processRequirementList = processRequirementList;
    }

    public ProcessRequirement getProcessRequirement(Resource resource) {
        return processRequirementList.get(resource.getIndex());
    }

    public long getUsage(Resource resource) {
        return processRequirementList.get(resource.getIndex()).getUsage();
    }

    public int getUsageMultiplicand() {
        int multiplicand = 1;
        for (ProcessRequirement processRequirement : processRequirementList) {
            multiplicand *= processRequirement.getUsage();
        }
        return multiplicand;
    }

}
