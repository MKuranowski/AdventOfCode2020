; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day7a
  (:require [core]
            [clojure.string :refer [join split]]
            [clojure.set :refer [union]]))

(defn parse-contains
  "Parses the 'contains' parts of a rule into a hash-map.
   E.g. '2 shiny gold bags, 9 faded blue bags.'
   into a {'shiny gold' 2, 'faded blue' 9}"
  [words] (loop [words-left words
                 map {}]
            (if (empty? words-left)
              map
              (recur
               (nthrest words-left 4)
               (assoc map (join " " (take 2 (rest words-left))) (core/parse-int (first words-left)))))))

(defn parse-rule
  "Parses a single rule.
   E.g. for 'bright white bags contain 1 shiny gold bag.'
   returns ['bright white' {'shiny gold' 1}].
   If a bag cannot contain other bags, returns ['bag type' nil]."
  [line] (let [type-seq (take 2 line)
               contians (nthrest line 4)]
           [(join " " type-seq)
            (if (= "no" (first contians))
              nil
              (parse-contains contians))]))

(defn get-all-rules
  "Parses all rules from a file.
   Returns a mapping from container bag to another mapping, from
   contained bags to count of those contained bags.
   If a bag doesn't contina other bags, that bag maps to nil.
   E.g. {'bright white' {'shiny gold' 1}, 'faded blue' nil}"
  [filename] (->> filename
                  (core/lines-from-file)
                  (map #(split % #" "))
                  (map parse-rule)
                  (into {})))

(defn add-contained-by
  "Updates a mapping bag type -> possible containers."
  [container contained-bags reverse-map]
  (loop [contained-bags contained-bags, reverse-map reverse-map]
    (if (empty? contained-bags)
      reverse-map
      (recur
       (rest contained-bags)
       (update reverse-map
               (first contained-bags)
               #(if (empty? %) #{container} (conj % container)))))))

(defn find-contained-by
  "Creates a mapping from a bag type -> possible containers,
   Essentially reversing the mapping returned by get-all-rules.
   
   E.g. for {'muted yellow' {'shiny gold' 2, 'faded blue' 9}}
   returns {'shiny gold' #{'muted yellow'}, 'faded blue' #{'muted yellow'}}."
  [rules] (loop [rules rules
                 reverse-map {}]
            (if (empty? rules)
              reverse-map
              (let [[container contains-map] (first rules)
                    contains (keys contains-map)]
                (recur
                 (rest rules)
                 (add-contained-by container contains reverse-map))))))

(defn find-all-contained-by [root reverse-map]
  (loop [can-hold-root (get reverse-map root), expand can-hold-root]
    (if (empty? expand)
      can-hold-root
      (let [expanded (->> expand
                          (map #(get reverse-map %))
                          (apply union))]
        (recur (into can-hold-root expanded) expanded)))))


(defn -main [filename]
  (->> filename
       get-all-rules
       find-contained-by
       (find-all-contained-by "shiny gold")
       count
       prn))
