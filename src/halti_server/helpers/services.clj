(ns halti-server.helpers.services
  (:require [halti-server.helpers.instances :refer [hosts->containers]]
            [halti-server.helpers.containers :refer [container->service-port-pairs]]))


(defn- service-port-pairs->grouped-addresses [service-port-pairs]
  (group-by :service_id service-port-pairs))


(defn ports-by-services [hosts]
  (service-port-pairs->grouped-addresses
                      (mapcat container->service-port-pairs
                        (mapcat hosts->containers hosts))))
