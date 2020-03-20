(ns oc.web.components.user-info-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.lib.user :as lib-user]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defc user-info-modal
  [{:keys [user-data org-data]}]
  (let [my-profile? (and (jwt/jwt)
                         (= (:user-id user-data) (jwt/user-id)))
        member? (jwt/user-is-part-of-the-team (:team-id org-data))
        team-role (when member? (utils/get-user-type user-data org-data))]
    (js/console.log "DBG user-info-modal/render user-data" user-data "org-data" org-data)
    [:div.user-info-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.user-info
        [:div.user-info-header
          [:div.user-info-header-title
            (if my-profile? "My profile" "Profile")]
          (if my-profile?
            [:button.mlb-reset.save-bt
              {:on-click #(nav-actions/show-user-settings :profile)}
              "Edit profile"]
            [:button.mlb-reset.cancel-bt
              {:on-click #(nav-actions/show-user-settings nil)}
              "Back"])]
        [:div.user-info-body
          (user-avatar-image user-data)
          [:div.user-info-name
            (lib-user/name-for user-data)]
          (when member?
            [:div.user-info-team-role
              (string/title team-role)])
          (when member?
            [:button.mlb-reset.view-posts-bt
              {:on-click #(do
                            (nav-actions/close-all-panels)
                            (nav-actions/nav-to-author! % (:user-id user-data) (oc-urls/contributor (:user-id user-data))))}
              "View posts"])
          (when (some seq (vals (-> user-data (select-keys [:title :timezone :email :profiles]))))
            [:div.user-info-about
              (js/console.log "DBG about?" (vals (-> user-data (select-keys [:title :timezone :email :profiles]))) (some seq (vals (-> user-data (select-keys [:title :timezone :email :profiles])))))
              [:div.user-info-about-label
                "About"]
              (when (:title user-data)
                [:div.user-info-about-title
                  (:title user-data)])
              (when (:timezone user-data)
                [:div.user-info-about-timezone
                  (:timezone user-data)
                  (when-let [time-str (utils/local-date-time (.toDate (.tz js/moment (:timezone user-data))))]
                    (str " (" time-str " local time)"))])
              (when (:email user-data)
                [:div.user-info-about-email
                  [:a
                    {:href (str "mailto:" (:email user-data))
                     :target "_blank"}
                    (:email user-data)]])
              (when (seq (filter seq (vals (:profiles user-data))))
                [:div.user-info-about-profiles
                  (for [[k v] (:profiles user-data)
                        :when (seq v)]
                    [:a
                      {:class (name k)
                       :key (str "profile-" (name k))
                       :href v}])])])]]]))
