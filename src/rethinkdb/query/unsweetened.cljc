(ns rethinkdb.query.unsweetened
  "Alternative interface to rethinkdb lambda functions with syntactic
   sugar removed.  The fn macro in rethinkdb.query has the benefit of
   providing a syntax that mirrors the native clojure fn, but
   oftentimes it's necessary to have the more versatile option of a
   definition that accepts pure data."
  (:require [clojure.walk :as walk]
            [rethinkdb.query-builder :as qb]))

(defn rethink-fn
  "Args is a vector of keywords. Term is a map produced by rqb/term.
   e.g. (rethink-fn [::my-arg] term)"
  [args term]
  (let [new-args (mapv #(vector :temp-var %) args)
        new-replacements (zipmap args new-args)
        new-terms (walk/postwalk-replace new-replacements term)]
    (qb/term :FUNC [new-args new-terms])))
