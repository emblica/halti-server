(ns halti-server.api.services.crud
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.services.db :as db :refer [find-services find-service insert-service]]
            [clj-time.core :as t]
            [schema.core :as s]
            [halti-server.events :as events]
            [halti-server.config]))



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
   :ports [s/Int]
   :environment [{:key s/Str :value s/Str}]})


(defn new-service []
  {:service_id (uuid)})

(defn create-service [req]
  (let [new-service-base (new-service)
        service (s/validate Service (merge new-service-base (:body req)))
        inserted-service (insert-service service)]
    (events/updated!)
    (json-request 201 {:service inserted-service})))


(defn update-service [req]
  (let [service-id (get-in req [:params :service-id])
        body (:body req)
        service (find-service {:service_id service-id})
        updated-service-data (s/validate Service (merge service body))
        updated-service (db/update-service {:service_id service-id} updated-service-data)]
    (events/updated!)
    (json-request 200 {:service updated-service-data})))

(defn list-services [req]
  (let [services (find-services {})]
    (json-request 200 {:services services})))

(defn single-service [service-id]
  (json-request 200 {:service (find-service {:service_id service-id})}))
