(ns open-company-web.components.navbar
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
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
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn on-transition-end [owner body]
  (doto body
    (dommy/remove-class! :left)
    (dommy/remove-class! :right)
    (dommy/remove-class! :animating)
    (dommy/toggle-class! :menu-visible))
  (events/unlistenByKey (om/get-state owner :transition-end-listener)))

(defn menu-click [owner e]
  (when e
    (.preventDefault e))
  (let [body (sel1 [:body])
        page (sel1 [:div.page])
        menu (sel1 [:ul#menu])]
    (dommy/add-class! body :animating)
    (if (dommy/has-class? body :menu-visible)
      (dommy/add-class! body :right)
      (dommy/add-class! body :left))
    (let [listener-key (events/listen page EventType/TRANSITIONEND #(on-transition-end owner body))]
      (om/set-state! owner :transition-end-listener listener-key))))

(defcomponent navbar [{:keys [company-data columns-num card-width latest-su email-loading slack-loading] :as data} owner options]

  (init-state [_]
    (utils/add-channel "close-side-menu" (chan)))

  (did-mount [_]
    (let [close-ch (utils/get-channel "close-side-menu")]
      (go (loop []
          (let [change (<! close-ch)]
            (menu-click owner nil)
            (recur))))))

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
                  (dom/button {:class "ready-slack-button"
                             :on-click (:share-slack-cb options)}
                    (dom/i {:class "fa fa-slack"})
                    (dom/label {} "SHARE ON SLACK"))
                  (dom/button {:class "ready-mail-button"
                             :on-click (:share-link-cb options)}
                    (if email-loading
                      (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                      (icon :link-72 {:size 20 :stroke "4" :color "rgba(78, 90, 107, 0.7)" :accent-color "rgba(78, 90, 107, 0.7)"}))
                    (dom/label {} "SHARE URL"))))
              (dom/button {:class "close-preview"
                           :on-click #(router/nav! (oc-urls/company))}
                (icon :simple-remove {:stroke "4" :color "rgba(255, 255, 255, 0.8)" :accent-color "rgba(255, 255, 255, 0.8)"})))))
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