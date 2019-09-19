(ns oc.web.lib.react-utils)

(defn build [component props & children]
  (let [React (.-React js/window)]
    (apply React/createElement component (clj->js props) children)))
