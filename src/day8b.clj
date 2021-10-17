; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day8b
  (:require [core]
            [day8a :refer [read-program exec]]))

(defn terminates-after-change? [program pc acu visited]
  (if (core/non-nil? program)
    (loop [pc pc, acu acu, visited visited]
    (if (= pc (count program)) acu ;; Hit first instruction after bootloader - fixed
        (if (nil? (get visited pc))
          (let [[npc nacu nvisited] (exec (get program pc) pc acu visited)]
            (recur npc nacu nvisited)))
      ))))

(defn swap-instruction [program idx]
  (let [[instruction argument] (get program idx)]
    (case instruction
      "jmp" (assoc program idx ["nop" argument])
      "nop" (assoc program idx ["jmp" argument])
      nil)))

(defn execute-expanding-swaps [program]
  (loop [pc 0, acu 0, visited #{}]
    (if (core/non-nil? (get visited pc)) nil ;; We actually failed, what
        (let [result-changed (terminates-after-change? (swap-instruction program pc) pc acu visited)]
          (if (core/non-nil? result-changed) result-changed
              (let [[npc nacu nvisited] (exec (get program pc) pc acu visited)]
                (recur npc nacu nvisited)))))))

(defn -main [filename]
  (->> filename
       read-program
       execute-expanding-swaps
       prn))
