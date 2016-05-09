(ns open-company-web.components.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [om-bootstrap.nav :as n]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.jwt :as jwt]
            [cljs.core.async :refer (put!)]))

(defcomponent navbar [data owner]

  (render [_]
    (let [columns-num (:columns-num data)
          card-width (:card-width data)
          header-width (+ (* card-width columns-num)    ; cards width
                          (* 20 (dec columns-num))      ; cards right margin
                          (when (> columns-num 1) 60))] ; x margins if needed
      (dom/nav {:class "oc-navbar group"}
        (dom/div {:class "oc-navbar-header"
                  :style #js {:width (str header-width "px")}}
          (om/build company-avatar data)
          (dom/ul {:class "nav navbar-nav navbar-right"}
            (dom/li {}
              (if (jwt/jwt)
                (om/build user-avatar {})
                (om/build login-button data)))))))))