(ns open-company-web.components.ui.side-drawer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy]
            [open-company-web.lib.utils :as utils]))

(defn open-value [open]
  (if open "open" "close"))

(defcomponent side-drawer [data owner options]

  (init-state [_]
    {:open nil})

  (will-receive-props [_ next-props]
    (when-not (= (:open next-props) (:open data))
      (om/set-state! owner :open (open-value (:open next-props)))))

  (render-state [_ {:keys [open]}]
    (dom/div {:class (utils/class-set {:side-drawer true
                                       :group true
                                       :open-drawer (= open "open")
                                       :close-drawer (= open "close")})}
      )))