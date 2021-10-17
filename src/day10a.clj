; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day10a
  (:require [core]))

(defn read-adapters [filename]
  (->> filename
       core/lines-from-file
       (map core/parse-int)
       sort))

(defn adapters-with-socket-and-device [filename]
  (let [adapters-with-socket (into [0] (read-adapters filename))]
    (conj adapters-with-socket (+ 3 (peek adapters-with-socket)))))

(defn differences [adapters]
  (->> (map list adapters (rest adapters)) ; Pair element with its next
               (map (fn [[a b]] (- b a))))
          )

(defn solve [differences]
  (* (core/count-if #(= 1 %) differences) (core/count-if #(= 3 %) differences)))

(defn -main [filename]
  (->> filename
       adapters-with-socket-and-device
       differences
       solve
       prn))
