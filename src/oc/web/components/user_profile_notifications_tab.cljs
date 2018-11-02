(ns oc.web.components.user-profile-notifications-tab
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]))

(defn change! [s k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(defn save-clicked [s]
  (reset! (::loading s) true)
  (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
        current-user-data @(drv/get-ref s :current-user-data)
        user-data (:user-data edit-user-profile)]
    (user-actions/user-profile-save current-user-data edit-user-profile)))

(rum/defcs user-profile-notifications-tab <
                                       rum/reactive
                                       (drv/drv :edit-user-profile)
                                       (drv/drv :current-user-data)
                                       ;; Locals
                                       (rum/local false ::loading)
                                       (rum/local false ::show-success)

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
  [s org-data real-close-cb]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)]
    [:div.user-profile-internal
      [:div.user-profile-content.group
        [:div.user-profile-field-box
          [:div.user-profile-field-label
            [:span.error
              {:style {:margin-left "0px"}}
              (when error
                "An error occurred while saving, please try again.")]]]
        ; Left column
        [:div.user-profile-column-left.fs-hide
          ;; Digest frequency
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "Notifications by " [:i.mdi.mdi-information-outline
                {:title "Receive notifications of newly created posts."
                 :data-toggle "tooltip"
                 :data-placement "top"}]]
            [:div.user-profile-field.digest-medium
              [:div.dropdown.dropdown-button
                [:button.btn-reset.user-type-btn.dropdown-toggle
                  {:id "user-digest-medium-dropdown"
                   :data-toggle "dropdown"
                   :aria-haspopup true
                   :aria-expanded false}
                  (case (:digest-medium current-user-data)
                    "slack"
                    "Slack"
                    "email"
                    "Email"
                    "In-app only")]
                [:ul.dropdown-menu.user-type-dropdown-menu
                  {:aria-labelledby "user-digest-medium-dropdown"}
                  [:li
                    {:on-click #(change! s :digest-medium "email")}
                    "Email"]
                  ;; Show Slack digest option if
                  (when (jwt/team-has-bot? (:team-id org-data))
                    [:li
                      {:on-click #(change! s :digest-medium "slack")}
                      "Slack"])
                  [:li
                    {:on-click #(change! s :digest-medium "in-app")}
                    "In-app only"]]]]]]
        ; Right column
        [:div.user-profile-column-right.fs-hide
          ;; Digest Medium
          [:div.user-profile-field-box
            [:div.user-profile-field-label
              "Frequency"]
            [:div.user-profile-field.digest-frequency-field.digest-frequency
              [:div.dropdown.dropdown-button
                [:button.btn-reset.user-type-btn.dropdown-toggle
                  {:id "user-digest-frequency-dropdown"
                   :data-toggle "dropdown"
                   :aria-haspopup true
                   :aria-expanded false
                   :disabled (and (not= (:digest-medium current-user-data) "email")
                                  (not= (:digest-medium current-user-data) "slack"))}
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
                    "Never"]]]]]]]
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
                        (user-actions/user-profile-reset)
                        (real-close-cb current-user-data))}
          "Cancel"]]]))