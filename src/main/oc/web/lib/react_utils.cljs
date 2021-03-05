(ns oc.web.lib.react-utils
  (:require ["react" :as react :refer (createElement)]
            [cljs.core :as cljs]))

(defn build [component props & children]
  (apply createElement component (clj->js props) children))