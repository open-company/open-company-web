(ns open-company-web.components.navbar
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (put!)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
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

(defcomponent navbar [data owner]

  (init-state [_]
    (utils/add-channel "close-side-menu" (chan)))

  (did-mount [_]
    (let [close-ch (utils/get-channel "close-side-menu")]
      (go (loop []
          (let [change (<! close-ch)]
            (menu-click owner nil)
            (recur))))))

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
              (if (responsive/is-mobile)
                (dom/div {:on-click (partial menu-click owner)}
                    (icon "menu-34"))
                (if (jwt/jwt)
                  (om/build user-avatar {:menu-click (partial menu-click owner)})
                  (om/build login-button (assoc data :menu-click (partial menu-click owner))))))))))))