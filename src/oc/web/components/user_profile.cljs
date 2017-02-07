(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(rum/defcs user-profile < rum/static
                           rum/reactive
                           (drv/drv :current-user-data)
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
          [:div.user-profile-name (:org-name (drv/react s :current-user-data))]
          [:div.user-profile-name-title.data-title "EMAIL"]
          [:div.user-profile-name (:email (drv/react s :current-user-data))]]
        [:div.right-column
          (when (:avatar-url (drv/react s :current-user-data))
            [:div.user-profile-avatar-title.data-title "AVATAR"])
          (when (:avatar-url (drv/react s :current-user-data))
            [:img.user-profile-avatar {:src (:avatar-url (drv/react s :current-user-data))}])]]
      [:div.user-profile-disclaimer
        [:span.left "User information is from your Slack account."]
        [:button.btn-reset.btn-link.left
          {:on-click #(dis/dispatch! [:refresh-slack-user])} "Refresh"]]]
    (footer (responsive/total-layout-width-int (responsive/calc-card-width) (responsive/columns-num)))])