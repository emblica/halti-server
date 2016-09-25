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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.buildin.hardsoftlong.HardSoftLongScoreDefinition;

import emblica.halti.domain.solver.ServiceDependency;

@PlanningSolution
public class ServiceDistribution implements Solution<HardSoftLongScore> {

    private GlobalPenaltyInfo globalPenaltyInfo;
    private List<Resource> resourceList;
    private List<Neighborhood> neighborhoodList;
    private List<Location> locationList;
    private List<Machine> machineList;
    private List<MachineCapacity> machineCapacityList;
    private List<Service> serviceList;
    private List<Process> processList;
    private List<BalancePenalty> balancePenaltyList;

    private List<ProcessAssignment> processAssignmentList;

    private HardSoftLongScore score;

    public GlobalPenaltyInfo getGlobalPenaltyInfo() {
        return globalPenaltyInfo;
    }

    public void setGlobalPenaltyInfo(GlobalPenaltyInfo globalPenaltyInfo) {
        this.globalPenaltyInfo = globalPenaltyInfo;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<Neighborhood> getNeighborhoodList() {
        return neighborhoodList;
    }

    public void setNeighborhoodList(List<Neighborhood> neighborhoodList) {
        this.neighborhoodList = neighborhoodList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    @ValueRangeProvider(id = "machineRange")
    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public List<MachineCapacity> getMachineCapacityList() {
        return machineCapacityList;
    }

    public void setMachineCapacityList(List<MachineCapacity> machineCapacityList) {
        this.machineCapacityList = machineCapacityList;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public List<BalancePenalty> getBalancePenaltyList() {
        return balancePenaltyList;
    }

    public void setBalancePenaltyList(List<BalancePenalty> balancePenaltyList) {
        this.balancePenaltyList = balancePenaltyList;
    }

    @PlanningEntityCollectionProperty
    public List<ProcessAssignment> getProcessAssignmentList() {
        return processAssignmentList;
    }

    public void setProcessAssignmentList(List<ProcessAssignment> processAssignmentList) {
        this.processAssignmentList = processAssignmentList;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.add(globalPenaltyInfo);
        facts.addAll(resourceList);
        facts.addAll(neighborhoodList);
        facts.addAll(locationList);
        facts.addAll(machineList);
        facts.addAll(machineCapacityList);
        facts.addAll(serviceList);
        facts.addAll(createServiceDependencyList());
        facts.addAll(processList);
        facts.addAll(balancePenaltyList);
        // Do not add the planning entity's (bedDesignationList) because that will be done automatically
        return facts;
    }

    private List<ServiceDependency> createServiceDependencyList() {
        List<ServiceDependency> serviceDependencyList = new ArrayList<ServiceDependency>(serviceList.size() * 5);
        for (Service service : serviceList) {
            for (Service toService : service.getToDependencyServiceList()) {
                ServiceDependency serviceDependency = new ServiceDependency();
                serviceDependency.setFromService(service);
                serviceDependency.setToService(toService);
                serviceDependencyList.add(serviceDependency);
            }
        }
        return serviceDependencyList;
    }

}
