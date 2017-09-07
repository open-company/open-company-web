(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.dom :as gdom]
            [goog.object :as gobj]))

(rum/defc invitee-lander < rum/static
  [email]
  [:div.invitee-lander
    [:div.steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Profile"]]
    [:div.main-cta
      [:div.title
        "Join your team on Carrot"]
      [:div.subtitle
        "Signing up as " [:span.email-address email]]]
    [:div.invitee-form
      [:div.field-label
        "Password"]
      [:input.field
        {:type "password"}]
      [:div.description
        "By signing up you are agreeing to our " [:a {:href oc-urls/about} "terms of service"] " and " [:a {:href oc-urls/about} "privacy policy"] "."]
      [:button.continue
        "Continue"]]])

(rum/defcs invitee-lander-collect < rum/static
                                    (rum/local nil ::user-data)
  [s]
  [:div.invitee-lander.collect
    [:div.steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Profile"]]
    [:div.main-cta
      [:div.title
        "Tell us about yourself"]
      [:div.subtitle
        "This information will be visible to your team"]]
    [:div.invitee-form
      [:div.logo-upload-container
        {:on-click #(iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                 :transformations {
                                   :crop {
                                     :aspectRatio 1}}}
                      (fn [res]
                        (reset! (::user-data s) (assoc @(::user-data s) :avatar-url (gobj/get res "url"))))
                      nil
                      (fn [_])
                      nil)}
        (user-avatar-image )
        [:div.add-picture-link
          "Upload profile photo"]
        [:div.add-picture-link-subtitle
          "A 160x160 PNG or JPG works best"]]
      [:div.field-label
        "First name"]
      [:input.field
        {:type "text"
         :value (:first-name @(::user-data s))
         :on-change #(reset! (::user-data s) (assoc @(::user-data s) :first-name (.. % -target -value)))}]
      [:div.field-label
        "Last name"]
      [:input.field
        {:type "text"
         :value (:last-name @(::user-data s))
         :on-change #(reset! (::user-data s) (assoc @(::user-data s) :last-name (.. % -target -value)))}]
      [:button.continue
        "Sign Up"]]])

(defn vertical-center-mixin [class-selector]
  {:after-render (fn [s]
                   (let [el (js/document.querySelector class-selector)]
                     (set! (.-marginTop (.-style el)) (str (* -1 (/ (.-clientHeight el) 2)) "px")))
                   s)})

(rum/defc email-wall < rum/static
                        (vertical-center-mixin ".email-wall")
  [email]
  [:div.email-wall
    "Please verify your email address"
    [:div.email-wall-sent-link "We have sent a link to " [:span.email-address email]]
    [:button.mlb-reset.resend-email
      "Resend email"]])

(rum/defc email-verified < rum/static
                            (vertical-center-mixin ".email-wall")
  [email]
  [:div.email-wall
    "Thanks for verifying"
    [:button.mlb-reset.resend-email
      "Get Started"]])

(rum/defc onboard-wrapper < rum/static
  []
  [:div.onboard-wrapper-container
    [:div.onboard-wrapper
      [:div.onboard-wrapper-left
        [:div.onboard-wrapper-logo]
        [:div.onboard-wrapper-box]]
      [:div.onboard-wrapper-right
        (invitee-lander-collect)]]])