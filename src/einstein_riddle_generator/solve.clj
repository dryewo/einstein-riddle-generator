(ns einstein-riddle-generator.solve
  (:require [loco.core :as loco]
            [loco.constraints :refer :all]
            [clojure.core.match :refer [match]]))

;; Rule: a DSL item like ['same [:x 0 1] [:x 2 3]]
;; Constraint: Loco-specific object returned by an $-function: ($= [:x 0 1] [:x 2 3])

;; a and b should be like [:x 0 1], not like :dogs
(defn rule->constraint [rule]
  (match rule
    ['same a b] ($= a b)
    ['exact n a] ($= n a)
    ['neighbor a b] ($= 1 ($abs ($- a b)))
    ['left a b] ($= 1 ($- a b))))

;; General constraints of the riddle:
;;  there are N houses, people live one in each house ect.

;; ii - number of features: nationality, color, pets, drink, tobacco
;; jj - number of houses

(defn fixed-constraints' [[ii jj]]
  (concat
    (for [i (range ii)
          j (range jj)]
      ($in [:x i j] 1 jj))
    (for [i (range ii)]
      ($distinct (for [j (range jj)]
                   [:x i j])))))

(def fixed-constraints (memoize fixed-constraints'))

;; size is [ii jj]
(defn solutions [size rules]
  (loco/solutions (concat
                    (map rule->constraint rules)
                    (fixed-constraints size))))

(defn count-solutions [size rules]
  (let [sols (solutions size rules)]
    (if (second sols)
      :many
      (if (first sols)
        :one
        :none))))
