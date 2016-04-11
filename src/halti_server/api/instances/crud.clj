(ns halti-server.api.instances.crud
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.instances.db :refer [find-instances find-instance]]
            [clj-time.core :as t]
            [halti-server.config]))



(defn- deadline []
  (t/minus (t/now) (t/seconds halti-server.config/obituary-time)))


(defn list-instances [req]
  (let [instances (find-instances {})]
    (json-request 200 {:instances instances})))

(defn list-healthy-instances [req]
  (let [instances (find-instances {:last_heartbeat {"$gt" (deadline)}})]
    (json-request 200 {:instances instances})))

(defn single-instance [instance-id]
  (json-request 200 {:instance (find-instance {:instance_id instance-id})}))
