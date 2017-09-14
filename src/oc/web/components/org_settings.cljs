(ns oc.web.components.org-settings
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.loading :refer (rloading)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]
            [oc.web.components.ui.org-settings-main-panel :refer (org-settings-main-panel)]
            [oc.web.components.ui.org-settings-team-panel :refer (org-settings-team-panel)]
            [oc.web.components.ui.org-settings-invite-panel :refer (org-settings-invite-panel)]))

;; FIXME: for billing stuff go back at this file from this commit 43a0566e2b78c3ca97c9d5b86b5cc2519bf76005

(rum/defc org-settings-tabs
  [org-slug active-tab on-click]
  [:div.org-settings-tabs.group
    [:div.org-settings-tab
      {:class (when (= :org-settings active-tab) "active")}
      [:a.org-settings-tab-link
        {:href (oc-urls/org-settings org-slug)
         :on-click #(do (utils/event-stop %) (on-click (oc-urls/org-settings org-slug)))}
        "Team"]]
    [:div.org-settings-tab
      {:class (when (= :org-settings-team active-tab) "active")}
      [:a.org-settings-tab-link
        {:href (oc-urls/org-settings-team org-slug)
         :on-click #(do (utils/event-stop %) (on-click (oc-urls/org-settings-team org-slug)))}
        "Manage Members"]]
    [:div.org-settings-tab
      {:class (when (= :org-settings-invite active-tab) "active")}
      [:a.org-settings-tab-link
        {:href (oc-urls/org-settings-invite org-slug)
         :on-click #(do (utils/event-stop %) (on-click (oc-urls/org-settings-invite org-slug)))}
        "Invite People"]]])

(defn settings-tab []
  (cond
    (utils/in? (:route @router/path) "org-settings-team") :org-settings-team
    (utils/in? (:route @router/path) "org-settings-invite") :org-settings-invite
    :else :org-settings))

(defn close-clicked [s]
  (let [org-data @(drv/get-ref s :org-data)
        org-editing @(drv/get-ref s :org-editing)
        invite-users (vec (filter #(not (:error %)) (:invite-users @(drv/get-ref s :invite-users))))
        has-unsent-invites (and (pos? (count invite-users))
                                (some #(not (empty? (:user %))) invite-users))
        active-tab (settings-tab)]
    (if (or (and (= :org-settings active-tab)
                 (:has-changes org-editing))
            (and (= :org-settings-invite active-tab)
                 has-unsent-invites))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :message "There are unsaved edits. OK to delete them?"
                        :link-button-title "Cancel"
                        :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                        :solid-button-title "Yes"
                        :solid-button-cb #(do
                                            (dis/dispatch! [:alert-modal-hide])
                                            (router/nav! (oc-urls/org (:slug org-data))))}]
        (dis/dispatch! [:alert-modal-show alert-data]))
      (router/nav! (oc-urls/org (:slug org-data))))))

(rum/defcs org-settings
  "Org settings main component. It handles the data loading/reset and the tab logic."
  < rum/static
    rum/reactive
    (drv/drv :org-data)
    (drv/drv :alert-modal)
    (drv/drv :teams-load)
    (drv/drv :org-editing)
    (drv/drv :invite-users)
    {:before-render (fn [s]
                      (let [teams-load @(drv/get-ref s :teams-load)]
                        (when (and (:auth-settings teams-load)
                                   (not (:teams-data-requested teams-load)))
                          (dis/dispatch! [:teams-get])))
                      s)}
  [s]
  (let [settings-tab (settings-tab)
        org-data (drv/react s :org-data)
        alert-modal-data (drv/react s :alert-modal)]
    (when (:read-only org-data)
      (utils/after 100 #(router/nav! (oc-urls/org (:slug org-data)))))
    (if org-data
      [:div.org-settings.fullscreen-page
        (carrot-close-bt {:on-click #(close-clicked s)})
        (when alert-modal-data
          (alert-modal))
        [:div.org-settings-inner
          [:div.org-settings-header
            "Settings"]
          (org-settings-tabs (:slug org-data) settings-tab router/nav!)
          (case settings-tab
            :org-settings-team
            (org-settings-team-panel org-data)
            :org-settings-invite
            (org-settings-invite-panel org-data)
            (org-settings-main-panel org-data))]]
      (rloading {:loading true}))))