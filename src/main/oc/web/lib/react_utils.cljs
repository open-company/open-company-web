(ns oc.web.lib.react-utils
  (:require ["react" :as react]
            [oops.core :refer (oset! oget)]
            [cljs.compiler :as compiler]
            [cljs.core :as cljs]))

(defn build [component props & children]
  (apply react/createElement component (clj->js props) children))