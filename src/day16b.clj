(ns day16b
  (:require [core]
            [day16a :refer [parse-contents]]
            [clojure.set :refer [intersection union difference]]
            [clojure.string :refer [starts-with?]]))

(defn is-valid-field?
  [rules field]
  (not-every? false? (map #(% field) (vals rules))))

(defn is-valid-ticket?
  [rules ticket]
  (every? true? (map #(is-valid-field? rules %) ticket)))

(defn only-valid-tickets
  [[rules own tickets]]
  (list rules own (filter #(is-valid-ticket? rules %) tickets)))

(defn rules-ok-for-value
  [rules value]
  (set (for [[rule-name rule-func] rules
             :when (rule-func value)]
         rule-name)))

(defn fields-ok-for-column
  [rules column]
  (apply intersection (map #(rules-ok-for-value rules %) column)))

(defn find-valid-columns
  [[rules _ tickets]]
  (let [columns (core/transpose tickets)]
    (map #(fields-ok-for-column rules %) columns)))

(defn simplify-valid-columns
  [all-cols]
  (loop [cols all-cols]
    (if (every? #(<= % 1) (map count cols))
      (mapv first cols)
      (let [sure-columns (apply union (for [col cols
                                            :when (<= (count col) 1)]
                                        col))]
       (recur (for [col cols]
               (if (> (count col) 1)
                 (difference col sure-columns)
                 col))))
      )))

(defn cols-that-start-with-departure
  [cols]
  (for [[idx col] (zipmap (iterate inc 0) cols)
        :when (starts-with? col "departure")]
    idx))



(defn -main [filename]
  (let [[rules own-ticket tickets] (->> filename
                                        core/lines-from-file
                                        (core/split-on empty?)
                                        parse-contents
                                        only-valid-tickets)
        interesting-cols (->> [rules own-ticket tickets]
                              find-valid-columns
                              simplify-valid-columns
                              cols-that-start-with-departure)]
       (prn (apply * (map #(own-ticket %) interesting-cols)))))
