(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.dom :as gdom]
            [goog.object :as gobj]))

(rum/defcs email-lander < rum/static
                          rum/reactive
                          (drv/drv :signup-with-email)
                          {:will-mount (fn [s]
                                        (let [signup-with-email @(drv/get-ref s :signup-with-email)]
                                          (when-not (contains? signup-with-email :email)
                                            (dis/dispatch! [:input [:signup-with-email] {:email "" :pswd "" :first-name "" :last-name ""}])))
                                        s)}
  [s]
  (let [signup-with-email (drv/react s :signup-with-email)]
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
          "Enter email"
          (when (= (:error signup-with-email) 409)
            [:span.error "Email already exists"])]
        [:input.field
          {:type "email"
           :class (when (= (:error signup-with-email) 409) "error")
           :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
           :value (:email signup-with-email)
           :on-change #(dis/dispatch! [:input [:signup-with-email :email] (.. % -target -value)])}]
        [:div.field-label
          "Password"]
        [:input.field
          {:type "password"
           :pattern ".{5,}"
           :value (:pswd signup-with-email)
           :on-change #(dis/dispatch! [:input [:signup-with-email :pswd] (.. % -target -value)])}]
        [:div.field-description
          "By signing up you are agreeing to our " [:a {:href oc-urls/about} "terms of service"] " and " [:a {:href oc-urls/about} "privacy policy"] "."]
        [:button.continue
          {:disabled (or (not (utils/valid-email? (:email signup-with-email)))
                         (<= (count (:pswd signup-with-email)) 4))
           :on-click #(dis/dispatch! [:signup-with-email])}
          "Continue"]
        [:div.footer-link
          "Already have an account?"
          [:a {:href oc-urls/login} "Login here"]]]]))

(rum/defcs email-lander-profile < rum/reactive
                                  (drv/drv :edit-user-profile)
                                  (rum/local false ::saving)
                                  {:will-mount (fn [s]
                                                 (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                                 s)
                                   :will-update (fn [s]
                                                 (when (and @(::saving s)
                                                            (not (:loading (:user-data @(drv/get-ref s :edit-user-profile))))
                                                            (not (:error @(drv/get-ref s :edit-user-profile))))
                                                    (utils/after 100 #(router/nav! oc-urls/sign-up-team)))
                                                 s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        user-data (:user-data edit-user-profile)]
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
          "This information will be visible to your team"]
        (when (:error edit-user-profile)
          [:div.subtitle.error
            "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:div.logo-upload-container
          {:on-click #(if (empty? (:avatar-url user-data))
                        (iu/upload! {:accept "image/*"
                                     :transformations {
                                       :crop {
                                         :aspectRatio 1}}}
                          (fn [res]
                            (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                          nil
                          (fn [_])
                          nil)
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] (utils/cdn ls/default-user-avatar-url true)]))}
          (user-avatar-image user-data)
          [:div.add-picture-link
            (if (empty? (:avatar-url user-data))
              "Upload profile photo"
              "Delete profile photo")]
          [:div.add-picture-link-subtitle
            "A 160x160 PNG or JPG works best"]]
        [:div.field-label
          "First name"]
        [:input.field
          {:type "text"
           :value (:first-name user-data)
           :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
        [:div.field-label
          "Last name"]
        [:input.field
          {:type "text"
           :value (:last-name user-data)
           :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
        [:button.continue
          {:disabled (or (and (empty? (:first-name user-data))
                              (empty? (:last-name user-data)))
                         (empty? (:avatar-url user-data)))
           :on-click #(do
                        (reset! (::saving s) true)
                        (dis/dispatch! [:user-profile-save]))}
          "Continue"]]]))

(rum/defcs email-lander-team < rum/reactive
                               (drv/drv :teams-data)
                               (drv/drv :org-editing)
                               (rum/local false ::saving)
                               {:after-render (fn [s]
                                               (let [org-editing @(drv/get-ref s :org-editing)
                                                     teams-data @(drv/get-ref s :teams-data)]
                                                 (when (and (empty? (:name org-editing))
                                                            (not (empty? teams-data)))
                                                   (dis/dispatch! [:input [:org-editing] (select-keys (first teams-data) [:name :logo-url :logo-width :logo-height])])))
                                               s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)]
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
                      (if (empty? (:logo-url org-editing))
                        (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                     :transformations {
                                       :crop {
                                         :aspectRatio 1}}}
                          (fn [res]
                            (let [url (gobj/get res "url")
                                  img (gdom/createDom "img")]
                              (set! (.-onload img) #(do
                                                      (dis/dispatch! [:input [:org-editing] (merge org-editing {:logo-url url :logo-width (.-width img) :logo-height (.-height img)})])
                                                      (gdom/removeNode img)))
                              (set! (.-className img) "hidden")
                              (gdom/append (.-body js/document) img)
                              (set! (.-src img) url)))
                          nil
                          (fn [_])
                          nil)
                        (dis/dispatch! [:input [:org-editing] (merge org-editing {:logo-url nil
                                                                                  :logo-width 0
                                                                                  :logo-height 0})])))}
          (org-avatar org-editing false)
          [:img.org-logo]
          [:div.add-picture-link
            (if (empty? (:logo-url org-editing))
              "Upload logo"
              "Delete logo")]
          [:div.add-picture-link-subtitle
            "A 160x160 PNG or JPG works best"]]
        [:div.field-label
          "Team name"]
        [:input.field
          {:type "text"
           :value (:name org-editing)
           :on-change #(dis/dispatch! [:input [:org-editing :name] (.. % -target -value)])}]
        [:button.continue
          {:disabled (empty? (:name org-editing))
           :on-click #(dis/dispatch! [:org-create])}
          "Create my team"]]]))

(rum/defcs slack-lander < rum/reactive
                          (drv/drv :edit-user-profile)
                          (rum/local false ::saving)
                          {:will-mount (fn [s]
                                         (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                         s)
                           :will-update (fn [s]
                                         (when (and @(::saving s)
                                                    (not (:loading (:user-data @(drv/get-ref s :edit-user-profile))))
                                                    (not (:error @(drv/get-ref s :edit-user-profile))))
                                            (utils/after 100 #(router/nav! oc-urls/sign-up-team)))
                                         s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        user-data (:user-data edit-user-profile)]
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
          {:on-click #(if (empty? (:avatar-url user-data))
                        (iu/upload! {:accept "image/*"
                                     :transformations {
                                       :crop {
                                         :aspectRatio 1}}}
                          (fn [res]
                            (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                          nil
                          (fn [_])
                          nil)
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] (utils/cdn ls/default-user-avatar-url true)]))}
          (user-avatar-image user-data)
          [:div.add-picture-link
            (if (empty? (:avatar-url user-data))
              "Upload profile photo"
              "Delete profile photo")]
          [:div.add-picture-link-subtitle
            "A 160x160 PNG or JPG works best"]]
        [:div.field-label
          "First name"]
        [:input.field
          {:type "text"
           :value (:first-name user-data)
           :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
        [:div.field-label
          "Last name"]
        [:input.field
          {:type "text"
           :value (:last-name user-data)
           :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
        [:button.continue
          {:disabled (or (and (empty? (:first-name user-data))
                              (empty? (:last-name user-data)))
                         (empty? (:avatar-url user-data)))
           :on-click #(do
                        (reset! (::saving s) true)
                        (dis/dispatch! [:user-profile-save true]))}
          "Sign Up"]]]))

(rum/defcs slack-lander-team < rum/reactive
                               (drv/drv :teams-load)
                               (drv/drv :teams-data)
                               (drv/drv :org-editing)
                               (rum/local false ::saving)
                               {:will-update (fn [s]
                                               (let [org-editing @(drv/get-ref s :org-editing)
                                                     teams-data @(drv/get-ref s :teams-data)
                                                     teams-load @(drv/get-ref s :teams-load)]
                                                 ;; Load the list of teams if it's not already
                                                 (when (and (empty? teams-data)
                                                            (:auth-settings teams-load)
                                                            (not (:teams-data-requested teams-load)))
                                                   (dis/dispatch! [:teams-get]))
                                                 ;; If the team is loaded setup the form
                                                 (when (and (nil? (:name org-editing))
                                                            (not (empty? teams-data)))
                                                   (dis/dispatch! [:input [:org-editing] (select-keys (first teams-data) [:name :logo-url :logo-width :logo-height])])))
                                               s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)]
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
                      (if (empty? (:logo-url org-editing))
                        (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                     :transformations {
                                       :crop {
                                         :aspectRatio 1}}}
                          (fn [res]
                            (let [url (gobj/get res "url")
                                  img (gdom/createDom "img")]
                              (set! (.-onload img) #(do
                                                      (dis/dispatch! [:input [:org-editing] (merge org-editing {:logo-url url :logo-width (.-width img) :logo-height (.-height img)})])
                                                      (gdom/removeNode img)))
                              (set! (.-className img) "hidden")
                              (gdom/append (.-body js/document) img)
                              (set! (.-src img) url)))
                          nil
                          (fn [_])
                          nil)
                        (dis/dispatch! [:input [:org-editing] (merge org-editing {:logo-url nil
                                                                                  :logo-width 0
                                                                                  :logo-height 0})])))}
          (org-avatar org-editing false)
          [:div.add-picture-link
            (if (empty? (:logo-url org-editing))
              "Upload logo"
              "Delete logo")]
          [:div.add-picture-link-subtitle
            "A 160x160 PNG or JPG works best"]]
        [:div.field-label
          "Team name"]
        [:input.field
          {:type "text"
           :value (:name org-editing)
           :on-change #(dis/dispatch! [:input [:org-editing :name] (.. % -target -value)])}]
        [:button.continue
          {:disabled (empty? (:name org-editing))
           :on-click #(dis/dispatch! [:org-create])}
          "Create my team"]]]))

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
        {:type "password"
         :pattern ".{5,}"}]
      [:div.description
        "By signing up you are agreeing to our " [:a {:href oc-urls/about} "terms of service"] " and " [:a {:href oc-urls/about} "privacy policy"] "."]
      [:button.continue
        "Continue"]]])

(rum/defcs invitee-lander-profile < rum/static
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

(rum/defcs email-wall < rum/reactive
                        (drv/drv :query-params)
                        (vertical-center-mixin ".onboard-email-container")
  [s]
  (let [email (:e (drv/react s :query-params))]
    [:div.onboard-email-container
      "Please verify your email address"
      [:div.email-wall-sent-link "We have sent a link to "
        (if (not (empty? email))
          [:span.email-address email]
          (str "your email address"))
        "."]]))

(defn exchange-token-when-ready [s]
  (when-let [auth-settings (:auth-settings @(drv/get-ref s :email-verification))]
    (when (and (not @(::exchange-started s))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (reset! (::exchange-started s) true)
      (dis/dispatch! [:auth-with-token :email-verification]))))

(defn dots-animation [s]
  (when-let [dots-node (rum/ref-node s :dots)]
    (let [dots (.-innerText dots-node)
          next-dots (case dots
                      "." ".."
                      ".." "..."
                      ".")]
      (set! (.-innerText dots-node) next-dots)
      (utils/after 800 #(dots-animation s)))))

(rum/defcs email-verified < rum/reactive
                            (drv/drv :email-verification)
                            (rum/local false ::exchange-started)
                            (vertical-center-mixin ".onboard-email-container")
                            {:will-mount (fn [s]
                                           (js/console.log "email-verified/will-mount" s)
                                           (exchange-token-when-ready s)
                                           s)
                             :did-mount (fn [s]
                                          (js/console.log "email-verified/did-mount" s)
                                          (dots-animation s)
                                          (exchange-token-when-ready s)
                                          s)
                             :did-update (fn [s]
                                          (exchange-token-when-ready s)
                                          s)}
  [s]
  (let [email-verification (drv/react s :email-verification)]
    (cond
      (= (:error email-verification) 401)
      [:div.onboard-email-container.error
        "This link is not valid, please try again."]
      (:error email-verification)
      [:div.onboard-email-container.error
        "An error occurred, please try again."]
      (:success email-verification)
      [:div.onboard-email-container
        "Thanks for verifying"
        [:button.mlb-reset.continue
          {:on-click #(router/nav! oc-urls/login)}
          "Get Started"]]
      :else
      [:div.onboard-email-container.small.dot-animation
        "Verifying, please wait" [:span.dots {:ref :dots} "."]])))

(defn get-component [c]
  (case c
    :email-lander (email-lander)
    :email-lander-profile (email-lander-profile)
    :email-lander-team (email-lander-team)
    :slack-lander (slack-lander)
    :slack-lander-team (slack-lander-team)
    :invitee-lander (invitee-lander)
    :invitee-lander-profile (invitee-lander-profile)
    :email-wall (email-wall)
    :email-verified (email-verified)
    [:div]))

(rum/defc onboard-wrapper < rum/static
  [component]
  [:div.onboard-wrapper-container
    [:div.onboard-wrapper
      [:div.onboard-wrapper-left
        [:div.onboard-wrapper-logo]
        [:div.onboard-wrapper-box]]
      [:div.onboard-wrapper-right
        (get-component component)]]])