(ns day-2-password-philosophy.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def regexpr #"(\d)+-(\d)+ (\w): (\w+)")

(defn read-input
  [input]
  (map #(rest (re-matches regexpr %)) input))

(def input (clojure.string/split-lines (slurp "resources/input.txt")))

(read-input "1-2 a: ddfadfa")
(read-input "4-6 n: trwpnnnvq\n12-15 p: zfpmpphpgghpppppppp")
(read-input input)

(def numbers (let [file-contents (slurp "resources/input.txt")
                   nums-as-strings (clojure.string/split file-contents #"\n")]
               (map read-string nums-as-strings)))
