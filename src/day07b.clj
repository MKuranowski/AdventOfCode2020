; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day7b
  (:require [day7a :refer [get-all-rules]]))

(defn count-cotained-bags [bag-name rules]
  (->> (get rules bag-name)
       (map (fn [[contained-bag count]]
              (+ count (* count (count-cotained-bags contained-bag rules)))))
       (apply +)
       ))

(defn -main [filename]
  (->> filename
       get-all-rules
       (count-cotained-bags "shiny gold")
       prn))
