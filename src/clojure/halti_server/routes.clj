(ns halti-server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.routes :refer [api-router]]
            [halti-server.events :as events]))



(defn hello [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "HALTI, FOR YOUR CONTAINERS"})


(defroutes main-router
 (GET "/" [] hello)
 (context "/api/v1" [] api-router))
 ;(route/not-found "<h1>Page not found</h1>"))
