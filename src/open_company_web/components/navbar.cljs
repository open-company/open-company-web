(ns open-company-web.components.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]))

(defn menu-click [owner e]
  (when e
    (.preventDefault e))
  (dis/toggle-menu))

(defcomponent navbar [{:keys [company-data columns-num card-width latest-su link-loading email-loading slack-loading menu-open] :as data} owner options]

  (render [_]
    (let [header-width (+ (* card-width columns-num)    ; cards width
                          (* 20 (dec columns-num))      ; cards right margin
                          (when (> columns-num 1) 60))] ; x margins if needed
      (dom/nav {:class "oc-navbar group"}
        (dom/div {:class "oc-navbar-header"
                  :style #js {:width (str header-width "px")}}
          (om/build company-avatar data)
          (when-not (:hide-right-menu data)
            (dom/ul {:class "nav navbar-nav navbar-right"}
              (dom/li {}
                (if (responsive/is-mobile)
                  (dom/div {:on-click (partial menu-click owner)}
                    (i/icon :menu-34 {}))
                  (if (jwt/jwt)
                    (user-avatar (partial menu-click owner))
                    (login-button (:auth-settings data))))))))))))