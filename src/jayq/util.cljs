(ns jayq.util)

(defn wait [ms func]
  (js* "setTimeout(~{func}, ~{ms})"))

(defn log [v & text]
  (let [vs (if (string? v)
             (apply str v text)
             v)]
    (. js/console (log vs))))
