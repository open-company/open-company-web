(ns open-company-web.components.ui.side-drawer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic-list-edit :refer (topic-list-edit)]))

(defcomponent side-drawer [data owner options]

  (init-state [_]
    {:open nil})

  (will-receive-props [_ next-props]
    (when-not (= (:open next-props) (:open data))
      (om/set-state! owner :open (if (:open next-props) "open" "close"))))

  (render-state [_ {:keys [open]}]
    (dom/div {:class (utils/class-set {:side-drawer true
                                       :group true
                                       :open-drawer (= open "open")
                                       :close-drawer (= open "close")})}
      (dom/div {:class "side-drawer-internal"}
        (om/build topic-list-edit (:list-data data) {:key (:list-key data)
                                                     :opts (:list-opts options)})))))