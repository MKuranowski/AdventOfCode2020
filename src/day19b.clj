; Copyright (c) 2022 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day19b
  (:require [core]
            [clojure.string :refer [split includes?]]
            [clojure.set :refer [union]]))

; A "matcher" is a predicate with the following signature:
; (String, Map<String, matcher>) -> Set<String>
;
; A matcher must return a set of all possible matches, to accomodate recursive/loopy rules
;
; On no match, an empty set must be returned.
; On a full match, the returned set must contain "".

(defn parse-conjunction-rule
  [rule]
  (let [rule-numbers (split rule #" ")]
    (fn [text matchers]
      ; Loop over every matcher in the conjunction, starting with the input set only containing the input text
      (loop [matcher (matchers (first rule-numbers))
             rest-rules (rest rule-numbers)
             inputs #{text}]
        (if (some? matcher)
          ; Move to the next matcher, updating the input set
          ; with every possible output of the matcher on every possible input.
          ; Note that on no match, an empty set will be passed through.
          (recur (matchers (first rest-rules))
                 (rest rest-rules)
                 (apply union (map #(matcher % matchers) inputs)))
          ; End of the loop
          inputs)))))

(defn parse-disjunction-rule
  [rule]
  (let [alternatives (map parse-conjunction-rule (split rule #" \| "))]
    ; The result of an disjunction rule is a union of the result sets of every alternative
    (fn [text matchers] (apply union (map #(% text matchers) alternatives)))))

(defn parse-char-rule
  [rule]
  (let [c (second rule)]
    (fn [text _] (if (= (first text) c) #{(apply str (rest text))} #{}))))


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
    (contains? r "")))

(defn -main [filename]
  (let [[rule-lines lines] (core/split-on empty? (core/lines-from-file filename))
        matchers (parse-rules rule-lines)]
    (prn (core/count-if #(matches-zero-fully % matchers) lines))))
