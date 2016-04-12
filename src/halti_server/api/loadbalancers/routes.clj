(ns halti-server.api.loadbalancers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.loadbalancers.crud :refer [single-loadbalancer list-loadbalancers create-loadbalancer update-loadbalancer]]
            [halti-server.api.loadbalancers.extra :refer [loadbalancer-configuration]]
            [clj-time.core :as t]))


(defroutes loadbalancers-router
 (GET "/" [] list-loadbalancers)
 (GET "/config" [] loadbalancer-configuration)
 (POST "/" [req] create-loadbalancer)
 (GET "/:loadbalancer-id" [loadbalancer-id] (single-loadbalancer loadbalancer-id))
 (PUT "/:loadbalancer-id" [req] update-loadbalancer))
