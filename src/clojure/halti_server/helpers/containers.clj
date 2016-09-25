(ns halti-server.helpers.containers)

(defn- decorated-port-f [base]
  (fn port->beautiful-port [port]
    (merge base
      {:address (:IP port)
       :port (:PublicPort port)
       :source (:PrivatePort port)})))

(defn container->service-port-pairs [container]
  (let [ports (:Ports container)
        service-id (subs (first (:Names container)) 1)
        instance-id (:host-instance-id container)
        port->beautiful-port (decorated-port-f {:service_id service-id
                                                :instance_id instance-id})] ;})]
    (map port->beautiful-port ports)))
