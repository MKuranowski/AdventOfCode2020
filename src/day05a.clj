; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day5a
  (:require [core]
            [clojure.string]))

(defn decode-seat-id
  [seat-string]
  (-> seat-string
      (clojure.string/replace #"F|L" "0")
      (clojure.string/replace #"B|R" "1")
      (Integer/parseInt 2)))


(defn -main [filename]
  (->> filename
       (core/lines-from-file)
       (map decode-seat-id)
       (apply max)
       (prn)))
