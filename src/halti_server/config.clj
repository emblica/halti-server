(ns halti-server.config)


(def heartbeat-interval 10) ; 10s
(def missing-heartbeat-count 2)
(def obituary-time (* missing-heartbeat-count heartbeat-interval))  ; 2 times the heartbeat
