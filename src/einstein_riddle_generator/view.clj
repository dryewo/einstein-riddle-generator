(ns einstein-riddle-generator.view
  (:require [clojure.core.match :refer [match]]
            [table.core :as tbl])
  (:import (java.util List)))

;; Examples:

;; Names: :swede, :dog, :dunhill
;; Vars: [:x 1 2], [:x 0 4], [:x 2 3]
;; Rules: ['same :swede :dog], ['exact 1 [:x 2 2]]
;; Lookup2d: [[:brit :swede] [:fish :cats]]

(defn index-of [^List arr x]
  (let [res (.indexOf arr x)]
    (when-not (neg? res) res)))

(defn index-of-2d [arr2d x]
  (loop [i    0
         rows arr2d]
    (if (empty? rows)
      nil
      (if-let [j (index-of (first rows) x)]
        [i j]
        (recur (inc i) (rest rows))))))

;; :swede -> [0 1]
;; 42 -> nil
(defn name->ij [lookup2d nn]
  (index-of-2d lookup2d nn))

;; [0 1] -> :swede
;; 42 -> 42
(defn ij->name [lookup2d ij]
  (match ij
    [i j] (-> lookup2d (nth i) (nth j))
    _ nil))

;; :swede -> [:x 0 1]
;; :42 -> 42
(defn name->var [lookup2d nn]
  (if-let [ij (name->ij lookup2d nn)]
    (apply vector :x ij)
    nn))

;; [:x 0 1] -> :swede
;; 42 -> 42
(defn var->name [lookup2d vv]
  (match vv
    [_ i j] (ij->name lookup2d [i j])
    _ vv))

;; ['same :swede :swede] -> ['same [:x 0 1] [:x 0 1]]
(defn rule:name->var [lookup2d [rule a b]]
  [rule (name->var lookup2d a) (name->var lookup2d b)])

;; ['same [:x 0 1] [:x 0 1]] -> ['same :swede :swede]
(defn rule:var->name [lookup2d [r a b]]
  [r (var->name lookup2d a) (var->name lookup2d b)])

;; [['same :swede :swede] ... ]-> [['same [:x 0 1] [:x 0 1]] ... ]
(defn rules:name->var [lookup2d rules]
  (map (partial rule:name->var lookup2d) rules))

;; [['same [:x 0 1] [:x 0 1]] ... ] -> [['same :swede :swede] ... ]
(defn rules:var->name [lookup2d rules]
  (map (partial rule:var->name lookup2d) rules))

;; {[:x 0 0] 1, [:x 0 1] 2, [:x 1 0] 1, [:x 1 1] 2} -> [[[:x 0 0] [:x 1 0]] [[:x 0 1] [:x 1 1]]]
(defn solution->2d [solution]
  (for [[_ xs] (into (sorted-map) (group-by second solution))]
    (->> xs
         (map first)
         sort)))

;; [[[:x 0 0] [:x 1 0]] [[:x 0 1] [:x 1 1]]] -> [[:brit :fish] [:swede :cats]]
(defn solution2d:var->name [lookup2d solution2d]
  (map (partial map (partial var->name lookup2d)) solution2d))

(defn pprint-solution [solution]
  (let [col-count                   (count (first solution))
        header-row                  (cons "" (range 1 (inc col-count)))
        solution-with-numbered-rows (map-indexed #(cons (inc %1) %2) solution)
        rows-with-header            (cons header-row solution-with-numbered-rows)]
    (tbl/table rows-with-header)))
