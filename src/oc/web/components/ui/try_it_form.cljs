(ns oc.web.components.ui.try-it-form
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.actions.user :as ua]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]))

(defn submit-try-it-form [e try-it-form-id form-submit-success-cb]
  (.preventDefault e)
  (js/OCStaticMailchimpApiSubmit
   e
   (js/$ (str "#" try-it-form-id))
   #(when (fn? form-submit-success-cb) (form-submit-success-cb)) nil))

(rum/defcs try-it-form < (rum/local "" ::email-value)
  [s try-it-form-id form-submit-success-cb]
  [:form.validate
    {:action ls/mailchimp-api-endpoint
     :method "post"
     :class "mailchimp-api-subscribe-form"
     :id try-it-form-id
     :no-validate true}
    [:div.try-it-combo-field
      [:div.mc-field-group
        [:input.mail.required
          {:type "email"
           :value @(::email-value s)
           :class (str try-it-form-id "-input")
           :id "mce-EMAIL"
           :on-change #(reset! (::email-value s) (.. % -target -value))
           :name "email"
           :placeholder "Email address"}]]
      [:button.mlb-reset.try-it-get-started
        {:type "submit"
         :id "mc-embedded-subscribe"
         :disabled (not (utils/valid-email? @(::email-value s)))
         :on-click #(submit-try-it-form % try-it-form-id form-submit-success-cb)}
        "Get Early Access"]]])

(defn get-started-click
  []
  (if (sel1 [:input.try-it-form-central-input])
    (.focus (sel1 [:input.try-it-form-central-input]))
    (router/nav! oc-urls/home-try-it-focus)))

(rum/defcs get-started-button < rum/static
                                rum/reactive
                                {:will-mount (fn [s]
                                               (when-not (utils/is-test-env?)
                                                 (ua/auth-settings-get))
                                               s)}
  [s {:keys [button-classes]}]
  [:div.get-started-button
    {:class (when button-classes button-classes)}
    [:button.mlb-reset.mlb-get-started
      {:on-click get-started-click}
      "Get Early Access"]
    [:div.mobile-already-account
      [:a {:href oc-urls/login} "Already have an account? " [:span.login "Sign in"]]]])