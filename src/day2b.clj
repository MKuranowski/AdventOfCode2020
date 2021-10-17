; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day2b
  (:require [core]))

(defn either [a b] (and (or a b) (not (and a b))))
(defn is-valid? [line]
  (let [[_ i1-str i2-str letter-str password] (re-matches #"(\d+)-(\d+) (\w): (\w+)" line)
        i1 (dec (core/parse-int i1-str))
        i2 (dec (core/parse-int i2-str))
        letter (first letter-str)]
    (either (= letter (nth password i1)) (= letter (nth password i2)))))

(defn -main [filename]
  (->> filename
       (core/lines-from-file)
       (map is-valid?)
       (filter true?)
       (count)
       prn))
