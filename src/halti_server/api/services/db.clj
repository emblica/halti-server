(ns halti-server.api.services.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing? acknowledged?]]))

(def c (:services collection-names))

(defn insert-service [& args]
  (apply (partial mc/insert-and-return @mdb c) args))

(defn find-service [& args]
  (apply (partial mc/find-one-as-map @mdb c) args))

(defn find-services [& args]
  (apply (partial mc/find-maps @mdb c) args))

(defn update-service [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))

(defn remove-service [& args]
  (let [result (apply (partial mc/remove @mdb c) args)
        ack? (acknowledged? result)]
    (and ack? (pos? (.getN result)))))
