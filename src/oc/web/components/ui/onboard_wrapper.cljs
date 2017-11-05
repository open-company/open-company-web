(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image default-avatar-url)]
            [goog.dom :as gdom]
            [goog.object :as gobj]))

(rum/defcs email-lander < rum/static
                          rum/reactive
                          (drv/drv :signup-with-email)
                          (rum/local false ::email-error)
                          (rum/local false ::password-error)
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
          (cond
            (= (:error signup-with-email) 409)
            [:span.error "Email already exists"]
            @(::email-error s)
            [:span.error "Email is not valid"])]
        [:input.field
          {:type "email"
           :class (when (= (:error signup-with-email) 409) "error")
           :pattern "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
           :value (:email signup-with-email)
           :on-change #(do
                         (reset! (::password-error s) false)
                         (reset! (::email-error s) false)
                         (dis/dispatch! [:input [:signup-with-email :email] (.. % -target -value)]))}]
        [:div.field-label
          "Password"
          (when @(::password-error s)
            [:span.error
              "Minimum 8 characters"])]
        [:input.field
          {:type "password"
           :pattern ".{8,}"
           :value (:pswd signup-with-email)
           :placeholder "Minimum 8 characters"
           :on-change #(do
                         (reset! (::password-error s) false)
                         (reset! (::email-error s) false)
                         (dis/dispatch! [:input [:signup-with-email :pswd] (.. % -target -value)]))}]
        [:div.field-description
          "By signing up you are agreeing to our "
          [:a
            "terms of service"]
          " and "
          [:a
            "privacy policy"]
          "."]
        [:button.continue
          {:class (when (or (not (utils/valid-email? (:email signup-with-email)))
                            (<= (count (:pswd signup-with-email)) 7))
                    "disabled")
           :on-click #(if (or (not (utils/valid-email? (:email signup-with-email)))
                              (<= (count (:pswd signup-with-email)) 7))
                        (do
                          (when-not (utils/valid-email? (:email signup-with-email))
                            (reset! (::email-error s) true))
                          (when (<= (count (:pswd signup-with-email)) 7)
                            (reset! (::password-error s) true)))
                        (dis/dispatch! [:signup-with-email]))}
          "Continue"]
        [:div.footer-link
          "Already have an account?"
          [:a {:href oc-urls/login} "Login here"]]]]))

(rum/defcs email-lander-profile < rum/reactive
                                  (drv/drv :edit-user-profile)
                                  (drv/drv :orgs)
                                  (rum/local false ::saving)
                                  (rum/local nil ::temp-user-avatar)
                                  {:will-mount (fn [s]
                                                 (reset! (::temp-user-avatar s) (utils/cdn default-avatar-url true))
                                                 (cook/set-cookie! (router/should-show-dashboard-tooltips (jwt/get-key :user-id)) true (* 60 60 24 7))
                                                 (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                                 s)
                                   :will-update (fn [s]
                                                 (when (and @(::saving s)
                                                            (not (:loading (:user-data @(drv/get-ref s :edit-user-profile))))
                                                            (not (:error @(drv/get-ref s :edit-user-profile))))
                                                    (let [orgs @(drv/get-ref s :orgs)]
                                                      (if (pos? (count orgs))
                                                        (utils/after 100 #(router/nav! (oc-urls/org (:slug (first orgs)))))
                                                        (utils/after 100 #(router/nav! oc-urls/sign-up-team)))))
                                                 s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        user-data (:user-data edit-user-profile)
        temp-user-avatar @(::temp-user-avatar s)
        fixed-user-data (if (empty? (:avatar-url user-data))
                          (assoc user-data :avatar-url temp-user-avatar)
                          user-data)]
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
        [:div.title.about-yourself
          "Tell us a bit about yourself..."]
        [:div.subtitle
          "This information will be visible to your team"]
        (when (:error edit-user-profile)
          [:div.subtitle.error
            "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:div.logo-upload-container
          {:on-click (fn []
                      (when (not= (:avatar-url user-data) temp-user-avatar)
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] temp-user-avatar]))
                      (iu/upload! {:accept "image/*"
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                        nil
                        (fn [_])
                        nil))}
          (user-avatar-image fixed-user-data)
          [:div.add-picture-link
            "Change photo"]
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
                                                            (empty? (:logo-url org-editing))
                                                            (seq teams-data))
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
        [:div.logo-upload-container.org-logo
          {:on-click (fn [_]
                      (if (empty? (:logo-url org-editing))
                        (iu/upload! {:accept "image/*"}
                          (fn [res]
                            (let [url (gobj/get res "url")
                                  img (gdom/createDom "img")]
                              (set! (.-onload img) (fn []
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
          (org-avatar org-editing false false true)
          [:div.add-picture-link
            (if (empty? (:logo-url org-editing))
              "Upload logo"
              "Delete logo")]
          [:div.add-picture-link-subtitle
            "A transparent background PNG works best"]]
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
                          (drv/drv :orgs)
                          (rum/local false ::saving)
                          (rum/local nil ::temp-user-avatar)
                          {:will-mount (fn [s]
                                         (reset! (::temp-user-avatar s) (utils/cdn default-avatar-url true))
                                         (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                         s)
                           :will-update (fn [s]
                                         (when (and @(::saving s)
                                                    (not (:loading (:user-data @(drv/get-ref s :edit-user-profile))))
                                                    (not (:error @(drv/get-ref s :edit-user-profile))))
                                            (let [orgs @(drv/get-ref s :orgs)]
                                              (if (pos? (count orgs))
                                                (utils/after 100 #(router/nav! (oc-urls/org (:slug (first orgs)))))
                                                (utils/after 100 #(router/nav! oc-urls/slack-lander-team)))))
                                         s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        user-data (:user-data edit-user-profile)
        temp-user-avatar @(::temp-user-avatar s)
        fixed-user-data (if (empty? (:avatar-url user-data))
                          (assoc user-data :avatar-url temp-user-avatar)
                          user-data)]
    [:div.onboard-lander
      [:div.steps.two-steps
        [:div.step-1
          "Get Started"]
        [:div.step-progress-bar]
        [:div.step-2
          "Your Team"]]
      [:div.main-cta
        [:div.title.about-yourself
          "Tell us a bit about yourself..."]
        [:div.subtitle
          "This information will be visible to your team"]]
      [:div.onboard-form
        [:div.logo-upload-container
          {:on-click (fn []
                      (when (not= (:avatar-url user-data) temp-user-avatar)
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] temp-user-avatar]))
                      (iu/upload! {:accept "image/*"
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                        nil
                        (fn [_])
                        nil))}
          (user-avatar-image fixed-user-data)
          [:div.add-picture-link
            "Change photo"]
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
                        (cook/set-cookie! (router/should-show-dashboard-tooltips (:user-id user-data)) true (* 60 60 24 7))
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
                                                            (nil? (:logo-url org-editing))
                                                            (seq teams-data))
                                                   (dis/dispatch! [:input [:org-editing] (select-keys (first teams-data) [:name :logo-url :logo-width :logo-height])])))
                                               s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        _ (drv/react s :teams-load)
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
        [:div.logo-upload-container.org-logo
          {:on-click (fn [_]
                      (if (empty? (:logo-url org-editing))
                        (iu/upload! {:accept "image/*"}
                          (fn [res]
                            (let [url (gobj/get res "url")
                                  img (gdom/createDom "img")]
                              (set! (.-onload img) (fn []
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
          (org-avatar org-editing false false true)
          [:div.add-picture-link
            (if (empty? (:logo-url org-editing))
              "Upload logo"
              "Delete logo")]
          [:div.add-picture-link-subtitle
            "A transparent background PNG works best"]]
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

(rum/defcs invitee-lander < rum/reactive
                            (drv/drv :confirm-invitation)
                            (rum/local false ::password-error)
  [s]
  (let [confirm-invitation (drv/react s :confirm-invitation)
        jwt (:jwt confirm-invitation)
        collect-pswd (:collect-pswd confirm-invitation)
        collect-pswd-error (:collect-pswd-error confirm-invitation)
        invitation-confirmed (:invitation-confirmed confirm-invitation)]
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
          "Signing up as " [:span.email-address (:email jwt)]]]
      [:div.onboard-form
        [:div.field-label
          "Password"
          (when collect-pswd-error
            [:span.error "An error occurred, please try again."])
          (when @(::password-error s)
            [:span.error "Minimum 8 characters"])]
        [:input.field
          {:type "password"
           :class (when collect-pswd-error "error")
           :value (or (:pswd collect-pswd) "")
           :on-change #(do
                         (reset! (::password-error s) false)
                         (dis/dispatch! [:input [:collect-pswd :pswd] (.. % -target -value)]))
           :placeholder "Minimum 8 characters"
           :pattern ".{8,}"}]
        [:div.description
          "By signing up you are agreeing to our "
          [:a
            "terms of service"]
          " and "
          [:a
            "privacy policy"]
          "."]
        [:button.continue
          {:class (when (< (count (:pswd collect-pswd)) 8) "disabled")
           :on-click #(if (< (count (:pswd collect-pswd)) 8)
                        (reset! (::password-error s) true)
                        (dis/dispatch! [:pswd-collect]))}
          "Continue"]]]))

(rum/defcs invitee-lander-profile < rum/reactive
                                    (drv/drv :edit-user-profile)
                                    (drv/drv :orgs)
                                    (rum/local false ::saving)
                                    (rum/local nil ::temp-user-avatar)
                                    {:will-mount (fn [s]
                                                   (reset! (::temp-user-avatar s) (utils/cdn default-avatar-url true))
                                                    (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                                  s)
                                     :will-update (fn [s]
                                                    (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
                                                          orgs @(drv/get-ref s :orgs)]
                                                      (when (and @(::saving s)
                                                                 (not (:loading (:user-data edit-user-profile)))
                                                                 (not (:error edit-user-profile)))
                                                        (utils/after 100 #(router/nav! (oc-urls/org (:slug (first orgs)))))))
                                                    s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        user-data (:user-data edit-user-profile)
        temp-user-avatar @(::temp-user-avatar s)
        fixed-user-data (if (empty? (:avatar-url user-data))
                          (assoc user-data :avatar-url temp-user-avatar)
                          user-data)]
    [:div.onboard-lander.second-step
      [:div.steps.two-steps
        [:div.step-1
          "Get Started"]
        [:div.step-progress-bar]
        [:div.step-2
          "Your Profile"]]
      [:div.main-cta
        [:div.title.about-yourself
          "Tell us a bit about yourself..."]
        [:div.subtitle
          "This information will be visible to your team"]
        (when (:error edit-user-profile)
            [:div.subtitle.error
              "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:div.logo-upload-container
          {:on-click (fn []
                      (when (not= (:avatar-url user-data) temp-user-avatar)
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] temp-user-avatar]))
                      (iu/upload! {:accept "image/*"
                                   :transformations {
                                     :crop {
                                       :aspectRatio 1}}}
                        (fn [res]
                          (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                        nil
                        (fn [_])
                        nil))}
          (user-avatar-image fixed-user-data)
          [:div.add-picture-link
            "Change photo"]
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
        (if (seq email)
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
                            (drv/drv :orgs)
                            (rum/local false ::exchange-started)
                            (vertical-center-mixin ".onboard-email-container")
                            {:will-mount (fn [s]
                                           (exchange-token-when-ready s)
                                           s)
                             :did-mount (fn [s]
                                          (dots-animation s)
                                          (exchange-token-when-ready s)
                                          s)
                             :did-update (fn [s]
                                          (exchange-token-when-ready s)
                                          s)}
  [s]
  (let [email-verification (drv/react s :email-verification)
        orgs (drv/react s :orgs)]
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
          {:on-click #(router/nav!
                        (let [org (utils/get-default-org orgs)]
                          (if org
                            (oc-urls/org (:slug org))
                            oc-urls/login)))}
          "Get Started"]]
      :else
      [:div.onboard-email-container.small.dot-animation
        "Verifying, please wait" [:span.dots {:ref :dots} "."]])))

(defn exchange-pswd-reset-token-when-ready [s]
  (when-let [auth-settings (:auth-settings @(drv/get-ref s :password-reset))]
    (when (and (not @(::exchange-started s))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (reset! (::exchange-started s) true)
      (dis/dispatch! [:auth-with-token :password-reset]))))

(rum/defcs password-reset-lander < rum/reactive
                                   (drv/drv :password-reset)
                                   (rum/local false ::exchange-started)
                                   (vertical-center-mixin ".onboard-email-container")
                                   {:will-mount (fn [s]
                                                  (exchange-pswd-reset-token-when-ready s)
                                                  s)
                                    :did-mount (fn [s]
                                                 (dots-animation s)
                                                 (exchange-pswd-reset-token-when-ready s)
                                                 s)
                                    :did-update (fn [s]
                                                 (exchange-pswd-reset-token-when-ready s)
                                                 s)}
  [s]
  (let [password-reset (drv/react s :password-reset)]
    (cond
      (= (:error password-reset) 401)
      [:div.onboard-email-container.error
        "This link is not valid, please try again."]
      (:error password-reset)
      [:div.onboard-email-container.error
        "An error occurred, please try again."]
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
    :password-reset-lander (password-reset-lander)
    [:div]))

(rum/defc onboard-wrapper < rum/static
  [component]
  [:div.onboard-wrapper-container
    [:div.onboard-wrapper
      {:class (str "onboard-" (name component))}
      [:div.onboard-wrapper-left
        [:div.onboard-wrapper-logo]
        [:div.onboard-wrapper-box]]
      [:div.onboard-wrapper-right
        (get-component component)]]])