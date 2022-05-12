; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day9b
  (:require [core]))

(defn all-subvectors
  ([v] (->> (range 2 (inc (count v)))  ; We only care abour sub-vectors of length at least 2
            (map #(all-subvectors v %))
            (apply concat)))
  ([v length] (all-subvectors v length 0))
  ([v length start-at]
   (let [end-at (+ start-at length)]
     (if (<= end-at (count v))
       (lazy-seq (cons (subvec v start-at end-at) (all-subvectors v length (inc start-at))))))))

(defn find-subvector-summing-to [v sum]
  (first (filter #(= (reduce + %) sum) (all-subvectors v))))

; Because we sum all numbers at once, in (find-subv-...) we need to use bigints
(defn -main [filename]
  (let [all-numbers (vec (map bigint (core/lines-from-file filename)))
        expected-sum (if (= filename "input/9btest.txt") 127 552655238)
        subv (find-subvector-summing-to all-numbers expected-sum)]
    (prn (+ (apply min subv) (apply max subv)))))
