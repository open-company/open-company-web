(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(rum/defcs user-profile < rum/reactive
                          (rum/local false ::loading)
                          (rum/local false ::show-success)
                          (drv/drv :current-user-data)
                          {:did-remount (fn [old-state new-state]
                                          (when @(::loading new-state)
                                            (reset! (::show-success new-state) true)
                                            (reset! (::loading new-state) false)
                                            (utils/after 2000 (fn [] (reset! (::show-success new-state) false))))
                                          new-state)}
  [s]
  [:div.user-profile.fullscreen-page
    (back-to-dashboard-btn {:title "User Profile"})
    [:div.user-profile-internal.mx-auto.my4
      [:div.user-profile-content.group
        [:div.left-column
          [:div.user-profile-name-title.data-title "FIRST NAME"]
          [:div.user-profile-name (:first-name (drv/react s :current-user-data))]
          [:div.user-profile-name-title.data-title "LAST NAME"]
          [:div.user-profile-name (:last-name (drv/react s :current-user-data))]
          [:div.user-profile-name-title.data-title "SLACK ORGANIZATION"]
          [:div.user-profile-name (:slack-org-name (drv/react s :current-user-data))]
          [:div.user-profile-name-title.data-title "EMAIL"]
          [:div.user-profile-name (:email (drv/react s :current-user-data))]]
        [:div.right-column
          (when (:avatar-url (drv/react s :current-user-data))
            [:div.user-profile-avatar-title.data-title "AVATAR"])
          (when (:avatar-url (drv/react s :current-user-data))
            [:img.user-profile-avatar {:src (:avatar-url (drv/react s :current-user-data))}])]]
      [:div.user-profile-disclaimer
        [:span.left "User information is from your Slack account."]
        [:button.user-profile-refresh.btn-reset.btn-link.left
          {:class (if @(::show-success s) "success" "")
           :on-click #(do
                        (reset! (::loading s) true)
                        (dis/dispatch! [:refresh-slack-user]))}
          (if @(::show-success s)
            "SAVED!"
            (if @(::loading s)
              (small-loading)
              "Refresh"))]]]
    (footer (responsive/total-layout-width-int (responsive/calc-card-width) (responsive/columns-num)))])