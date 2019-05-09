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
            [oc.web.mixins.ui :as mixins]
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

(defn real-menu-close []
  (dis/dispatch! [:input [:expanded-user-menu] false]))

(defn menu-toggle []
  (dis/dispatch! [:update [:expanded-user-menu] not]))

(defn menu-close [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-menu-close)))

(defn logout-click [s e]
  (.preventDefault e)
  (menu-close s)
  (jwt-actions/logout))

(defn user-profile-click [s e]
  (.preventDefault e)
  (qsg-actions/next-profile-photo-trail)
  (if (responsive/is-tablet-or-mobile?)
    (user-profile/show-modal :profile)
    (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :profile)))
  (menu-close s))

(defn notifications-settings-click [s e]
  (.preventDefault e)
  (menu-close s)
  (utils/after (+ utils/oc-animation-duration 100) #(user-profile/show-modal :notifications)))

(defn team-settings-click [s e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :company-logo-2)
    (qsg-actions/next-company-logo-trail))
  (menu-close s)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :main)))

(defn manage-team-click [s e]
  (.preventDefault e)
  (menu-close s)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :team)))

(defn invite-team-click [s e  qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :invite-team-2)
    (qsg-actions/finish-invite-team-trail))
  (menu-close s)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :invite)))

(defn integrations-click [s e]
  (.preventDefault e)
  (menu-close s)
  (utils/after (+ utils/oc-animation-duration 100) #(org-settings/show-modal :integrations)))

(defn sign-in-sign-up-click [s e]
  (menu-close s)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(defn show-qsg-click [s e]
  (menu-close s)
  (.preventDefault e)
  (qsg-actions/show-qsg-view))

(defn whats-new-click [s e]
  (.preventDefault e)
  (whats-new/show))

(defn reminders-click [s e qsg-data]
  (.preventDefault e)
  (when (= (:step qsg-data) :create-reminder-2)
    (qsg-actions/finish-create-reminder-trail))
  (menu-close s)
  (nav-actions/show-reminders))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  (drv/drv :current-user-data)
                  (drv/drv :qsg)
                  ;; Locals
                  (rum/local false ::unmounting)
                  (rum/local false ::unmounted)
                  ;; Mixins
                  mixins/no-scroll-mixin
                  mixins/first-render-mixin
                  {:did-mount (fn [s]
                   (whats-new/init ".whats-new")
                   s)
                   :did-update (fn [s]
                    (when (and @(::unmounting s)
                               (compare-and-set! (::unmounted s) false true))
                      (utils/after 180 real-menu-close))
                    s)}
  [s]
  (let [{:keys [expanded-user-menu org-data board-data]} (drv/react s :navbar-data)
        current-user-data (drv/react s :current-user-data)
        user-role (user-store/user-role org-data current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        qsg-data (drv/react s :qsg)
        show-reminders? (utils/link-for (:links org-data) "reminders")
        appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        org-slug (router/current-org-slug)
        is-admin-or-author? (#{:admin :author} user-role)
        show-invite-people? (and org-slug
                                 is-admin-or-author?)]
    [:div.menu
      {:class (utils/class-set {:expanded-user-menu expanded-user-menu
                                :appear appear-class})
       :on-click #(when-not (utils/event-inside? % (rum/ref-node s :menu-container))
                    (menu-close s))}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(menu-close s)}]
      [:div.menu-container
        {:ref :menu-container}
        [:div.menu-header.group
          [:button.mlb-reset.mobile-close-bt
            {:on-click #(do
                         (menu-close s)
                         (nav-actions/mobile-nav-sidebar))}]
          [:div.user-name
            {:class utils/hide-class}
            (str (jwt/get-key :first-name) " " (jwt/get-key :last-name))]
          (user-avatar-image current-user-data)]
        (when (jwt/jwt)
          [:a.qsg-profile-photo-2
            {:href "#"
             :on-click (partial user-profile-click s)}
            (when (= (:step qsg-data) :profile-photo-2)
              (qsg-breadcrumb qsg-data))
            [:div.oc-menu-item.personal-profile
              "My Profile"]])
        (when (jwt/jwt)
          [:a
            {:href "#"
             :on-click (partial notifications-settings-click s)}
            [:div.oc-menu-item.notifications-settings
              "Notifications"]])
        [:div.oc-menu-separator]
        (when show-reminders?
          [:a.qsg-create-reminder-2
            {:href "#"
             :on-click #(reminders-click s % qsg-data)}
            (when (= (:step qsg-data) :create-reminder-2)
              (qsg-breadcrumb qsg-data))
            [:div.oc-menu-item.reminders
              "Recurring updates"]])
        (when (or (= user-role :admin)
                  show-invite-people?)
          [:div.oc-menu-separator])
        (when (and (not is-mobile?)
                   (= user-role :admin)
                   org-slug)
          [:a.qsg-company-logo-2
            {:href "#"
             :on-click #(team-settings-click s % qsg-data)}
            (when (= (:step qsg-data) :company-logo-2)
              (qsg-breadcrumb qsg-data))
            [:div.oc-menu-item.digest-settings
              "Admin Settings"]])
        (when (and (not is-mobile?)
                   show-invite-people?)
          [:a.qsg-invite-team-2
            {:href "#"
             :on-click #(invite-team-click s %  qsg-data)}
            (when (= (:step qsg-data) :invite-team-2)
              (qsg-breadcrumb qsg-data))
            [:div.oc-menu-item.invite-team
              "Invite people"]])
        (when (and (not is-mobile?)
                   org-slug
                   (= user-role :admin))
          [:a
            {:href "#"
             :on-click #(manage-team-click s %)}
            [:div.oc-menu-item.manage-team
              "Manage team"]])
        (when (and (not is-mobile?)
                   org-slug
                   (= user-role :admin))
          [:a
            {:href "#"
             :on-click #(integrations-click s %)}
            [:div.oc-menu-item.team-integrations
              "Integrations"]])
        ; (when (and org-slug
        ;            (= user-role :admin))
        ;   [:a {:href "#" :on-click #(js/alert "Coming soon")} 
        ;     [:div.oc-menu-item
        ;       "Billing"]])
        [:div.oc-menu-separator]
        [:a.whats-new-link
          (if is-mobile?
            {:href "https://whats-new.carrot.io/"
             :target "_blank"}
            {:on-click (partial whats-new-click s)})
          [:div.oc-menu-item.whats-new
            "What’s New"]]
        (when (and (not is-mobile?)
                   (jwt/jwt)
                   (= user-role :admin))
          [:a
            {:on-click (partial show-qsg-click s)}
            [:div.oc-menu-item.show-qsg
              "Quickstart Guide"]])
        [:a
          {:on-click #(chat/chat-click 42861)}
          [:div.oc-menu-item.support
            "Chat with us"]]
        [:div.oc-menu-separator]
        (if (jwt/jwt)
          [:a.sign-out
            {:href oc-urls/logout :on-click (partial logout-click s)}
            [:div.oc-menu-item.logout
              "Sign Out"]]
          [:a {:href "" :on-click (partial sign-in-sign-up-click s)}
            [:div.oc-menu-item
              "Sign In / Sign Up"]])]]))