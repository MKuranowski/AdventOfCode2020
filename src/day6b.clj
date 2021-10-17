; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day6b
  (:require [day6a :refer [answer-groups]]
            [clojure.set :refer [intersection]]))

(defn common-answers
  [group]
  (->> group
       (map set)
       (apply intersection)))

(defn -main [filename]
  (->> filename
       answer-groups
       (map common-answers)
       (map count)
       (reduce +)
       (prn)))
