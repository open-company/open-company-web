(ns open-company-web.components.menu
  (:require [cljs.core.async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn close-menu []
  (dis/toggle-menu))

(defn stop [e]
  (.preventDefault e)
  (.stopPropagation e))

(defn logout-click [e]
  (stop e)
  (dis/dispatch! [:logout]))

(defn user-profile-click [e]
  (stop e)
  (dis/save-last-company-slug)
  (close-menu)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn company-profile-click [e]
  (stop e)
  (close-menu)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/company-profile))))

(defn on-transition-end [owner body]
  (doto body
    (dommy/remove-class! :left)
    (dommy/remove-class! :right)
    (dommy/remove-class! :animating)
    (dommy/toggle-class! :menu-visible))
  (events/unlistenByKey (om/get-state owner :transition-end-listener)))

(defn toggle-menu [owner close?]
  (let [body (sel1 [:body])
        page (sel1 [:div.page])]
    (dommy/add-class! body :animating)
    (if (dommy/has-class? body :menu-visible)
      (dommy/add-class! body :right)
      (dommy/add-class! body :left))
    (let [listener-key (events/listen page EventType/TRANSITIONEND #(on-transition-end owner body))]
      (om/set-state! owner :transition-end-listener listener-key))))

(defcomponent menu [data owner options]

  (did-mount [_]
    (when (:menu-open data)
      (let [body (sel1 [:body])]
        (dommy/add-class! body :menu-visible))))

  (will-receive-props [_ next-props]
    (cond
      (and (:menu-open data)
           (not (:menu-open next-props)))
      (toggle-menu owner true)
      (and (not (:menu-open data))
           (:menu-open next-props))
      (toggle-menu owner false)))

  (render [_]
    (dom/ul {:id "menu"}
      (dom/li {:class "oc-title"} "OpenCompany")
      (when (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "USER INFO" :href oc-urls/user-profile :on-click user-profile-click} "USER INFO")))
      (when (and (router/current-company-slug)
                 (not (utils/in? (:route @router/path) "profile")))
        (dom/li {:class "menu-link"} (dom/a {:title "COMPANY SETTINGS" :href (oc-urls/company-profile) :on-click company-profile-click} "COMPANY SETTINGS")))
      (when (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "SIGN OUT" :href oc-urls/logout :on-click logout-click} "SIGN OUT")))
      (when-not (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "SIGN IN / SIGN UP" :href oc-urls/login} "SIGN IN / SIGN UP")))
      ; (dom/li {} (dom/a {:title "ABOUT US" :href oc-urls/about} "ABOUT US"))
      (dom/li {:class "menu-link"} (dom/a {:title "CONTACT" :href oc-urls/contact-mail-to} "CONTACT")))))