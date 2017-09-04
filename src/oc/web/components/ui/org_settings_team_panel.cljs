(ns oc.web.components.ui.org-settings-team-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-picker :refer (user-type-dropdown)]))

(defn user-action [team-id user action method other-link-params]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-action team-id user action method other-link-params nil]))

(rum/defcs org-settings-team-panel
  < rum/reactive
    (drv/drv :current-user-data)
    (drv/drv :teams-load)
    (drv/drv :team-data)
    {:before-render (fn [s]
                     (let [teams-load-data @(drv/get-ref s :teams-load)]
                       (when (and (:auth-settings teams-load-data)
                                  (not (:teams-data-requested teams-load-data)))
                         (dis/dispatch! [:teams-get])))
                     s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data]
  (let [team-data (drv/react s :team-data)
        cur-user-data (drv/react s :current-user-data)
        org-authors (:authors org-data)]
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-team.org-settings-panel-row
        ;; Team table
        [:table.org-settings-table
          [:thead
            [:tr
              [:th "Name"]
              [:th "Status"]
              [:th "Role"]]]
          [:tbody
            (for [user (sort-by utils/name-or-email (:users team-data))
                  :let [user-type (utils/get-user-type user (dis/org-data))
                        author (some #(when (= (:user-id %) (:user-id user)) %) org-authors)
                        remove-fn (fn []
                                    (when author
                                      (api/remove-author author))
                                    (user-action (:team-id team-data) user "remove" "DELETE"  {:ref "application/vnd.open-company.user.v1+json"}))]]
              [:tr
                {:key (str "org-settings-team-" (:user-id user))}
                [:td
                  (user-avatar-image user)
                  (utils/name-or-email user)]
                [:td
                  [:div.td-status.group
                    [:div.status-label (s/capital (:status user))]
                    (when (= "pending" (:status user))
                      [:button.mlb-reset.mlb-link
                        {:on-click (fn []
                                     (dis/dispatch! [:input [:um-invite] {:email (:email user)
                                                                          :invite-from "email"
                                                                          :user-type user-type
                                                                          :error nil}])
                                     (utils/after 100 #(dis/dispatch! [:invite-user])))}
                        "Resend"])
                    (when (= "pending" (:status user))
                      [:button.mlb-reset.mlb-link-red
                        {:on-click remove-fn}
                        "Cancel"])]]
                [:td
                  (user-type-dropdown (:user-id user) user-type #(api/switch-user-type user-type % user author) false remove-fn)]])]]]]))