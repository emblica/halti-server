(ns halti-server.api.state.crud
  (:require [halti-server.utils :refer [json-request uuid]]
            [halti-server.api.state.db :refer [find-unscheduled-containers]]
            [clj-time.core :as t]
            [halti-server.utils :refer [deadline]]))



(defn status [req]
  (let [unscheduled-containers (find-unscheduled-containers)]
    (json-request 200 {:unscheduled unscheduled-containers})))
