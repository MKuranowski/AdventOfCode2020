; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day18a
  (:require [core]))

(defn conj-token [tokens token] (if (empty? token) tokens (conj tokens (apply str token))))

(defn tokenize
  ([input] (tokenize input false))
  ([input endOnBracket]
   (loop [chr (first input), other (rest input), token (vector-of :char), tokens []]
     (if (nil? chr)
       (if endOnBracket (throw (Exception. "unbalanced brackets")) (list (conj-token tokens token) other))
       (if (and endOnBracket (= chr \)))
         (list (conj-token tokens token) other)
         (case chr
           \space (recur (first other) (rest other) (vector-of :char) (conj-token tokens token))
           \newline (recur (first other) (rest other) (vector-of :char) (conj-token tokens token))
           \( (let [[nested-tokens after-brackets] (tokenize other true)]
                (recur (first after-brackets) (rest after-brackets) (vector-of :char) (conj tokens nested-tokens)))
           (recur (first other) (rest other) (conj token chr) tokens)
          ))))))

(defn do-op [lval op rval]
  (case op
    "+" (+ lval rval)
    "*" (* lval rval)))

(defn calc [tokens]
  (letfn
   [(eval-token [token] (if (vector? token) (calc token) (Long/parseLong token)))
    (exec-tokens [tokens]
                 (loop [lval (eval-token (first tokens))
                        op (second tokens)
                        rval (eval-token (nth tokens 2))
                        tokens (nthnext tokens 3)]
                   (if (seq tokens)
                     (recur (do-op lval op rval)
                            (first tokens)
                            (eval-token (second tokens))
                            (nnext tokens))
                     (do-op lval op rval))))]
    (exec-tokens tokens)))

(defn -main [filename]
  (->> filename
       core/lines-from-file
       (map tokenize)
       (map first)
       (map calc)
       (reduce +)
       prn))
