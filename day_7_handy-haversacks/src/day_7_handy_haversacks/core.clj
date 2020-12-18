(ns day-7-handy-haversacks.core)
(require '(clojure [zip :as zip]))
(def z (zip/vector-zip [1 [:a :b] 2 3 [40 50 60]]))

(def input-1
  '("light red bags contain 1 bright white bag, 2 muted yellow bags."
     "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
     "bright white bags contain 1 shiny gold bag."
     "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
     "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
     "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
     "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."
     "faded blue bags contain no other bags."
     "dotted black bags contain no other bags."))


(def input-2 (map clojure.string/trim
                  (clojure.string/split-lines
                    "shiny gold bags contain 2 dark red bags.
                   dark red bags contain 2 dark orange bags.
                   dark orange bags contain 2 dark yellow bags.
                   dark yellow bags contain 2 dark green bags.
                   dark green bags contain 2 dark blue bags.
                   dark blue bags contain 2 dark violet bags.
                   dark violet bags contain no other bags.")))

; Example
; (make-bagId "faded blue")
; => :faded-blue
(defn make-keyword
  "Convert string to keyword 'faded blue' -> :faded-blue"
  [name]
  (keyword (clojure.string/replace name " " "-")))



(defn extract-id
  [line]
  (make-keyword (re-find #"^\w+ \w+" line)))



; (content-tokens (first input-1))
; => (("1" "bright white") ("2" "muted yellow"))
(defn content-tokens
  [line]
  (map rest (re-seq #"(\d) (\w+ \w+)" line)))



; (make-content-nodes (first input-1))
; => [[:bright-white 1] [:muted-yellow 2]]
(defn make-content-nodes
  [line]
  (let [token-groups (content-tokens line)]
    (reduce #(let [child-arity (Integer/parseInt (first %2))
                   child-id (make-keyword (second %2))]
               (conj %1 [child-id child-arity]))
            []
            token-groups)))



; (make-rule (first input-2))
; => {:light-red [[:bright-white 1] [:muted-yellow 2]]}
(defn make-rule
  [line]
  {(extract-id line) (make-content-nodes line)})


(defn make-rules [lines]
  (apply merge (map make-rule lines)))

(def rules-1 (make-rules input-1))
(def rules-2 (make-rules input-2))

(defn print-loc [loc]
  (if-not (zip/end? loc)
    (println "loc " (zip/node loc)))
  (recur (zip/next loc)))


; (make-tree (zip/vector-zip []) :faded-blue rules-1)
; (make-tree (zip/vector-zip []) :vibrant-plum rules-2)
(defn make-tree
  ([id rules] (make-tree nil id rules))
  ([loc id rules]
   (let [item (rules id)]
     (if-not loc (clojure.zip/vector-zip item))

     )))



(defn arity [zipped-rule]
  (-> zipped-rule
      zip/next
      zip/next
      zip/node))

;(arity (zip/vector-zip [:light-red 1 [:bright-white 1 :muted-yellow 2]]))
; => 1



(defn bag-name [zipped-rule]
  (-> zipped-rule
      zip/next
      zip/node))

;(bag-name (zip/vector-zip [:light-red 1 [:bright-white 1 :muted-yellow 2]]))
;=> :light-red



(defn bag-contents [zipped-rule]
  (-> zipped-rule
      zip/down
      zip/rightmost
      zip/children))

;(bag-contents (zip/vector-zip [:light-red 1 [:bright-white 1 :muted-yellow 2]]))
; => (:bright-white 1 :muted-yellow 2)











; ZIPPER EXPERIMENTS
;
;(-> [:light-red 1 [:bright-white 1 :muted-yellow 2]]
;    zip/vector-zip
;    zip/down
;    zip/rightmost
;    zip/children)
;=> (:bright-white 1 :muted-yellow 2)

