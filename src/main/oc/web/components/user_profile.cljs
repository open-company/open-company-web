(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defc user-profile
  [user-data]
  (let [not-found? (empty? user-data)]
    [:div.user-profile-container
      [:div.user-profile
        [:div.user-profile-header
          [:div.user-profile-header-avatar
           {:class (when not-found? "foggy-avatar")}
            (user-avatar-image user-data {:preferred-avatar-size 512})]
          [:div.user-profile-header-info
            [:div.user-profile-header-info-name
              [:span.name
                (if not-found?
                  "User not found"
                  (:name user-data))]
              (when-not (string/empty-or-nil? (:role-string user-data))
                [:span.role
                  (str "(" (string/capital (:role-string user-data)) ")")])
              (when (:self? user-data)
                [:button.mlb-reset.edit-profile-bt
                  {:on-click #(nav-actions/show-user-settings :profile)}
                  "Edit profile"])]
            ; (when (seq (or (:title user-data) (:role-string user-data)))
            ;   [:div.user-profile-header-info-title
            ;     (if (seq (:title user-data))
            ;       [:div.title
            ;         (:title user-data)
            ;         [:span.role (str "(" (:role-string user-data) ")")]]
            ;       [:div.title (string/capital (:role-string user-data))])])
            (when not-found?
              [:div.user-profile-header-info-blurb
               "This user doesn't exist or has been removed from the team."])
            (when (seq (:title user-data))
              [:div.user-profile-header-info-title
                (:title user-data)])
            (when (seq (:blurb user-data))
              [:div.user-profile-header-info-blurb
                (:blurb user-data)])
            (let [lts (user-utils/location-timezone-string user-data true)]
              (when (seq lts)
                [:div.user-profile-header-info-locale
                  lts]))]]
        (let [has-profile-links? (seq (filter seq (vals (:profiles user-data))))]
          (when (or (:email user-data)
                    (seq (:slack-users user-data))
                    has-profile-links?)
            [:div.user-profile-footer
              (when (:email user-data)
                [:a.profile-link.email
                  {:href (str "mailto:" (:email user-data))
                  :target "_blank"}
                  (:email user-data)])
              (when (:slack-users user-data)
                ;; https://acmeco.slack.com/team/U1H63D8SZ
                (for [slack-user (vals (:slack-users user-data))
                      :when (and (seq (:display-name slack-user))
                                (not= (:display-name slack-user) "-"))]
                  [:a.profile-link.slack
                    {:key (str "profile-link-" (:slack-org-id slack-user) "-" (:id slack-user))
                    :on-click #(utils/event-stop %)
                    :href "."}
                    (if (string/starts-with? (:display-name slack-user) "@")
                      (:display-name slack-user)
                      (str "@" (:display-name slack-user)))]))
              (when has-profile-links?
                (for [[k v] (:profiles user-data)
                      :when (not (string/empty-or-nil? v))
                      :let [un (last (string/split v #"/"))]]
                  [:a.profile-link
                    {:class (name k)
                    :key (str "profile-" (name k))
                    :target "_blank"
                    :href (if (or (string/starts-with? v "http")
                                  (string/starts-with? v "//"))
                            v
                            (str "https://" v))}
                    un]))]))]]))