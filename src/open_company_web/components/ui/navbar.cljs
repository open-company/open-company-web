(ns open-company-web.components.ui.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn menu-click [owner e]
  (when e
    (.preventDefault e))
  (dis/toggle-menu))

(defcomponent navbar [{:keys [columns-num
                              card-width
                              show-share-su-button] :as data} owner options]

  (render [_]
    (let [header-width (+ (* card-width columns-num)    ; cards width
                          (* 20 (dec columns-num))      ; cards right margin
                          (when (> columns-num 1) 60))] ; x margins if needed
      (dom/nav {:class "oc-navbar group"}
        (when (and (not (utils/is-test-env?)) (not (jwt/jwt)))
          (login-overlays-handler))
        (dom/div {:class "oc-navbar-header"
                  :style #js {:width (str header-width "px")}}
          (if (utils/in? (:route @router/path) "companies")
            (dom/a {:href "https://opencompany.com/" :title "OpenCompany.com"}
              (dom/img {:src "/img/oc-wordmark.svg" :style {:height "25px" :margin-top "12px"}}))
            (om/build company-avatar data))
          (when-not (:hide-right-menu data)
            (dom/ul {:class "nav navbar-nav navbar-right"}
              (dom/li {}
                (if (responsive/is-mobile-size?)
                  (dom/div {:on-click (partial menu-click owner)}
                    (i/icon :menu-34 {}))
                  (if (jwt/jwt)
                    (dom/div {}
                      (when show-share-su-button
                        (dom/div {:class "sharing-button-container dropdown"}
                          (dom/button {:class "btn-reset sharing-button right"
                                       :aria-haspopup true
                                       :aria-expanded false
                                       :id "share-an-update"
                                       :data-toggle "dropdown"
                                       :disabled (not (nil? (:foce-key data)))}
                            (dom/i {:class "fa fa-share"}) " SHARE AN UPDATE")
                          (dom/div {:class "dropdown-menu" :aria-labelledby "share-an-update"}
                            (dom/button {:class "btn-reset dropdown-item"
                                         :on-click #(do
                                                      (.preventDefault %)
                                                      (router/nav! (oc-urls/stakeholder-update-preview :email)))}
                              (dom/i {:class "fa fa-envelope"}) "  SHARE BY EMAIL")
                            (dom/button {:class "btn-reset dropdown-item"
                                         :on-click #(do
                                                      (.preventDefault %)
                                                      (router/nav! (oc-urls/stakeholder-update-preview :slack)))}
                              (dom/i {:class "fa fa-slack"}) "  SHARE TO SLACK")
                            (dom/button {:class "btn-reset dropdown-item"
                                         :on-click #(do
                                                      (.preventDefault %)
                                                      (router/nav! (oc-urls/stakeholder-update-preview :link)))}
                              (dom/i {:class "fa fa-link"}) "  SHARE A LINK"))))
                      (user-avatar (partial menu-click owner)))
                    (login-button)))))))))))