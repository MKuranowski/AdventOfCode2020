(ns day10b
  (:require [core]
            [day10a :refer [adapters-with-socket-and-device]]))

(defn build-previous-table [adapters]
  (into {} (for [a adapters
                 :let [values (subseq adapters >= (- a 3) < a)]]
             [a values])))

(defn add-from [adapter prev-table ways]
  (assoc ways adapter
         (->> (get prev-table adapter)
              (map #(get ways %))
              (reduce +))))

(defn calculate-ways [adapters previous-table]
  (loop [a (second adapters)
         others (nnext adapters)
         ways {0 1}]
    (let [nways (add-from a previous-table ways)]
      (if (empty? others) nways
          (recur (first others) (rest others) nways)))))

(defn -main [filename]
  (let [adapters-vec (adapters-with-socket-and-device filename)
        adapters-set (apply sorted-set adapters-vec)
        last-adapter (peek adapters-vec)]
    (-> (calculate-ways adapters-set (build-previous-table adapters-set))
        (get last-adapter)
         prn)))
