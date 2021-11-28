; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day14a
  (:require [core]
            [clojure.string]))

(defn get-ones-mask
  [str-mask]
  (Long/parseLong (clojure.string/replace str-mask #"X" "0") 2))

(defn get-zeros-mask
  [str-mask]
  (Long/parseLong (clojure.string/replace str-mask #"X" "1") 2))

(defn apply-mask-to-value
  "Applies the ones-mask and zeros-mask to the value.
   The ones-mask should be a number with ones whenever a one appeared in the AOC mask,
   and the zeros-mask should be a number with ones whenever anything other than a zero
   apperead."
  [value ones-mask zeros-mask]
  (bit-and (bit-or value ones-mask) zeros-mask))

(defn exec-prog
  [all-instructions]
  (loop [instructions all-instructions
         ones-mask 0
         zeros-mask 0
         mem {}]
    (if-let [instruction (first instructions)]
      (if-let [[_ new-mask] (re-matches #"mask = ([01X]+)" instruction)]
        (recur (rest instructions) (get-ones-mask new-mask) (get-zeros-mask new-mask) mem)
        (if-let [[_ loc val] (re-matches #"mem\[(\d+)\] = (\d+)" instruction)]
          (recur (rest instructions) ones-mask zeros-mask
           (assoc mem loc (apply-mask-to-value (Long/parseLong val 10) ones-mask zeros-mask)))
          (throw (Exception. (str "Unknown instruction: " instruction)))))
      (apply + (vals mem)))))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       exec-prog
       prn))
