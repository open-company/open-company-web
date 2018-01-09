(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
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

(defn- clean-org-name [org-name]
  (string/trim org-name))

(defn- delay-focus-field-with-ref
  "Given a Rum state and a ref, async focus the filed if it exists."
  [s r]
  (utils/after 2500
   #(when-let [field (rum/ref-node s r)]
     (.focus field))))

(rum/defcs lander < rum/static
                          rum/reactive
                          (drv/drv :signup-with-email)
                          (rum/local false ::email-error)
                          (rum/local false ::password-error)
                          {:will-mount (fn [s]
                            (let [signup-with-email @(drv/get-ref s :signup-with-email)]
                              (when-not (contains? signup-with-email :email)
                                (dis/dispatch!
                                 [:input
                                  [:signup-with-email]
                                  {:email ""
                                   :pswd ""
                                   :first-name ""
                                   :last-name ""}])))
                            s)}
  [s]
  (let [signup-with-email (drv/react s :signup-with-email)]
    [:div.onboard-lander.lander
      [:div.main-cta
        [:div.title.main-lander
          "Welcome!"]]
      [:div.onboard-form
        [:button.mlb-reset.signup-with-slack
          {:on-click #(do
                       (.preventDefault %)
                       (when (:auth-settings @dis/app-state)
                         (dis/dispatch! [:login-with-slack])))}
          [:div.signup-with-slack-content
            "Sign Up with "
            [:div.slack-blue-icon]]]
        [:div.or-with-email
          [:div.or-with-email-line]
          [:div.or-with-email-copy
            "Or, sign up with email"]]
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
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
              {:href oc-urls/terms}
              "terms of service"]
            " and "
            [:a
              {:href oc-urls/privacy}
              "privacy policy"]
            "."]
          [:button.continue
            {:class (when (or (not (utils/valid-email? (:email signup-with-email)))
                              (<= (count (:pswd signup-with-email)) 7))
                      "disabled")
             :on-click #(if (or (not (utils/valid-email? (:email signup-with-email)))
                                (<= (count (:pswd signup-with-email)) 7))
                          (do
                            (when (not (utils/valid-email? (:email signup-with-email)))
                              (reset! (::email-error s) true))
                            (when (<= (count (:pswd signup-with-email)) 7)
                              (reset! (::password-error s) true)))
                          (dis/dispatch! [:signup-with-email]))}
            "Continue"]]
        [:div.footer-link
          "Already have an account?"
          [:a {:href oc-urls/login} "Login here"]]]]))

(rum/defcs lander-profile < rum/reactive
                                  (drv/drv :edit-user-profile)
                                  (drv/drv :orgs)
                                  (rum/local false ::saving)
                                  (rum/local nil ::temp-user-avatar)
                                  {:will-mount (fn [s]
                                    (reset! (::temp-user-avatar s) (utils/cdn default-avatar-url true))
                                    (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                    s)
                                   :did-mount (fn [s]
                                    (delay-focus-field-with-ref s "first-name")
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
                          user-data)
        orgs (drv/react s :orgs)]
    [:div.onboard-lander.lander-profile
      [:div.main-cta
        [:div.title.about-yourself
          "Tell us a bit about yourself…"]
        (when (:error edit-user-profile)
          [:div.subtitle.error
            "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
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
              "+ Upload a profile photo"]
            [:div.add-picture-link-subtitle
              "A 160x160 PNG or JPG works best"]]
          [:div.field-label
            "First name"]
          [:input.field
            {:type "text"
             :ref "first-name"
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
            "That’s me"]]]]))

(defn- setup-team-data
  ""
  [s]
  (let [org-editing @(drv/get-ref s :org-editing)
        teams-data @(drv/get-ref s :teams-data)
        teams-load @(drv/get-ref s :teams-load)]
    (when (and (zero? (count (:name org-editing)))
               (zero? (count (:logo-url org-editing)))
               (seq teams-data))
      (let [first-team (select-keys
                        (first teams-data)
                        [:name :logo-url :logo-width :logo-height])]
        (dis/dispatch!
         [:input
          [:org-editing]
          first-team])
        (when-not (:logo-height first-team)
          (let [img (gdom/createDom "img")]
            (set! (.-onload img)
             #(do
               (dis/dispatch!
                [:input
                 [:org-editing]
                 (merge @(drv/get-ref s :org-editing)
                  {:logo-width (.-width img)
                   :logo-height (.-height img)})])
               (gdom/removeNode img)))
            (gdom/append (.-body js/document) img)
            (set! (.-src img) (:logo-url first-team))))))))

(rum/defcs lander-team < rum/reactive
                         (drv/drv :teams-load)
                         (drv/drv :teams-data)
                         (drv/drv :org-editing)
                         (rum/local false ::saving)
                         {:will-mount (fn [s]
                           (dis/dispatch! [:input [:org-editing :name] ""])
                           s)
                          :did-mount (fn [s]
                           (setup-team-data s)
                           (delay-focus-field-with-ref s "org-name")
                           s)
                          :will-update (fn [s]
                           (let [teams-data @(drv/get-ref s :teams-data)
                                 teams-load @(drv/get-ref s :teams-load)]
                             ;; Load the list of teams if it's not already
                             (when (and (empty? teams-data)
                                        (:auth-settings teams-load)
                                        (not (:teams-data-requested teams-load)))
                               (dis/dispatch! [:teams-get]))
                             ;; If the team is loaded setup the form
                             (setup-team-data s))
                           s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        _ (drv/react s :teams-load)
        org-editing (drv/react s :org-editing)]
    [:div.onboard-lander.lander-team
      [:div.main-cta
        [:div.title.company-setup
          "Your team…"]]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.logo-upload-container.org-logo.group
            {:on-click (fn [_]
                        (if (empty? (:logo-url org-editing))
                          (iu/upload! {:accept "image/*"}
                            (fn [res]
                              (let [url (gobj/get res "url")
                                    img (gdom/createDom "img")]
                                (set! (.-onload img) (fn []
                                                        (dis/dispatch!
                                                         [:input
                                                          [:org-editing]
                                                          (merge
                                                           org-editing
                                                           {:logo-url url
                                                            :logo-width (.-width img)
                                                            :logo-height (.-height img)})])
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
            (org-avatar org-editing false :never)
            [:div.add-picture-link
              (if (empty? (:logo-url org-editing))
                "+ Upload your company logo"
                "+ Change your company logo")]
            [:div.add-picture-link-subtitle
              "A transparent background PNG works best"]]
          [:div.field-label
            "Team name"
            (when (:error org-editing)
              [:span.error "Must be at least 3 characters"])]
          [:input.field
            {:type "text"
             :ref "org-name"
             :class (when (:error org-editing) "error")
             :value (:name org-editing)
             :on-change #(dis/dispatch! [:input [:org-editing]
                          (merge org-editing {:error nil :name (.. % -target -value)})])}]
          [:button.continue
            {:class (when (< (count (clean-org-name (:name org-editing))) 3) "disabled")
             :on-click #(let [org-name (clean-org-name (:name org-editing))]
                          (dis/dispatch! [:input [:org-editing :name] org-name])
                          (if (and (seq org-name)
                                   (> (count org-name) 2))
                           ;; Create org and show setup screen
                           (dis/dispatch! [:org-create])
                           (dis/dispatch! [:input [:org-editing :error] true])))}
            "All set!"]]]]))

(rum/defcs invitee-lander < rum/reactive
                            (drv/drv :confirm-invitation)
                            (rum/local false ::password-error)
                            {:did-mount (fn [s]
                              (delay-focus-field-with-ref s "password")
                              s)}
  [s]
  (let [confirm-invitation (drv/react s :confirm-invitation)
        jwt (:jwt confirm-invitation)
        collect-pswd (:collect-pswd confirm-invitation)
        collect-pswd-error (:collect-pswd-error confirm-invitation)
        invitation-confirmed (:invitation-confirmed confirm-invitation)]
    [:div.onboard-lander.invitee-lander
      [:div.main-cta
        [:div.title
          "Join your team on Carrot"]
        [:div.subtitle
          "Signing up as " [:span.email-address (:email jwt)]]]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
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
             :ref "password"
             :on-change #(do
                           (reset! (::password-error s) false)
                           (dis/dispatch! [:input [:collect-pswd :pswd] (.. % -target -value)]))
             :placeholder "Minimum 8 characters"
             :pattern ".{8,}"}]
          [:div.description
            "By signing up you are agreeing to our "
            [:a
              {:href oc-urls/terms}
              "terms of service"]
            " and "
            [:a
              {:href oc-urls/privacy}
              "privacy policy"]
            "."]
          [:button.continue
            {:class (when (< (count (:pswd collect-pswd)) 8) "disabled")
             :on-click #(if (< (count (:pswd collect-pswd)) 8)
                          (reset! (::password-error s) true)
                          (dis/dispatch! [:pswd-collect]))}
            "Continue"]]]]))

(rum/defcs invitee-lander-profile < rum/reactive
                                    (drv/drv :edit-user-profile)
                                    (drv/drv :orgs)
                                    (rum/local false ::saving)
                                    (rum/local nil ::temp-user-avatar)
                                    {:will-mount (fn [s]
                                      (reset! (::temp-user-avatar s) (utils/cdn default-avatar-url true))
                                       (utils/after 100 #(dis/dispatch! [:user-profile-reset]))
                                      s)
                                     :did-mount (fn [s]
                                      (delay-focus-field-with-ref s "first-name")
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
    [:div.onboard-lander.invitee-lander-profile
      [:div.main-cta
        [:div.title.about-yourself
          "Tell us a bit about yourself…"]
        [:div.subtitle
          "This information will be visible to your team"]
        (when (:error edit-user-profile)
            [:div.subtitle.error
              "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
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
              "+ Upload a profile photo"]
            [:div.add-picture-link-subtitle
              "A 160x160 PNG or JPG works best"]]
          [:div.field-label
            "First name"]
          [:input.field
            {:type "text"
             :ref "first-name"
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
            "That’s me"]]]]))

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
          {:on-click #(let [org (utils/get-default-org orgs)]
                        (if org
                          (if (and (empty? (jwt/get-key :first-name))
                                   (empty? (jwt/get-key :last-name)))
                            (do
                              (cook/set-cookie!
                               (router/show-nux-cookie (jwt/user-id))
                               (:new-user router/nux-cookie-values)
                               (* 60 60 24 7))
                              (router/nav! oc-urls/confirm-invitation-profile))
                            (router/nav! (oc-urls/org (:slug org))))
                          (router/nav! oc-urls/login)))}
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
    :lander (lander)
    :lander-profile (lander-profile)
    :lander-team (lander-team)
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