(ns oc.web.components.user-info-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [clojure.string :as string]
            [oc.web.lib.utils :as utils]
            [oc.lib.user :as lib-user]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- time-with-timezone [timezone]
  (utils/time-without-leading-zeros
    (.toLocaleTimeString (js/Date.)
     (.. js/window -navigator -language)
     #js {:hour "2-digit"
          :minute "2-digit"
          :format "hour:minute"
          :timeZone timezone})))

(rum/defcs user-info-modal < rum/reactive
                             (drv/drv :panel-stack)
  [s {:keys [user-data org-data]}]
  (let [my-profile? (and (jwt/jwt)
                         (= (:user-id user-data) (jwt/user-id)))
        member? (jwt/user-is-part-of-the-team (:team-id org-data))
        team-role (when member? (utils/get-user-type user-data org-data))
        panel-stack (drv/react s :panel-stack)]
    [:div.user-info-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.user-info
        [:div.user-info-header
          [:div.user-info-header-title
            (if my-profile? "My profile" "Profile")]
          (when (> (count panel-stack) 1)
            [:button.mlb-reset.cancel-bt
              {:on-click #(nav-actions/show-user-settings nil)}
              "Back"])
          (when my-profile?
            [:button.mlb-reset.save-bt
              {:on-click #(nav-actions/show-user-settings :profile)}
              "Edit profile"])]
        [:div.user-info-body
          (user-avatar-image user-data)
          [:div.user-info-name
            (lib-user/name-for user-data)]
          (when (:title user-data)
            [:div.user-info-title
              (:title user-data)])
          (when member?
            [:button.mlb-reset.view-posts-bt
              {:on-click #(do
                            (nav-actions/close-all-panels)
                            (nav-actions/nav-to-author! % (:user-id user-data) (oc-urls/contributor (:user-id user-data))))}
              (if my-profile?
                "View my posts"
                "View posts")])
          (when (some seq (vals (-> user-data (select-keys [:location :timezone :email :profiles :slack-users]))))
            [:div.user-info-about
              [:div.user-info-about-label
                "About"]
              (when (or (:location user-data)
                        (:timezone user-data))
                [:div.user-info-about-location
                  (when (:location user-data)
                    (:location user-data))
                  (when (:timezone user-data)
                    (str
                     (when (:location user-data)
                       " (")
                     (str (time-with-timezone (:timezone user-data)) " local time")
                     (when (:location user-data)
                       ")")))])
              (when (:email user-data)
                [:div.user-info-about-email
                  [:a
                    {:href (str "mailto:" (:email user-data))
                     :target "_blank"}
                    (:email user-data)]])
              (when (:slack-users user-data)
                (for [slack-user (vals (:slack-users user-data))]
                  [:div.user-info-about-slack
                    {:key (str "slack-user-" (:slack-org-id slack-user) "-" (:id slack-user))}
                    [:a
                      {:href "."
                       :on-click #(utils/event-stop %)}
                      (if (string/starts-with? (:display-name slack-user) "@")
                        (:display-name slack-user)
                        (str "@" (:display-name slack-user)))]]))
              (when (seq (filter seq (vals (:profiles user-data))))
                [:div.user-info-about-profiles
                  (for [[k v] (:profiles user-data)
                        :when (seq v)]
                    [:a
                      {:class (name k)
                       :key (str "profile-" (name k))
                       :target "_blank"
                       :href (if (or (string/starts-with? v "http")
                                     (string/starts-with? v "//"))
                               v
                               (str "https://" v))}])])
              (when (:blurb user-data)
                [:p.user-info-about-blurb
                  (:blurb user-data)])])]]]))
