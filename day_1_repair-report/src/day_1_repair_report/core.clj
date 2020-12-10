(ns day-1-repair-report.core)

(def numbers (let [file-contents (slurp "resources/input.txt")
                   nums-as-strings (clojure.string/split file-contents #"\n")]
               (map read-string nums-as-strings)))

(defn subsets [n items]
  "Thank you Stack Overflow!"
  (cond
    (= n 0) '(())
    (empty? items) '()
    :else (concat (map
                    #(cons (first items) %)
                    (subsets (dec n) (rest items)))
                  (subsets n (rest items)))))

(defn findterms
  [cardinality desired-sum l]
  (let
    [tuples (subsets cardinality l)]
    (first (filter #(= (apply + %) desired-sum) tuples))))

(apply * (findterms 3 2020 numbers))