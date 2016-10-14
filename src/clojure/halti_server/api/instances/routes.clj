(ns halti-server.api.instances.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.instances.crud :refer [single-instance list-instances list-healthy-instances]]
            [halti-server.api.instances.lifecycle :refer [register heartbeat notify]]
            [clj-time.core :as t]))


(defroutes instances-router
 (GET "/" [] list-instances)
 (GET "/healthy" [] list-healthy-instances)
 (POST "/register" [] register)
 (GET "/:instance-id" [instance-id] (single-instance instance-id))
 (POST "/:instance-id/heartbeat" [] heartbeat)
 (POST "/:instance-id/notify" [] notify))
