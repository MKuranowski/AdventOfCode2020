; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day13b
  (:require [core]
            [clojure.string :refer [split]]))

(defn parse-bus-lines
  [file-line] (->> (split file-line #",")
                   (map list (iterate inc 0)) ;; similar to Python 'enumerate'
                   (filter #(not= (second %) "x"))
                   (map #(list (first %) (core/parse-int (second %))))))

(defn parse-file [filename]
  (->> filename
       core/lines-from-file
       second
       parse-bus-lines))

(defn modulus-result [[offset modulus]]
  (list
   modulus
   (mod (- modulus offset) modulus)))

;; Stolen from https://rosettacode.org/wiki/Modular_inverse#Recursive_implementation
(defn multiplicative-inverse [a-arg b-arg]
  (loop [a a-arg, b b-arg, s0 1, s1 0]
    (if (= 0 b) (mod s0 b-arg)
        (recur b, (mod a b), s1, (- s0 (* s1 (quot a b)))))))

;; Stolen from https://fangya.medium.com/chinese-remainder-theorem-with-python-a483de81fbb8
(defn chinese-remainder-theroem [mods-and-results]
  (let [prod (reduce * (map first mods-and-results))]
    (mod
     (reduce
      +
      (for [[modulus result] mods-and-results
            :let [p (quot prod modulus)]]
        (* p result (multiplicative-inverse p modulus))))
     prod)))

(defn -main [filename]
  (->> filename
       parse-file
       (map modulus-result)
       chinese-remainder-theroem
       prn))
