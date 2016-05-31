(ns halti-server.api.services.crud
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.helpers.services :refer [ports-by-services]]
            [halti-server.api.services.db :as db :refer [find-services find-service insert-service]]
            [halti-server.api.instances.db :refer [find-healthy-hosts]]
            [clj-time.core :as t]
            [schema.core :as s]
            [halti-server.events :as events]
            [halti-server.config]))


(def Port
  "Specification for a port"
  {:port s/Int
   (s/optional-key :source) s/Int
   :protocol (s/enum "tcp" "udp")})


(def Service
  "A schema for a Service"
  {:name s/Str
   :cpu s/Num
   :enabled s/Bool
   :memory s/Num
   :instances s/Int
   :version s/Str
   :service_id s/Str
   :image s/Str
   (s/optional-key :command) s/Str
   (s/optional-key :_id) s/Any
   :ports [Port]
   :environment [{:key s/Str :value s/Str}]})


(defn new-service []
  {:service_id (uuid)})

(defn port->complete-port [port]
  (if (number? port)
    {:port port :protocol "tcp"}
    (merge {:protocol "tcp"} port)))

(defn ports->complete-ports [ports]
  (map port->complete-port ports))

(defn create-service [req]
  (let [new-service-base (new-service)
        raw-service (merge new-service-base (:body req))
        service-with-complete-ports (update raw-service :ports ports->complete-ports)
        service (s/validate Service service-with-complete-ports)
        inserted-service (insert-service service)]
    (events/updated!)
    (json-request 201 {:service inserted-service})))


(defn update-service [req]
  (let [service-id (get-in req [:params :service-id])
        body (:body req)
        service (find-service {:service_id service-id})
        raw-service (merge service body)
        service-with-complete-ports (update raw-service :ports ports->complete-ports)
        updated-service-data (s/validate Service service-with-complete-ports)
        updated-service (db/update-service {:service_id service-id} updated-service-data)]
    (events/updated!)
    (json-request 202 {:service updated-service-data})))

(defn list-services [req]
  (let [services (find-services {})]
    (json-request 200 {:services services})))

(defn remove-service [service-id]
  (if-let [removed? (db/remove-service {:service_id service-id})]
    (do
      (events/updated!)
      (json-request 202 {:ack true}))
    (json-request 400 {:ack false :error "Service not found or remove not acknowledged"})))

(defn single-service [service-id]
  (let [service (find-service {:service_id service-id})
        ports-by-services (ports-by-services (find-healthy-hosts))
        service-ports (pmap #(dissoc % :service_id) (get ports-by-services service-id))
        service-with-ports (assoc service :running_on service-ports)]
    (json-request 200 {:service service-with-ports})))
