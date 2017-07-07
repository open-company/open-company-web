(ns oc.web.components.ui.try-it-form
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]))

(rum/defcs try-it-form < (rum/local (int (rand 100)) ::input-class)
                         (rum/local "" ::email-value)
  [s try-it-input-class & [get-started-cb]]
  (let [try-it-final-class (or try-it-input-class (str "try-it-class-" @(::input-class s)))]
    [:form.validate
      {:action ls/mailchimp-api-endpoint
       :method "post"
       :id "mailchimp-api-subscribe-form"
       :no-validate true}
      [:div.try-it-combo-field
        [:div.mc-field-group
          [:input.mail.required
            {:type "text"
             :value @(::email-value s)
             :class try-it-final-class
             :id "mce-EMAIL"
             :on-change #(reset! (::email-value s) (.. % -target -value))
             :name "email"
             :placeholder "Email address"}]]
        [:button.mlb-reset.try-it-get-started
          {:type "submit"
           :id "mc-embedded-subscribe"
           :disabled (not (utils/valid-email? @(::email-value s)))
           :on-click (fn [e]
                       (let [email-value @(::email-value s)]
                         (when (utils/valid-email? email-value)
                           (js/mailchipApiSubmit e @(::email-value s) #(js/console.log %) #(js/console.log %))
                           (when (fn? get-started-cb)
                             (get-started-cb email-value)))))}
          "Get Early Access"]]]))

(defn get-started-click
  []
  (if (sel1 [:input.try-it-input-central])
    (.focus (sel1 [:input.try-it-input-central]))
    (router/nav! oc-urls/home-try-it-focus)))

(rum/defcs get-started-button < rum/static
                                rum/reactive
                                {:will-mount (fn [s]
                                              (when-not (utils/is-test-env?)
                                                (dis/dispatch! [:auth-settings-get]))
                                              s)}
  [s {:keys [button-classes]}]
  [:div.get-started-button
    {:class (when button-classes button-classes)}
    [:button.mlb-reset.mlb-get-started
      {:on-click get-started-click}
      "Get Early Access"]
    [:div.mobile-already-account
      [:a {:href oc-urls/login} "Already have an account? " [:span.login "Sign in"]]]])