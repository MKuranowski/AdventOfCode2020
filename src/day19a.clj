; Copyright (c) 2022 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day19a
  (:require [core]
            [clojure.string :refer [split includes?]]))

; A "matcher" is a predicate with the following signature:
; (CharSequence, Map<String, matcher>) -> nil or CharSequence
;
; nil must be returned when there's no match
; a CharSequence must be returned on match and must represent the rest of the text (after a match).
;
; Note that the returned CharSequence may be empty - this means that the text was matched fully

(defn parse-conjunction-rule
  [rule]
  (let [rule_numbers (split rule #" ")]
    (fn [text matchers]
      ; Loop over every matcher in the conjunction rule
      (loop [matcher (matchers (first rule_numbers))
             rest_rule_numbers (rest rule_numbers)
             text text]
        (if (some? matcher)
          ; See if the matcher matches
          (if-let [text_rest (matcher text matchers)]
            ; Matcher did match - continue with the next matcher
            (recur (matchers (first rest_rule_numbers))
                   (rest rest_rule_numbers)
                   text_rest)
            ; Matcher didn't match - short circuit to _nil_ (no match)
            nil)
          ; End of the matcher list - return the rest of the text
          text)))))

(defn parse-disjunction-rule
  [rule]
  ; Parse every alternative as a conjunction rule
  (let [alternatives (map parse-conjunction-rule (split rule #" \| "))]
    (fn [text matchers]
      ; Loop over every alternative
      (loop [alternative (first alternatives)
             rest_alternatives (rest alternatives)]
        (if (some? alternative)
          ; Try to match the alternative
          (if-some [text_rest (alternative text matchers)]
            ; Matched - return the rest of the text
            text_rest
            ; No match - try next alternative
            (recur (first rest_alternatives)
                   (rest rest_alternatives)))
          ; End of alternative list, and nothing matched
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
