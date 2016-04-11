(ns halti-server.db
  (:require [monger.json]
            [monger.core :refer [connect-via-uri]]
            [monger.joda-time]))

(def collection-names {:instances "instances"
                       :services "services"})

(def mdb (atom nil))



(defn create-connection [uri]
  (let [conn (connect-via-uri uri)]
    (reset! mdb (:db conn))))
