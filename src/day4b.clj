; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day4b
  (:require [core]
            [day4a :refer [passports]]))

(def valid-regexes
  {"byr" #"^19[2-9][0-9]|200[0-2]$"
   "iyr" #"^201[0-9]|2020$"
   "eyr" #"^202[0-9]|2030$"
   "hgt" #"^(1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in$"
   "hcl" #"^#[0-9a-f]{6}$"
   "ecl" #"^amb|blu|brn|gry|grn|hzl|oth$"
   "pid" #"^[0-9]{9}$"})

(defn valid-field?
  [passport key regex]
  ((complement nil?) (re-matches regex (get passport key ""))))

(defn valid-passport?
  [passport]
  (->> (map (fn [[key regex]] (valid-field? passport key regex)) valid-regexes)
       (every? true?)))

(defn -main [filename]
  (->> filename
       (core/lines-from-file)
       (passports)
       (map valid-passport?)
       (filter true?)
       (count)
       (prn)))
