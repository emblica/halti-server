(ns halti-server.scheduler
  (:require [halti-server.utils :refer [flip]]))

;
; (def hosts
;   [{:name "A" :cpu 2 :memory 1000 :services #{} :containers []}
;    {:name "B" :cpu 1 :memory 2000 :services #{} :containers []}
;    {:name "C" :cpu 2 :memory 4000 :services #{} :containers []}])
;
; (def services
;   [{:name "App A" :cpu 0.8  :memory 500 :instances 3}
;    {:name "App B" :cpu 0.1  :memory 200 :instances 2}
;    {:name "App C" :cpu 4    :memory 800 :instances 1}
;    {:name "App D" :cpu 0.01 :memory 10  :instances 5}])
;


(defn container-id+service->container [container-id service]
  (assoc (dissoc service :instances) :container-id container-id))

(defn service->containers [service]
  (let [instances (:instances service)
        instance-ids (range instances)]
    (map container-id+service->container instance-ids (repeat service))))


(defn services->containers [services]
  (mapcat concat (map service->containers services)))

(def not-pos? (comp not pos?))


(defn have-capabilities? [host service]
  (let [capabilities (set (:capabilities host))
        requirements (:requirements service)
        filled? (partial contains? capabilities)]
    (every? filled? requirements)))

(defn has-already-instance-of-service? [host service]
  (let [services (:services host) ; These applications are already running on host
        app-name (:service-id service)]
    (contains? services app-name)))

(defn assign-container-to-host [host containers not-suitable-containers]
  ; Check if there is still containers to check
  (if (empty? containers)
    ; No more containers to assing to this host, lets return host and containers that couldn't fit
    [host not-suitable-containers]
    ; Ok it seems there is at least one container to check out
    (let [container (first containers) ; Select first container
          app-name (:service-id container) ; Container is instance of this application
          container-name app-name ; Form name from app-name and container id
          rest-containers (rest containers) ; Leave rest containers here
          avail-cpu (- (:cpu host) (:cpu container)) ; How much host have cpu available if we assing this container to this host
          avail-memory (- (:memory host) (:memory container)) ; --::-- same for memory
          avail-resources {:cpu avail-cpu :memory avail-memory} ; Lets put them into hashmap so its easier to assing later
          cpu-overprovisioned? (not-pos? avail-cpu) ; See if were now overprovisioning cpu
          memory-overprovisioned? (not-pos? avail-memory) ; See if were now overprovisioning memory
          host-containers (:containers host) ; These containers are already running on host
          services (:services host) ; These applications are already running on host
          capabilities (:capabilities host)]

      (cond
        ; Dont assing multiple instances of same application to one host
        (has-already-instance-of-service? host container)
        #(assign-container-to-host host rest-containers (conj not-suitable-containers container)) ; - sad it was duplicate so lets just continue
        ; Check that host has needed capabilities
        (not (have-capabilities? host container))
        #(assign-container-to-host host rest-containers (conj not-suitable-containers container)) ; - sad, no capabilities so lets just continue
        ; Looks like this is new application for this host
        ; it also looks like have satisfied requirements
        ; so if it fits to hosts memory we got deal!
        (or cpu-overprovisioned? memory-overprovisioned?)
        #(assign-container-to-host host rest-containers (conj not-suitable-containers container)) ; - sad, no space so lets just continue
        ; If good start provisioning
        :else
        (let [new-resources (assoc avail-resources :containers (conj host-containers container-name)) ; It fits! set new resources
              host-with-new-resources (merge host new-resources) ; here we merge the new resources with old ones
              hosts-apps (conj services app-name) ; And we should also add new application to the host
              host-with-apps (assoc host-with-new-resources :services hosts-apps)] ; Lets assoc new apps to host
          #(assign-container-to-host host-with-apps rest-containers not-suitable-containers)))))) ; And continue forward

(defn allocate-containers [containers not-allocated-hosts allocated-hosts]
  "Allocates containers to hosts returns map with hosts with allocated containers and leftover containers"
  (if (empty? not-allocated-hosts)
    {:hosts allocated-hosts :left-over-containers containers}
    (let [host (first not-allocated-hosts)
          rest-hosts (rest not-allocated-hosts)
          [allocated-host rest-containers] (trampoline assign-container-to-host host containers [])]
      #(allocate-containers rest-containers rest-hosts (conj allocated-hosts allocated-host)))))




(defn distribute-services [hosts services]
  "Returns map with two keys: hosts and left-over-containers"
  (trampoline allocate-containers (services->containers services) hosts []))


;(distribute-services hosts services)
