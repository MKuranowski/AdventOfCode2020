; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day2a
  (:require [core]))

(defn is-valid? [line]
  (let [[_ low up letter password] (re-matches #"(\d+)-(\d+) (\w): (\w+)" line)]
    (<= (core/parse-int low)
        (->> (re-seq (re-pattern letter) password) (count))
        (core/parse-int up))))

(defn -main [filename]
  (->> filename
       (core/lines-from-file)
       (map is-valid?)
       (filter true?)
       (count)
       prn))
