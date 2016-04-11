(ns halti-server.events
  (:require [clojure.core.async :as async :refer [go <! chan >! close!]]))



(def bus (chan))

(def subscribers (atom []))

(defn updated! []
  (go (>! bus :updated)))

(defn observe-updates [observer]
  (swap! subscribers conj observer))

(go
  (loop []
    (if-let [signal (<! bus)]
      (do (doseq [cb @subscribers] (cb signal))
          (recur)))))
