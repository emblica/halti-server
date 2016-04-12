(ns halti-server.api.loadbalancers.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]))

(def c (:loadbalancers collection-names))

(defn insert-loadbalancer [& args]
  (apply (partial mc/insert-and-return @mdb c) args))

(defn find-loadbalancer [& args]
  (apply (partial mc/find-one-as-map @mdb c) args))

(defn find-loadbalancers [& args]
  (apply (partial mc/find-maps @mdb c) args))

(defn update-loadbalancer [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
