(ns einstein-riddle-generator.solve-test
  (:require [midje.sweet :refer :all]
            [einstein-riddle-generator.solve :refer :all]))

(facts ""
  (solutions [2 2] [['left [:x 0 0] [:x 1 1]]]) => [{[:x 0 0] 2, [:x 0 1] 1, [:x 1 0] 2, [:x 1 1] 1}])

(facts ""
  (count-solutions [2 2] [['left [:x 0 0] [:x 1 1]] ['left [:x 1 1] [:x 0 0]]]) => :none
  (count-solutions [2 2] []) => :many
  (count-solutions [2 2] [['left [:x 0 0] [:x 1 1]]]) => :one)
