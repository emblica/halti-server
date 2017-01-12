(ns halti-server.api.instances.lifecycle
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.instances.db :refer [insert-instance update-instance find-instance insert-instance-event]]
            [halti-server.api.services.db :refer [find-services]]
            [halti-server.config :refer [heartbeat-interval]]
            [halti-server.events :as events]
            [clj-time.core :as t]))


(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))


(defn new-instance []
  {:instance_id (uuid)
   :created (t/now)
   :containers []
   :last_heartbeat (t/now)})


(defn register [req]
 (let [created-instance (merge (dissoc-in (:body req) [:info :RegistryConfig]) (new-instance))
       inserted-instance (insert-instance created-instance)
       instance-id (:instance_id inserted-instance)]
   (json-request 201 {:instance_id instance-id
                      :instance inserted-instance
                      :heartbeat_interval heartbeat-interval})))


(defn services-to-run [instance-id]
  (let [instance (find-instance {:instance_id instance-id})
        service-ids (or (get-in instance [:config :containers]) [])]
    (find-services {:service_id {"$in" service-ids}})))


(defn heartbeat [req]
  "Heartbeats instance"
  (let [instance-id (get-in req [:params :instance-id])
        now (t/now)
        containers (get-in req [:body :containers])
        heartbeated? (update-instance {:instance_id instance-id} {"$set" {:last_heartbeat (t/now) :containers containers}})
        status-code (if heartbeated? 200 400)
        result (if heartbeated? {:heartbeat now :alive true :services (services-to-run instance-id)} {:error "heartbeat failed, you might have wrong instance id"})]
    (events/updated!)
    (json-request status-code result)))

(defn notify [req]
  "Notify instance state ie. pulling image or errors"
  (let [instance-id (get-in req [:params :instance-id])
        now (t/now)
        event (get-in req [:body :event])
        event-type (get-in req [:body :event_type])
        event-meta (get-in req [:body :event_meta])
        instance-event {:timestamp now
                        :event event
                        :event_type event-type
                        :meta event-meta}
        inserted-event (insert-instance-event instance-event)
        status-code 201
        result {:ack now}]
    (json-request status-code result)))
