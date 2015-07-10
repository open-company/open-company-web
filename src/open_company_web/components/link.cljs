(ns open-company-web.components.link
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [secretary.core :as secretary]))

(defcomponent link [data owner]
  (render [_]
    (dom/a #js {
      :href (:href data)
      :onClick (fn[e]
                (-> e .preventDefault)
                (secretary/dispatch! (:href data))
                (.pushState (.-history js/window) "Test" "Title" (:href data)))
      } (:name data))))
