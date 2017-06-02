(ns halti-server.api.state.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]
            [halti-server.utils :refer [deadline day-ago ->pretty]]))


(def s (:scheduler collection-names))

; (defn find-service [& args]
;   (->pretty (apply (partial mc/find-one-as-map @mdb c) args)))

(defn find-unscheduled-containers [& args]
  (:containers (->pretty (mc/find-one-as-map @mdb s {:key "unscheduled-containers"}))))
