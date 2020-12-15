(ns day-7-handy-haversacks.core)

(def one-raw-input "light red bags contain 1 bright white bag, 2 muted yellow bags.")

(def raw-sample-input
  '("light red bags contain 1 bright white bag, 2 muted yellow bags."
     "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
     "bright white bags contain 1 shiny gold bag."
     "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
     "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
     "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
     "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."
     "faded blue bags contain no other bags."
     "dotted black bags contain no other bags."
     ))
;What would a rule look like?
;A rule is a bag name that contains other bag names along with their cardinalities.
;There needs to be a iterative-recursive function that iterates over a bags contents and recursively expands the contents.

; ContentItem [cardinality, bagId], where bagId is a reference to a Bag
; Bag [bagId, set of ContentItem]
; Rules - the set of all Bags
; function expand:
;   input: bagId, Rules
;   output: Map. Keys are bagId and values are how many of these bags are ultimately contained within.

;Part 1 - How many bag colors can eventually contain at least one shiny gold bag?
; Solution steps:
; 1. Get all the keys from Rules. That should be every bagId in the domain
; 2. Expand every bagId
; 3. Save the expanded Bags as a map (ExpandedBags), keyed by bag ID.
;   The values are output of the expand function.
; 4. Filter ExpandedBags to get all the bagIds that contain a shiny-gold bag

;Rule Structure
;
; bagId-1 bagId-2 " bags contain " (cardinality-A contentId-A1 contentId-A2 " bag/s, ")* cardinality-B contentId-B1 contentId-B2 " bag/s."
;

;Example
;(make-bagId "faded blue")
;=> :faded-blue
(defn make-keyword
  "Convert string to keyword 'faded blue' -> :faded-blue"
  [name]
  (keyword (clojure.string/replace name " " "-")))


(defn extract-id
  [line]
  (make-keyword (re-find #"^\w+ \w+" line)))



;(extract-content-items one-raw-input)
;=> {:bright-white 1, :muted-yellow 2}
(defn extract-content-items
  [line]
  (let [groups (re-seq #"(\d) (\w+ \w+)" line)
        items (map rest groups)]
    (reduce #(assoc %1 (make-keyword (second %2)) (Integer/parseInt (first %2))) {} items)))


;(make-bag one-raw-input)
;=> {:light-red {:bright-white 1, :muted-yellow 2}}
(defn make-bag
  [line]
  {(extract-id line) (extract-content-items line)})


;(make-rules raw-sample-input)
;=>
;{:vibrant-plum {:faded-blue 5, :dotted-black 6},
; :light-red {:bright-white 1, :muted-yellow 2},
; ...
(defn make-rules
  [input]
  (apply merge (map make-bag input)))

(def sample-rules (make-rules raw-sample-input))


;(test-one :shiny-gold sample-rules)
;=> {:dark-olive 1, :vibrant-plum 2, :faded-blue 13, :dotted-black 16
(defn expand-id
  ([id rules] (expand-id id rules 1))
  ([id rules cardinality]
   (let [rule (id rules)]
     (if (empty? rule)
       {}
       (let [contents (apply merge (map (fn [[k v]] {k (* cardinality v)}) rule))]
         (reduce (fn [c bag-id] (merge-with + c (expand-id bag-id rules (bag-id rule)))) contents (keys rule)))))))

