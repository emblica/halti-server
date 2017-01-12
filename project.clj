(defproject halti-server "0.1.1"
  :description "Halti server is container lightweight container orchestration platform"
  :url "https://github.com/emblica/halti-server"
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.1.18"]
                 [compojure "1.5.0"]
                 [com.taoensso/timbre "4.3.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-devel "1.4.0"]
                 [ring/ring-core "1.4.0"]
                 [ring.middleware.logger "0.5.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-time "0.11.0"]
                 [com.novemberain/monger "3.0.2"]
                 [cheshire "5.5.0"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/core.async "0.2.374"]
                 [prismatic/schema "1.1.0"]]
  :main ^:skip-aot halti-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
