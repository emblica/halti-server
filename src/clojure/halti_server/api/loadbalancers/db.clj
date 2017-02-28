(ns halti-server.api.loadbalancers.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]
            [halti-server.utils :refer [->pretty]]))

(def c (:loadbalancers collection-names))

(defn insert-loadbalancer [& args]
  (->pretty (apply (partial mc/insert-and-return @mdb c) args)))
(defn find-loadbalancer [& args]
  (->pretty (apply (partial mc/find-one-as-map @mdb c) args)))

(defn find-loadbalancers [& args]
  (map ->pretty (apply (partial mc/find-maps @mdb c) args)))

(defn find-enabled-loadbalancers []
  (map ->pretty (find-loadbalancers {:enabled true})))

(defn update-loadbalancer [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
