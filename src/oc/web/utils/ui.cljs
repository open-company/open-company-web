(ns oc.web.utils.ui)

(defn resize-textarea [textarea]
  (when textarea
    (set! (.-height (.-style textarea)) "")
    (set! (.-height (.-style textarea)) (str (.-scrollHeight textarea) "px"))))