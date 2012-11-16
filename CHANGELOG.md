# Changelog

## 0.2.1

* Removed extern file from the source, let the user grab the
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
