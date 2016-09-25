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


import org.apache.commons.lang3.ObjectUtils;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import emblica.halti.domain.solver.ProcessAssignmentDifficultyComparator;

@PlanningEntity(difficultyComparatorClass = ProcessAssignmentDifficultyComparator.class)

public class ProcessAssignment {

    private Process process;
    private Machine originalMachine;

    private Machine machine;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Machine getOriginalMachine() {
        return originalMachine;
    }

    public void setOriginalMachine(Machine originalMachine) {
        this.originalMachine = originalMachine;
    }

    @PlanningVariable(valueRangeProviderRefs = {"machineRange"})
    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Service getService() {
        return process.getService();
    }

    public boolean isMoved() {
        return !ObjectUtils.equals(originalMachine, machine);
    }

    public int getProcessMoveCost() {
        return process.getMoveCost();
    }

    public int getMachineMoveCost() {
        return (machine == null || originalMachine == null) ? 0 : originalMachine.getMoveCostTo(machine);
    }

    public Neighborhood getNeighborhood() {
        return machine == null ? null : machine.getNeighborhood();
    }

    public Location getLocation() {
        return machine == null ? null : machine.getLocation();
    }

    public int capabilitiesSatisfied() {
      int total = 1;
      int satisfied = 1;
      if (machine == null) return 0;
      for (String capability : process.getService().getNeededCapabilityList()) {
        total++;
        if (machine.hasCapability(capability)) {
          satisfied++;
        }
      }
      return ((satisfied/total)*100);
    }

    public long getUsage(Resource resource) {
        return process.getUsage(resource);
    }

    @Override
    public String toString() {
        return (machine == null || originalMachine == null) ? process.getService().getName() : process.getService().getName() + machine.getUUID();
    }

}
