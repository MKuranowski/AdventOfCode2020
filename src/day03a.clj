; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day3a
  (:require [core]))

(defn count-trees [rows idx step-right step-down]
  (if-let [row (first rows)]
    (+
     (if (= \# (nth (cycle row) idx \0)) 1 0)
     (count-trees (drop step-down rows) (+ idx step-right) step-right step-down))
    0))

(defn -main [filename]
  (prn (count-trees (core/lines-from-file filename) 0 3 1)))
