(ns halti-server.api.state.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [halti-server.api.state.crud :refer [status]]
            [clj-time.core :as t]))


(defroutes state-router
 (GET "/" [] status))
