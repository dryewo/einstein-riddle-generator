(defproject einstein-riddle-generator "0.1.0-SNAPSHOT"
  :description "Einstein Riddle Generator"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [loco "0.3.1"]
                 [org.clojure/math.combinatorics "0.1.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/tools.cli "0.3.5"]
                 [table "0.5.0"]]
  :main ^:skip-aot einstein-riddle-generator.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev     {:plugins      [[lein-midje "3.2"]]
                       :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                      [org.clojure/java.classpath "0.2.3"]
                                      [criterium "0.4.4"]
                                      [midje "1.8.3"]]}})
