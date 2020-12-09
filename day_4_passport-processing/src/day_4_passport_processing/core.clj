(ns day-4-passport-processing.core)

(def input (let
             [file-contents (slurp "resources/input.txt")
              lines (clojure.string/split-lines file-contents)]
             lines))

;(defn get-documents
;  [lines]
;  (loop [remaining-lines lines
;         document ""
;         documents []]
;    (if (empty? remaining-lines)
;
;      ;Done. Return results.
;      (if (empty? document)
;        documents
;        (conj documents document))
;
;      ;There are still more lines of data to process
;      (let [line (first remaining-lines)]
;
;        (if (not-empty line)
;
;          ;The document continues on this line. Append the line and keep going.
;          (recur
;            (rest lines)
;            (clojure.string/trim (str (clojure.string/trim document) " " line))
;            documents)
;
;          ;We hit the document separator. Add the current doc to collection and keep going.
;          (recur
;            (drop-while clojure.string/blank? remaining-lines)
;            ""
;            (conj documents document)))))))

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

;(all-docs (take 3 input))

;(re-seq #"\w+:\w+" (consume-next-doc (take 1 input)))
;(map #(clojure.string/split % #":") (clojure.string/split (first (consume-next-doc (take 1 input))) #"\s"))
