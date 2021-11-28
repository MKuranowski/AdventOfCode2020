; Copyright (c) 2021 Mikołaj Kuranowski
; SPDX-License-Identifier: WTFPL
(ns day15b
  (:require [day15a :refer [play-game]]))

(defn -main [_] (prn (play-game [6 3 15 13 1 0] 30000000)))
