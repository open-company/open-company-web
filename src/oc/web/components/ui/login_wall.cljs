(ns oc.web.components.ui.login-wall
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.shared.useragent :as ua]
            [oc.web.expo :as expo]))

(def default-title "Please log in to continue")
(def default-desc "You need to be logged in to view a post.")

(rum/defcs login-wall < rum/reactive
                        (drv/drv :auth-settings)
                        (drv/drv :login-with-email-error)
                        (rum/local "" ::email)
                        (rum/local "" ::pswd)
  [s {:keys [title desc]}]
  (let [auth-settings (drv/react s :auth-settings)
        login-enabled (and auth-settings
                           (not (nil?
                            (utils/link-for
                             (:links auth-settings)
                             "authenticate"
                             "GET"
                             {:auth-source "email"}))))
        login-action #(when login-enabled
                        (.preventDefault %)
                        (user-actions/maybe-save-login-redirect)
                        (user-actions/login-with-email @(::email s) @(::pswd s)))
        login-with-email-error (drv/react s :login-with-email-error)]
    (if (jwt/jwt)
      [:div.login-wall-container
        (loading)]
      [:div.login-wall-container
        (login-overlays-handler)
        [:div.login-wall-wrapper
          [:div.login-wall-left
            [:div.login-wall-logo]
            [:div.login-wall-box]]
          [:div.login-wall-right
            [:div.login-wall-right-content
              [:div.login-overlay-cta.group
                [:button.mlb-reset.top-back-button
                  {:on-touch-start identity
                   :on-click #(user-actions/show-login nil)
                   :aria-label "Back"}]
                [:div.login-title (or title default-title)]
                [:button.mlb-reset.top-continue
                  {:aria-label "Login"
                   :class (when-not login-enabled "disabled")
                   :on-click login-action}]]
              (when (seq (or desc default-desc))
                [:div.login-description (or desc default-desc)])
              [:button.mlb-reset.signup-with-slack
                {:on-touch-start identity
                 :on-click #(do
                             (.preventDefault %)
                             (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                                   {:auth-source "slack"})]
                               (user-actions/maybe-save-login-redirect)
                               (user-actions/login-with-slack auth-link)))}
                [:div.signup-with-slack-content
                  [:div.slack-icon
                    {:aria-label "slack"}]
                  "Continue with Slack"]]
              [:button.mlb-reset.signup-with-google
                {:on-touch-start identity
                 :on-click #(do
                             (.preventDefault %)
                             (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                                                  {:auth-source "google"})]
                               (user-actions/maybe-save-login-redirect)
                               (user-actions/login-with-google auth-link
                                                               (when ua/mobile-app?
                                                                 {:redirect-origin (expo/get-deep-link-origin)}))))}
               [:div.signup-with-google-content
                 [:div.google-icon
                  {:aria-label "google"}]
                  "Continue with Google "]]
              [:div.or-login
                [:div.or-login-copy "Or, sign in with email"]]
              ;; Email fields
              [:div.group
                ;; Error messages
                (when-not (nil? login-with-email-error)
                  (cond
                    (= login-with-email-error :verify-email)
                    [:span.small-caps.green
                      "Hey buddy, go verify your email, again, eh?"]
                    (= login-with-email-error 401)
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
                  [:div.fields-container.group
                    [:div.field-label
                      "Work Email"]
                    [:input.field-content.email
                      {:type "email"
                       :name "email"
                       :class utils/hide-class
                       :value @(::email s)
                       :on-change #(reset! (::email s) (.. % -target -value))}]
                    [:div.field-label
                      "Password"]
                    [:input.field-content.password
                      {:type "password"
                       :name "password"
                       :class utils/hide-class
                       :value @(::pswd s)
                       :on-change #(reset! (::pswd s) (.. % -target -value))}]
                    [:div.forgot-password
                      [:a
                        {:on-click #(user-actions/show-login :password-reset)}
                        "Forgot password?"]]]
                  [:button.mlb-reset.continue-btn
                    {:aria-label "Login"
                     :class (when-not login-enabled "disabled")
                     :on-click login-action
                     :disabled (or (not (seq @(::email s)))
                                   (not (seq @(::pswd s))))}
                    "Continue"]
                  [:div.footer-link
                    "Don't have an account yet?"
                    [:a
                      {:href oc-urls/sign-up
                       :on-click (fn [e]
                                   (utils/event-stop e)
                                   (router/nav! oc-urls/sign-up))}
                      "Sign up here"]]
                 ]]]]]])))
