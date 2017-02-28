(ns halti-server.api.instances.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]
            [halti-server.utils :refer [deadline day-ago ->pretty]]))

(def c (:instances collection-names))
(def e (:instance-events collection-names))


(defn insert-instance [& args]
  (->pretty (apply (partial mc/insert-and-return @mdb c) args)))

(defn insert-instance-event [& args]
  (->pretty (apply (partial mc/insert-and-return @mdb e) args)))

(defn find-instance [& args]
  (->pretty (apply (partial mc/find-one-as-map @mdb c) args)))

(defn find-instances [& args]
  (map ->pretty (apply (partial mc/find-maps @mdb c) args)))

(defn find-events [& args]
  (map ->pretty (apply (partial mc/find-maps @mdb e) args)))

(defn find-latest-events [query]
  (find-events (merge query {:timestamp {"$gt" (day-ago)}})))


(defn find-allocated-instances [service-id]
  (find-instances {:config.containers service-id}))

(defn find-healthy-hosts []
  (find-instances {:last_heartbeat {"$gt" (deadline)}}))

(defn update-instance [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
