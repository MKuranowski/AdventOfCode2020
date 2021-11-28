; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day14b
  (:require [core]
            [clojure.string :refer [includes? replace-first]]))

(defn get-all-adresses
  [adrr-floating]
  (if (includes? adrr-floating "X")
    (lazy-seq (concat
               (get-all-adresses (replace-first adrr-floating #"X" "0"))
               (get-all-adresses (replace-first adrr-floating #"X" "1"))))
    (cons (Long/parseLong adrr-floating 2) nil)))

(defn apply-mask-to-addr
  [whole-mask addr]
  (loop [idx 0
         bits (vec (take 36 (repeat \0)))
         mask whole-mask]
    (if-let [mask-char (first mask)]
      (recur (inc idx)
             (assoc bits
                    idx
                    (case mask-char
                      \0 (if (bit-test addr (- 35 idx)) \1 \0)
                      mask-char))
             (rest mask))
      (apply str bits))))

(defn add-values-to-mem
  [mem mask original-addr value]
  (merge mem (zipmap
              (get-all-adresses (apply-mask-to-addr mask original-addr))
              (repeat value))))

(defn exec-prog
  [all-instructions]
  (loop [instructions all-instructions
         mask nil
         mem {}]
    (if-let [instruction (first instructions)]
      (if-let [[_ new-mask] (re-matches #"mask = ([01X]+)" instruction)]
        (recur (rest instructions) new-mask mem)
        (if-let [[_ addr val] (re-matches #"mem\[(\d+)\] = (\d+)" instruction)]
          (recur (rest instructions) mask (add-values-to-mem mem
                                                             mask
                                                             (Long/parseLong addr 10)
                                                             (Long/parseLong val 10)))
          (throw (Exception. (str "Unknown instruction: " instruction)))))
      (apply + (vals mem)))))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       exec-prog
       prn))
