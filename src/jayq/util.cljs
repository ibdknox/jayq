(ns jayq.util)

(defn map->js
  "Deprecated! Use cljs.core/clj->js instead."
  [m]
  (let [out (js-obj)]
    (doseq [[k v] m]
      (aset out (name k) v))
    out))

(defn wait [ms func]
  (js* "setTimeout(~{func}, ~{ms})"))

(defn log [v & text]
  (let [vs (if (string? v)
             (apply str v text)
             v)]
    (. js/console (log vs))))

(def
  ^{:doc
    "Deprecated! Use cljs.core/clj->js instead.
     Recursively transforms ClojureScript maps into Javascript objects,
     other ClojureScript colls into JavaScript arrays, and ClojureScript
     keywords into JavaScript strings."}
  clj->js
  (if (undefined? cljs.core/clj->js)
    (fn clj->js [x]
      (cond
        (string? x) x
        (keyword? x) (name x)
        (map? x) (let [obj (js-obj)]
                   (doseq [[k v] x]
                     (aset obj (clj->js k) (clj->js v)))
                   obj)
        (coll? x) (apply array (map clj->js x))
        :else x))
    cljs.core/clj->js))
