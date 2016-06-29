(ns open-company-web.components.home
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.login-required :refer (login-required)]))

(defcomponent home [data owner]
  (render [_]
    (dom/div {:class "home fullscreen-page"}
      (dom/div {:class "page-no-navbar py4"}
        (when-not (jwt/jwt)
          (om/build login-required (assoc data :welcome true)))))))