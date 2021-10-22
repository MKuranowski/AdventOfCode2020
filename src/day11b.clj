; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day11b
  (:require [core]
            [clojure.string :refer [join]]))

(def all-next-coords
  [#(list (identity %1) (dec %2))
   #(list (identity %1) (inc %2))
   #(list (inc %1) (identity %2))
   #(list (inc %1) (inc %2))
   #(list (inc %1) (dec %2))
   #(list (dec %1) (identity %2))
   #(list (dec %1) (inc %2))
   #(list (dec %1) (dec %2))])

(defn is-occupied-in-dir [r c next-coords table]
  (loop [[r c] (next-coords r c)]
    (case (get (get table r) c)
      \# true
      \. (recur (next-coords r c))
      false)))

(defn count-visible-occupied [r c table]
  (->> all-next-coords
       (map #(is-occupied-in-dir r c % table))
       (core/count-if true?)))

(defn new-value [r c table]
  (case (get (get table r) c)
    \L (if (= 0 (count-visible-occupied r c table)) \# \L)
    \# (if (<= 5 (count-visible-occupied r c table)) \L \#)
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
