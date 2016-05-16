(ns einstein-riddle-generator.generate
  (:require [clojure.math.combinatorics :as c]
            [einstein-riddle-generator.solve :as s]
            [clojure.core.reducers :as r]))

;; Constructs a canonical solution
(defn make-solution [[ii jj]]
  (into (sorted-map)
        (for [i (range ii)
              j (range jj)]
          [[:x i j] (inc j)])))

;; Makes all rules that state exact value of each var
(defn make-exact-rules [size]
  (for [[v n] (make-solution size)]
    ['exact n v]))

;; Makes all rules that state equality for each possible pair of vars
(defn make-same-rules [[ii jj]]
  (for [j (range jj)
        p (c/combinations (for [i (range ii)] [:x i j]) 2)]
    (apply vector 'same p)))

(defn make-bind-rules [constr-name [ii jj]]
  (for [h  (range (dec jj))
        i1 (range ii)
        i2 (range ii)]
    [(symbol constr-name) [:x i1 h] [:x i2 (inc h)]]))

;; Makes al rules that state "A is to the left of B"
(defn make-left-rules [size]
  (make-bind-rules 'left size))

;; Makes all rules that state "A is a neighbor of B"
(defn make-neighbor-rules [size]
  (make-bind-rules 'neighbor size))

(defn make-all-rules [size]
  (concat (make-exact-rules size)
          (make-same-rules size)
          (make-left-rules size)
          (make-neighbor-rules size)))

;; Returns a random combination of length k from items.
(defn take-n-shuffled [items k]
  (take k (shuffle items)))

(defn random-combinations [all-rules length]
  (repeatedly #(take-n-shuffled all-rules length)))

(defn make-fold-fn [size]
  (fn
    ([] [])
    ([acc x]
     (let [res (s/count-solutions size x)]
       (if (= :one res)
         (conj acc x)
         acc)))))

;; Explores solutions using random samples of increasing length
;; Returns length of the first good combination found
(defn find-minimum-length [size sample-size & {:keys [start] :or {start 1}}]
  (let [all-rules  (make-all-rules size)
        max-length (count all-rules)]
    ((fn step [length]
       (when (<= length max-length)
         (println "Processing length:" length)
         (println (c/count-combinations all-rules length) "combinations total")
         (let [rule-combinations (into [] (take sample-size (random-combinations all-rules length)))
               results           (r/fold concat (make-fold-fn size) rule-combinations)]
           (println (count results) "combinations yilded :one")
           (if (seq results)
             length
             (recur (inc length))))))
      start)))

(comment
  (count (make-all-rules [10 10]))
  (time (find-minimum-length [3 3] 500))                    ; 4
  (time (find-minimum-length [4 4] 2000 :start 7))          ; 8
  (time (find-minimum-length [5 5] 2000 :start 13))         ; 14
  (time (find-minimum-length [6 6] 10000 :start 23))        ; 24
  (time (find-minimum-length [7 7] 10000 :start 35))        ; 37
  (time (find-minimum-length [8 8] 10000 :start 48))        ; 52
  (time (find-minimum-length [9 9] 10000 :start 60))        ; 63
  (time (find-minimum-length [10 10] 1000 :start 65))       ; 69
  )

;; Returns number of 'exact rules
;; 0 is the hardest
;; The more 'exact rules, the easier
(defn difficulty-level [rules]
  (->> rules
       (map first)
       (filter #{'exact})
       count))

;; Good rule combination has exactly one solution
(defn good-combinations [size length & {:keys [difficulty]}]
  ;(prn size length difficulty)
  (let [all-rules    (make-all-rules size)
        combinations (random-combinations all-rules length)
        skipped      (atom 0)]
    (println "Total possible combinations:" (c/count-combinations all-rules length))
    (->> combinations
         (pmap #(if (= :one (s/count-solutions size %))
                 (do (println "Skipped" @skipped) [% true])
                 (do (swap! skipped inc) [% false])))
         (filter second)
         (map first)
         (filter #(or (nil? difficulty)
                      (= difficulty (difficulty-level %)))))))
