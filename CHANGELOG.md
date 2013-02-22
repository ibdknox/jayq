# Changelog

## 2.3.0

* Added `jayq.core/replace-with`

## 2.2.0

* Added `jayq.core/prop`

* Added missing signatures of queue/dequeue and fix first arg on
  dequeue (it should be a jquery object instance instead of an HTMLElement)

## 2.1.0

* `jayq.utils/log` now returns the value it logs see https://github.com/ibdknox/jayq/pull/28

## 2.0.0 Breaking changes!

* Removed `jayq.util/clj->js` and `jayq.util/map->js` as both were
  made useless by the recent addition of `cljs.core/clj->js`.

To upgrade to this version of jayq, you will need to use a recent
build of clojurescript: 0.0-1552+ or via
[lein-cljsbuild](https://github.com/emezeske/lein-cljsbuild) 0.2.10+.

## 1.1.0

* Update jayq.util/clj->js to use cljs.core's version of
  the same function if available + kill warnings with latest cljs rev
  on jayq.core

## 1.0.0

* Handles `jq.core/ajax` `:data` encoding via pr-str when the
`:contentType` option is set to either `application/edn`
`application/clojure` `text/clojure` `text/edn`. The `:contentType`
value can be a string or a keyword, and can be followed by a charset.

```clojure
(jayq.core/ajax {:url "foo"
                 :type :post
                 :contentType :application/edn
                 :data {:bar [1 2 3]}})
```

## 0.3.2

* Add second arity to `jayq.core/inner`

* Add jayq.core/read, reads content of (script) element with clojure
  data as content.
  ex: ```<script type="text/edn">{:foo "bar"}</script>```

## 0.3.1

* Clean up the jQuery type extension and replace `jQuery.prototype.call`
  definition with `IFn` extension

## 0.3.0

* Add `jQuery.Deferred.*` wrappers

* Add `jayq.macros/let-ajax` `jayq.macros/let-deferred` `jayq.macros/do->`

## 0.2.3

* Performance improvements to `$` `css` `attr` `val` `data`

* Clean up (remove some unecessary locals)

* Add support for map arity to `css` `attr` `data` (ex: (attr {:foo "bar" ...}))

* Add offset & dimension functions

* events type can be passed as a string argument

## 0.2.2

* bugfix: revert previous commit replacing coll? with sequential?

## 0.2.1

* Removed externs file from the source, let the user grab the
  appropriate file from the google-closure repository.
* Replaced uses of `coll?` with `sequential?`

## 0.2.0

* Possible breaking change: `jayq.core/ajax` responses with `text/clojure`
  `text/edn` `application/clojure` `application/edn` mime types are
  now read as clojure data before being passed to callbacks.
* `:edn` & `:clojure` dataType option support in $.ajax
* Fixed bug affecting `clj->js` serialization of Persistent data
  structure after the first chunk.
* Improve coverage of jQuery API traversal & manipulation functions
* Consistency issue on `closest` when used with keywords
