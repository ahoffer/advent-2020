(ns day-1-repair-report.core)

(defn check
  [a b]
  (= (+ a b) 2020))

(defn checklist
  [a list]
  (if (empty? list)
    nil
    (if (check a (first list))
      (first list)
      (checklist a (drop 1 list)))))

(def l '(2019 2020 2021))

(defn checklists
  [list1 list2]
  (if (empty? list1)
    nil
    (let [addend (first list1)
          answer (checklist addend list2)]
      (println answer)
      (if answer
        [addend answer]
        (checklists (drop 1 list1) list2)))))

(def l2 '(2 3 1))
