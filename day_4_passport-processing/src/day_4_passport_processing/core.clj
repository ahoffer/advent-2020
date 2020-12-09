(ns day-4-passport-processing.core)

(def input (let
             [file-contents (slurp "resources/input.txt")
              lines (clojure.string/split-lines file-contents)]
             lines))

(defn consume-next-doc
  [lines]
  (let
    [top-of-doc (drop-while empty? lines)
     doc-lines (take-while not-empty top-of-doc)
     document (clojure.string/join " " doc-lines)
     remaining-lines (drop-while empty? (drop (count doc-lines) top-of-doc))]
    (list document remaining-lines)))

(consume-next-doc (take 1 input))
(consume-next-doc (take 2 input))
(consume-next-doc (take 3 input))
(consume-next-doc (subvec input 2 4))

(defn all-docs
  [lines]
  (let [[document remaining-lines] (consume-next-doc lines)
        more-lines (not (empty? remaining-lines))]
    (if more-lines
      (cons document (all-docs remaining-lines))
      (list document))))

(defn doc-to-map
  [doc-str]
  (let
    [kv-pairs (clojure.string/split doc-str #"\s")
     kv-vecs (map #(clojure.string/split % #":") kv-pairs)
     doc-map (reduce #(assoc %1 (first %2) (second %2)) {} kv-vecs)]
    doc-map))

(def documents (all-docs input))

(def document-data (map doc-to-map documents))

(defn north-pole-cred?
  [datum]
  (contains? datum "cid"))

(defn contains-expected?
  [datum]
  (let [fields '("byr"
                  "iyr"
                  "eyr"
                  "hgt"
                  "hcl"
                  "ecl"
                  "pid"
                  )]
    (every? #(contains? datum %) fields)))

;PART TWO
;byr (Birth Year) - four digits; at least 1920 and at most 2002.
;iyr (Issue Year) - four digits; at least 2010 and at most 2020.
;eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
;hgt (Height) - a number followed by either cm or in:
;If cm, the number must be at least 150 and at most 193.
;If in, the number must be at least 59 and at most 76.
;hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
;ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
;pid (Passport ID) - a nine-digit number, including leading zeroes.
;cid (Country ID) - ignored, missing or not.

(defn valid-height?
  [hgt]
  (let [[match num unit] (re-find #"^(\d+)(in|cm)$" hgt)]
    (and (not (nil? match))
         (let
           [value (Integer/parseInt num)]
           (if (= unit "cm")
             (<= 150 value 193)
             (<= 59 value 76))))))

(defn valid?
  [datum]
  (and (contains-expected? datum)
       (<= 1920 (Integer/parseInt (datum "byr")) 2002)
       (<= 2010 (Integer/parseInt (datum "iyr")) 2020)
       (<= 2020 (Integer/parseInt (datum "eyr")) 2030)
       (valid-height? (datum "hgt"))
       (not (nil? (re-find #"^#[a-z0-9]{6}$" (datum "hcl"))))
       (not (nil? (re-find #"^amb$|^blu$|^brn$|^gry$|^grn$|^hzl$|^oth$" (datum "ecl"))))
       (not (nil? (re-find #"^\d{9}$" (datum "pid"))))))

;(count (filter valid? document-data))
;(valid? {"hcl" "#7d3b0c", "pid" "431742871", "ecl" "hzl", "hgt" "169cm", "cid" "340", "eyr" "2023", "iyr" "2017", "byr" "1994"})