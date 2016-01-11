(ns open-company-web.components.navbar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros (defcomponent)]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.ui.link :refer (link)]
              [open-company-web.components.ui.user-avatar :refer (user-avatar)]
              [open-company-web.components.ui.company-avatar :refer (company-avatar)]
              [om-bootstrap.nav :as n]
              [open-company-web.router :as router]))

(defn company-title [data]
  (str (:name data)))

(defcomponent navbar [data owner]
  (render [_]
    (n/navbar {:inverse? true :fixed-top? true :fluid true :collapse? true}
      (dom/div {:class "navbar-header"}
        (when (contains? @router/path :slug)
          (om/build company-avatar {:company-data data :navbar-brand true})))
      (dom/div {:id "navbar" :class "navbar-collapse collapse"}
        (dom/ul {:class "nav navbar-nav navbar-right"}
          (dom/li {}
            (om/build user-avatar {})))))))
