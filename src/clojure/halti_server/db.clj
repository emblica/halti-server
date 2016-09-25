(ns halti-server.db
  (:require [monger.json]
            [monger.core :refer [connect-via-uri]]
            [taoensso.timbre :as timbre :refer [info error warn debug]]
            [monger.joda-time]))

(def collection-names {:instances "instances"
                       :loadbalancers "loadbalancers"
                       :services "services"})

(def mdb (atom nil))



(defn create-connection [uri]
  (let [conn (connect-via-uri uri)]
    (do
      (info "Connection established to mongodb")
      (reset! mdb (:db conn)))))
