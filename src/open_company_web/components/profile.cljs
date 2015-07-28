(ns open-company-web.components.profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.profile-components.company :refer [company]]
            [om-bootstrap.nav :as n]
            [om-bootstrap.panel :as p]))

(defcomponent profile [data owner]
  (will-mount [_]
    (om/set-state! owner :selected-tab 1))
  (render [_]
    (dom/div
      (n/nav {
        :class "profile-tab-navigation"
        :bs-style "tabs"
        :active-key (om/get-state owner :selected-tab)
        :on-select #(om/set-state! owner :selected-tab %) }
        (n/nav-item {:key 1 :href "/companies/profile/company"} "Company")
        (n/nav-item {:key 2 :href "/companies/profile/team"} "Team")
        (n/nav-item {:key 3 :href "/companies/profile/products"} "Products / Services")
        (n/nav-item {:key 4 :href "/companies/profile/Funding"} "Funding"))
      (om/build company data))))
