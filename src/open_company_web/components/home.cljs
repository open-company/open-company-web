(ns open-company-web.components.home
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.footer :refer (footer)]
            [open-company-web.components.ui.login-required :refer (login-required)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defcomponent home [data owner]

  (init-state [_]
    {:columns-num (responsive/columns-num)})

  (did-mount [_]
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (render-state [_ {:keys [columns-num]}]
    (let [card-width  (responsive/calc-card-width)]
      (dom/div {:class "home fullscreen-page"}
        (dom/div {:class "home-internal"}
          (when-not (jwt/jwt)
            (login-required (assoc data :welcome true))))
        (om/build footer {:su-preview false
                          :card-width card-width
                          :columns-num columns-num})))))