(ns halti-server.core
  (:require [halti-server.routes :refer [main-router]]
            [org.httpkit.server :refer [run-server]]
            [compojure.handler :refer [site]]
            [taoensso.timbre :as timbre :refer [info error warn debug]]
            [ring.middleware.reload :as reload]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [halti-server.service-scheduler :as sc]
            [halti-server.db :refer [create-connection]])
  (:gen-class))


(defn env [k v]
  (or (System/getenv K) v))

(def PORT (env "PORT" 4040))
(def MONGO_URI (env "MONGO_URI" "mongodb://192.168.99.100:32768/halti"))

(def in-dev? (= "no" (env "PRODUCTION" "no")))


(defn wrap-with-simple-logging [handler]
  (fn [request]
    (info (:request-method request) " - " (:uri request))
    (handler request)))


(defn -main
  "Halti-server main function"
  [& args]
  (info "Halti - starting up! server @ 0.0.0.0:" PORT)
  (let [handler (if in-dev?
                  (reload/wrap-reload (site #'main-router)) ;; only reload when dev
                  (site main-router))]
    (create-connection MONGO_URI) ; Initialize mongodb
    (sc/start-scheduler!)
    (run-server (wrap-with-simple-logging handler) {:port PORT})))
