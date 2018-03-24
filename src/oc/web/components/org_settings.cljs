(ns oc.web.components.org-settings
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.org-settings-main-panel :refer (org-settings-main-panel)]
            [oc.web.components.ui.org-settings-team-panel :refer (org-settings-team-panel)]
            [oc.web.components.ui.org-settings-invite-panel :refer (org-settings-invite-panel)]))

;; FIXME: for billing stuff go back at this file from this commit 43a0566e2b78c3ca97c9d5b86b5cc2519bf76005

(rum/defc org-settings-tabs
  [org-slug active-tab]
  [:div.org-settings-tabs.group
    [:div.org-settings-tab
      {:class (when (= :main active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings org-slug)
         :on-click #(do (utils/event-stop %) (dis/dispatch! [:org-settings-show :main]))}
        "Team"]]
    [:div.org-settings-tab
      {:class (when (= :team active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings-team org-slug)
         :on-click #(do (utils/event-stop %) (dis/dispatch! [:org-settings-show :team]))}
        "Manage Members"]]
    [:div.org-settings-tab
      {:class (when (= :invite active-tab) "active")}
      [:a.org-settings-tab-link
        {;:href (oc-urls/org-settings-invite org-slug)
         :on-click #(do (utils/event-stop %) (dis/dispatch! [:org-settings-show :invite]))}
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
                        :message "There are unsaved edits. OK to delete them?"
                        :link-button-title "Cancel"
                        :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                        :solid-button-title "Yes"
                        :solid-button-cb #(do
                                            (dis/dispatch! [:alert-modal-hide])
                                            (dis/dispatch! [:org-settings-hide]))}]
        (dis/dispatch! [:alert-modal-show alert-data]))
      (dis/dispatch! [:org-settings-hide]))))

(rum/defcs org-settings
  "Org settings main component. It handles the data loading/reset and the tab logic."
  < rum/static
    rum/reactive
    ;; Derivatives
    (drv/drv :org-data)
    (drv/drv :org-settings)
    (drv/drv :alert-modal)
    (drv/drv :teams-load)
    (drv/drv :org-editing)
    (drv/drv :invite-data)
    ;; Mixins
    no-scroll-mixin

    {:before-render (fn [s]
                      (let [teams-load @(drv/get-ref s :teams-load)]
                        (when (and (:auth-settings teams-load)
                                   (not (:teams-data-requested teams-load)))
                          (dis/dispatch! [:teams-get])))
                      s)}
  [s]
  (let [settings-tab (drv/react s :org-settings)
        org-data (drv/react s :org-data)
        alert-modal-data (drv/react s :alert-modal)]
    (when (:read-only org-data)
      (utils/after 100 #(dis/dispatch! [:org-settings-hide])))
    (if org-data
      [:div.org-settings.fullscreen-page
        [:button.mlb-reset.carrot-modal-close
          {:on-click #(close-clicked s)}]
        (when alert-modal-data
          (alert-modal))
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