; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day1a
  (:require [clojure.math.combinatorics :as combo]
            [core]))

(defn solve [filename how-many-numbers]
  (->> filename
       (core/lines-from-file)
       (map core/parse-int)
       (#(combo/combinations % how-many-numbers))
       (filter #(= (reduce + %) 2020))
       (map #(reduce * %))
       first))

(defn -main [filename]
    (prn (solve filename 2)))
