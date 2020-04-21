(ns oc.web.components.user-profile-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [goog.object :as googobj]
            [cljsjs.moment-timezone]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn real-close-cb [editing-user-data dismiss-action mobile-back-bt]
  (when (:has-changes editing-user-data)
    (user-actions/user-profile-reset))
  (dismiss-action)
  (when mobile-back-bt
    (nav-actions/menu-toggle)))

(def default-user-profile (oc.web.stores.user/random-user-image))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-avatar (rum/ref-node s "user-profile-avatar")
          $header-avatar (js/$ header-avatar)
          edit-user-profile-avatar @(drv/get-ref s :edit-user-profile-avatar)
          title (if (empty? edit-user-profile-avatar)
                  "Add a photo"
                  "Change photo")
          profile-tab? (.hasClass $header-avatar "profile-tab")]
      (if profile-tab?
        (.tooltip $header-avatar #js {:title title
                                      :trigger "hover focus"
                                      :position "top"
                                      :container "body"})
        (.tooltip $header-avatar "destroy")))))

(defn close-cb [current-user-data dismiss-action & [mobile-back-bt]]
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
                                          (real-close-cb current-user-data dismiss-action mobile-back-bt))}]
      (alert-modal/show-alert alert-data))
    (real-close-cb current-user-data dismiss-action mobile-back-bt)))

(defn error-cb [res error]
  (notification-actions/show-notification
    {:title "Image upload error"
     :description "An error occurred while processing your image. Please retry."
     :expire 3
     :dismiss true}))

(defn success-cb
  [res]
  (let [url (googobj/get res "url")]
    (if-not url
      (error-cb nil nil)
      (do
        (dis/dispatch! [:input [:edit-user-profile-avatar] url])
        (user-actions/user-avatar-save url)))))

(defn progress-cb [res progress])

(defn upload-user-profile-pictuer-clicked []
  (iu/upload! user-utils/user-avatar-filestack-config success-cb progress-cb error-cb))

(defn change! [s kc v]
  (reset! (::name-error s) false)
  (reset! (::email-error s) false)
  (reset! (::password-error s) false)
  (reset! (::current-password-error s) false)
  (dis/dispatch! [:input (vec (concat [:edit-user-profile] kc)) v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(defn save-clicked [s]
  (when (compare-and-set! (::loading s) false true)
    (reset! (::name-error s) false)
    (reset! (::email-error s) false)
    (reset! (::password-error s) false)
    (reset! (::current-password-error s) false)
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
        (user-actions/user-profile-save current-user-data edit-user-profile
         (fn [success]
           (when success
             (real-close-cb edit-user-profile #(nav-actions/show-user-settings nil) nil))
           (notification-actions/show-notification
            {:title (if success "Profile saved" "Error")
             :description (if success
                            "Your profile has been updated."
                            "An error occurred while saving your profile. Please retry.")
             :expire 3
             :id (if success :user-profile-save-succeeded :user-profile-save-failed)
             :dismiss true})))))))

(defn- placeholder [k]
  (case k
   :facebook
   "facebook.com/..."
   :linked-in
   "linkedin.com/in/..."
   :instagram
   "instagram.com/..."
   :twitter
   "twitter.com/..."
   :email
   "Your email address"
   :title
   "CEO, CTO, Designer, Engineer..."
   :location
   "ie: New York, NY"
   ""))

(defn- default-value [k]
  (case k
   :facebook
   "facebook.com/"
   :linked-in
   "linkedin.com/in/"
   :instagram
   "instagram.com/"
   :twitter
   "twitter.com/"
   ""))

(rum/defcs user-profile-modal <
  rum/reactive
  (drv/drv :edit-user-profile)
  (drv/drv :current-user-data)
  (drv/drv :edit-user-profile-avatar)
  ;; Locals
  (rum/local false ::loading)
  (rum/local false ::show-success)
  (rum/local false ::name-error)
  (rum/local false ::email-error)
  (rum/local false ::password-error)
  (rum/local false ::current-password-error)
  ;; Mixins
  ui-mixins/refresh-tooltips-mixin
  (ui-mixins/autoresize-textarea :blurb)
  {:will-mount (fn [s]
    (user-actions/get-user nil)
    s)
    :did-update (fn [s]
     (let [user-data (:user-data @(drv/get-ref s :edit-user-profile))]
       (when (and @(::loading s)
                  (not (:has-changes s)))
         (reset! (::show-success s) true)
         (reset! (::loading s) false)
         (utils/after 2500 (fn [] (reset! (::show-success s) false)))))
     s)}
  [s]
  (let [edit-user-profile-avatar (drv/react s :edit-user-profile-avatar)
        is-jelly-head-avatar (str/includes? edit-user-profile-avatar "/img/ML/happy_face_")
        user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        user-for-avatar (merge current-user-data {:avatar-url edit-user-profile-avatar})
        timezones (.names (.-tz js/moment))
        guessed-timezone (.. js/moment -tz guess)
        show-password? (= (:auth-source current-user-data) "email")
        links-tab-index (atom 5)]
    [:div.user-profile-modal-container
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-cb current-user-data nav-actions/close-all-panels)}]
      [:div.user-profile-modal
        [:div.user-profile-header
          [:div.user-profile-header-title
            "My profile"]
          [:button.mlb-reset.save-bt
            {:on-click #(when-not @(::loading s)
                          (if (:has-changes current-user-data)
                            (save-clicked s)
                            (nav-actions/show-user-settings nil)))
             :class (utils/class-set {:disabled (or (not (:has-changes current-user-data))
                                                    @(::show-success s)
                                                    @(::loading s))})}
            (if @(::show-success s)
              "Saved!"
              "Save")]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-cb current-user-data #(nav-actions/show-user-settings nil)))}
            "Back"]]
        [:div.user-profile-body
          [:div.user-profile-avatar
            {:ref "user-profile-avatar"
             :on-click #(upload-user-profile-pictuer-clicked)}
            (if is-jelly-head-avatar
              [:div.empty-user-avatar-placeholder]
              (user-avatar-image user-for-avatar))
            [:div.user-profile-avatar-label "Edit profile photo"]]
          [:div.user-profile-modal-fields
            [:form
              {:action "."
               :on-submit #(utils/event-stop %)}
              [:div.field-label.big-web-tablet-only
                [:label.half-field-label
                  {:for "profile-first-name"}
                  "First name"]
                [:label.half-field-label
                  {:for "profile-last-name"}
                  "Last name"]
                (when @(::name-error s)
                  [:span.error "Please provide your name."])]

              [:label.field-label.mobile-only
                {:for "profile-first-name"}
                "First name"
                (when @(::name-error s)
                  [:span.error "Please provide your name."])]

              [:input.field-value.oc-input.half-field
                {:value (:first-name current-user-data)
                 :type "text"
                 :tab-index 1
                 :placeholder (placeholder :first-name)
                 :id "profile-first-name"
                 :max-length user-utils/user-name-max-lenth
                 :on-change #(change! s [:first-name] (.. % -target -value))}]

              [:label.field-label.mobile-only
                  {:for "profile-last-name"}
                  "Last name"]

              [:input.field-value.oc-input.half-field
                {:value (:last-name current-user-data)
                 :type "text"
                 :tab-index 2
                 :id "profile-last-name"
                 :placeholder (placeholder :last-name)
                 :max-length user-utils/user-name-max-lenth
                 :on-change #(change! s [:last-name] (.. % -target -value))}]

              [:label.field-label
                {:for "profile-role"}
                "Role"]
              [:input.field-value.oc-input
                {:value (:title current-user-data)
                 :type "text"
                 :id "profile-role"
                 :placeholder (placeholder :title)
                 :tab-index 3
                 :max-length 56
                 :on-change #(change! s [:title] (.. % -target -value))}]
              [:div.field-label
                "Email"
                (when @(::email-error s)
                  [:span.error "This email isn't valid."])]
              [:input.field-value.not-allowed.oc-input
                {:value (:email current-user-data)
                 :placeholder (placeholder :email)
                 :read-only true
                 :type "text"}]
              [:label.field-label
                {:for "profile-blurb"}
                "Blurb"]
              [:textarea.field-value.oc-input
                {:value (:blurb current-user-data)
                 :ref :blurb
                 :id "profile-blurb"
                 :placeholder (placeholder :blurb)
                 :tab-index 3
                 :columns 2
                 :max-length 256
                 :on-change #(change! s [:blurb] (.. % -target -value))}]
              [:label.field-label
                {:for "profile-location"}
                "Location"]
              [:input.field-value.oc-input
                {:value (:location current-user-data)
                 :type "text"
                 :id "profile-location"
                 :placeholder (placeholder :location)
                 :tab-index 4
                 :max-length 56
                 :on-change #(change! s [:location] (.. % -target -value))}]
              [:label.field-label
                {:for "profile-timezone"}
                "Timezone"]
              [:select.field-value.oc-input
                {:value (:timezone current-user-data)
                 :id "profile-timezone"
                 :tab-index 5
                 :on-change #(change! s [:timezone] (.. % -target -value))}
                ;; Promoted timezones
                (for [t (remove nil? ["US/Eastern" "US/Central" "US/Mountain" "US/Pacific" guessed-timezone])]
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
                    t])]
              (for [[k v] (:profiles current-user-data)
                    :let [field-name (str "profile-profiles-" (name k))
                          tab-index (swap! links-tab-index inc)]]
                [:div.profile-group
                  {:key field-name}
                  [:label.field-label
                    {:for field-name}
                    (str/capital (str/camel k))]
                  [:input.field-value.oc-input
                    {:value (get (:profiles current-user-data) k)
                     :placeholder (placeholder k)
                     :max-length 128
                     :id field-name
                     :tab-index tab-index
                     :on-focus #(when-not (seq v)
                                  (set! (.. % -target -value) (default-value k)))
                     :on-change #(change! s [:profiles k] (.. % -target -value))
                     :type "text"}]])
              (when show-password?
                [:div.fields-divider-line])
              ;; Current password
              (when show-password?
                [:label.field-label
                  {:for "profile-password"}
                  "Currrent password"
                  (when @(::current-password-error s)
                      [:span.error "Current password required"])])
              (when show-password?
                [:input.field-value.oc-input
                  {:type "password"
                   :id "profile-password"
                   :tab-index (+ 4 (count (:profiles current-user-data)) 1)
                   :placeholder (placeholder :password)
                   :on-change #(change! s :current-password (.. % -target -value))
                   :value (:current-password current-user-data)}])
              (when show-password?
                [:label.field-label
                  {:for "profile-new-password"}
                  "New password"
                  (when @(::password-error s)
                    [:span.error "Minimum 8 characters"])])
              (when show-password?
                [:input.field-value.oc-input
                  {:type "password"
                   :id "profile-new-password"
                   :tab-index (+ 4 (count (:profiles current-user-data)) 1)
                   :placeholder (placeholder :new-password)
                   :on-change #(change! s :password (.. % -target -value))
                   :value (:password current-user-data)}])]]]]]))