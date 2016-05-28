(ns halti-server.api.loadbalancers.db
  (:require [halti-server.db :refer [mdb collection-names]]
            [monger.collection :as mc]
            [monger.result :refer [updated-existing?]]))

(def c (:loadbalancers collection-names))

(defn insert-loadbalancer [& args]
  (dissoc (apply (partial mc/insert-and-return @mdb c) args) :_id))

(defn find-loadbalancer [& args]
  (dissoc (apply (partial mc/find-one-as-map @mdb c) args) :_id))

(defn find-loadbalancers [& args]
  (pmap #(dissoc % :_id) (apply (partial mc/find-maps @mdb c) args)))

(defn find-enabled-loadbalancers []
  (pmap #(dissoc % :_id) (find-loadbalancers {:enabled true})))

(defn update-loadbalancer [& args]
  (updated-existing? (apply (partial mc/update @mdb c) args)))
