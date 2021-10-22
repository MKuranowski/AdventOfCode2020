; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day13a
  (:require [core]
            [clojure.string :refer [split]]))

(defn parse-bus-lines
  [file-line] (->> (split file-line #",")
                   (filter #(not= % "x"))
                   (map core/parse-int)))

(defn parse-file [filename]
  (let [lines (core/lines-from-file filename)]
    (list (core/parse-int (first lines))
          (parse-bus-lines (second lines)))))

(defn next-departure-after [bus-id time]
  (list bus-id
        (- bus-id (mod time bus-id))))

(defn -main [filename]
  (let [[time buses] (parse-file filename)
        time (float time)]
    (->> buses
         (map #(next-departure-after % time))
         (apply min-key second)
         (apply *)
         (prn))))
