; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day15a
  (:require [core]))

(defn play-game
  ([] (play-game 3 6 {0 0, 3 1} 2020))
  ([initial-numbers] (play-game initial-numbers 2020))
  ([initial-numbers stop-at] (play-game (count initial-numbers)
                                (peek initial-numbers)
                                (zipmap (pop initial-numbers)
                                        (iterate inc 0))
                                stop-at))
  ([idx last-spoken last-seen stop-at]
   (loop [idx idx
          last-spoken last-spoken
          last-seen last-seen]
     (if (= idx stop-at) last-spoken
         (recur (inc idx)
                (if-let [previous-idx (get last-seen last-spoken)]
                  (- idx previous-idx 1)
                  0)
                (assoc last-seen last-spoken (dec idx)))))))

(defn -main [_] (prn (play-game [6 3 15 13 1 0] 2020)))
