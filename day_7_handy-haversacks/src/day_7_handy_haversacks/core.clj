(ns day-7-handy-haversacks.core)
(require '(clojure [zip :as zip]))
(require '[taoensso.timbre :as timbre])

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
; (make-rule (first input-2))
;=> {:shiny-gold [[:dark-red 2]]}
(defn make-rule
  [line]
  {(extract-id line) (make-content-nodes line)})


(defn make-rules [lines]
  (apply merge (into [] (map make-rule lines))))

(def rules-1 (make-rules input-1))
(def rules-2 (make-rules input-2))

(defn print-loc [loc]
  (if (zip/end? loc)
    (println ":end")
    (do
      (println "loc " (zip/node loc))
      (print-loc (zip/next loc)))))

; NOTES
;Get the number of children. For example, Shiny gold has two children, dark olive and vibrant plum
;(-> rules-1
;    :shiny-gold
;    zip/vector-zip
;    zip/children
;    count
;    )

; HOW TO NAVIGATE
;(-> rules-1
;    :shiny-gold
;    zip/vector-zip
;    zip/down
;    zip/leftmost
;    zip/right
;    zip/node
;    )


; (insert-child-at (:shiny-gold rules-1) :vibrant-plum ["NODE"])
(defn insert-child-at
  [tree id node]
  "A node is any vector.
  Returns a vector-as-tree.
  Does NOT return a zipper (loc)"
  (loop [loc (zip/vector-zip tree)]
    (if (zip/end? loc)
      (throw (AssertionError. (str "Could not find id " id)))
      (let [value (zip/node loc)]
        (if (= value id)
          (zip/root (zip/insert-child (zip/up loc) node))
          (recur (zip/next loc)))))))


; (expand-tree (rules-1 :vibrant-plum) rules-1)
;(defn expand-tree
;  [tree rules]
;  (let [tree-loc (zip/vector-zip tree)]
;    (if (not (clojure.zip/branch? tree-loc))
;      tree
;      (let [child-ids (filter keyword? (flatten (clojure.zip/children tree-loc)))]
;        (loop [loc tree-loc
;               ids child-ids]
;          (if (empty? ids)
;            (zip/root loc)
;            (let [(insert-child-at  )]
;              (println "ids\t" ids "loc\t" loc)
;              (recur loc (rest ids)))))))))

;newly-inserted (clojure.zip/insert-child (zip/next loc) contents) ]
;      newly-inserted )
;) )


;(println "single-id-for-testing\t" single-id-for-testing "contents\t" contents)
;(print-loc (zip/root newly-inserted))

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

