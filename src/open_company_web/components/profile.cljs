(ns open-company-web.components.profile
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.components.profile-components.company :refer [basics mission on-the-web]]
            [om-bootstrap.nav :as n]
            [om-bootstrap.panel :as p]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :refer [add-channel get-channel]]
            [open-company-web.api :refer [save-or-create-company]]
            [cljs.core.async :refer [put! chan <!]]
            [open-company-web.dispatcher :refer [app-state]]))

(defcomponent profile [data owner]
  (init-state [_]
    (let [save-chan (chan)]
      (add-channel "save-company" save-chan)))
  (will-mount [_]
    (om/set-state! owner :selected-tab 1)
    (let [save-change (get-channel "save-company")]
        (go (loop []
          (let [change (<! save-change)
                ticker (:ticker @router/path)
                company-data ((keyword ticker) @app-state)]
            (save-or-create-company ticker company-data)
            (recur))))))
  (render [_]
    (let [ticker (:ticker @router/path)
          company-data ((keyword ticker) data)]
      (dom/div
        (n/nav {
          :class "profile-tab-navigation"
          :bs-style "tabs"
          :active-key (om/get-state owner :selected-tab)
          :on-select #(om/set-state! owner :selected-tab %) }
          (let [url (str "/" ticker)]
            (n/nav-item {
              :key 1
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Company"))
          (let [url (str "/" ticker "#team")]
            (n/nav-item {
              :key 2
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Team"))
          (let [url (str "/" ticker "#products")]
            (n/nav-item {
              :key 3
              :href url
              :onClick (fn[e] (.preventDefault e) (router/nav! url))}
              "Products / Services"))
          (let [url (str "/" ticker "#funding")]
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
