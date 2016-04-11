(ns halti-server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.routes :refer [api-router]]))



(defn hello [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})


(defroutes main-router
 (GET "/" [] hello)
 (context "/api/v1" [] api-router))
 ;(route/not-found "<h1>Page not found</h1>"))
