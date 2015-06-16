# jayq

[![Clojars Project](http://clojars.org/jayq/latest-version.svg)](http://clojars.org/jayq)

A jQuery wrapper for ClojureScript.

## Wait a second.. I thought we weren't supposed to use jQuery?

When ClojureScript first came out, the prevailing wisdom was that since jQuery wasn't compatible
with the Google Closure Compiler, we shouldn't be using it. So I set off to work on Pinot, a library
that wrapped the goog.\* APIs. The journey was painful and frustrating, and before long I realized I was
essentially rewriting parts of jQuery for no gain. Instead of doing that, I figured my time would be
better spent building on top of the most battle hardened JS library out there. Before I started down this path though, I wanted to make sure that I had answers to all the arguments
against using it. They were as follows:

* It can't be compiled by the Closure compiler and means we can't use Advanced compilation.
* It will add another thing for you to download and increase the size of the cljs code, because the symbols won't be munged by the Closure compiler.
* It won't lend itself to Clojure idioms.

I'll tackle each of these in order.

In terms of it not being able to be compiled, we shouldn't be packaging jquery with our apps anyways.
Virtually the entire web is built on $ and that means nearly every computer connected to the internet already
has a google CDN'd version of jquery on their machine. That means there's no extra weight and no reason
to compile it. This also addresses the first part of the second one - there's nothing more to download.

While it's true that if we used jQuery directly, all method calls would be left alone and could
not be replaced with a minified name. With a wrapper, however, that happens exactly once per method and
all occurences of the wrapper will be munged. This means that at most we're talking about a difference
on the magnitude of bytes. If you need to optimize for size at that level, you shouldn't be using
CLJS anyways.

Lastly, there's the argument that it won't lead to idiomatic usage. That's likely true if we use jQuery
directly, but I'm not sure I really believe that's a valid argument. The same goes for Clojure if we use
Java libraries directly all over the place. Wrappers, however, allow us to utilize all the functionality
provided by these libraries, but still create Clojure idioms over top of them. There's no reason to ignore
the most solid base out there, when we can just build greater abstractions on top of it. Do I think
jQuery is the pinnacle of the client side web? Not at all, but I do believe it provides a great foundation
for us to build exactly that.

## Installation

Add the following to your `project.clj`

```clojure
[jayq "2.5.4"]
```
Note: If you are upgrading to a major version be sure to check the
[changelog](https://github.com/ibdknox/jayq/blob/master/CHANGELOG.md)
for breaking changes.

## Usage

### jayq.core [Source](https://github.com/ibdknox/jayq/blob/master/src/jayq/core.cljs)

Most of the API in `jayq.core` maps 1:1 with jQuery functions.

```clojure
(ns myapp
  (:use [jayq.core :only [$ css html]]))

(def $interface ($ :#interface))

(-> $interface
  (css {:background "blue"})
  (html "Loading!"))

```

#### jayq.core/ajax clojure & edn mime types support

Requests with `:contentType` option matching `text/clojure` `text/edn`
`application/clojure` `application/edn` (as string or keyword) will
have the :data turned into its string representation (via pr-str)

Responses with `text/clojure` `text/edn` `application/clojure`
`application/edn` mime types are read as clojure data before being
passed to callbacks.  The same applies if the dataType option is set
to `:edn` or `:clojure`.


### jayq.util [Source](https://github.com/ibdknox/jayq/blob/master/src/jayq/util.cljs)

* `jayq.util/log [value & text]` : console.log wrapper

* `jayq.util/wait [ms f]` : setTimeout wrapper


### jayq.macros [Source](https://github.com/ibdknox/jayq/blob/master/src/jayq/macros.clj)

* `jayq.macros/queue [elem & body]` : a wrapper of `jayq.core/queue`
  that includes the lambda with a scoped `this` symbol

* `jayq.macros/ready [& body]` : a wrapper of `jayq.core/document-ready`

* `jayq.macros/let-ajax [steps & body]`: `let` like form allowing
  chaining of ajax calls and binding return values to locals for use
  once all the calls are complete (or in a :let intermediary step).
  The step value expected is a valid jq.core/ajax request map.
  You can supply :let/:when steps (like in for/doseq) between "regular" steps.

```clojure
(let-ajax [a {:url "http://localhost:8000/1.json"
              :dataType :json}
           b  {:dataType :json :url "http://localhost:8000/2.json"}]
       (merge a b))
```

* `jayq.macros/let-deferred [steps & body]`: `let` like form allowing
  chaining of deferreds and binding return values to locals for use
  once all the deferreds are realized (or in a :let/:when intermediary step).
  The step value expected is anything that returns a deferred instance.
  You can supply :let/:when steps (like in for/doseq) between "regular" steps.

```clojure
(let-deferred
    [a (jq/ajax "http://localhost:8000/1.json")
     :let [foo "bar"]
     :when (= (concat a foo) "foobar")
     b (jq/ajax "http://localhost:8000/2.json")]
(merge a b foo))
```

* `jayq.macros/do-> [m-specs steps & body]`: `let-*` macros are built
  from it. `m-specs` is a map of :bind and :return functions that dictate
  the workflow (see: `jayq.core/deferred-m` and `jayq.core/ajax-m`).

Error handling in `let-ajax` and `let-deferred` forms should be done using
`jq.core/fail` or the :error key on the request map.

## Changelog

See [CHANGELOG.md](https://github.com/ibdknox/jayq/blob/master/CHANGELOG.md)

## Compiling

If you're using advanced Clojurescript compilation you'll need to
reference a jQuery externs file.

You can find externs files from the
[closure-compiler repository](https://github.com/google/closure-compiler/tree/master/contrib/externs)
for a specific jQuery version.

Add this to your compilation options (assuming that your put the
externs file in `./externs/`):

```clojure

  {
    :optimizations :advanced
    :externs ["externs/jquery.js"]
    ...
  }
```

Without this, you will see errors like `Object ... has no method XX`. See http://lukevanderhart.com/2011/09/30/using-javascript-and-clojurescript.html for more on externs.

## License

Copyright (C) 2011 Chris Granger

Distributed under the Eclipse Public License, the same as Clojure.
