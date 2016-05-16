(ns einstein-riddle-generator.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.tools.cli :as cli]
            [einstein-riddle-generator.view :as v]
            [einstein-riddle-generator.generate :as g])
  (:gen-class))

(defn load-shuffled-lookup []
  (let [lookup (edn/read-string (slurp (io/resource "features.edn")))]
    (cons (shuffle (:nationality lookup)) (shuffle (map shuffle (vals (dissoc lookup :nationality)))))))

(def recommended-length
  {2  1
   3  4
   4  9
   5  15
   6  25
   7  39
   8  55
   9  64
   10 72})

(def cli-options
  [["-d" "--difficulty N" "Number of 'exact' clues"
    :parse-fn #(Integer/parseInt %)]
   ["-l" "--length L" "Number of clues"
    :parse-fn #(Integer/parseInt %)]
   ["-s" "--size S" "Problem size"
    :default 5
    :validate [#(<= 2 % 10) "Must be a number between 2 and 10"]
    :parse-fn #(Integer/parseInt %)]
   [nil "--show-solution" "Show the solution"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["This is my program. There are many like it, but this one is mine."
        ""
        "Usage: program-name [options] action"
        ""
        "Options:"
        options-summary
        "Please refer to the manual page for more information."]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn run [{:keys [difficulty size length show-solution] :or {length (recommended-length size)}}]
  (println "Size:" size "Length:" length "Difficulty:" (or difficulty "none"))
  (let [shuffled-lookup (load-shuffled-lookup)
        rules           (time (v/rules:var->name
                                shuffled-lookup
                                (first (g/good-combinations [size size] length :difficulty difficulty))))]
    (println)
    (clojure.pprint/pprint rules)
    (println)
    (println "Solution:")
    (when-not show-solution
      (println "\u001B[8m"))
    (v/pprint-solution
      (v/solution2d:var->name shuffled-lookup (v/solution->2d (g/make-solution [size size]))))
    (when-not show-solution
      (println "\u001B[28m")
      (println "In order to see the solution, copy the blank lines above to your text editor, or use --show-solution flag next time."))))

(comment
  (run {:size 5}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [options errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    (run options)
    (System/exit 0)))
