(ns halti-server.utils
  (:require [clojure.data.json :as json]
            [clj-time.core :as t]
            [halti-server.config]
            [clj-time.coerce :as c]))



(defn json-request [status body]
  {:status  status
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str body)})


(defn uuid [] (str (java.util.UUID/randomUUID)))


(defn deadline []
  (t/minus (t/now) (t/seconds halti-server.config/obituary-time)))

(defn day-ago []
  (t/minus (t/now) (t/days 1)))

(defn ->pretty [ob]
  (dissoc ob :_id))



(defn flip [function]
  (fn
    ([] (function))
    ([x] (function x))
    ([x y] (function y x))
    ([x y z] (function z y x))
    ([a b c d] (function d c b a))
    ([a b c d & rest]
     (->> rest
        (concat [a b c d])
        reverse
        (apply function)))))

(extend-type org.joda.time.DateTime
  json/JSONWriter
  (-write [date out]
    (json/-write (c/to-string date) out)))
