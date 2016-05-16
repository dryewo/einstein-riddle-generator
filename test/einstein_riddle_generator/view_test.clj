(ns einstein-riddle-generator.view-test
  (:require [midje.sweet :refer :all]
            [einstein-riddle-generator.view :refer :all]))

(facts ""
  (index-of [1 2 3] 3) => 2
  (index-of [1 2] 1) => 0
  (index-of [1 2 3] 4) => nil)

(facts ""
  (index-of-2d [[1 2] [3 4]] 1) => [0 0]
  (index-of-2d [[1 2] [3 4]] 4) => [1 1])

(def lookup2d [[:brit :swede] [:fish :cats]])

(facts ""
  (name->ij lookup2d :fish) => [1 0]
  (name->ij lookup2d 42) => nil
  (ij->name lookup2d [1 0]) => :fish
  (ij->name lookup2d 42) => nil)

(facts ""
  (name->var lookup2d :brit) => [:x 0 0]
  (name->var lookup2d 42) => 42
  (var->name lookup2d [:x 1 0]) => :fish
  (var->name lookup2d 42) => 42)

(facts ""
  (rule:name->var lookup2d ['exact 1 :fish]) => ['exact 1 [:x 1 0]]
  (rule:var->name lookup2d ['exact 1 [:x 1 0]]) => ['exact 1 :fish])

(facts ""
  (rules:name->var lookup2d [['exact 1 :fish]]) => [['exact 1 [:x 1 0]]]
  (rules:var->name lookup2d [['exact 1 [:x 1 0]]]) => [['exact 1 :fish]])

(facts ""
  (solution->2d {[:x 0 0] 1, [:x 0 1] 2, [:x 1 0] 1, [:x 1 1] 2})
  => [[[:x 0 0] [:x 1 0]] [[:x 0 1] [:x 1 1]]])

(facts ""
  (solution2d:var->name lookup2d [[[:x 0 0] [:x 1 0]] [[:x 0 1] [:x 1 1]]])
  => [[:brit :fish] [:swede :cats]])
