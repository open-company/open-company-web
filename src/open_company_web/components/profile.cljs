(ns open-company-web.components.profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.profile-components.company :refer [basics mission on-the-web]]
            [om-bootstrap.nav :as n]
            [om-bootstrap.panel :as p]
            [open-company-web.router :as router]))

(defcomponent profile [data owner]
  (will-mount [_]
    (om/set-state! owner :selected-tab 1))
  (render [_]
    (let [symbol (:symbol data)
          company-data ((keyword symbol) data)]
      (dom/div
        (n/nav {
          :class "profile-tab-navigation"
          :bs-style "tabs"
          :active-key (om/get-state owner :selected-tab)
          :on-select #(om/set-state! owner :selected-tab %) }
          (let [url (str "/" symbol)]
            (n/nav-item {
              :key 1
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Company"))
          (let [url (str "/" symbol "#team")]
            (n/nav-item {
              :key 2
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Team"))
          (let [url (str "/" symbol "#products")]
            (n/nav-item {
              :key 3
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Products / Services"))
          (let [url (str "/" symbol "#funding")]
            (n/nav-item {
              :key 4
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Funding")))
        (case (om/get-state owner :selected-tab)

          ;; Company
          1 (dom/div {}
              (om/build basics company-data)
              (om/build mission company-data)
              (om/build on-the-web company-data)))))))
