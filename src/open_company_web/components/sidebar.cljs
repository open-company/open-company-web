(ns open-company-web.components.sidebar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.link :refer [link]]))



(defcomponent sidebar [data owner]
  (render [_]
    (dom/div {:class "col-mid-1 sidebar"}
      (dom/ul {:class "nav nav-sidebar"}
        (dom/li {:class (if (= (:active data) "profile") "active" "")}
          (om/build link {:href "/profile" :name "Profile"}))
        (dom/li {:class (if (= (:active data) "organization") "active" "")}
          (om/build link {:href "/organization" :name "Organization"}))
        (dom/li {:class (if (= (:active data) "equity") "active" "")}
          (om/build link {:href "/equity" :name "Equity"}))
        (dom/li {:class (if (= (:active data) "agreements") "active" "")}
          (om/build link {:href "/agreements" :name "Agreements"}))
        (dom/li {:class (if (= (:active data) "reports") "active" "")}
          (om/build link {:href "/reports" :name "Reports"}))))))
