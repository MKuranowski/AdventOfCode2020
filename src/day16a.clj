(ns day16a
  (:require [core]
            [clojure.string]))

(defn parse-rule
  [rule]
  (if-let [[_ field low1 high1 low2 high2] (re-matches #"([a-z ]+): (\d+)-(\d+) or (\d+)-(\d+)" rule)]
    (let [low1-num (core/parse-int low1)
          high1-num (core/parse-int high1)
          low2-num (core/parse-int low2)
          high2-num (core/parse-int high2)]
     (list field #(or (<= low1-num % high1-num) (<= low2-num % high2-num))))
    (throw (Exception. (str "Unknown rule: " rule)))))

(defn parse-rules
  [rules]
  (apply hash-map (flatten (map parse-rule rules))))

(defn parse-ticket
  [ticket]
  (vec (map core/parse-int (clojure.string/split ticket #","))))

(defn parse-contents
  [[rules own-ticket tickets]]
   (list (parse-rules rules)
         (parse-ticket (second own-ticket))
         (map parse-ticket (rest tickets))))

(defn get-ticket-error-rate
  [rules ticket]
  (reduce + (for [field-value ticket
                  :when (every? false? (for [rule (vals rules)] (rule field-value)))]
              field-value)))

(defn get-tickets-error-rate
  [[rules _ tickets]]
  (reduce + (map #(get-ticket-error-rate rules %) tickets)))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       (core/split-on empty?)
       parse-contents
       get-tickets-error-rate
       prn))
