# jayq

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

## Usage

```clojure
[jayq "0.2.0"]
```

```clojure
(ns myapp
  (:use [jayq.core :only [$ css inner]]))

(def $interface ($ :#interface))

(-> $interface
  (css {:background "blue"})
  (inner "Loading!"))

```

## Change note

### 0.2.0

* `text/clojure` `text/edn` `application/clojure` `application/edn` mime types
  are now read as clojure data before being passed to callbacks
* `:edn` & `:clojure` dataType option support in $.ajax
* Fixed bug affecting `clj->js` serialization of Persistent data
  structure after the first chunk.
* Improve coverage of jQuery API traversal & manipulation functions
* Consistency issue on `closest` when used with keywords

## Compiling

If you're using advanced Clojurescript compilation you'll need to reference the jquery externs file. Add this to your compilation options:

```clojure

  {
    :optimizations :advanced
    :externs ["externs/jquery.js"]
    ...
  }
```

Without this, you'l see errors like 'Object ... has no method XX'. See http://lukevanderhart.com/2011/09/30/using-javascript-and-clojurescript.html for more on externs.

## License

Copyright (C) 2011 Chris Granger

Distributed under the Eclipse Public License, the same as Clojure.
