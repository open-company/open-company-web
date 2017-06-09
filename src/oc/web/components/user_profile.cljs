(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [cljsjs.moment-timezone]))

(defn change! [k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(rum/defcs user-profile < rum/reactive
                          (rum/local false ::loading)
                          (rum/local false ::show-success)
                          (drv/drv :edit-user-profile)
                          {:init (fn [s _]
                                   (dis/dispatch! [:user-profile-reset])
                                   s)
                           :did-remount (fn [old-state new-state]
                                          (when @(::loading new-state)
                                            (reset! (::show-success new-state) true)
                                            (reset! (::loading new-state) false)
                                            (utils/after 2000 (fn [] (reset! (::show-success new-state) false))))
                                          new-state)}
  [s]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)
        timezones (.names (.-tz js/moment))]
    [:div.user-profile.fullscreen-page
      [:div.user-profile-header {} "Your Profile"]
      [:div.user-profile-internal
        [:div.user-profile-content.group
          [:div.user-profile-avatar-box.group
            [:div.user-profile-avatar
              (user-avatar-image current-user-data)]
            [:div.user-profile-avatar-change
              [:button.mlb-reset.mlb-link.upload-photo
                {:on-click #()}
                [:span.user-avatar-upload-cta "Upload Photo"]
                [:span.user-avatar-upload-description
                  "A 160x160 transparent Gif or PNG works best"]]]]
          ; Left column
          [:div.user-profile-column-left
            ; First name
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "FIRST NAME"]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :on-change #(change! :first-name (.. % -target -value))
                   :value (:first-name current-user-data)}]]]
            ; Current password
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "CURRENT PASSWORD"]
              [:div.user-profile-field
                [:input
                  {:type "password"
                   :on-change #(change! :current-password (.. % -target -value))
                   :value (:current-password current-user-data)}]]]
            ; Email
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "EMAIL"]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :on-change #(change! :email (.. % -target -value))
                   :value (:email current-user-data)}]]]]
          ; Right column
          [:div.user-profile-column-right
            ; Last name
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "LAST NAME"]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :on-change #(change! :last-name (.. % -target -value))
                   :value (:last-name current-user-data)}]]]
            ; New password
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "NEW PASSWORD"]
              [:div.user-profile-field
                [:input
                  {:type "password"
                   :on-change #(change! :password (.. % -target -value))
                   :value (:password current-user-data)}]]]
            ; Time zone
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "TIME ZONE"]
              [:div.user-profile-field
                [:select
                  {:value (:timezone current-user-data)
                   :on-change #(change! :timezone (.. % -target -value))}
                  (for [t timezones]
                    [:option
                      {:key (str "timezone-" t)}
                      t])]]]]
          ; Digest frequency
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "DIGEST FREQUENCY " [:i.fa.fa-info-circle]]
            [:div.user-profile-field
              [:select
                {:value (:digest-period current-user-data)
                 :on-change #(change! :digest-period (.. % -target -value))}
                [:option "DAILY"]
                [:option "WEEKLY"]
                [:option "MONTHLY"]]]]]]
        [:div.user-profile-footer
          [:button.mlb-reset.mlb-default
            {:on-click #(dis/dispatch! [:user-profile-save])
             :disabled (not (:has-changes current-user-data))}
             (when (:loading current-user-data)
                (small-loading))
            "Save"]
          [:button.mlb-reset.mlb-link-black
            {:on-click #(dis/dispatch! [:user-profile-reset])
             :disabled (not (:has-changes current-user-data))}
            "Cancel"]]]))