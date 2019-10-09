(ns oc.web.components.org-settings
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]
            [oc.web.components.ui.org-settings-main-panel :refer (org-settings-main-panel)]
            [oc.web.components.ui.org-settings-team-panel :refer (org-settings-team-panel)]
            [oc.web.components.ui.org-settings-invite-panel :refer (org-settings-invite-panel)]
            [oc.web.components.ui.org-settings-billing-panel :refer (org-settings-billing-panel)]))

;; FIXME: for billing stuff go back at this file from this commit 43a0566e2b78c3ca97c9d5b86b5cc2519bf76005

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] (or panel :main)]))

(defn dismiss-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] nil]))

(rum/defc org-settings-tabs
  [org-data active-tab]
  [:div.org-settings-tabs.group
    [:div.org-settings-bottom-line]
    (when (utils/is-admin? org-data)
      [:div.org-settings-tab
        {:class (when (= :main active-tab) "active")}
        [:a.org-settings-tab-link
          {:href "#"
           :on-click #(do (utils/event-stop %) (show-modal :main))}
          "SETTINGS"]])
    (when (utils/is-admin? org-data)
      [:div.org-settings-tab
        {:class (when (= :team active-tab) "active")}
        [:a.org-settings-tab-link
          {:href "#"
           :on-click #(do (utils/event-stop %) (show-modal :team))}
          "MANAGE TEAM"]])
    (when (utils/is-admin-or-author? org-data)
      [:div.org-settings-tab
        {:class (when (= :invite active-tab) "active")}
        [:a.org-settings-tab-link
          {:href "#"
           :on-click (fn [e]
                       (utils/event-stop e)
                       (show-modal :invite))}
          "INVITE PEOPLE"]])
    (when (and (utils/is-admin? org-data)
               ls/billing-enabled)
      [:div.org-settings-tab
        {:class (when (= :billing active-tab) "active")}
        [:a.org-settings-tab-link
          {:href "#"
           :on-click (fn [e]
                      (utils/event-stop e)
                      (show-modal :billing))}
          "PAYMENTS & BILLING"]])])

(defn close-clicked [s]
  (let [org-data @(drv/get-ref s :org-data)
        org-editing @(drv/get-ref s :org-editing)
        invite-users (filterv #(not (:error %)) (:invite-users @(drv/get-ref s :invite-data)))
        has-unsent-invites (and (pos? (count invite-users))
                                (some #(seq (:user %)) invite-users))
        active-tab @(drv/get-ref s :org-settings)]
    (if (or (and (= :main active-tab)
                 (:has-changes org-editing))
            (and (= :invite active-tab)
                 has-unsent-invites))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "org-settings-unsaved-edits"
                        :message "Leave without saving your changes?"
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(do
                                            (alert-modal/hide-alert)
                                            (dismiss-modal))}]
        (alert-modal/show-alert alert-data))
      (dismiss-modal))))

(defn logo-on-load [org-avatar-editing url img]
  (org-actions/org-avatar-edit-save {:logo-url url
                                     :logo-width (.-width img)
                                     :logo-height (.-height img)})
  (gdom/removeNode img))

(defn logo-add-error
  "Show an error alert view for failed uploads."
  [img]
  (notification-actions/show-notification
   {:title "Image upload error"
    :description "An error occurred while processing your company avatar. Please retry."
    :expire 5
    :id :org-avatar-upload-failed
    :dismiss true})
  (when img
    (gdom/removeNode img)))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-logo (rum/ref-node s "org-settings-header-logo")
          $header-logo (js/$ header-logo)
          org-avatar-editing @(drv/get-ref s :org-avatar-editing)
          title (if (empty? (:logo-url org-avatar-editing))
                  "Add a logo"
                  "Change logo")]
      (.tooltip $header-logo #js {:title title
                                  :trigger "hover focus"
                                  :position "top"
                                  :container "body"}))))

(defn logo-on-click [org-avatar-editing]
  (iu/upload! org-utils/org-avatar-filestack-config
    (fn [res]
      (qsg-actions/finish-company-logo-trail)
      (let [url (gobj/get res "url")
            img (gdom/createDom "img")]
        (set! (.-onerror img) #(logo-add-error img))
        (set! (.-onload img) #(logo-on-load org-avatar-editing url img))
        (set! (.-className img) "hidden")
        (gdom/append (.-body js/document) img)
        (set! (.-src img) url)))
    nil
    (fn [err]
      (qsg-actions/finish-company-logo-trail)
      (logo-add-error nil))))

(rum/defcs org-settings
  "Org settings main component. It handles the data loading/reset and the tab logic."
  < rum/static
    rum/reactive
    ;; Derivatives
    (drv/drv :org-data)
    (drv/drv :org-editing)
    (drv/drv :org-settings)
    (drv/drv :alert-modal)
    (drv/drv :org-editing)
    (drv/drv :invite-data)
    (drv/drv :org-avatar-editing)
    (drv/drv :qsg)
    ;; Mixins
    no-scroll-mixin

    {:will-mount (fn [s]
      (let [org-data @(drv/get-ref s :org-data)]
        (org-actions/get-org org-data)
        (team-actions/force-team-refresh (:team-id org-data)))
      s)
     :did-mount (fn [s]
      (update-tooltip s)
      s)
     :did-update (fn [s]
      (update-tooltip s)
      s)}
  [s]
  (let [org-editing (drv/react s :org-editing)
        settings-tab (drv/react s :org-settings)
        org-data (drv/react s :org-data)
        alert-modal-data (drv/react s :alert-modal)
        main-tab? (= settings-tab :main)
        org-avatar-editing (drv/react s :org-avatar-editing)
        org-data-for-avatar (merge org-data org-avatar-editing)
        qsg-data (drv/react s :qsg)]
    (when (:read-only org-data)
      (utils/after 100 dismiss-modal))
    (if org-data
      [:div.org-settings.fullscreen-page
        {:class (when (:visible qsg-data) "showing-qsg")}
        [:div.org-settings-inner
          (when-not alert-modal-data
            [:button.settings-modal-close.mlb-reset
              {:on-click #(close-clicked s)}])
          [:div.org-settings-header
            [:div.org-settings-header-avatar.qsg-company-logo-3
              {:ref "org-settings-header-logo"
               :class (utils/class-set {:missing-logo (empty? (:logo-url org-avatar-editing))
                                        :main-panel main-tab?
                                        utils/hide-class true})
               :on-click logo-on-click}
              (when (= (:step qsg-data) :company-logo-3)
                (qsg-breadcrumb qsg-data))
              (org-avatar org-data-for-avatar false :never)]
            [:div.org-name (:name org-data)]
            [:div.org-url (str ls/web-server "/" (:slug org-data))]]
          (org-settings-tabs org-data settings-tab)
          (case settings-tab
            :team
            (org-settings-team-panel org-data)
            :invite
            (org-settings-invite-panel org-data #(close-clicked s))
            :billing
            (org-settings-billing-panel org-data #(close-clicked s))
            (org-settings-main-panel org-data #(close-clicked s)))]]
      (loading {:loading true}))))