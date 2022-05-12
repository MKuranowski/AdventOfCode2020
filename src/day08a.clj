; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day8a
  (:require [core]
            [clojure.string :refer [split]]))

(defn read-program [filename]
  (->> filename
       core/lines-from-file
       (map (fn [line] (let [[i a] (split line #" ")] [i (core/parse-int a)])))
       vec))

(defn exec [[instruction argument] pc acu visited]
  (case instruction
    "acc" (list (inc pc) (+ acu argument) (conj visited pc))
    "jmp" (list (+ pc argument) acu (conj visited pc))
    (list (inc pc) acu (conj visited pc))))

(defn execute-until-loop
  ([program] (execute-until-loop program 0 0 #{}))
  ([program pc acu visited]
   (loop [pc pc, acu acu, visited visited]
     (if (nil? (get visited pc))
       (let [[npc nacu nvisited] (exec (get program pc) pc acu visited)]
         (recur npc nacu nvisited))
       acu))))

(defn -main [filename]
  (->> filename
       read-program
       execute-until-loop
       prn))
