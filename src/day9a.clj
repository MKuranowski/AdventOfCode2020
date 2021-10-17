; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day9a
  (:require [core]
            [clojure.math.combinatorics :refer [combinations]]))

(defn is-valid? [number summands]
  (some (fn [[a b]] (= number (+ a b))) (combinations summands 2)))

(defn enqueue-number [number summands]
  (conj (subvec summands 1) number))

(defn find-invalid [first-summands numbers]
  (loop [summands first-summands
         numbers numbers]
    (if (is-valid? (first numbers) summands)
      (recur (enqueue-number (first numbers) summands) (rest numbers))
      (first numbers))))

(defn -main [filename]
  (let [all-numbers (map core/parse-int (core/lines-from-file filename))]
    (prn (find-invalid (vec (take 25 all-numbers)) (nthrest all-numbers 25)))))
