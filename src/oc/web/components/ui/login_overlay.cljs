(ns oc.web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [clojure.string :as s]
            [taoensso.timbre :as timbre]
            [goog.object :as gobj]
            [goog.style :as gstyle]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user-actions]
            [oc.web.stores.user :as user-store]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.components.ui.icon :as i]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(defn close-overlay [e]
  (utils/event-stop e)
  (user-actions/show-login false))

(def dont-scroll
  {:will-mount (fn [s]
                 (when-not (user-store/auth-settings?)
                   (utils/after 100 #(user-actions/auth-settings-get)))
                s)
   :before-render (fn [s]
                    (if (responsive/is-mobile-size?)
                      (let [display-none #js {:display "none"}]
                        (when (sel1 [:div.main])
                          (gstyle/setStyle (sel1 [:div.main]) display-none))
                        (when (sel1 [:nav.navbar-bottom])
                          (gstyle/setStyle (sel1 [:nav.navbar-bottom]) display-none))
                        (when (sel1 [:div.fullscreen-page])
                          (gstyle/setStyle (sel1 [:div.fullscreen-page]) display-none))))
                    s)
   :will-unmount (fn [s]
                   (if (responsive/is-mobile-size?)
                    (let [display-block #js {:display "block"}]
                      (when (sel1 [:div.main])
                        (gstyle/setStyle (sel1 [:div.main]) display-block))
                      (when (sel1 [:nav.navbar-bottom])
                        (gstyle/setStyle (sel1 [:nav.navbar-bottom]) display-block))
                      (when (sel1 [:div.fullscreen-page])
                          (gstyle/setStyle (sel1 [:div.fullscreen-page]) display-block))))
                   s)})

(rum/defcs login-with-email < rum/reactive
                              dont-scroll
                              no-scroll-mixin
                              (drv/drv user-store/auth-settings-key)
                              {:will-mount (fn [s]
                                (dis/dispatch! [:input [:login-with-email] {:email "" :pswd ""}])
                                s)
                               :did-mount (fn [s]
                                (.focus (sel1 [:input.email]))
                                s)}
  [state]
  (let [auth-settings (drv/react state user-store/auth-settings-key)
        login-enabled (and auth-settings
                           (not (nil?
                            (utils/link-for
                             (:links auth-settings)
                             "authenticate"
                             "GET"
                             {:auth-source "email"}))))
        login-action #(when login-enabled
                        (let [login-with-email (:login-with-email @dis/app-state)
                              email (:email login-with-email)
                              pswd (:pswd login-with-email)]
                          (.preventDefault %)
                          (user-actions/login-with-email email pswd)))]
    [:div.login-overlay-container.group
      {:on-click (partial close-overlay)}
      ;; Close X button
      [:button.carrot-modal-close.mlb-reset
          {:on-click (partial close-overlay)}]
      ;; Modal container
      [:div.login-overlay.login-with-email.group
        {:on-click #(utils/event-stop %)}
        [:div.login-overlay-cta.group
          [:button.mlb-reset.top-back-button
            {:on-touch-start identity
             :on-click #(router/history-back!)
             :aria-label "Back"}]
          [:div.sign-in-cta "Sign In"]
          [:button.mlb-reset.top-continue
            {:aria-label "Login"
             :class (when-not login-enabled "disabled")
             :on-click login-action}]]
        ;; Slack button
        [:button.mlb-reset.signin-with-slack
          {:on-click #(do
                       (.preventDefault %)
                       (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                             {:auth-source "slack"})]
                         (user-actions/login-with-slack auth-link)))
           :on-touch-start identity}
          [:div.signin-with-slack-content
            "Sign In with "
            [:div.slack-blue-icon
              {:aria-label "slack"}]]]
        ;; Or with email
        [:div.or-with-email
          [:div.or-with-email-line]
          [:div.or-with-email-copy
            "Or, sign in with email"]]
        ;; Email fields
        [:div.group
          ;; Error messages
          (when-not (nil? (:login-with-email-error (rum/react dis/app-state)))
            (cond
              (= (:login-with-email-error (rum/react dis/app-state)) :verify-email)
              [:span.small-caps.green
                "Hey buddy, go verify your email, again, eh?"]
              (= (:login-with-email-error (rum/react dis/app-state)) 401)
              [:span.small-caps.red
                "The email or password you entered is incorrect."
                [:br]
                "Please try again, or "
                [:a.underline.red
                  {:on-click #(user-actions/show-login :password-reset)}
                  "reset your password"]
                "."]
              :else
              [:span.small-caps.red
                "System troubles logging in."
                [:br]
                "Please try again, then "
                [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
                "."]))
          [:form.sign-in-form
            ;; Email label
            [:div.sign-in-label-container
              [:label.sign-in-label "Enter Email"]]
            ;; Email field
            [:div.sign-in-field-container
              [:input.sign-in-field.email
                {:value (:email (:login-with-email (rum/react dis/app-state)))
                 :on-change #(dis/dispatch! [:input [:login-with-email :email] (.. % -target -value)])
                 :type "email"
                 :id "sign-in-email"
                 :auto-focus true
                 :tabIndex 1
                 :autoCapitalize "none"
                 :name "email"}]]
            [:div.sign-in-label-container
              [:label.sign-in-label "Password"]]
            [:div.sign-in-field-container
              [:input.sign-in-field.pswd
                {:value (:pswd (:login-with-email (rum/react dis/app-state)))
                 :on-change #(dis/dispatch! [:input [:login-with-email :pswd] (.. % -target -value)])
                 :type "password"
                 :id "sign-in-pswd"
                 :tabIndex 2
                 :name "pswd"}]
              [:div.left.forgot-password
                [:a {:on-click #(user-actions/show-login :password-reset)} "Forgot Password?"]]]
            ;; Login button
            [:button.mlb-reset.mlb-default.continue
              {:class (when-not login-enabled "disabled")
               :on-touch-start identity
               :on-click login-action}
              "Sign In"]]]
        ;; Link to signup
        [:div.footer-link
          "Don't have an account yet?"
          [:a
            {:href oc-urls/sign-up
             :on-click (fn [e]
                         (utils/event-stop e)
                         (router/nav! oc-urls/sign-up))}
            "Signup here"]]]]))

(rum/defcs password-reset < rum/reactive
                            dont-scroll
                            no-scroll-mixin
                            (drv/drv user-store/auth-settings-key)
                            {:will-mount (fn [s]
                              (dis/dispatch! [:input [:password-reset] {:email ""}])
                              s)
                             :did-mount (fn [s]
                              (.focus (sel1 [:div.sign-in-field-container.email]))
                              s)}
  [state]
  (let [auth-settings (drv/react state user-store/auth-settings-key)]
    [:div.login-overlay-container.group
      {:on-click (partial close-overlay)}
      [:button.carrot-modal-close.mlb-reset
          {:on-click (partial close-overlay)}]
      [:div.login-overlay.password-reset
        {:on-click #(utils/event-stop %)}
        [:div.login-overlay-cta.group
          [:div.sign-in-cta "Password Reset"
            (when-not auth-settings
              (small-loading))]]
        [:div.pt2.pl3.pr3.pb2.group
          (when (contains? (:password-reset (rum/react dis/app-state)) :success)
            (cond
              (:success (:password-reset (rum/react dis/app-state)))
              [:div.green "We sent you an email with the instructions to reset your account password."]
              :else
              [:div.red "An error occurred, please try again."]))
          [:form.sign-in-form
            [:div.sign-in-label-container
              [:label.sign-in-label "Please enter your email address"]]
            [:div.sign-in-field-container.email
              [:input.sign-in-field
                {:value (:email (:password-reset (rum/react dis/app-state)))
                 :tabIndex 1
                 :type "email"
                 :autoCapitalize "none"
                 :auto-focus true
                 :on-change #(dis/dispatch! [:input [:password-reset :email] (.. % -target -value )])
                 :name "email"}]]
            (if (:success (:password-reset (rum/react dis/app-state)))
              [:div.group.pb3.mt3
                [:div.right
                  [:dubtton.mlb-reset.mlb-default
                    {:on-click #(user-actions/show-login nil)}
                    "Done"]]]
              [:div.group
                [:button.mlb-reset.mlb-default.continue
                  {:on-click #(user-actions/password-reset (:email (:password-reset @dis/app-state)))
                   :disabled (not (utils/valid-email? (:email (:password-reset @dis/app-state))))
                   :on-touch-start identity}
                  "Reset Password"]
                [:button.mlb-reset.mlb-link-black
                  {:on-click #(user-actions/show-login nil)
                   :disabled (not auth-settings)
                   :on-touch-start identity}
                  "Cancel"]])]]]]))

(rum/defcs collect-name-password < rum/reactive
                                   dont-scroll
                                   no-scroll-mixin
                                   (drv/drv user-store/auth-settings-key)
                                   {:will-mount (fn [s]
                                    (dis/dispatch! [:input [:collect-name-pswd] {:firstname "" :lastname "" :pswd ""}])
                                    s)
                                   :did-mount (fn [s]
                                     ; initialise the keys to string to avoid jumps in UI focus
                                     (utils/after 500
                                      #(dis/dispatch!
                                        [:input
                                         [:collect-name-pswd]
                                         {:firstname (or
                                                      (:first-name (:current-user-data @dis/app-state))
                                                      "")
                                          :lastname (or
                                                     (:last-name (:current-user-data @dis/app-state))
                                                     "")
                                          :pswd (or (:pswd (:collect-name-pswd @dis/app-state)) "")}]))
                                     (utils/after 100 #(.focus (sel1 [:input.firstname])))
                                     s)}
  [state]
  (let [auth-settings (drv/react state user-store/auth-settings-key)]
    [:div.login-overlay-container.group
      {:on-click #(utils/event-stop %)}
      [:div.login-overlay.collect-name-pswd.group
        [:div.login-overlay-cta.pl2.pr2.group
          [:div.sign-in-cta "Provide Your Name and a Password"
            (when-not auth-settings
              (small-loading))]]
        [:div.pt2.pl3.pr3.pb2.group
          (when-not (nil? (:collect-name-pswd-error (rum/react dis/app-state)))
            [:span.small-caps.red
              "System troubles logging in."
              [:br]
              "Please try again, then "
              [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
              "."])
          [:form.sign-in-form
            [:div.sign-in-label-container
              [:label.sign-in-label {:for "collect-name-pswd-firstname"} "Your Name"]]
            [:div.sign-in-field-container.group
              [:input.sign-in-field.firstname.half.left
                {:value (:firstname (:collect-name-pswd (rum/react dis/app-state)))
                 :id "collect-name-pswd-firstname"
                 :on-change #(dis/dispatch! [:input [:collect-name-pswd :firstname] (.. % -target -value)])
                 :placeholder "First name"
                 :type "text"
                 :tabIndex 1
                 :name "firstname"}]
              [:input.sign-in-field.lastname.half.right
                {:value (:lastname (:collect-name-pswd (rum/react dis/app-state)))
                 :id "collect-name-pswd-lastname"
                 :on-change #(dis/dispatch! [:input [:collect-name-pswd :lastname] (.. % -target -value)])
                 :placeholder "Last name"
                 :type "text"
                 :tabIndex 2
                 :name "lastname"}]]
            [:div.sign-in-label-container
              [:label.sign-in-label {:for "signup-pswd"} "Password"]]
            [:div.sign-in-field-container
              [:input.sign-in-field.pswd
                {:value (:pswd (:collect-name-pswd (rum/react dis/app-state)))
                 :id "collect-name-pswd-pswd"
                 :on-change #(dis/dispatch! [:input [:collect-name-pswd :pswd] (.. % -target -value)])
                 :pattern ".{8,}"
                 :placeholder "Minimum 8 characters"
                 :type "password"
                 :tabIndex 4
                 :name "pswd"}]]
            [:button.mlb-reset.mlb-default.continue
              {:disabled (or (and (s/blank? (:firstname (:collect-name-pswd (rum/react dis/app-state))))
                                  (s/blank? (:lastname (:collect-name-pswd (rum/react dis/app-state)))))
                             (< (count (:pswd (:collect-name-pswd (rum/react dis/app-state)))) 8))
               :on-touch-start identity
               :on-click #(do
                            (utils/event-stop %)
                            (user-actions/name-password-collect
                              (:collect-name-pswd @dis/app-state)))}
              "Let Me In"]]]]]))

(rum/defcs collect-password < rum/reactive
                              dont-scroll
                              no-scroll-mixin
                              (drv/drv user-store/auth-settings-key)
                              {:will-mount (fn [s]
                                (dis/dispatch! [:input [:collect-pswd] {:pswd ""}])
                                s)
                               :did-mount (fn [s]
                                ; initialise the keys to string to avoid jumps in UI focus
                                (utils/after 500
                                 #(dis/dispatch!
                                   [:input
                                    [:collect-pswd]
                                    {:pswd (or (:pswd (:collect-pswd @dis/app-state)) "")}]))
                                (utils/after 1000 #(when-let [pswd-el (sel1 [:input.sign-in-field.pswd])]
                                                     (.focus pswd-el)))
                                s)}
  [state]
  (let [auth-settings (drv/react state user-store/auth-settings-key)]
    [:div.login-overlay-container.group
      {:on-click #(utils/event-stop %)}
      [:div.login-overlay.collect-pswd.group
        [:div.login-overlay-cta.pl2.pr2.group
          [:div.sign-in-cta "Enter your new password"
            (when-not auth-settings
              (small-loading))]]
        [:div.pt2.pl3.pr3.pb2.group
          (when-not (nil? (:collect-password-error (rum/react dis/app-state)))
            [:span.small-caps.red
              "System troubles logging in."
              [:br]
              "Please try again, then "
              [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
              "."])
          [:form.sign-in-form
            [:div.sign-in-label-container
              [:label.sign-in-label {:for "signup-pswd"} "Password"]]
            [:div.sign-in-field-container
              [:input.sign-in-field.pswd
                {:value (:pswd (:collect-pswd (rum/react dis/app-state)))
                 :id "collect-pswd-pswd"
                 :on-change #(dis/dispatch! [:input [:collect-pswd :pswd] (.. % -target -value)])
                 :pattern ".{8,}"
                 :placeholder "Minimum 8 characters"
                 :type "password"
                 :tabIndex 4
                 :name "pswd"}]]
            [:button.mlb-reset.mlb-default.continue
              {:disabled (< (count (:pswd (:collect-pswd (rum/react dis/app-state)))) 8)
               :on-click #(do
                            (utils/event-stop %)
                            (user-actions/pswd-collect (:collect-pswd @dis/app-state) true))
               :on-touch-start identity}
              "Let Me In"]]]]]))

(rum/defcs login-overlays-handler < rum/static
                                    rum/reactive
                                    (drv/drv user-store/show-login-overlay?)
  [s]
  (cond
    ; login via email
    (or (= (drv/react s user-store/show-login-overlay-key) :login-with-email)
        (= (drv/react s user-store/show-login-overlay-key) :login-with-slack))
    (login-with-email)
    ; signup via email
    (or (= (drv/react s user-store/show-login-overlay-key) :signup-with-email)
        (= (drv/react s user-store/show-login-overlay-key) :signup-with-slack))
    (do
      (utils/after 150 #(router/nav! oc-urls/sign-up))
      [:div])
    ; password reset
    (= (drv/react s user-store/show-login-overlay-key) :password-reset)
    (password-reset)
    ; form to collect name and password
    (= (drv/react s user-store/show-login-overlay-key) :collect-name-password)
    (collect-name-password)
    ; form to insert a new password
    (= (drv/react s user-store/show-login-overlay-key) :collect-password)
    (collect-password)
    ; show nothing
    :else
    [:div.hidden]))