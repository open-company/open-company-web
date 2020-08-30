(ns oc.web.lib.react-utils)

(defn build [component props & children]
  (apply js/window.React.createElement component (clj->js props) children))
