(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.dom :as gdom]
            [goog.object :as gobj]))

(rum/defcs email-lander < rum/static
                          (rum/local nil ::signup-data)
  [s]
  [:div.onboard-lander
    [:div.steps.three-steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Profile"]
      [:div.step-progress-bar]
      [:div.step-3
        "Your Team"]]
    [:div.main-cta
      [:div.title
        "Get Started"]]
    [:div.onboard-form
      [:div.field-label
        "Enter email"]
      [:input.field
        {:type "email"
         :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
         :value (:email @(::signup-data s))
         :on-change #(reset! (::signup-data s) (assoc @(::signup-data s) :email (.. % -target -value)))}]
      [:div.field-label
        "Password"]
      [:input.field
        {:type "password"
         :value (:password @(::signup-data s))
         :on-change #(reset! (::signup-data s) (assoc @(::signup-data s) :password (.. % -target -value)))}]
      [:div.field-description
        "By signing up you are agreeing to our " [:a {:href oc-urls/about} "terms of service"] " and " [:a {:href oc-urls/about} "privacy policy"] "."]
      [:button.continue
        "Continue"]
      [:div.footer-link
        "Already have an account?"
        [:a {:href oc-urls/login} " Login here"]]]])

(rum/defcs email-lander-profile < rum/static
                                  (rum/local nil ::user-data)
  [s]
  [:div.onboard-lander.second-step
    [:div.steps.three-steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Profile"]
      [:div.step-progress-bar]
      [:div.step-3
        "Your Team"]]
    [:div.main-cta
      [:div.title
        "Tell us about yourself"]
      [:div.subtitle
        "This information will be visible to your team"]]
    [:div.onboard-form
      [:div.logo-upload-container
        {:on-click #(if (empty? (:avatar-url @(::user-data s)))
                      (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (reset! (::user-data s) (assoc @(::user-data s) :avatar-url (gobj/get res "url"))))
                        nil
                        (fn [_])
                        nil)
                      (reset! (::user-data s) (merge @(::user-data s) {:avatar-url nil})))}
        (user-avatar-image @(::user-data s))
        [:div.add-picture-link
          (if (empty? (:avatar-url @(::user-data s)))
            "Upload profile photo"
            "Delete profile photo")]
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
        "Continue"]]])

(defn team-logo-on-load [s url img]
  (reset! (::team-data s) (merge @(::team-data s) {:logo-url url
                                                   :logo-width (.-width img)
                                                   :logo-height (.-height img)}))
  (gdom/removeNode img))

(rum/defcs email-lander-team < rum/static
                               (rum/local nil ::team-data)
  [s]
  [:div.onboard-lander.second-step.third-step
    [:div.steps.three-steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Profile"]
      [:div.step-progress-bar]
      [:div.step-3
        "Your Team"]]
    [:div.main-cta
      [:div.title
        "About your team"]
      [:div.subtitle
        "How your company will appear on Carrot"]]
    [:div.onboard-form
      [:div.logo-upload-container
        {:on-click (fn [_]
                    (if (empty? (:avatar-url @(::user-data s)))
                      (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (let [url (gobj/get res "url")
                                img (gdom/createDom "img")]
                            (set! (.-onload img) #(team-logo-on-load s url img))
                            (set! (.-className img) "hidden")
                            (gdom/append (.-body js/document) img)
                            (set! (.-src img) url)))
                        nil
                        (fn [_])
                        nil)
                      (reset! (::team-data s) (merge @(::team-data s) {:logo-url nil}))))}
        (user-avatar-image @(::team-data s))
        [:div.add-picture-link
          (if (empty? (:logo-url @(::team-data s)))
            "Upload logo"
            "Delete logo")]
        [:div.add-picture-link-subtitle
          "A 160x160 PNG or JPG works best"]]
      [:div.field-label
        "Team name"]
      [:input.field
        {:type "text"
         :value (:name @(::team-data s))
         :on-change #(reset! (::team-data s) (assoc @(::team-data s) :name (.. % -target -value)))}]
      [:button.continue
        "Create my team"]]])

(rum/defcs slack-lander < rum/static
                          (rum/local nil ::user-data)
  [s]
  [:div.onboard-lander
    [:div.steps.two-steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Team"]]
    [:div.main-cta
      [:div.title
        "Tell us about yourself"]
      [:div.subtitle
        "This information will be visible to your team"]]
    [:div.onboard-form
      [:div.logo-upload-container
        {:on-click #(if (empty? (:avatar-url @(::user-data s)))
                      (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (reset! (::user-data s) (assoc @(::user-data s) :avatar-url (gobj/get res "url"))))
                        nil
                        (fn [_])
                        nil)
                      (reset! (::user-data s) (merge @(::user-data s) {:avatar-url nil})))}
        (user-avatar-image @(::user-data s))
        [:div.add-picture-link
          (if (empty? (:avatar-url @(::user-data s)))
            "Upload profile photo"
            "Delete profile photo")]
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

(rum/defcs slack-lander-team < rum/static
                               (rum/local nil ::team-data)
  [s]
  [:div.onboard-lander.second-step
    [:div.steps.two-steps
      [:div.step-1
        "Get Started"]
      [:div.step-progress-bar]
      [:div.step-2
        "Your Team"]]
    [:div.main-cta
      [:div.title
        "About your team"]
      [:div.subtitle
        "How your company will appear on Carrot"]]
    [:div.onboard-form
      [:div.logo-upload-container
        {:on-click (fn [_]
                    (if (empty? (:logo-url @(::team-data s)))
                      (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (let [url (gobj/get res "url")
                                img (gdom/createDom "img")]
                            (set! (.-onload img) #(team-logo-on-load s url img))
                            (set! (.-className img) "hidden")
                            (gdom/append (.-body js/document) img)
                            (set! (.-src img) url)))
                        nil
                        (fn [_])
                        nil)
                      (reset! (::team-data s) (merge @(::team-data s) {:logo-url nil}))))}
        (user-avatar-image @(::team-data s))
        [:div.add-picture-link
          (if (empty? (:logo-url @(::team-data s)))
            "Upload logo"
            "Delete logo")]
        [:div.add-picture-link-subtitle
          "A 160x160 PNG or JPG works best"]]
      [:div.field-label
        "Team name"]
      [:input.field
        {:type "text"
         :value (:name @(::team-data s))
         :on-change #(reset! (::team-data s) (assoc @(::team-data s) :name (.. % -target -value)))}]
      [:div.field-label
        "Email domain"]
      [:input.field
        {:type "text"
         :auto-capitalize "none"
         :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
         :value (:email-domain @(::team-data s))
         :on-change #(reset! (::team-data s) (assoc @(::team-data s) :email-domain (.. % -target -value)))}]
      [:div.field-description
        "Anyone who signs up with this email domain can view team Boards"]
      [:button.continue
        "Create my team"]]])

(rum/defc invitee-lander < rum/static
  [email]
  [:div.onboard-lander
    [:div.steps.two-steps
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
    [:div.onboard-form
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
  [:div.onboard-lander.second-step
    [:div.steps.two-steps
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
        (user-avatar-image @(::user-data s))
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
        (email-lander-team)]]])