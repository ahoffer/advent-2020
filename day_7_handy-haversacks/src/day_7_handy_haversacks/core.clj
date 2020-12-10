(ns day-7-handy-haversacks.core)

;light red bags contain 1 bright white bag, 2 muted yellow bags.
;dark orange bags contain 3 bright white bags, 4 muted yellow bags.
;bright white bags contain 1 shiny gold bag.
;muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
;shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
;dark olive bags contain 3 faded blue bags, 4 dotted black bags.
;vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
;faded blue bags contain no other bags.
;dotted black bags contain no other bags.

;What would a rule look like?
;A rule is a bag name that contains other bag names along with their cardinalities.
;There needs to be a iterative-recursive function that iterates over a bags contents and recursively expands the contents.

; ContentItem [cardinality, BagId], where BagId is a reference to a Bag
; Bag [BagId, set of ContentItem]
; function expand.
;   input: a BagId
;   output: Map. Keys are BagId and values are how many of these bags are ultimately contained within.
