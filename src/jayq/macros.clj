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
       ~(reduce (fn [m [x f]]
                  (case x
                    :let `(let ~f ~m)
                    `(~bind ~f (fn [~x] ~m))))
                `(~return (do ~@body))
                (reverse steps-pairs)))))

(defmacro let-ajax [steps & body]
  `(do-> jayq.core/ajax-m ~steps ~@body))

(defmacro let-deferred [steps & body]
  `(do-> jayq.core/deferred-m ~steps ~@body))