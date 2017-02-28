(ns halti-server.api.instances.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]
            [halti-server.utils :refer [deadline]]))

(def c (:instances collection-names))
(def e (:instance-events collection-names))


(defn insert-instance [& args]
  (dissoc (apply (partial mc/insert-and-return @mdb c) args) :_id))

(defn insert-instance-event [& args]
  (dissoc (apply (partial mc/insert-and-return @mdb e) args) :_id))

(defn find-instance [& args]
  (dissoc (apply (partial mc/find-one-as-map @mdb c) args) :_id))

(defn find-instances [& args]
  (pmap #(dissoc % :_id) (apply (partial mc/find-maps @mdb c) args)))

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
