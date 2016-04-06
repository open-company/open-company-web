(ns open-company-web.components.ui.drawer-toggler
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy]))

(defn rotate [owner options]
  (when (contains? options :click-cb)
    ((:click-cb options)))
  (when-let [drawer (om/get-ref owner "drawer-toggler")]
    (if (dommy/has-class? drawer :rotate45)
      (do
       (dommy/remove-class! drawer :rotate45)
       (dommy/add-class! drawer :rotate0))
      (do
       (dommy/add-class! drawer :rotate45)
       (dommy/remove-class! drawer :rotate0)))))

(defcomponent drawer-toggler [data owner options]
  (render [_]
    (dom/div #js {:className "drawer-toggler"
                  :ref "drawer-toggler"}
      (dom/button #js {:className "drawer-toggler-bt"
                       :onClick #(rotate owner options)}))))