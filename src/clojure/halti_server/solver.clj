(ns halti-server.solver)


(import emblica.halti.solver.HaltiSolver)
(import emblica.halti.domain.ServiceDistribution)
(import emblica.halti.domain.GlobalPenaltyInfo)
(import emblica.halti.domain.Resource)
(import emblica.halti.domain.Neighborhood)
(import emblica.halti.domain.Location)
(import emblica.halti.domain.Machine)
(import emblica.halti.domain.MachineCapacity)
(import emblica.halti.domain.Service)
(import emblica.halti.domain.ProcessRequirement)
(import emblica.halti.domain.ProcessAssignment)


(def global-penalty (doto (new GlobalPenaltyInfo)
                        (.setProcessMoveCostWeight 10)
                        (.setServiceMoveCostWeight 10)
                        (.setMachineMoveCostWeight 10)))


(def RESOURCE-TYPES {:cpu 0
                     :memory 1})

(def cpu-resource (doto (new Resource)
                    (.setIndex (:cpu RESOURCE-TYPES))
                    (.setTransientlyConsumed false)
                    (.setLoadCostWeight 1.0)))

(def memory-resource (doto (new Resource)
                      (.setIndex (:memory RESOURCE-TYPES))
                      (.setTransientlyConsumed false)
                      (.setLoadCostWeight 1.0)))

(def resources [cpu-resource memory-resource])


(def default-neighborhood (new Neighborhood))

(def neighborhood [default-neighborhood])

(def default-location (new Location))

(def locations [default-location])

(def memory-2gb (doto (new MachineCapacity)
                    (.setResource memory-resource)
                    (.setMaximumCapacity 2048)
                    (.setSafetyCapacity 2000)))


(def cores-2 (doto (new MachineCapacity)
                (.setResource cpu-resource)
                (.setMaximumCapacity 2)
                (.setSafetyCapacity 2)))


(def upcloud-20 [memory-2gb cores-2])


(def node1 (doto (new Machine)
              (.setName "node1")
              (.setNeighborhood default-neighborhood)
              (.setLocation default-location)
              (.setMachineCapacityList upcloud-20)))

(def node2 (doto (new Machine)
              (.setName "node2")
              (.setNeighborhood default-neighborhood)
              (.setLocation default-location)
              (.setMachineCapacityList upcloud-20)))

(def node3 (doto (new Machine)
              (.setName "node3")
              (.setNeighborhood default-neighborhood)
              (.setLocation default-location)
              (.setMachineCapacityList upcloud-20)))



(def machines [node1 node2 node3])

(def machine-capacitys [cores-2 memory-2gb])


(def static-app (doto (new Service)
                  (.setName "static-app")
                  (.setLocationSpread 2)
                  (.setToDependencyServiceList [])
                  (.setFromDependencyServiceList [])))

(def mysql (doto (new Service)
              (.setName "mysql")
              (.setLocationSpread 1)
              (.setToDependencyServiceList [])
              (.setFromDependencyServiceList [])))

(def webapp (doto (new Service)
              (.setName "webapp")
              (.setLocationSpread 2)
              (.setToDependencyServiceList [mysql])
              (.setFromDependencyServiceList [])))


(def services [static-app mysql webapp])



(def app-memory (doto (new ProcessRequirement)
                    (.setResource memory-resource)
                    (.setUsage 512)))

(def app-cpu (doto (new ProcessRequirement)
                (.setResource cpu-resource)
                (.setUsage 1)))

(def app-requirements [app-cpu app-memory])

(def mysql-memory (doto (new ProcessRequirement)
                    (.setResource memory-resource)
                    (.setUsage 128)))

(def mysql-cpu (doto (new ProcessRequirement)
                    (.setResource cpu-resource)
                    (.setUsage 1)))

(def mysql-requirements [mysql-cpu mysql-memory])

(def mysql-process (doto (new emblica.halti.domain.Process)
                    (.setService mysql)
                    (.setMoveCost 5)
                    (.setProcessRequirementList mysql-requirements)))

(def staticapp-process (doto (new emblica.halti.domain.Process)
                        (.setService static-app)
                        (.setMoveCost 1)
                        (.setProcessRequirementList app-requirements)))

(def webapp-process (doto (new emblica.halti.domain.Process)
                        (.setService webapp)
                        (.setMoveCost 1)
                        (.setProcessRequirementList app-requirements)))



(def processes [mysql-process staticapp-process webapp-process webapp-process])

(defn assign-first [process]
  (doto (new ProcessAssignment)
    (.setProcess process)
    (.setOriginalMachine node1)))



(def process-assignments (map assign-first processes))


(def cloud-state (doto (new ServiceDistribution)
                    (.setGlobalPenaltyInfo global-penalty)
                    (.setResourceList resources)
                    (.setNeighborhoodList neighborhood)
                    (.setLocationList locations)
                    (.setMachineList machines)
                    (.setMachineCapacityList machine-capacitys)
                    (.setServiceList services)
                    (.setProcessList processes)
                    (.setBalancePenaltyList [])
                    (.setProcessAssignmentList process-assignments)))



; (def t (doto (HaltiSolver/createSolver "solver/solver_config.xml")
;           (.solve cloud-state)))
;
; (time (map #(println %) (.getProcessAssignmentList (.getBestSolution t))))
;
; (map #(println %)) (.getProcessAssignmentList cloud-state)
;
;
; (.getScore cloud-state)
; (.getScore (.getBestSolution t))
;
