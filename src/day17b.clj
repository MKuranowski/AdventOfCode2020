; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day17b
  (:require [core]
            [clojure.math.combinatorics :refer [cartesian-product]]
            [clojure.set :refer [union difference]]))

(defn coord-add
  [coords deltas]
  (mapv + coords deltas))

(def neighbor-offsets (rest (cartesian-product [0 1 -1] [0 1 -1] [0 1 -1] [0 1 -1])))

(defn neighbor-coords
  [cube]
  (mapv coord-add (repeat cube) neighbor-offsets))

(defn count-neighbours
  [cubes cube]
  (core/count-if some? (map cubes (neighbor-coords cube))))

(defn cubes-to-remove
  [cubes]
  (apply hash-set
         (filter (complement #(<= 2 (count-neighbours cubes %) 3)) cubes)))

(defn add-cubes-possible-coords
  [cubes coord-idx]
  (let [coords (map #(% coord-idx) cubes)]
    (range (- (apply min coords) 1) (+ (apply max coords) 2))))

(defn cubes-to-add
  [cubes]
  (apply hash-set
         (filter #(and (nil? (cubes %)) (= 3 (count-neighbours cubes %)))
                 (map (partial apply vector)
                      (cartesian-product (add-cubes-possible-coords cubes 0)
                                         (add-cubes-possible-coords cubes 1)
                                         (add-cubes-possible-coords cubes 2)
                                         (add-cubes-possible-coords cubes 3))))))

(defn evolve
  [cubes]
  (union (difference cubes (cubes-to-remove cubes)) (cubes-to-add cubes)))

(defn load-from-file
  [lines]
  (->> (map
        (fn [[line x]] (map list line (repeat x) (iterate inc 0)))
        (map list lines (iterate inc 0)))
       (apply concat)
       (filter #(= (first %) \#))
       (map (fn [[_ x y]] [x y 0 0]))
       (apply hash-set)))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       load-from-file
       (iterate evolve)
       (take 7)
       last
       count
       prn))
