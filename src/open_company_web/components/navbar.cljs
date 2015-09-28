(ns open-company-web.components.navbar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.link :refer [link]]
              [om-bootstrap.nav :as n]
              [open-company-web.router :as router]))

(defn company-title [data]
  (str (:name data)))

(defcomponent navbar [data owner]
  (render [_]
    (n/navbar {:inverse? true :fixed-top? true :fluid true :collapse? true}
      (dom/div {:class "navbar-header"}
        (om/build link {
          :class "navbar-brand"
          :href (if (contains? data :slug) (str "/companies/" (:slug data)) "")
          :name (company-title data)}))
      (dom/div {:id "navbar" :class "navbar-collapse collapse"}
        (dom/ul {:class "nav navbar-nav navbar-right"}
          (dom/li nil
            (om/build link {:href "/logout" :name "Logout"})))))))
