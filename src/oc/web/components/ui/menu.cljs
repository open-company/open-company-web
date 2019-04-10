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
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.components.user-profile :as user-profile]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn menu-toggle []
  (dis/dispatch! [:update [:expanded-user-menu] not]))

(defn menu-close []
  (dis/dispatch! [:input [:expanded-user-menu] false]))

(defn logout-click [e]
  (.preventDefault e)
  (menu-toggle)
  (jwt-actions/logout))

(defn user-profile-click [e]
  (.preventDefault e)
  (qsg-actions/next-profile-photo-trail)
  (if (responsive/is-tablet-or-mobile?)
    (user-profile/show-modal :profile)
    (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :profile)))
  (menu-toggle))

(defn notifications-settings-click [e]
  (.preventDefault e)
  (menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :notifications)))

(defn team-settings-click [e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :company-logo-2)
    (qsg-actions/next-company-logo-trail))
  (menu-toggle)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :main)))

(defn manage-team-click [e qsg-data]
  (.preventDefault e)
  (let [invite-team-step? (= (:step qsg-data) :invite-team-2)]
    (when invite-team-step?
      (qsg-actions/finish-invite-team-trail))
    (menu-toggle)
    (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal (if invite-team-step? :invite :team)))))

(defn sign-in-sign-up-click [e]
  (menu-toggle)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(defn show-qsg-click [e]
  (menu-toggle)
  (.preventDefault e)
  (qsg-actions/show-qsg-view))

(defn whats-new-click [e]
  (.preventDefault e)
  (whats-new/show))

(defn reminders-click [e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :create-reminder-2)
    (qsg-actions/finish-create-reminder-trail))
  (menu-toggle)
  (nav-actions/show-reminders))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  (drv/drv :current-user-data)
                  (drv/drv :qsg)
                  ;; Mixins
                  no-scroll-mixin
                  {:did-mount (fn [s]
                   (whats-new/init ".whats-new")
                   s)}
  [s]
  (let [{:keys [expanded-user-menu org-data board-data]} (drv/react s :navbar-data)
        current-user-data (drv/react s :current-user-data)
        user-role (user-store/user-role org-data current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        qsg-data (drv/react s :qsg)
        show-reminders? (utils/link-for (:links org-data) "reminders")]
    [:div.menu
      {:class (utils/class-set {:expanded-user-menu expanded-user-menu})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(menu-close)}]
      [:div.menu-container
        [:div.menu-header.group
          [:button.mlb-reset.mobile-close-bt
            {:on-click #(do
                         (menu-toggle)
                         (nav-actions/mobile-nav-sidebar))}]
          [:div.user-name
            {:class utils/hide-class}
            (str (jwt/get-key :first-name) " " (jwt/get-key :last-name))]
          (user-avatar-image current-user-data)]
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
              "Notifications"]])
        (when show-reminders?
          [:a.qsg-create-reminder-2
            {:href "#"
             :on-click #(reminders-click % qsg-data)}
            (when (= (:step qsg-data) :create-reminder-2)
              (qsg-breadcrumb qsg-data))
            [:div.oc-menu-item.reminders
              "Reminders"]])
        [:div.oc-menu-separator]
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
        ; (when (and (router/current-org-slug)
        ;            (= user-role :admin))
        ;   [:div.oc-menu-item
        ;     [:a {:href "#" :on-click #(js/alert "Coming soon")} "Billing"]])
        (if (jwt/jwt)
          [:a.sign-out
            {:href oc-urls/logout :on-click logout-click} 
            [:div.oc-menu-item.logout
              "Sign Out"]]
          [:a {:href "" :on-click sign-in-sign-up-click} 
            [:div.oc-menu-item
              "Sign In / Sign Up"]])]]))