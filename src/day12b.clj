; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day12b
  (:require [core]
            [day12a :refer [read-navi manhattan-dist]]))

(def rot-lookup-R
  {90 #(list %2 (- %1))
   180 #(list (- %1) (- %2))
   270 #(list (- %2) %1)})

(def rot-lookup-L
  {90 #(list (- %2) %1)
   180 #(list (- %1) (- %2))
   270 #(list %2 (- %1))})


(defn exec-navi [all-instructions]
  (loop [instructions all-instructions
         e 0
         n 0
         be 10
         bn 1]
    (if (seq instructions)
      (let [[i arg] (first instructions)]
        (case i
          \N (recur (rest instructions) e n be (+ bn arg))
          \E (recur (rest instructions) e n (+ be arg) bn)
          \S (recur (rest instructions) e n be (- bn arg))
          \W (recur (rest instructions) e n (- be arg) bn)
          \L (let [[new-be new-bn] ((rot-lookup-L arg) be bn)]
               (recur (rest instructions) e n new-be new-bn))
          \R (let [[new-be new-bn] ((rot-lookup-R arg) be bn)]
               (recur (rest instructions) e n new-be new-bn))
          \F (let [delta-e (* arg be), delta-n (* arg bn)]
               (recur (rest instructions) (+ e delta-e) (+ n delta-n) be bn))))
      (list e n))))

(defn -main [filename]
  (->> filename
       read-navi
       exec-navi
       manhattan-dist
       prn))
