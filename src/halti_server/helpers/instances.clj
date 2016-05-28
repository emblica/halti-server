(ns halti-server.helpers.instances)


(defn hosts->containers [host]
  (map #(assoc % :host-instance-id (:instance_id host)) (:containers host)))
