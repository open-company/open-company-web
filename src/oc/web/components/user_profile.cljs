(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [cljsjs.moment-timezone]
            [goog.object :as googobj]
            [goog.dom :as gdom]))

(defn- img-on-load [url img]
  (dis/dispatch! [:input [:edit-user-profile :avatar-url] url])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true])
  (gdom/removeNode img))

(defn success-cb
  [res]
  (let [url    (googobj/get res "url")
        node   (gdom/createDom "img")]
    (if-not url
      (dis/dispatch! [:error-banner-show "An error has occurred while processing the image URL. Please try again." 5000])
      (do
        (set! (.-onload node) #(img-on-load url node))
        (set! (.-className node) "hidden")
        (gdom/append (.-body js/document) node)
        (set! (.-src node) url)))))

(defn progress-cb [res progress])

(defn error-cb [res error]
  (dis/dispatch! [:error-banner-show "An error has occurred while processing the image URL. Please try again." 5000]))

(defn change! [k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(rum/defcs user-profile < rum/reactive
                          (rum/local false ::loading)
                          (rum/local false ::show-success)
                          (drv/drv :edit-user-profile)
                          (drv/drv :orgs)
                          {:init (fn [s _]
                                   (dis/dispatch! [:user-profile-reset])
                                   s)
                           :after-render (fn [s]
                                           (when (empty? (:timezone (:user-data @(drv/get-ref s :edit-user-profile))))
                                              (change! :timezone (.. js/moment -tz guess)))
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
        timezones (.names (.-tz js/moment))
        orgs (drv/react s :orgs)]
    [:div.user-profile.fullscreen-page
      [:div.user-profile-header {} "Your Profile"]
      [:div.user-profile-internal
        [:div.user-profile-content.group
          [:div.user-profile-avatar-box.group
            [:button.user-profile-avatar.mlb-reset
              {:on-click #(iu/upload! {:accept "image/*"} success-cb progress-cb error-cb)}
              (user-avatar-image current-user-data)]
            [:div.user-profile-avatar-change
              [:button.mlb-reset.mlb-link.upload-photo
                {:on-click #(iu/upload! {:accept "image/*"} success-cb progress-cb error-cb)}
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
                  ;; Promoted timezones
                  (for [t ["US/Eastern" "US/Central" "US/Mountain" "US/Pacific"]]
                    [:option
                      {:key (str "timezone-" t "-promoted")
                       :value t} t])
                  ;; Divider line option
                  [:option
                    {:disabled true
                     :value ""}
                    "------------"]
                  ;; All the timezones, repeating the promoted
                  (for [t timezones]
                    [:option
                      {:key (str "timezone-" t)
                       :value t}
                      t])]]]]
          ; Digest frequency
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "DIGEST FREQUENCY " [:i.fa.fa-info-circle]]
            [:div.user-profile-field.digest-frequency-field.digest-frequency
              [:select
                {:value (:digest-frequency current-user-data)
                 :on-change #(change! :digest-frequency (.. % -target -value))}
                [:option {:value "weekly"} "WEEKLY"]
                [:option {:value "daily"} "DAILY"]
                [:option {:value "never"} "NEVER"]]]
            [:div.user-profile-field.digest-frequency-field.digest-day
              [:select
                {:value (:digest-day current-user-data)
                 :disabled (not= (:digest-frequency current-user-data) "weekly")
                 :on-change #(change! :digest-day (.. % -target -value))}
                [:option {:value "monday"} "MONDAY"]
                [:option {:value "tuesday"} "TUESDAY"]
                [:option {:value "wednesday"} "WEDNESDAY"]
                [:option {:value "thursday"} "THURSDAY"]
                [:option {:value "friday"} "FRIDAY"]
                [:option {:value "saturday"} "SATURDAY"]
                [:option {:value "sunday"} "SUNDAY"]]]
            [:div.user-profile-field.digest-frequency-field.digest-time
              [:select
                {:value (:digest-time current-user-data)
                 :disabled (= (:digest-frequency current-user-data) "never")
                 :on-change #(change! :digest-time (.. % -target -value))}
                [:option {:value "7am"} "7AM"]
                [:option {:value "8am"} "8AM"]
                [:option {:value "9am"} "9AM"]
                [:option {:value "10am"} "10AM"]
                [:option {:value "11am"} "11AM"]
                [:option {:value "12pm"} "12PM"]
                [:option {:value "1pm"} "1PM"]
                [:option {:value "2pm"} "2PM"]
                [:option {:value "3pm"} "3PM"]
                [:option {:value "4pm"} "4PM"]
                [:option {:value "5pm"} "5PM"]
                [:option {:value "6pm"} "6PM"]
                [:option {:value "7pm"} "7PM"]
                [:option {:value "8pm"} "8PM"]
                [:option {:value "9pm"} "9PM"]
                [:option {:value "10pm"} "10PM"]
                [:option {:value "11pm"} "11PM"]
                [:option {:value "12am"} "12AM"]
                [:option {:value "1am"} "1AM"]
                [:option {:value "2am"} "2AM"]
                [:option {:value "3am"} "3AM"]
                [:option {:value "4am"} "4AM"]
                [:option {:value "5am"} "5AM"]
                [:option {:value "6am"} "6AM"]]]]]]
        [:div.user-profile-footer
          [:button.mlb-reset.mlb-default
            {:on-click #(dis/dispatch! [:user-profile-save])
             :disabled (not (:has-changes current-user-data))}
             (when (:loading current-user-data)
                (small-loading))
            "Save"]
          [:button.mlb-reset.mlb-link-black
            {:on-click #(let [last-org-slug (cook/get-cookie (router/last-org-cookie))
                              first-org-slug (:slug (first orgs))
                              to-url (if last-org-slug
                                      (oc-urls/org last-org-slug)
                                      (if first-org-slug
                                        (oc-urls/org first-org-slug)
                                        oc-urls/orgs))]
                          (when (:has-changes current-user-data)
                            (dis/dispatch! [:user-profile-reset]))
                          (router/nav! to-url))}
            "Cancel"]]]))