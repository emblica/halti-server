(ns halti-server.api.instances.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]))

(def c (:instances collection-names))


(defn insert-instance [& args]
  (apply (partial mc/insert-and-return @mdb c) args))

(defn find-instance [& args]
  (apply (partial mc/find-one-as-map @mdb c) args))

(defn find-instances [& args]
  (apply (partial mc/find-maps @mdb c) args))

(defn update-instance [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
