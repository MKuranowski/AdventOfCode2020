; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns core
  (:require [clojure.java.io :refer [file]]
            [clojure.string :refer [split-lines]]))

(defn parse-int
  "Tries to parse a base-10 integer"
  [s] (Integer/parseInt s 10))

(defn lines-from-file
  "Returns a vactor of all lines in a file"
  [filename]
  (split-lines (slurp (file filename))))

