(ns oc.web.components.ui.menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.org-settings :as org-settings]))

(defn mobile-menu-toggle []
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:update [:mobile-menu-open] not])))

(defn mobile-menu-close []
  (dis/dispatch! [:input [:mobile-menu-open] false]))

(defn logout-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  (user-actions/logout))

(defn user-profile-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn team-settings-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  ; (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings)))
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :main)))

(defn um-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  ; (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings-team))))
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :team)))

(defn invite-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  ; (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings-invite))))
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :invite)))

(defn sign-in-sign-up-click [e]
  (mobile-menu-toggle)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
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
                 is-admin?
                 (not (responsive/is-mobile-size?)))
        [:div.oc-menu-item
          [:a {:href (oc-urls/org-settings-team) :on-click um-click} "Manage Members"]])
      (when (and (router/current-org-slug)
                 is-admin?
                 (not (responsive/is-mobile-size?)))
        [:div.oc-menu-item.divider-item
          [:a {:href (oc-urls/org-settings-invite) :on-click invite-click} "Invite People"]])
      ; (when (and (router/current-org-slug)
      ;            is-admin?)
      ;   [:div.oc-menu-item.divider-item
      ;     [:a {:href "#" :on-click #(js/alert "Coming soon")} "Billing"]])
      ; (when (and (jwt/jwt)
      ;            (or is-admin?
      ;                is-author?))
      ;   [:div.oc-menu-item.divider-item
      ;     [:a {:href "#" :on-click #(js/alert "Coming soon")} "Archive"]])
      (when (and (jwt/jwt)
                 (not (responsive/is-mobile-size?)))
        [:div.oc-menu-item.divider-item
          [:a {:href oc-urls/user-profile :on-click user-profile-click} "User Profile"]])
      (if (jwt/jwt)
        [:div.oc-menu-item
          [:a.sign-out {:href oc-urls/logout :on-click logout-click} "Sign Out"]]
        [:div.oc-menu-item
          [:a {:href "" :on-click sign-in-sign-up-click} "Sign In / Sign Up"]])]))