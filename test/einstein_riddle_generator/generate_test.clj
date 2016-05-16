(ns einstein-riddle-generator.generate-test
  (:require [midje.sweet :refer :all]
            [einstein-riddle-generator.generate :refer :all]))

(facts ""
  (make-solution [2 2]) => {[:x 0 0] 1, [:x 0 1] 2, [:x 1 0] 1, [:x 1 1] 2})

(facts ""
  (make-exact-rules [2 2])
  => (just [['exact 1 [:x 0 0]]
            ['exact 2 [:x 0 1]]
            ['exact 1 [:x 1 0]]
            ['exact 2 [:x 1 1]]]
           :in-any-order))

(facts ""
  (make-same-rules [2 2])
  => (just [['same [:x 0 0] [:x 1 0]]
            ['same [:x 0 1] [:x 1 1]]]
           :in-any-order))

(facts ""
  (make-left-rules [2 2])
  => (just [['left [:x 0 0] [:x 0 1]]
            ['left [:x 0 0] [:x 1 1]]
            ['left [:x 1 0] [:x 0 1]]
            ['left [:x 1 0] [:x 1 1]]]
           :in-any-order))

(facts ""
  (make-neighbor-rules [2 2])
  => (just [['neighbor [:x 0 0] [:x 0 1]]
            ['neighbor [:x 0 0] [:x 1 1]]
            ['neighbor [:x 1 0] [:x 0 1]]
            ['neighbor [:x 1 0] [:x 1 1]]]
           :in-any-order))

(facts ""
  (difficulty-level []) => 0
  (difficulty-level [['foo 3 4] ['exact 1 2]]) => 1)
