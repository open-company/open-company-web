(ns oc.web.lib.js-infer
  (:require [oops.core :refer (oget+)]))

(defn void-infer [^js x prop]
  (oget+ x prop))