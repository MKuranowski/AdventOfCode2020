; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day6a
  (:require [core]
            [clojure.string :refer [join]]))

(defn answer-groups
  [filename]
  (->> filename
       core/lines-from-file
       (core/split-on empty?)))

(defn yes-answers
  [group]
  (->> group
       (join "")
       (set)))

(defn -main [filename]
  (->> filename
       answer-groups
       (map yes-answers)
       (map count)
       (reduce +)
       (prn)))
