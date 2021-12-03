; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day18b
  (:require [core]
            [day18a :refer [tokenize]]))

(defn calc [tokens]
  (letfn
   [(eval-token [token] (if (vector? token) (calc token)
                            (if (string? token) (Long/parseLong token) token)))
    (collapse-brackets
     [tokens] (->> tokens
                   (map #(if (vector? %) (eval-token %) %))
                   vec))
    (collpase-addition
     [all-tokens]
     (loop [tokens all-tokens, add-op-idx (.indexOf tokens "+")]
       (if (< add-op-idx 0) tokens
           (let [lval (eval-token (get tokens (dec add-op-idx)))
                 rval (eval-token (get tokens (inc add-op-idx)))
                 new-tokens (vec (concat (subvec tokens 0 (dec add-op-idx))
                                         [(+ lval rval)]
                                         (subvec tokens (+ 2 add-op-idx))))]
             (recur new-tokens (.indexOf new-tokens "+"))))))
    (collpase-multiplication
     [all-tokens]
     (loop [tokens all-tokens, add-op-idx (.indexOf tokens "*")]
       (if (< add-op-idx 0) tokens
           (let [lval (eval-token (tokens (dec add-op-idx)))
                 rval (eval-token (tokens (inc add-op-idx)))
                 new-tokens (vec (concat (subvec tokens 0 (dec add-op-idx))
                                         [(* lval rval)]
                                         (subvec tokens (+ 2 add-op-idx))))]
             (recur new-tokens (.indexOf new-tokens "*"))))))
    (exec-tokens
     [tokens]
     (->> tokens
          collapse-brackets
          collpase-addition
          collpase-multiplication))]
    (first (exec-tokens tokens))))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       (map tokenize)
       (map first)
       (map calc)
       (reduce +)
       prn))
