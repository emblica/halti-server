(ns halti-server.api.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.utils :refer [json-request]]
            [halti-server.api.instances.routes :refer [instances-router]]
            [halti-server.api.services.routes :refer [services-router]]
            [ring.middleware.json :refer [wrap-json-body]]))


(defn api-listing [req]
  (json-request 200 {:endpoints ["instances" "services"]}))



(def api-router
  (wrap-json-body
    (routes
      (GET "/" [] api-listing)
      (context "/instances" [] instances-router)
      (context "/services" [] services-router))
    {:keywords? true}))
