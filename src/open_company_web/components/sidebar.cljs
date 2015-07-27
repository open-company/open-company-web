(ns open-company-web.components.sidebar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.link :refer [link]]))



(defcomponent sidebar [data owner]
  (render [_]
    (dom/div {:class "col-mid-1 sidebar"}
      (dom/ul {:class "nav nav-sidebar"}
        (dom/li {:class "active"}
          (om/build link {:href "/profile" :name "Profile"}))
        (dom/li nil
          (om/build link {:href "/organization" :name "Organization"}))
        (dom/li nil
          (om/build link {:href "/equity" :name "Equity"}))
        (dom/li nil
          (om/build link {:href "/agreements" :name "Agreements"}))
        (dom/li nil
          (om/build link {:href "/reports" :name "Reports"}))))))
