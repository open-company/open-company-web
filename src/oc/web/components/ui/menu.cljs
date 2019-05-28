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
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn menu-close [& [s]]
  (nav-actions/menu-close))

(defn logout-click [s e]
  (.preventDefault e)
  (menu-close s)
  (jwt-actions/logout))

(defn user-profile-click [s e]
  (.preventDefault e)
  (nav-actions/show-user-settings :profile))

(defn notifications-settings-click [s e]
  (.preventDefault e)
  (nav-actions/show-user-settings :notifications))

(defn team-settings-click [s e]
  (.preventDefault e)
  (nav-actions/show-org-settings :org))

(defn manage-team-click [s e]
  (.preventDefault e)
  (nav-actions/show-org-settings :team))

(defn invite-team-click [s e]
  (.preventDefault e)
  (nav-actions/show-org-settings :invite))

(defn integrations-click [s e]
  (.preventDefault e)
  (nav-actions/show-org-settings :integrations))

(defn sign-in-sign-up-click [s e]
  (menu-close s)
  (.preventDefault e)
  (user-actions/show-login :login-with-slack))

(defn whats-new-click [s e]
  (.preventDefault e)
  (whats-new/show))

(defn reminders-click [s e]
  (.preventDefault e)
  (nav-actions/show-reminders))

(rum/defcs menu < rum/reactive
                  (drv/drv :navbar-data)
                  (drv/drv :current-user-data)
  {:did-mount (fn [s]
   (whats-new/check-whats-new-badge)
   s)
   :did-remount (fn [_ s]
   (whats-new/check-whats-new-badge)
    s)}
  [s]
  (let [{:keys [panel-stack org-data board-data]} (drv/react s :navbar-data)
        current-user-data (drv/react s :current-user-data)
        user-role (user-store/user-role org-data current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        show-reminders? (utils/link-for (:links org-data) "reminders")
        expanded-user-menu (= (last panel-stack) :menu)
        org-slug (router/current-org-slug)
        is-admin-or-author? (#{:admin :author} user-role)
        show-invite-people? (and org-slug
                                 is-admin-or-author?)]
    [:div.menu
      {:class (utils/class-set {:expanded-user-menu expanded-user-menu})
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
          [:a
            {:href "#"
             :on-click (partial user-profile-click s)}
            [:div.oc-menu-item.personal-profile
              "My profile"]])
        (when (jwt/jwt)
          [:a
            {:href "#"
             :on-click (partial notifications-settings-click s)}
            [:div.oc-menu-item.notifications-settings
              "Notifications"]])
        [:div.oc-menu-separator]
        (when show-reminders?
          [:a
            {:href "#"
             :on-click #(reminders-click s %)}
            [:div.oc-menu-item.reminders
              "Recurring updates"]])
        (when (or (= user-role :admin)
                  show-invite-people?)
          [:div.oc-menu-separator])
        (when (and (not is-mobile?)
                   (= user-role :admin)
                   org-slug)
          [:a
            {:href "#"
             :on-click #(team-settings-click s %)}
            [:div.oc-menu-item.digest-settings
              "Admin settings"]])
        (when (and (not is-mobile?)
                   show-invite-people?)
          [:a
            {:href "#"
             :on-click #(invite-team-click s %)}
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
            {:on-click #(whats-new-click s %)})
          [:div.oc-menu-item.whats-new
            "What’s new"]]
        [:a
          {:on-click #(chat/chat-click 42861)}
          [:div.oc-menu-item.support
            "Chat with us"]]
        [:div.oc-menu-separator]
        (if (jwt/jwt)
          [:a.sign-out
            {:href oc-urls/logout :on-click (partial logout-click s)}
            [:div.oc-menu-item.logout
              "Sign out"]]
          [:a {:href "" :on-click (partial sign-in-sign-up-click s)}
            [:div.oc-menu-item
              "Sign in / Sign up"]])]]))