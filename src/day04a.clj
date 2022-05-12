; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day4a
  (:require [core]
            [clojure.string :refer [split]]
            [clojure.set :refer [subset?]]))

(def required-fields
  #{"byr" "iyr" "eyr" "hgt"
    "hcl" "ecl" "pid"})

(defn parse-passport
  [passport-lines]
  (->> passport-lines
       (map #(split % #"\s"))
       (flatten)
       (map #(split % #":"))
       (flatten)
       (apply hash-map)))

(defn passports
  [lines-from-file]
  (->> lines-from-file
       (core/split-on empty?)
       (map parse-passport)))

(defn valid-passport?
  [passport]
  (->> passport
       (keys)
       (apply hash-set)
       (subset? required-fields)))

(defn -main [filename]
  (->> filename
       (core/lines-from-file)
       (passports)
       (map valid-passport?)
       (filter true?)
       (count)
       (prn)))
