(ns halti-server.api.instances.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]
            [halti-server.utils :refer [deadline]]))

(def c (:instances collection-names))


(defn insert-instance [& args]
  (dissoc (apply (partial mc/insert-and-return @mdb c) args) :_id))

(defn find-instance [& args]
  (dissoc (apply (partial mc/find-one-as-map @mdb c) args) :_id))

(defn find-instances [& args]
  (pmap #(dissoc % :_id) (apply (partial mc/find-maps @mdb c) args)))


(defn find-healthy-hosts []
  (find-instances {:last_heartbeat {"$gt" (deadline)}}))

(defn update-instance [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
