(ns open-company-web.components.icon
    (:require [om-tools.dom :as dom]))

(defn icon [id]
  (let [outline-color "#0f0f0f"
        accent-color "red"
        size 30]
     (dom/svg {:viewBox "0 0 16 16" :width (str size "px") :height (str size "px")
               :style {:color accent-color :stroke outline-color :strokeWidth "2px"}
               ;; use tag isn't supported by react 0.14.7 and 0.14.8 isn't on cljsjs
               ;; Also their changelog doesn't mention it at all so I'm not sure if .8 would work
               :dangerouslySetInnerHTML {:__html (str "<use xlink:href=/img/oc-icons.svg#nc-icon-" (name id) ">")}})))