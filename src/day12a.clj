; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day12a
  (:require [core]
            [clojure.math.numeric-tower :refer [abs]]))

(def rot-lookup
  {\N {90 \E, 180 \S, 270 \W}
   \E {90 \S, 180 \W, 270 \N}
   \S {90 \W, 180 \N, 270 \E}
   \W {90 \N, 180 \E, 270 \S}})

(defn read-navi [filename]
  (->> filename
       core/lines-from-file
       (map #(list (first %) (core/parse-int (subs % 1))))))

(defn exec-navi [all-instructions]
  (loop [instructions all-instructions
         e 0
         n 0
         heading \E]
    (if (seq instructions)
      (let [[i arg] (first instructions)]
          (case i
            \N (recur (rest instructions) e (+ n arg) heading)
            \E (recur (rest instructions) (+ e arg) n heading)
            \S (recur (rest instructions) e (- n arg) heading)
            \W (recur (rest instructions) (- e arg) n heading)
            \L (recur (rest instructions) e n ((rot-lookup heading) (- 360 arg)))
            \R (recur (rest instructions) e n ((rot-lookup heading) arg))
            \F (recur (cons (list heading arg) (rest instructions)) e n heading)))
      (list e n))))

(defn manhattan-dist
  [[x y]] (+ (abs x) (abs y)))

(defn -main [filename]
  (->> filename
       read-navi
       exec-navi
       manhattan-dist
       prn))
