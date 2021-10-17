; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day5b
  (:require [core]
            [day5a :refer [decode-seat-id]]
            [clojure.set :refer [difference]]))

(def all-seats (set (range 0 889))) ; 888 is the highest seat ID, as per part 1

(defn set-of-reserved-seats
  [filename] (->> filename
                  (core/lines-from-file)
                  (map decode-seat-id)
                  (set)))

(defn -main [filename]
  (let [reserved-seats (set-of-reserved-seats filename)
        empty-seats (difference all-seats reserved-seats)]
    (->> empty-seats
         (filter #(and (get reserved-seats (inc %))
                       (get reserved-seats (dec %))))
         (first)
         (prn))))
