(ns jayq.macros)

(defmacro queue
  [elem & body]
  `(jayq.core/queue ~elem
                    (fn []
                      (cljs.core/this-as ~'this
                                         ~@body))))

(defmacro ready
  [& body]
  `(jayq.core/document-ready (fn []
                               ~@body)))

(defmacro do->
  [m-specs steps & body]
  (let [steps-pairs (partition 2 steps)
        bind (gensym)
        return (gensym)]
    `(let [~bind (:bind ~m-specs)
           ~return (:return ~m-specs)]
       ~(reduce (fn [m [x f]] `(~bind ~f (fn [~x] ~m)))
                `(~return (do ~@body))
                (reverse steps-pairs)))))