; Copyright (c) 2022 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day19a
  (:require [core]
            [clojure.string :refer [split includes?]]))

(defn parse-conjunction-rule
  [rule]
  (let [rule_numbers (split rule #" ")]
    (fn [text matchers]
      (loop [matcher (matchers (first rule_numbers))
             rest_rule_numbers (rest rule_numbers)
             text text]
        (if (some? matcher)
          (if-let [text_rest (matcher text matchers)]
            (recur (matchers (first rest_rule_numbers))
                   (rest rest_rule_numbers)
                   text_rest)
            nil)
          text)))))

(defn parse-disjunction-rule
  [rule]
  (let [alternatives (map parse-conjunction-rule (split rule #" \| "))]
    (fn [text matchers]
      (loop [alternative (first alternatives)
             rest_alternatives (rest alternatives)]
        (if (some? alternative)
          (if-some [text_rest (alternative text matchers)]
            text_rest
            (recur (first rest_alternatives)
                   (rest rest_alternatives)))
          nil)))))

(defn parse-char-rule
  [rule]
  (let [c (second rule)]
    (fn [text _] (if (= (first text) c) (rest text) nil))))

(defn parse-rule
  [rule]
  (cond
    (includes? rule "\"") (parse-char-rule rule)
    (includes? rule "|") (parse-disjunction-rule rule)
    :else (parse-conjunction-rule rule)))

(defn parse-rule-line
  [line]
  (let [[_ name rule-text] (re-matches #"(.*): (.*)" line)]
    [name (parse-rule rule-text)]))

(defn parse-rules [lines] (into {} (map parse-rule-line lines)))

(defn matches-zero-fully
  [text matchers]
  (let [r ((matchers "0") text matchers)]
    (and (some? r) (empty? r)))
  )

(defn -main [filename]
  (let [[rule-lines lines] (core/split-on empty? (core/lines-from-file filename))
        matchers (parse-rules rule-lines)]
    (prn (core/count-if #(matches-zero-fully % matchers) lines))))
