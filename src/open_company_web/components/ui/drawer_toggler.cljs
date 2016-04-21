(ns open-company-web.components.ui.drawer-toggler
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.icon :refer (icon)]))

(defn rotate [owner & [from-extern]]
  (om/update-state! owner :open not)
  (let [props (om/get-props owner)]
    (when (and (not from-extern)
               (contains? props :click-cb))
      ((:click-cb props))))
  (when-let [drawer (om/get-ref owner "drawer-toggler")]
    (if (dommy/has-class? drawer :rotate45)
      (do
        (dommy/remove-class! drawer :rotate45)
        (dommy/add-class! drawer :rotate0)
        (dommy/remove-class! drawer :above-navbar))
      (do
        (utils/scroll-to-y 0)
        (dommy/add-class! drawer :above-navbar)
        (dommy/remove-class! drawer :rotate0)
        (dommy/add-class! drawer :rotate45)))))

(defcomponent drawer-toggler [data owner options]

  (init-state [_]
    {:open false})

  (will-receive-props [_ next-props]
    (when (and (not= (:close data) (:close next-props))
               (:close next-props)
               (om/get-state owner :open))
      (rotate owner true)))

  (render-state [_ {:keys [open]}]
    (dom/div #js {:className "drawer-toggler"
                  :ref "drawer-toggler"}
      (dom/button #js {:className "drawer-toggler-bt"
                       :onClick #(rotate owner)} (icon :circle-remove {:size 24})))))