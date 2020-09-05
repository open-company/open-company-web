(ns oc.web.lib.react-utils
  (:require ["react" :as react]))

(defn build [component props & children]
  (apply react/createElement component (clj->js props) children))