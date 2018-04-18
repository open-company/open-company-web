(ns oc.web.components.user-profile
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.error-banner :as error-banner-actions]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [cljsjs.moment-timezone]
            [goog.object :as googobj]
            [goog.dom :as gdom]))

(def default-user-profile (oc.web.stores.user/random-user-image))

(defn- img-on-load [url img]
  (dis/dispatch! [:input [:edit-user-profile :avatar-url] url])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true])
  (gdom/removeNode img))

(defn success-cb
  [res]
  (let [url    (googobj/get res "url")
        node   (gdom/createDom "img")]
    (if-not url
      (error-banner-actions/show-banner
        "An error has occurred while processing the image URL. Please try again."
        5000)
      (do
        (set! (.-onload node) #(img-on-load url node))
        (set! (.-className node) "hidden")
        (gdom/append (.-body js/document) node)
        (set! (.-src node) url)))))

(defn progress-cb [res progress])

(defn error-cb [res error]
  (error-banner-actions/show-banner
   "An error has occurred while processing the image URL. Please try again."
   5000))

(defn change! [s k v]
  (reset! (::name-error s) false)
  (reset! (::email-error s) false)
  (reset! (::password-error s) false)
  (reset! (::current-password-error s) false)
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(defn real-close-cb [orgs current-user-data]
  (let [last-org-slug (cook/get-cookie (router/last-org-cookie))
        first-org-slug (:slug (first orgs))
        to-url (if last-org-slug
                (oc-urls/org last-org-slug)
                (if first-org-slug
                  (oc-urls/org first-org-slug)
                  oc-urls/login))]
    (when (:has-changes current-user-data)
      (user-actions/user-profile-reset))
    (router/nav! to-url)))

(defn close-cb [orgs current-user-data]
  (dis/dispatch! [:input [:latest-entry-point] 0])
  (if (:has-changes current-user-data)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "user-profile-unsaved-edits"
                      :message "Leave without saving your changes?"
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Lose changes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (real-close-cb orgs current-user-data))}]
      (alert-modal/show-alert alert-data))
    (real-close-cb orgs current-user-data)))

(def default-user-profile-image-key {:accept "image/*"
                                     :transformations {
                                       :crop {
                                         :aspectRatio 1}}})

(defn upload-user-profile-pictuer-clicked []
  (dis/dispatch! [:input [:edit-user-profile :avatar-url] default-user-profile])
  (iu/upload! default-user-profile-image-key success-cb progress-cb error-cb))

(defn save-clicked [s]
  (reset! (::name-error s) false)
  (reset! (::email-error s) false)
  (reset! (::password-error s) false)
  (reset! (::current-password-error s) false)
  (reset! (::loading s) true)
  (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
        current-user-data @(drv/get-ref s :current-user-data)
        user-data (:user-data edit-user-profile)]
    (cond
      (and (empty? (:first-name user-data))
           (empty? (:last-name user-data)))
      (reset! (::name-error s) true)

      (not (utils/valid-email? (:email user-data)))
      (reset! (::email-error s) true)

      (and (seq (:password user-data))
           (empty? (:current-password user-data)))
      (reset! (::current-password-error s) true)

      (and (seq (:password user-data))
           (< (count (:password user-data)) 8))
      (reset! (::password-error s) true)

      :else
      (user-actions/user-profile-save current-user-data edit-user-profile))))

(rum/defcs user-profile < rum/reactive
                          ;; Derivatives
                          (drv/drv :orgs)
                          (drv/drv :alert-modal)
                          (drv/drv :edit-user-profile)
                          (drv/drv :current-user-data)
                          ;; Locals
                          (rum/local false ::loading)
                          (rum/local false ::show-success)
                          (rum/local false ::name-error)
                          (rum/local false ::email-error)
                          (rum/local false ::password-error)
                          (rum/local false ::current-password-error)
                          {:will-mount (fn [s]
                            (user-actions/user-profile-reset)
                            s)
                           :after-render (fn [s]
                              (when-not (utils/is-test-env?)
                                (doto (js/$ "[data-toggle=\"tooltip\"]")
                                  (.tooltip "fixTitle")
                                  (.tooltip "hide")))
                              s)
                           :did-remount (fn [old-state new-state]
                            (let [user-data (:user-data @(drv/get-ref new-state :edit-user-profile))]
                              (when (and @(::loading new-state)
                                         (not (:has-changes user-data)))
                                (reset! (::show-success new-state) true)
                                (reset! (::loading new-state) false)
                                (utils/after 2500 (fn [] (reset! (::show-success new-state) false)))))
                            new-state)}
  [s]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)
        timezones (.names (.-tz js/moment))
        orgs (drv/react s :orgs)]
    [:div.user-profile.fullscreen-page
      (when (drv/react s :alert-modal)
        (alert-modal/alert-modal))
      (carrot-close-bt {:on-click #(close-cb orgs current-user-data)})
      [:div.user-profile-header {} "Your Profile"]
      [:div.user-profile-internal
        [:div.user-profile-content.group
          [:div.user-profile-avatar-box.group
            [:button.user-profile-avatar.mlb-reset
              {:on-click #(upload-user-profile-pictuer-clicked)}
              (user-avatar-image current-user-data)]
            [:button.mlb-reset.mlb-link.upload-photo
              {:on-click #(upload-user-profile-pictuer-clicked)}
              [:span.user-avatar-upload-cta "Upload profile photo"]
              [:span.user-avatar-upload-description
                "A 160x160 PNG or JPG works best"]]]
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              [:span.error
                {:style {:margin-left "0px"}}
                (when error
                  "An error occurred while saving, please try again.")]]]
          ; Left column
          [:div.user-profile-column-left
            ; First name
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "First Name"
                (when @(::name-error s)
                  [:span.error "Please provide your name."])]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :tab-index 1
                   :on-change #(change! s :first-name (.. % -target -value))
                   :value (:first-name current-user-data)}]]]
            ; Current password
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "Current Password"
                (when @(::current-password-error s)
                  [:span.error "Current password required"])]
              [:div.user-profile-field
                [:input
                  {:type "password"
                   :tab-index 3
                   :on-change #(change! s :current-password (.. % -target -value))
                   :value (:current-password current-user-data)}]]]
            ; Email
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "Email"
                (when @(::email-error s)
                  [:span.error "This email isn't valid."])]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :tab-index 5
                   :on-change #(change! s :email (.. % -target -value))
                   :value (:email current-user-data)}]]]
            ;; Digest frequency
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "Digest Frequency " [:i.mdi.mdi-information-outline
                  {:title "Receive a digest of newly created posts."
                   :data-toggle "tooltip"
                   :data-placement "top"}]]
              [:div.user-profile-field.digest-frequency-field.digest-frequency
                [:div.dropdown {:class "dropdown-button"}
                  [:button.btn-reset.user-type-btn.dropdown-toggle
                    {:id "user-digest-frequency-dropdown"
                     :data-toggle "dropdown"
                     :aria-haspopup true
                     :aria-expanded false}
                    (case (:digest-frequency current-user-data)
                      "daily"
                      "Daily"
                      "weekly"
                      "Weekly"
                      "Never")]
                  [:ul.dropdown-menu.user-type-dropdown-menu
                    {:aria-labelledby "user-digest-frequency-dropdown"}
                    [:li
                      {:on-click #(change! s :digest-frequency "daily")}
                      "Daily"]
                    [:li
                      {:on-click #(change! s :digest-frequency "weekly")}
                      "Weekly"]
                    [:li
                      {:on-click #(change! s :digest-frequency "never")}
                      "Never"]]]]]]


          ; Right column
          [:div.user-profile-column-right
            ; Last name
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "Last Name"]
              [:div.user-profile-field
                [:input
                  {:type "text"
                   :tab-index 2
                   :on-change #(change! s :last-name (.. % -target -value))
                   :value (:last-name current-user-data)}]]]
            ; New password
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "New Password"
                (when @(::password-error s)
                  [:span.error "Minimum 8 characters"])]
              [:div.user-profile-field
                [:input
                  {:type "password"
                   :tab-index 4
                   :on-change #(change! s :password (.. % -target -value))
                   :value (:password current-user-data)}]]]
            ;; Time zone
            [:div.user-profile-field-box
              [:div.user-profile-field-label
                "Time Zone"]
              [:div.user-profile-field.timezone
                [:select
                  {:value (:timezone current-user-data)
                   :on-change #(change! s :timezone (.. % -target -value))}
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
                      t])]]]
            ;; Digest Medium
            (when true ; should be conditional on team having a bot, but that's not in scope here in user profile
              [:div.user-profile-field-box
                [:div.user-profile-field-label
                  "Digest Type"]
                [:div.user-profile-field.digest-medium
                  [:div.dropdown {:class "dropdown-button"}
                    [:button.btn-reset.user-type-btn.dropdown-toggle
                      {:id "user-digest-medium-dropdown"
                       :data-toggle "dropdown"
                       :aria-haspopup true
                       :aria-expanded false}
                      (case (:digest-medium current-user-data)
                        "slack"
                        "Slack"
                        "Email")]
                    [:ul.dropdown-menu.user-type-dropdown-menu
                      {:aria-labelledby "user-digest-medium-dropdown"}
                      [:li
                        {:on-click #(change! s :digest-medium "email")}
                        "Email"]
                      ;; Show Slack digest option if
                      (when (and ;; at least one team has a Slack bot
                                 (some jwt/team-has-bot? (jwt/get-key :teams))
                                 ;; the user is also a Slack user
                                 (seq (jwt/get-key :slack-id)))
                        [:li
                          {:on-click #(change! s :digest-medium "slack")}
                          "Slack"])]]]])]]]
            ;; Eventually we want them to be able specify day and time of digest, but not yet
            ; [:div.user-profile-field.digest-frequency-field.digest-day
            ;   [:select
            ;     {:value (:digest-day current-user-data)
            ;      :disabled (not= (:digest-frequency current-user-data) "weekly")
            ;      :on-change #(change! s :digest-day (.. % -target -value))}
            ;     [:option {:value "monday"} "MONDAY"]
            ;     [:option {:value "tuesday"} "TUESDAY"]
            ;     [:option {:value "wednesday"} "WEDNESDAY"]
            ;     [:option {:value "thursday"} "THURSDAY"]
            ;     [:option {:value "friday"} "FRIDAY"]
            ;     [:option {:value "saturday"} "SATURDAY"]
            ;     [:option {:value "sunday"} "SUNDAY"]]]
            ; [:div.user-profile-field.digest-frequency-field.digest-time
            ;   [:select
            ;     {:value (:digest-time current-user-data)
            ;      :disabled (= (:digest-frequency current-user-data) "never")
            ;      :on-change #(change! s :digest-time (.. % -target -value))}
            ;     [:option {:value "7am"} "7AM"]
            ;     [:option {:value "8am"} "8AM"]
            ;     [:option {:value "9am"} "9AM"]
            ;     [:option {:value "10am"} "10AM"]
            ;     [:option {:value "11am"} "11AM"]
            ;     [:option {:value "12pm"} "12PM"]
            ;     [:option {:value "1pm"} "1PM"]
            ;     [:option {:value "2pm"} "2PM"]
            ;     [:option {:value "3pm"} "3PM"]
            ;     [:option {:value "4pm"} "4PM"]
            ;     [:option {:value "5pm"} "5PM"]
            ;     [:option {:value "6pm"} "6PM"]
            ;     [:option {:value "7pm"} "7PM"]
            ;     [:option {:value "8pm"} "8PM"]
            ;     [:option {:value "9pm"} "9PM"]
            ;     [:option {:value "10pm"} "10PM"]
            ;     [:option {:value "11pm"} "11PM"]
            ;     [:option {:value "12am"} "12AM"]
            ;     [:option {:value "1am"} "1AM"]
            ;     [:option {:value "2am"} "2AM"]
            ;     [:option {:value "3am"} "3AM"]
            ;     [:option {:value "4am"} "4AM"]
            ;     [:option {:value "5am"} "5AM"]
            ;     [:option {:value "6am"} "6AM"]]]
        [:div.user-profile-footer
          [:button.mlb-reset.mlb-default
            {:on-click #(save-clicked s)
             :class (when @(::show-success s) "no-disable")
             :disabled (not (:has-changes current-user-data))}
             (when (:loading current-user-data)
                (small-loading))
            (if @(::show-success s)
              "Saved!"
              "Save")]
          [:button.mlb-reset.mlb-link-black
            {:on-click #(if (:has-changes current-user-data)
                          (do
                            (reset! (::name-error s) false)
                            (reset! (::email-error s) false)
                            (reset! (::password-error s) false)
                            (reset! (::current-password-error s) false)
                            (user-actions/user-profile-reset))
                          (real-close-cb orgs current-user-data))}
            "Cancel"]]]))