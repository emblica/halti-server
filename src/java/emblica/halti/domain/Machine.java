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
import java.util.Map;
import java.util.Set;
import java.util.HashSet;


public class Machine {

    private String uuid;
    private Neighborhood neighborhood;
    private Location location;

    // Order is equal to resourceList so resource.getIndex() can be used
    private List<MachineCapacity> machineCapacityList;
    private Set<String> machineCapabilities = new HashSet<>();
    private Map<Machine, Integer> machineMoveCostMap; // key is toMachine

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<MachineCapacity> getMachineCapacityList() {
        return machineCapacityList;
    }

    public void setMachineCapacityList(List<MachineCapacity> machineCapacityList) {
        this.machineCapacityList = machineCapacityList;
    }

    public Set<String> getMachineCapabilities() {
        return machineCapabilities;
    }

    public void setMachineCapabilities(Set<String> machineCapabilities) {
        this.machineCapabilities = machineCapabilities;
    }

    public boolean hasCapability(String capability) {
        return this.machineCapabilities.contains(capability);
    }

    public MachineCapacity getMachineCapacity(Resource resource) {
        return machineCapacityList.get(resource.getIndex());
    }

    public Map<Machine, Integer> getMachineMoveCostMap() {
        return machineMoveCostMap;
    }

    public void setMachineMoveCostMap(Map<Machine, Integer> machineMoveCostMap) {
        this.machineMoveCostMap = machineMoveCostMap;
    }

    public int getMoveCostTo(Machine toMachine) {
        return 1;
        //return machineMoveCostMap.get(toMachine);
    }

}
