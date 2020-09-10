(ns oc.web.lib.react-utils
  (:require ["react" :as react :refer (createElement)]
            [oops.core :refer (oset! oget)]
            [cljs.compiler :as compiler]
            [cljs.core :as cljs]))

(defn build [component props & children]
  (apply createElement component (clj->js props) children))