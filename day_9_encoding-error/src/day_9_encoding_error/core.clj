(ns day-9-encoding-error.core)

(def sample [35                                             ;0
             20                                             ;1
             15                                             ;2
             25                                             ;3
             47                                             ;4
             40                                             ;5
             62
             55
             65
             95
             102
             117
             150
             182
             127
             219
             299
             277
             309
             576])

(defn slice
  [low-idx high-idx coll]
  (drop low-idx (take high-idx coll)))

;(slice 2 6 sample)
;=> (15 25 47 40)

(defn get-window
  "Return the data in the window for the element with the given index.
  Index is zero-based.
  Index must be greater than or equal to window-size"
  [idx window-size data]
  (drop (- idx window-size) (take idx data)))

;Example 1
;(get-window 5 5 sample)
;=> (35 20 15 25 47 40)

;Example 2
;(get-window 7 3 sample)
;=> (47 40 62)


(defn xmas-addends
  [idx window-size data]
  (let [window (get-window idx window-size data)
        n (nth data idx)]
    (first (for [x1 window x2 window
                 :when (and (not (= x1 x2)) (= n (+ x1 x2)))]
             [x1 x2]))))

;Example 1
;(xmas-addends 6 5 sample)
;=> [15 47]

;Example 2 - No valid answer
;(xmas-addends 6 3 sample)
;=> nil


(defn find-first-invalid
  [window-size data]
  (loop
    [idx window-size]
    (if (< idx (count data))
      (if (nil? (xmas-addends idx window-size data))
        (nth data idx)
        (recur (inc idx)))
      nil)))

;Example
;(find-first-invalid 5 sample)
;=> 127

(def input (let [file-contents (slurp "resources/input.txt")
                 nums-as-strings (clojure.string/split-lines file-contents)]
             (map read-string nums-as-strings)))

;Solution to Part 1
;(find-first-invalid 25 input)
;=> 167829540

; Start summing from index 0. Either we hit the target or exceed it.
; If we hit the target, return the successful sequence.
; If we miss the target, up the starting index and try again.
; Return nil if the target index is outside the bounds of the data
(defn xmas-search-from-beginning
  ([target data] (xmas-search-from-beginning 0 target data))
  ([n target data]
   (if (> n (count data))
     nil
     (let [xmas-seq (take n data)
           sum (reduce + xmas-seq)]
       (if (= target sum)
         xmas-seq
         (xmas-search-from-beginning (inc n) target data))))))

; Example 1
;(xmas-search-from-beginning 0 127 sample)
;=> nil

;Example 2
;(xmas-search-from-beginning 0 127 (drop 2 sample))
;=> (15 25 47 40)

(defn xmas-search-for-seq
  [target data]
  (let [xmas-seq (xmas-search-from-beginning target data)]
    (if (some? xmas-seq)
      xmas-seq
      (xmas-search-for-seq target (rest data)))))

;(xmas-search-for-seq 127 sample)
;=> (15 25 47 40)


(defn find-xmas-weakness
  [window-size data]
  (let [target (find-first-invalid window-size data)
        xmas-seq (xmas-search-for-seq target data)
        smallest (apply min xmas-seq)
        largest (apply max xmas-seq)]
    (+ smallest largest)))

;(find-xmas-weakness 5 sample)
;=> 62

;(find-xmas-weakness 25 input)
;=> 28045630