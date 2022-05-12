; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day3b
  (:require [core]
            [day3a :refer [count-trees]]))

(def steps [[1 1] [3 1] [5 1] [7 1] [1 2]])
(defn -main [filename]
  (prn (let [rows (core/lines-from-file filename)]
         (reduce
          *
          (map
           (fn [[step-right step-down]] (count-trees rows 0 step-right step-down))
           steps)))))
