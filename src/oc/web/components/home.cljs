(ns oc.web.components.home
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.sign-up :refer (sign-up)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defcomponent home [data owner]

  (init-state [_]
    {:columns-num (responsive/columns-num)})

  (did-mount [_]
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (render-state [_ {:keys [columns-num]}]
    (let [card-width  (responsive/calc-card-width)]
      (if-not (jwt/jwt)
        (sign-up)
        (dom/div {:class "home fullscreen-page"}
          (when-not (:loading data)
            (dom/div {:class "home-internal"})
            (om/build footer {:su-preview false
                              :footer-width (responsive/total-layout-width-int card-width columns-num)})))))))