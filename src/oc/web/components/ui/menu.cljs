(ns oc.web.components.ui.menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (dis/dispatch! [:logout]))

(defn boards-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/boards (router/current-org-slug)))))

(defn user-profile-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn team-settings-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings))))

(defn um-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-team-settings))))

(defn sign-in-sign-up-click [e]
  (dis/dispatch! [:mobile-menu-toggle])
  (.preventDefault e)
  (dis/dispatch! [:login-overlay-show :login-with-slack]))

(defn list-boards-click [e]
  (dis/dispatch! [:mobile-menu-toggle])
  (.preventDefault e)
  (router/nav! (oc-urls/boards)))

(def nav-height 69)
(def menu-item-height 38)

(defn setup-mobile-nav-height [mobile-menu-open]
  (when (responsive/is-mobile-size?)
    (let [nav-item (sel1 [:nav])
          menu-items-count (count (sel [:div.menu :div.oc-menu-item]))
          final-nav-height (str (+ nav-height
                                   (when mobile-menu-open (+ (* menu-item-height menu-items-count) 14)))
                                "px")]
      (dommy/set-style! nav-item :height final-nav-height))))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  {:did-mount (fn [s]
                                (setup-mobile-nav-height (:mobile-menu-open @(drv/get-ref s :navbar-data)))
                                s)
                   :did-update (fn [s]
                                 (setup-mobile-nav-height (:mobile-menu-open @(drv/get-ref s :navbar-data)))
                                 s)}
  [s]
  (let [{:keys [mobile-menu-open org-data board-data]} (drv/react s :navbar-data)
        is-admin? (jwt/is-admin? (:team-id org-data))
        is-author? (utils/link-for (:links org-data) "create")]
    [:div.menu
      {:class (utils/class-set {:dropdown-menu (not (responsive/is-mobile-size?))
                                :mobile-menu-open (and (responsive/is-mobile-size?)
                                                       mobile-menu-open)})
       :aria-labelledby "dropdown-toggle-menu"}
      [:div.top-arrow]
      [:div.menu-header
        [:div.user-name
          (str "Hi " (jwt/get-key :first-name) "!")]
        [:div.user-type
          (cond
            is-admin?
            "You're an Admin"
            is-author?
            "You're a Contributor")]]
      (when (and is-admin?
                 (router/current-org-slug)
                 (not (responsive/is-mobile-size?)))
        [:div.oc-menu-item
          [:a {:href (oc-urls/org-settings) :on-click team-settings-click} "Team Settings"]])
      (when (and (router/current-org-slug)
                 is-admin?)
        [:div.oc-menu-item
          [:a {:href (oc-urls/org-team-settings) :on-click um-click} "Manage Members"]])
      (when (and (router/current-org-slug)
                 is-admin?)
        [:div.oc-menu-item
          [:a {:href (oc-urls/org-team-settings) :on-click um-click} "Invite People"]])
      (when (and (router/current-org-slug)
                 is-admin?)
        [:div.oc-menu-item.divider-item
          [:a {:href "#" :on-click #(js/alert "Coming soon")} "Billing"]])
      ;; Temp commenting this out since we need API support to know how many companies the user has
      (when (and (jwt/jwt)
                 (or is-admin?
                     is-author?))
        [:div.oc-menu-item.divider-item
          [:a {:href "#" :on-click #(js/alert "Coming soon")} "Archive"]])
      (when (jwt/jwt)
        [:div.oc-menu-item
          [:a {:href oc-urls/user-profile :on-click user-profile-click} "User Profile"]])
      (if (jwt/jwt)
        [:div.oc-menu-item
          [:a.sign-out {:href oc-urls/logout :on-click logout-click} "Sign Out"]]
        [:div.oc-menu-item
          [:a {:href "" :on-click sign-in-sign-up-click} "Sign In / Sign Up"]])]))