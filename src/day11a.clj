; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day11a
  (:require [core]
            [clojure.math.combinatorics :refer [cartesian-product]]
            [clojure.string :refer [join]]))

(defn adjecent-seats
  [r c] (->> (cartesian-product [(dec r) r (inc r)] [(dec c) c (inc c)])
             (filter #(or (not= (first %) r) (not= (second %) c)))))

(defn count-adjecent-occupied
  [r c table] (->> (adjecent-seats r c)
                   (map #(get (get table (first %)) (second %)))
                   (filter #(= % \#))
                   (count)))

(defn new-value [r c table]
  (case (get (get table r) c)
    \L (if (= 0 (count-adjecent-occupied r c table)) \# \L)
    \# (if (<= 4 (count-adjecent-occupied r c table)) \L \#)
    \.))

(defn new-row [r table]
  (->> (range (count (table r)))
       (map #(new-value r % table))
       (apply str)))

(defn new-table [table]
  (->> (range (count table))
       (map #(new-row % table))
       vec))

(defn new-table-until-stable [table]
  (loop [t table]
    (let [nt (new-table t)]
      (if (= t nt)
        t
        (recur nt)))))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       (new-table-until-stable)
       (join "\n")
       (core/count-if #(= % \#))
       (prn)))
