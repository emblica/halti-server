(ns halti-server.api.services.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.services.crud :refer [single-service list-services create-service update-service]]
            [clj-time.core :as t]))


(defroutes services-router
 (GET "/" [] list-services)
 (POST "/" [req] create-service)
 (GET "/:service-id" [service-id] (single-service service-id))
 (PUT "/:service-id" [req] update-service))
