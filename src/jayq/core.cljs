(ns jayq.core 
  (:refer-clojure :exclude [val empty remove find])
  (:require [clojure.string :as string])
  (:use [jayq.util :only [map->js]]))

(defn crate-meta [func]
  (.-prototype._crateGroup func))

(defn ->selector [sel]
  (cond
    (string? sel) sel
    (fn? sel) (str "[crateGroup=" (crate-meta sel) "]")
    (keyword? sel) (name sel)
    :else sel))

(def $ (fn [sel & [context]] 
         (if-not context
           (js/jQuery (->selector sel))
           (js/jQuery (->selector sel) context))))

(extend-type js/jQuery
  ISeqable
  (-seq [this] (when (.get this 0)
                 this))
  ISeq
  (-first [this] (.slice this 0 1))
  (-rest [this] (if (> (count this) 1)
                  (.slice this 1)
                  (list)))

  ICounted
  (-count [this] (. this (size)))

  IIndexed
  (-nth [this n]
    (when (< n (count this))
      (.slice this n (inc n))))
  (-nth [this n not-found]
    (if (< n (count this))
      (.slice this n (inc n))
      (if (undefined? not-found)
        nil
        not-found)))

  ISequential

  ILookup
  (-lookup
    ([this k]
       (or (.slice this k (inc k)) nil))
    ([this k not-found]
       (-nth this k not-found)))

  IReduce
  (-reduce [this f]
    (ci-reduce coll f (first this) (count this)))
  (-reduce [this f start]
    (ci-reduce coll f start i))) 

(set! jQuery.prototype.call
      (fn
        ([_ k] (-lookup (js* "this") k))
        ([_ k not-found] (-lookup (js* "this") k not-found))))

(defn anim [elem props dur]
  (.animate elem (map->js props) dur))

(defn text [$elem txt]
  (.text $elem txt))

(defn css [$elem opts]
  (if (keyword? opts)
    (.css $elem (name opts))
    (.css $elem (map->js opts))))

(defn attr [$elem a & [v]]
  (let [a (name a)]
    (if-not v
      (. $elem (attr a))
      (. $elem (attr a v)))))

(defn data [$elem k & [v]]
  (let [k (name k)]
    (if-not v
      (. $elem (data k))
      (. $elem (data k v)))))

(defn add-class [$elem cl]
  (let [cl (name cl)]
    (.addClass $elem cl)))

(defn remove-class [$elem cl]
  (let [cl (name cl)]
    (.removeClass $elem cl)))

(defn append [$elem content]
  (.append $elem content))

(defn prepend [$elem content]
  (.prepend $elem content))

(defn remove [$elem]
  (.remove $elem))

(defn hide [$elem & [speed on-finish]]
  (.hide $elem speed on-finish))

(defn show [$elem & [speed on-finish]]
  (.show $elem speed on-finish))

(defn fade-out [$elem & [speed on-finish]]
  (.fadeOut $elem speed on-finish))

(defn fade-in [$elem & [speed on-finish]]
  (.fadeIn $elem speed on-finish))

(defn slide-up [$elem & [speed on-finish]]
  (.slideUp $elem speed on-finish))

(defn slide-down [$elem & [speed on-finish]]
  (.slideDown $elem speed on-finish))

(defn bind [$elem ev func]
  (.bind $elem (name ev) func))

(defn find [$elem selector]
  (.find $elem (name selector)))

(defn trigger [$elem ev]
  (.trigger $elem (name ev)))

(defn delegate [$elem sel ev func]
  (.delegate $elem (->selector sel) (name ev) func))

(defn inner [$elem v]
  (.html $elem v))

(defn empty [$elem]
  (.empty $elem))

(defn val [$elem & [v]]
  (if v
    (.val $elem v)
    (. $elem (val))))

(defn queue [$elem callback]
  (. $elem (queue callback)))

(defn dequeue [elem]
  (. ($ elem) (dequeue)))

(defn xhr [[method uri] content callback]
  (let [params (map->js {:type (string/upper-case (name method))
                         :data (map->js content)
                         :success callback})]
    (.ajax js/jQuery uri params)))

