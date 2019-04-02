(ns oc.web.components.ui.menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.chat :as chat]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.jwt :as jwt-actions]
            [oc.web.lib.whats-new :as whats-new]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.components.user-profile :as user-profile]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn mobile-menu-toggle []
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:update [:mobile-menu-open] not])))

(defn mobile-menu-close []
  (dis/dispatch! [:input [:mobile-menu-open] false]))

(defn logout-click [e]
  (.preventDefault e)
  (mobile-menu-toggle)
  (jwt-actions/logout))

(defn user-profile-click [e]
  (.preventDefault e)
  (qsg-actions/next-profile-photo-trail)
  (if (responsive/is-tablet-or-mobile?)
    (user-profile/show-modal :profile)
    (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :profile)))
  (mobile-menu-toggle))

(defn notifications-settings-click [e]
  (.preventDefault e)
  (mobile-menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :notifications)))

(defn team-settings-click [e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :company-logo-2)
    (qsg-actions/next-company-logo-trail))
  (mobile-menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :main)))

(defn manage-team-click [e qsg-data]
  (.preventDefault e)
  (let [invite-team-step? (= (:step qsg-data) :invite-team-2)]
    (when invite-team-step?
      (qsg-actions/finish-invite-team-trail))
    (mobile-menu-toggle)
    (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal (if invite-team-step? :invite :team)))))

(defn sign-in-sign-up-click [e]
  (mobile-menu-toggle)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(defn show-qsg-click [e]
  (mobile-menu-toggle)
  (.preventDefault e)
  (qsg-actions/show-qsg-view))

(defn whats-new-click [e]
  (.preventDefault e)
  (whats-new/show))

(defn reminders-click [e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :create-reminder-2)
    (qsg-actions/finish-create-reminder-trail))
  (mobile-menu-toggle)
  (nav-actions/show-reminders))

(defn billing-click [e]
  (.preventDefault e)
  (mobile-menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :billing)))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  (drv/drv :qsg)
                  {:did-mount (fn [s]
                   (whats-new/init ".whats-new")
                   s)}
  [s]
  (let [{:keys [mobile-menu-open org-data team-data current-user-data]} (drv/react s :navbar-data)
        user-role (user-store/user-role org-data current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        qsg-data (drv/react s :qsg)
        show-reminders? (utils/link-for (:links org-data) "reminders")]
    [:div.menu
      {:class (utils/class-set {:mobile-menu-open (and (responsive/is-mobile-size?)
                                                       mobile-menu-open)})}
      [:div.menu-header
        [:button.mlb-reset.mobile-close-bt
          {:on-click #(do
                       (mobile-menu-toggle)
                       (nav-actions/mobile-nav-sidebar))}]
        (user-avatar-image current-user-data)
        [:div.mobile-user-name
          {:class utils/hide-class}
          (str (jwt/get-key :first-name) " " (jwt/get-key :last-name))]
        [:div.user-name
          {:class utils/hide-class}
          (str "Hi " (jwt/get-key :first-name) "!")]
        [:div.user-type
          (case user-role
            :admin
            "Admin"
            :author
            "Contributor"
            :viewer
            "Viewer")]]
      (when (jwt/jwt)
        [:a.qsg-profile-photo-2
          {:href "#"
           :on-click user-profile-click}
          (when (= (:step qsg-data) :profile-photo-2)
            (qsg-breadcrumb qsg-data))
          [:div.oc-menu-item.personal-profile
            "My Profile"]])
      (when (jwt/jwt)
        [:a
          {:href "#"
           :on-click notifications-settings-click}
          [:div.oc-menu-item.notifications-settings
            "Notification Settings"]])
      (when show-reminders?
        [:a.qsg-create-reminder-2
          {:href "#"
           :on-click #(reminders-click % qsg-data)}
          (when (= (:step qsg-data) :create-reminder-2)
            (qsg-breadcrumb qsg-data))
          [:div.oc-menu-item.reminders
            "Reminders"]])
      [:div.oc-menu-separator]
      (when org-data
        [:div.org-item
          (org-avatar org-data false false true)
          [:div.org-name (:name org-data)]
          [:div.org-url (str ls/web-server "/" (:slug org-data))]])
      (when (and (not is-mobile?)
                 (router/current-org-slug)
                 (= user-role :admin))
        [:a.qsg-invite-team-2
          {:href "#"
           :on-click #(manage-team-click % qsg-data)}
          (when (= (:step qsg-data) :invite-team-2)
            (qsg-breadcrumb qsg-data))
          [:div.oc-menu-item.manage-team
            "Manage Team"]])
      (when (and (not is-mobile?)
                 (= user-role :admin)
                 (router/current-org-slug))
        [:a.qsg-company-logo-2
          {:href "#"
           :on-click #(team-settings-click % qsg-data)}
          (when (= (:step qsg-data) :company-logo-2)
            (qsg-breadcrumb qsg-data))
          [:div.oc-menu-item.digest-settings
            "Settings"]])
      (when (and (not is-mobile?)
                 ls/billing-enabled
                 (= user-role :admin)
                 (router/current-org-slug))
        [:a
          {:href "#"
           :on-click billing-click}
          [:div.oc-menu-item.billing
            "Billing"]])
      [:a.whats-new-link
        (if is-mobile?
          {:href "https://whats-new.carrot.io/"
           :target "_blank"}
          {:on-click whats-new-click})
        [:div.oc-menu-item.whats-new
          "What’s New"]]
      (when (and (not is-mobile?)
                 (jwt/jwt)
                 (= user-role :admin))
        [:a
          {:on-click show-qsg-click}
          [:div.oc-menu-item.show-qsg
            "Quickstart Guide"]])
      [:a
        {:on-click #(chat/chat-click 42861)}
        [:div.oc-menu-item.support
          "Support"]]
      ;; Show billing stuff only to admins and if feature is enabled for env
      (when (and ls/billing-enabled
                 (= user-role :admin)
                 (not is-mobile?))
        (case
          (:exceeded-users team-data)
          [:div.billing-yellow-box
            [:div.billing-yellow-box-title
              "Team size exceeded"]
            [:div.billing-yellow-box-desc
              "You've outgrown the Free plan which covers up to 10 users."]
            [:a.billing-yellow-box-link
              {:href "#"
               :on-click billing-click}
              "Upgrade your plan"]]
          (:upgrade-plan team-data)
          [:div.billing-yellow-box
            [:div.billing-yellow-box-title
              "6 month history limit"]
            [:div.billing-yellow-box-desc
              "Your free plan maintains up to six months of history in Carrot."]
            [:a.billing-yellow-box-link
              {:href "#"
               :on-click billing-click}
              "Upgrade your plan for unlimited history"]]))
      [:div.oc-menu-separator]
      (if (jwt/jwt)
        [:a.sign-out
          {:href oc-urls/logout :on-click logout-click} 
          [:div.oc-menu-item.logout
            "Sign Out"]]
        [:a {:href "" :on-click sign-in-sign-up-click} 
          [:div.oc-menu-item
            "Sign In / Sign Up"]])]))