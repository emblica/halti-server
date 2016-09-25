(ns halti-server.solver
  (:import (emblica.halti.domain ServiceDistribution
                                 GlobalPenaltyInfo
                                 Resource
                                 Neighborhood
                                 Location
                                 Machine
                                 MachineCapacity
                                 Service
                                 ProcessRequirement
                                 ProcessAssignment)
           (emblica.halti.solver HaltiSolver)))


(def GLOBAL-PENALTY (doto (new GlobalPenaltyInfo)
                        (.setProcessMoveCostWeight 10)
                        (.setServiceMoveCostWeight 10)
                        (.setMachineMoveCostWeight 10)))


(def RESOURCE-TYPES {:cpu 0
                     :memory 1})

(def CPU-RESOURCE (doto (new Resource)
                    (.setIndex (:cpu RESOURCE-TYPES))
                    (.setTransientlyConsumed false)
                    (.setLoadCostWeight 1.0)))

(def MEMORY-RESOURCE (doto (new Resource)
                      (.setIndex (:memory RESOURCE-TYPES))
                      (.setTransientlyConsumed false)
                      (.setLoadCostWeight 1.0)))

(def RESOURCES [CPU-RESOURCE MEMORY-RESOURCE])


(defn location->location-object [identifier]
   (doto (new Location)
     (.setIdentifier identifier)))

(defn neighborhood->neighborhood-object [identifier]
   (doto (new Neighborhood)
     (.setIdentifier identifier)))

; CONSTANTS
(def MEMORY-SAFE-MARGIN 0.95) ; 95%
(def CPU-OVERPROVISIONING-FACTOR 5) ; we can overprovision 5 times
(def CPU-SAFE-MARGIN 1.0) ; 100%
(def DEFAULT-LOCATION (location->location-object "default"))
(def DEFAULT-NEIGHBORHOOD (neighborhood->neighborhood-object "default"))

(defn memory->memory-capacity [memory]
  (let [memory-integer (int memory)
        safe-zone (int (Math/floor (* memory MEMORY-SAFE-MARGIN)))]
    (doto (new MachineCapacity)
      (.setResource MEMORY-RESOURCE)
      (.setMaximumCapacity memory-integer)
      (.setSafetyCapacity safe-zone))))

(defn cpu-cores->cpu-capacity [cpu]
  (let [cpu-cores (* (int cpu) CPU-OVERPROVISIONING-FACTOR 10) ; Multiply by 10
        safe-zone (int (Math/floor (* cpu-cores CPU-SAFE-MARGIN)))]
    (doto (new MachineCapacity)
      (.setResource MEMORY-RESOURCE)
      (.setMaximumCapacity cpu-cores)
      (.setSafetyCapacity safe-zone))))




(defn machine->machine-object [locations neighborhoods machine-data]
  (let [machine-uuid (:instance-id machine-data)
        cpu-capacity (cpu-cores->cpu-capacity (:cpu machine-data))
        memory-capacity (memory->memory-capacity (:memory machine-data))
        capacitys [cpu-capacity memory-capacity] ; Order is important!
        capabilities (set (:capabilities machine-data))
        location (get locations (:location machine-data) DEFAULT-LOCATION)
        neighborhood (get neighborhoods (:instance-id machine-data) DEFAULT-NEIGHBORHOOD)
        machine (doto (new Machine)
                  (.setUUID machine-uuid)
                  (.setNeighborhood neighborhood) ; Every machine is now in own neighborhood
                  (.setLocation location)
                  (.setMachineCapabilities capabilities)
                  (.setMachineCapacityList capacitys))]
    {:machine machine
     :capacitys capacitys}))


(defn memory-requirement [memory]
  (doto (new ProcessRequirement)
    (.setResource MEMORY-RESOURCE)
    (.setUsage (int memory))))

(defn cpu-requirement [cpu]
  (doto (new ProcessRequirement)
    (.setResource CPU-RESOURCE)
    (.setUsage (int cpu))))

(defn service->process [process-id service service-object]
  (let [memory (memory-requirement (:memory service))
        cpu (cpu-requirement (:cpu service))]
    (doto (new emblica.halti.domain.Process)
        (.setService service-object)
        (.setMoveCost 5)
        (.setProcessRequirementList [cpu memory]))))


(defn service->service-and-processes [service]
  (let [capabilities (or (:capabilities service) [])
        service-object (doto (new Service)
                         (.setName (:service-id service))
                         (.setLocationSpread (:instances service))
                         (.setNeededCapabilityList capabilities)
                         (.setToDependencyServiceList [])
                         (.setFromDependencyServiceList []))
        process-ids (range (:instances service))
        processes (map service->process process-ids (repeat service) (repeat service-object))]
    {:service service-object
     :processes processes}))



(defn assign-machine-> [machine process]
  (doto (new ProcessAssignment)
    (.setProcess process)
    (.setOriginalMachine machine)))


(defn ->cloud-state [all-neighborhoods all-locations machines services]
  (let [machine-list (map :machine machines)
        processes (mapcat :processes services)
        first-machine (first machine-list)
        assign-to-first (partial assign-machine-> first-machine)]
    (doto (new ServiceDistribution)
         (.setGlobalPenaltyInfo GLOBAL-PENALTY)
         (.setResourceList RESOURCES)
         (.setNeighborhoodList all-neighborhoods)
         (.setLocationList all-locations)
         (.setMachineList machine-list)
         (.setMachineCapacityList (mapcat :capacitys machines))
         (.setServiceList (map :service services))
         (.setProcessList processes)
         (.setBalancePenaltyList [])
         (.setProcessAssignmentList (map assign-to-first processes)))))

(defn- neighborhood-map [neighborhood]
 {neighborhood (neighborhood->neighborhood-object neighborhood)})

(defn machines->neighborhoods [machines]
  (let [neighborhood-names (set (map :instance-id machines))
        neighborhoods (map neighborhood-map neighborhood-names)]
    (apply merge neighborhoods)))

(defn- location-map [location]
  {location (location->location-object location)})

(defn machines->locations [machines]
  (let [location-names ["TEST"] ; TODO: real implementation
        locations (map location-map location-names)]
    (apply merge locations)))


(def solver (HaltiSolver/createSolver "solver/solver_config.xml"))

(defn assignment->tuple [assignment]
  {:machine (.getUUID (.getMachine assignment))
   :service (.getName (.getService assignment))})


(defn mapmval [f m]
 (reduce (fn [nm [k v]] (assoc nm k (f v))) {} m))



(def by-machine (comp (fn [group] (map #(hash-map :instance-id %1 :containers %2) (keys group) (vals group)))
                      (partial mapmval (partial mapv :service))
                      (partial group-by :machine)))


(defn distribute-services [machines services]
  (let [neighborhoods (machines->neighborhoods machines)
        locations (machines->locations machines)
        machine-objects (map (partial machine->machine-object locations neighborhoods) machines)
        service-objects (map service->service-and-processes services)
        all-neighborhoods (conj (vals neighborhoods) DEFAULT-NEIGHBORHOOD)
        all-locations (conj (vals locations) DEFAULT-LOCATION)
        cloud-state (->cloud-state
                      all-neighborhoods
                      all-locations
                      machine-objects
                      service-objects)
        assignments (-> solver
                      (.solve cloud-state)
                      (.getProcessAssignmentList))
        assignment-tuples (map assignment->tuple assignments)]
    (by-machine assignment-tuples)))
