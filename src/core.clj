; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns core
  (:require [clojure.java.io :refer [file]]
            [clojure.string :refer [split-lines]]))

(defn parse-int
  "Tries to parse a base-10 integer"
  [s] (Integer/parseInt s 10))

(defn lines-from-file
  "Returns a vector of all lines in a file"
  [filename]
  (split-lines (slurp (file filename))))

(defn split-on
  "Splits a sequence by sub-sequences for which (pred elem) is true.
   The provided predicate must return pure booleans."
  [pred coll]
  (let [!pred (complement pred)]
    (->> coll (partition-by pred) (filter #(!pred (first %))))))

(def non-nil? (complement nil?))
