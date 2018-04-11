(ns oc.web.components.org-settings
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.org-settings-main-panel :refer (org-settings-main-panel)]
            [oc.web.components.ui.org-settings-team-panel :refer (org-settings-team-panel)]
            [oc.web.components.ui.org-settings-invite-panel :refer (org-settings-invite-panel)]))

;; FIXME: for billing stuff go back at this file from this commit 43a0566e2b78c3ca97c9d5b86b5cc2519bf76005

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] (or panel :main)]))

(defn dismiss-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] nil]))

(rum/defc org-settings-tabs
  [org-slug active-tab]
  [:div.org-settings-tabs.group
    [:div.org-settings-tab
      {:class (when (= :main active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings org-slug)
         :on-click #(do (utils/event-stop %) (show-modal :main))}
        "Team"]]
    [:div.org-settings-tab
      {:class (when (= :team active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings-team org-slug)
         :on-click #(do (utils/event-stop %) (show-modal :team))}
        "Manage Members"]]
    [:div.org-settings-tab
      {:class (when (= :invite active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings-invite org-slug)
         :on-click #(do (utils/event-stop %) (show-modal :invite))}
        "Invite People"]]])

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

(rum/defcs org-settings
  "Org settings main component. It handles the data loading/reset and the tab logic."
  < rum/static
    rum/reactive
    ;; Derivatives
    (drv/drv :org-data)
    (drv/drv :org-settings)
    (drv/drv :alert-modal)
    (drv/drv :org-editing)
    (drv/drv :invite-data)
    ;; Mixins
    no-scroll-mixin

    {:will-mount (fn [s]
                   (let [org-data @(drv/get-ref s :org-data)]
                     (org-actions/get-org org-data)
                     (team-actions/force-team-refresh (:team-id org-data)))
                   s)}
  [s]
  (let [settings-tab (drv/react s :org-settings)
        org-data (drv/react s :org-data)
        alert-modal-data (drv/react s :alert-modal)]
    (when (:read-only org-data)
      (utils/after 100 #(dismiss-modal)))
    (if org-data
      [:div.org-settings.fullscreen-page
        [:button.mlb-reset.carrot-modal-close
          {:on-click #(close-clicked s)}]
        (when alert-modal-data
          (alert-modal/alert-modal))
        [:div.org-settings-inner
          [:div.org-settings-header
            "Settings"]
          (org-settings-tabs (:slug org-data) settings-tab)
          (case settings-tab
            :team
            (org-settings-team-panel org-data)
            :invite
            (org-settings-invite-panel org-data #(close-clicked s))
            (org-settings-main-panel org-data #(close-clicked s)))]]
      (loading {:loading true}))))