(ns jayq.macros)

(defmacro queue
  [elem & body]
  `(jayq.core/queue ~elem 
                    (fn []
                      (cljs.core/this-as ~'this
                                         ~@body))))
