(ns halti-server.config)


(def heartbeat-interval 10) ; 10s
(def missing-heartbeat-count 5)
(def obituary-time (* missing-heartbeat-count heartbeat-interval))  ; 5 times the heartbeat
