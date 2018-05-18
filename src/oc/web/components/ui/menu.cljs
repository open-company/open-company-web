(ns oc.web.components.ui.menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.whats-new-modal :as whats-new-modal]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

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

(defn invite-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  ; (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings-invite))))
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :invite)))

(defn sign-in-sign-up-click [e]
  (mobile-menu-toggle)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(defn whats-new-click [e]
  (utils/event-stop e)
  (mobile-menu-toggle)
  (whats-new-modal/show-modal))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  (drv/drv :current-user-data)
  [s]
  (let [{:keys [mobile-menu-open org-data board-data]} (drv/react s :navbar-data)
        current-user-data (drv/react s :current-user-data)
        user-role (user-store/user-role org-data current-user-data)]
    [:div.menu
      {:class (utils/class-set {:dropdown-menu (not (responsive/is-mobile-size?))
                                :mobile-menu-open (and (responsive/is-mobile-size?)
                                                       mobile-menu-open)})
       :aria-labelledby "dropdown-toggle-menu"
       :aria-expanded true}
      [:div.menu-header
        (user-avatar-image current-user-data)
        [:div.user-name.fs-hide
          (str "Hi " (jwt/get-key :first-name) "!")]
        [:div.user-type
          (case user-role
            :admin
            "Admin"
            :author
            "Contributor"
            :viewer
            "Viewer")]]
      (when (and (jwt/jwt)
                 (not (responsive/is-mobile-size?)))
        [:a
          {:href oc-urls/user-profile
           :on-click user-profile-click}
          [:div.oc-menu-item.personal-profile
            "Personal Profile"]])
      [:div.oc-menu-separator]
      (when org-data
        [:a
          {:href (oc-urls/org (:slug org-data))
           :on-click #(router/nav! (oc-urls/org (:slug org-data)))}
          [:div.org-item
            [:div.org-avatar-border
              (org-avatar org-data false false true)]
            [:div.org-name (:name org-data)]
            [:div.org-url (str ls/web-server "/" (:slug org-data))]]])
      (when (and (router/current-org-slug)
                 (= user-role :admin)
                 (not (responsive/is-mobile-size?)))
        [:a
          {:href (oc-urls/org-settings-invite)
           :on-click invite-click}
          [:div.oc-menu-item.invite-people
            "Invite People"]])
      (when (and (= user-role :admin)
                 (router/current-org-slug)
                 (not (responsive/is-mobile-size?)))
        [:a
          {:href (oc-urls/org-settings)
           :on-click team-settings-click}
          [:div.oc-menu-item.digest-settings
            "Digest Settings"]])
      [:a
        {:href "#"
         :on-click whats-new-click}
        [:div.oc-menu-item.whats-new
          "What’s New"]]
      [:a
        {:href oc-urls/help
         :target "_blank"}
        [:div.oc-menu-item.support
          "Support"]]

      ; (when (and (router/current-org-slug)
      ;            (= user-role :admin))
      ;   [:div.oc-menu-item
      ;     [:a {:href "#" :on-click #(js/alert "Coming soon")} "Billing"]])
      [:div.oc-menu-separator]
      (if (jwt/jwt)
        [:a.sign-out
          {:href oc-urls/logout :on-click logout-click} 
          [:div.oc-menu-item.logout
            "Sign Out"]]
        [:a {:href "" :on-click sign-in-sign-up-click} 
          [:div.oc-menu-item
            "Sign In / Sign Up"]])]))