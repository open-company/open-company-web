(ns open-company-web.components.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (put!)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :refer (icon)]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]))

(defn close-preview-clicked [e]
  (.preventDefault e)
  (.stopPropagation e)
  (router/nav! (oc-urls/company)))

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
        (when (:su-preview data)
          (dom/div {:class "su-snapshot-preview"}
            (dom/div {:class "su-snapshot-preview-internal group"
                      :style #js {:width (str header-width "px")}}
              (when (> columns-num 1)
                (dom/div {:class "su-snapshot-buttons group"}
                  (dom/button {:class "btn-reset btn-solid mx1"
                               :on-click (:share-slack-cb options)}

                    (if slack-loading
                      (loading/small-loading)
                      (dom/i {:class "fa fa-slack mr1"}))
                    "SHARE ON SLACK")
                  (dom/button {:class "btn-reset btn-solid mx1"
                               :on-click (:share-link-cb options)}
                    (if link-loading
                      (loading/small-loading)
                      (icon :link-72 {:class "mr1 inline" :size 20 :stroke "4" :color "rgba(78, 90, 107, 0.7)" :accent-color "rgba(78, 90, 107, 0.7)"}))
                     "SHARE URL")
                  (dom/button {:class "btn-reset btn-solid mx1"
                               :on-click (:share-email-cb options)}
                    (if email-loading
                      (loading/small-loading)
                      (icon :email-84 {:class "mr1 inline" :size 20 :stroke "4" :color "rgba(78, 90, 107, 0.7)" :accent-color "rgba(78, 90, 107, 0.7)"}))
                     "SHARE VIA EMAIL")))
              (dom/button {:class "close-preview"
                           :on-click close-preview-clicked}
                (icon :simple-remove {:class "mr1" :stroke "4" :color "rgba(255, 255, 255, 0.8)" :accent-color "rgba(255, 255, 255, 0.8)"})))))
        (dom/div {:class "oc-navbar-header"
                  :style #js {:width (str header-width "px")}}
          (om/build company-avatar data)
          (when-not (:hide-right-menu data)
            (dom/ul {:class "nav navbar-nav navbar-right"}
              (dom/li {}
                (if (responsive/is-mobile)
                  (dom/div {:on-click (partial menu-click owner)}
                    (icon "menu-34"))
                  (if (jwt/jwt)
                    (om/build user-avatar {:menu-click (partial menu-click owner)})
                    (om/build login-button (assoc data :menu-click (partial menu-click owner)))))))))))))