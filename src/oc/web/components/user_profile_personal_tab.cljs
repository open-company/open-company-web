(ns oc.web.components.user-profile-personal-tab
  (:require [rum.core :as rum]
            [cljsjs.moment-timezone]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]))

(defn change! [s k v]
  (reset! (::name-error s) false)
  (reset! (::email-error s) false)
  (reset! (::password-error s) false)
  (reset! (::current-password-error s) false)
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

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

(rum/defcs user-profile-personal-tab < rum/reactive
                                       (drv/drv :edit-user-profile)
                                       (drv/drv :current-user-data)
                                       ;; Locals
                                       (rum/local false ::loading)
                                       (rum/local false ::show-success)
                                       (rum/local false ::name-error)
                                       (rum/local false ::email-error)
                                       (rum/local false ::password-error)
                                       (rum/local false ::current-password-error)

                                       {:after-render (fn [s]
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
  [s real-close-cb]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)
        timezones (.names (.-tz js/moment))]
    [:div.user-profile-internal
      [:div.user-profile-content.group
        [:div.user-profile-field-box
          [:div.user-profile-field-label
            [:span.error
              {:style {:margin-left "0px"}}
              (when error
                "An error occurred while saving, please try again.")]]]
        ; Left column
        [:div.user-profile-column-left
          {:class utils/hide-class}
          ; First name
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "First name"
              (when @(::name-error s)
                [:span.error "Please provide your name."])]
            [:div.user-profile-field
              [:input
                {:type "text"
                 :tab-index 1
                 :max-length user-utils/user-name-max-lenth
                 :on-change #(change! s :first-name (.. % -target -value))
                 :value (:first-name current-user-data)}]]]
          ;; Mobile last name
          [:div.user-profile-field-box.mobile-only
            [:div.user-profile-field-label
              "Last name"]
            [:div.user-profile-field
              [:input
                {:type "text"
                 :tab-index 2
                 :max-length user-utils/user-name-max-lenth
                 :on-change #(change! s :last-name (.. % -target -value))
                 :value (:last-name current-user-data)}]]]
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
                 :read-only true
                 :disabled true
                 :value (:email current-user-data)}]]]
          ; Current password
          (when (= (:auth-source current-user-data) "email")
            [:div.user-profile-field-box.password-update
              [:div.user-profile-field-label
                "Current password"
                (when @(::current-password-error s)
                  [:span.error "Current password required"])]
                [:div.user-profile-field
                  [:input
                    {:type "password"
                     :tab-index 3
                     :on-change #(change! s :current-password (.. % -target -value))
                     :value (:current-password current-user-data)}]]])]
        ; Right column
        [:div.user-profile-column-right
          {:class utils/hide-class}
          ; Last name
          [:div.user-profile-field-box.big-web-only
            [:div.user-profile-field-label
              "Last name"]
            [:div.user-profile-field
              [:input
                {:type "text"
                 :tab-index 2
                 :on-change #(change! s :last-name (.. % -target -value))
                 :value (:last-name current-user-data)}]]]
          ;; Time zone
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "Timezone"]
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
                    t])]]
          ; New password
          (when (= (:auth-source current-user-data) "email")
            [:div.user-profile-field-box.password-update
              [:div.user-profile-field-label
                "New password"
                (when @(::password-error s)
                  [:span.error "Minimum 8 characters"])]
              [:div.user-profile-field
                [:input
                  {:type "password"
                   :tab-index 4
                   :on-change #(change! s :password (.. % -target -value))
                   :value (:password current-user-data)}]]])]]]
      [:div.user-profile-footer.group
        [:button.mlb-reset.save-bt
          {:on-click #(save-clicked s)
           :class (when @(::show-success s) "no-disable")
           :disabled (not (:has-changes current-user-data))}
           (when (:loading current-user-data)
              (small-loading))
          (if @(::show-success s)
            "Saved!"
            "Save")]
        [:button.mlb-reset.cancel-bt
          {:on-click #(if (:has-changes current-user-data)
                        (do
                          (reset! (::name-error s) false)
                          (reset! (::email-error s) false)
                          (reset! (::password-error s) false)
                          (reset! (::current-password-error s) false)
                          (user-actions/user-profile-reset))
                        (real-close-cb current-user-data))}
          "Cancel"]]]))