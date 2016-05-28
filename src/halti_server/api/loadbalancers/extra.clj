(ns halti-server.api.loadbalancers.extra
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.loadbalancers.db :as db :refer [find-enabled-loadbalancers]]
            [halti-server.api.instances.db :refer [find-healthy-hosts]]
            [halti-server.api.services.db :refer [find-enabled-services]]
            [halti-server.helpers.services :refer [ports-by-services]]
            [clj-time.core :as t]
            [halti-server.events :as events]
            [halti-server.utils :refer [deadline]]))



(defn- remove-service-id+source [port]
  (-> port
    (dissoc :service_id)
    (dissoc :source)))


(defn- ports+loadbalancer->loadbalancer-config [ports loadbalancer]
  (let [loadbalancer-source (:source_port loadbalancer)
        only-selected-port (fn [port] (= (:source port) loadbalancer-source))
        service-id (:service_id loadbalancer)
        service-ports (get ports service-id)
        selected-ports (mapv remove-service-id+source (filter only-selected-port service-ports))]
    (merge loadbalancer {:backends selected-ports})))

(defn- loadbalancer-configuration-data []
  (let [enabled-loadbalancers (find-enabled-loadbalancers)
        healthy-hosts (find-healthy-hosts)
        ports-by-services (ports-by-services healthy-hosts)
        enabled-services (find-enabled-services)
        loadbalancer+backends (partial ports+loadbalancer->loadbalancer-config ports-by-services)]
    (mapv loadbalancer+backends enabled-loadbalancers)))




(defn loadbalancer-configuration [req]
  (json-request 200 (loadbalancer-configuration-data)))
