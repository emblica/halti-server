(ns halti-server.api.loadbalancers.crud
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.loadbalancers.db :as db :refer [find-loadbalancers find-loadbalancer insert-loadbalancer]]
            [clj-time.core :as t]
            [schema.core :as s]
            [halti-server.events :as events]
            [halti-server.config]))



(def Loadbalancer
  "A schema for a Loadbalancer"
  {:name s/Str
   :enabled s/Bool
   :loadbalancer_id s/Str
   :hostname s/Str
   :service_id s/Str
   :source_port s/Int
   (s/optional-key :_id) s/Any
   :force_https s/Bool
   :ports {:http s/Bool :https s/Bool}})



(defn new-loadbalancer []
  {:loadbalancer_id (uuid)})

(defn create-loadbalancer [req]
  (let [new-loadbalancer-base (new-loadbalancer)
        loadbalancer (s/validate Loadbalancer (merge new-loadbalancer-base (:body req)))
        inserted-loadbalancer (insert-loadbalancer loadbalancer)]
    (json-request 201 {:loadbalancer inserted-loadbalancer})))


(defn update-loadbalancer [req]
  (let [loadbalancer-id (get-in req [:params :loadbalancer-id])
        body (:body req)
        loadbalancer (find-loadbalancer {:loadbalancer_id loadbalancer-id})
        updated-loadbalancer-data (s/validate Loadbalancer (merge loadbalancer body))
        updated-loadbalancer (db/update-loadbalancer {:loadbalancer_id loadbalancer-id} updated-loadbalancer-data)]
    (json-request 200 {:loadbalancer updated-loadbalancer-data})))

(defn list-loadbalancers [req]
  (let [loadbalancers (find-loadbalancers {})]
    (json-request 200 {:loadbalancers loadbalancers})))

(defn single-loadbalancer [loadbalancer-id]
  (json-request 200 {:loadbalancer (find-loadbalancer {:loadbalancer_id loadbalancer-id})}))
