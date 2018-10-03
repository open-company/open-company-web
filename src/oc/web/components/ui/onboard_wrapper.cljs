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
            [oc.web.utils.ui :as ui-utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
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
                    (drv/drv user-store/signup-with-email)
                    (drv/drv :auth-settings)
                    (rum/local false ::email-error)
                    (rum/local false ::password-error)
                    (rum/local "" ::email)
                    (rum/local "" ::pswd)
                    {:will-mount (fn [s]
                      (user-actions/signup-with-email-reset-errors)
                      s)}
  [s]
  (let [signup-with-email (drv/react s user-store/signup-with-email)
        auth-settings (drv/react s :auth-settings)]
    [:div.onboard-lander.lander
      [:div.main-cta
        [:div.mobile-header
          [:button.mlb-reset.top-back-button
            {:on-touch-start identity
             :on-click #(router/history-back!)
             :aria-label "Back"}]
          [:div.mobile-logo]]
        [:div.title.main-lander
          "Welcome to Carrot"]]
      [:div.onboard-form
        [:button.mlb-reset.signup-with-slack
          {:on-touch-start identity
           :on-click #(do
                       (.preventDefault %)
                       (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                             {:auth-source "slack"})]
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
                         (user-actions/login-with-google auth-link)))}
          [:div.signup-with-google-content
            [:div.google-icon
              {:aria-label "google"}]
            "Continue with Google "]]
        [:div.or-with-email
          [:div.or-with-email-copy
            "Or, sign up with email"]]
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.field-label.email-field
            "Work email"
            (cond
              (= (:error signup-with-email) 409)
              [:span.error "Email already exists"]
              @(::email-error s)
              [:span.error "Email is not valid"])]
          [:input.field.fs-hide
            {:type "email"
             :class (when (= (:error signup-with-email) 409) "error")
             :pattern "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
             :value @(::email s)
             :on-change #(let [v (.. % -target -value)]
                           (reset! (::password-error s) false)
                           (reset! (::email-error s) false)
                           (reset! (::email s) v))}]
          [:div.field-label
            "Password"
            (when @(::password-error s)
              [:span.error
                "Minimum 8 characters"])]
          [:input.field
            {:type "password"
             :pattern ".{8,}"
             :value @(::pswd s)
             :placeholder "Minimum 8 characters"
             :on-change #(let [v (.. % -target -value)]
                           (reset! (::password-error s) false)
                           (reset! (::email-error s) false)
                           (reset! (::pswd s) v))}]
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
            {:class (when (or (not (utils/valid-email? @(::email s)))
                              (<= (count @(::pswd s)) 7))
                      "disabled")
             :on-touch-start identity
             :on-click #(if (or (not (utils/valid-email? @(::email s)))
                                (<= (count @(::pswd s)) 7))
                          (do
                            (when-not (utils/valid-email? @(::email s))
                              (reset! (::email-error s) true))
                            (when (<= (count @(::pswd s)) 7)
                              (reset! (::password-error s) true)))
                          (user-actions/signup-with-email {:email @(::email s) :pswd @(::pswd s)}))}
            "Sign up"]]
        [:div.footer-link
          "Already have an account?"
          [:a {:href oc-urls/login} "Login here"]]]]))

(defn- profile-setup-team-data
  ""
  [s & [setup-email-domain]]
  ;; Load the list of teams if it's not already
  (team-actions/teams-get-if-needed)
  (let [org-editing @(drv/get-ref s :org-editing)
        teams-data @(drv/get-ref s :teams-data)]
    (if (and (zero? (count (:name org-editing)))
             (seq teams-data))
      (let [first-team (select-keys
                        (first teams-data)
                        [:name])]
        (dis/dispatch!
         [:update
          [:org-editing]
          #(merge % first-team)])))))

(rum/defcs lander-profile < rum/reactive
                                  (drv/drv :edit-user-profile)
                                  (drv/drv :current-user-data)
                                  (drv/drv :teams-data)
                                  (drv/drv :org-editing)
                                  (rum/local false ::saving)
                                  {:will-mount (fn [s]
                                    (dis/dispatch! [:input [:org-editing :name] ""])
                                    (user-actions/user-profile-reset)
                                    s)
                                   :did-mount (fn [s]
                                    (profile-setup-team-data s true)
                                    (delay-focus-field-with-ref s "first-name")
                                    s)
                                   :will-update (fn [s]
                                    (when (and @(::saving s)
                                               (:updated-jwt (:user-data @(drv/get-ref s :edit-user-profile))))
                                      (if (:error @(drv/get-ref s :edit-user-profile))
                                        (reset! (::saving s) false)
                                        (org-actions/create-or-update-org @(drv/get-ref s :org-editing))))
                                   s)}
  [s]
  (let [edit-user-profile (drv/react s :edit-user-profile)
        current-user-data (drv/react s :current-user-data)
        teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)
        user-data (:user-data edit-user-profile)
        continue-disabled (or @(::saving s)
                              (and (empty? (:first-name user-data))
                                        (empty? (:last-name user-data)))
                              (<= (count (clean-org-name (:name org-editing))) 1))
        continue-fn #(when-not continue-disabled
                       (reset! (::saving s) true)
                       (user-actions/user-profile-save current-user-data edit-user-profile)
                       (let [org-name (clean-org-name (:name org-editing))]
                         (dis/dispatch! [:input [:org-editing :name] org-name])))]
    [:div.onboard-lander.lander-profile
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]
          [:button.mlb-reset.top-continue
           {:class (when continue-disabled "disabled")
            :on-touch-start identity
            :on-click continue-fn
            :aria-label "Continue"}
            "Continue"]]
        [:div.title.about-yourself
          "Tell us about you"]
        [:div.steps.first-step
          [:div.step]
          [:div.step]
          [:div.step]]
        [:div.steps-separator]]
      (when (:error edit-user-profile)
        [:div.subtitle.error
          "An error occurred while saving your data, please try again"])
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.field-label
            "First name"]
          [:input.field.fs-hide
            {:type "text"
             :ref "first-name"
             :placeholder "First name"
             :value (or (:first-name user-data) "")
             :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
          [:div.field-label
            "Last name"]
          [:input.field.fs-hide
            {:type "text"
             :placeholder "Last name"
             :value (or (:last-name user-data) "")
             :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
          [:div.field-label
            "Company name"
            (when (:error org-editing)
              [:span.error "Must be at least 3 characters"])]
          [:input.field.fs-hide
            {:type "text"
             :ref "org-name"
             :placeholder "e.g., Acme, or Acme Design"
             :class (when (:error org-editing) "error")
             :value (:name org-editing)
             :on-change #(dis/dispatch! [:input [:org-editing]
               (merge org-editing {:error nil :name (.. % -target -value)})])}]
          [:button.continue
            {:class (when continue-disabled "disabled")
             :on-touch-start identity
             :on-click continue-fn}
            "Create team"]
          [:div.logout-cancel
            "Need to start over? "
            [:button.mlb-reset.logout-cancel
              {:on-click #(user-actions/logout oc-urls/sign-up)}
              "Cancel sign up"]]]]]))

(defn- setup-team-data
  ""
  [s & [setup-email-domain]]
  ;; Load the list of teams if it's not already
  (team-actions/teams-get-if-needed)
  (let [org-editing @(drv/get-ref s :org-editing)
        teams-data @(drv/get-ref s :teams-data)
        google-domain (jwt/get-key :google-domain)
        with-email-domain (if (and setup-email-domain
                                   google-domain
                                   (clojure.string/blank? (:email-domain org-editing)))
                            {:email-domain google-domain}
                            {:email-domain (or (:email-domain org-editing) "")})]
    (if (and (zero? (count (:name org-editing)))
             (zero? (count (:logo-url org-editing)))
             (seq teams-data))
      (let [first-team (select-keys
                        (first teams-data)
                        [:name :logo-url :logo-width :logo-height])
            fixed-first-team (merge first-team with-email-domain)]
        (dis/dispatch!
         [:update
          [:org-editing]
          #(merge % fixed-first-team)])
        (when (and (not (zero? (count (:logo-url first-team))))
                   (not (:logo-height first-team)))
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
            (set! (.-src img) (:logo-url first-team)))))
      (dis/dispatch!
       [:update
        [:org-editing]
        #(merge % with-email-domain)]))))

(rum/defcs lander-team < rum/reactive
                         (drv/drv :teams-data)
                         (drv/drv :org-editing)
                         (rum/local false ::saving)
                         {:will-mount (fn [s]
                           (dis/dispatch! [:input [:org-editing :name] ""])
                           (dis/dispatch! [:input [:org-editing :email-domain] ""])
                           s)
                          :did-mount (fn [s]
                           (setup-team-data s true)
                           (delay-focus-field-with-ref s "org-name")
                           s)
                          :will-update (fn [s]
                           (setup-team-data s)
                           s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)
        is-mobile? (responsive/is-tablet-or-mobile?)
        continue-disabled (or
                           (< (count (clean-org-name (:name org-editing))) 3)
                           (:domain-error org-editing))
        continue-fn #(when-not continue-disabled
                       (let [org-name (clean-org-name (:name org-editing))]
                         (dis/dispatch! [:input [:org-editing :name] org-name])
                         (if (and (seq org-name)
                                  (> (count org-name) 2))
                           ;; Create org and show setup screen
                           (org-actions/create-or-update-org @(drv/get-ref s :org-editing))
                           (dis/dispatch! [:input [:org-editing :error] true]))))]
    [:div.onboard-lander.lander-team
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]]
        [:div.title.company-setup
          "Set up your company"]]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          (when-not is-mobile?
            [:div.logo-upload-container.org-logo.group.fs-hide
              {:on-click (fn [_]
                          (if (empty? (:logo-url org-editing))
                            (iu/upload! org-utils/org-avatar-filestack-config
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
                  "Add company logo"
                  "Change company logo")]
              [:div.add-picture-link-subtitle
                "A 160x160 transparent Gif or PNG works best."]])
          [:div.field-label
            "Company name"
            (when (:error org-editing)
              [:span.error "Must be at least 3 characters"])]
          [:input.field.fs-hide
            {:type "text"
             :ref "org-name"
             :class (when (:error org-editing) "error")
             :value (:name org-editing)
             :on-change #(dis/dispatch! [:input [:org-editing]
               (merge org-editing {:error nil :name (.. % -target -value)})])}]
                 ;; Email domains row
          [:div.org-email-domains-row.group
            [:div.field-label
              "Allowed email domain " [:span.info "(optional)"]
              (when (:error org-editing)
                [:label.error
                   "Only company email domains are allowed."])]
            [:div.org-email-domain-field
              {:class (when (:domain-error org-editing) "error")}
              [:input.um-invite-field.email
                {:name "um-domain-invite"
                 :ref "um-domain-invite"
                 :type "text"
                 :auto-capitalize "none"
                 :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
                 :value (:email-domain org-editing)
                 :on-change #(let [domain (.. % -target -value)]
                               (dis/dispatch! [:input [:org-editing :email-domain] domain])
                               (dis/dispatch! [:input [:org-editing :domain-error] (and (seq domain)
                                                                                        (not (utils/valid-domain? domain)))]))
                 :placeholder "Domain, e.g. acme.com"}]]
            [:div.field-label.info "Anyone with this email domain can automatically join your team."]]
          [:button.continue
            {:class (when continue-disabled "disabled")
             :on-touch-start identity
             :on-click continue-fn}
            "Continue"]]]]))

(rum/defcs lander-sections < rum/reactive
                             (drv/drv :org-data)
                             (drv/drv :sections-setup)
                             (rum/local false ::patching-sections)
  [s]
  (let [sections-list (drv/react s :sections-setup)
        org-data (drv/react s :org-data)
        disabled @(::patching-sections s)
        continue-fn (fn []
                      (reset! (::patching-sections s) true)
                      ;; refresh user api data after org creation
                      (user-actions/entry-point-get (:slug org-data))
                      (org-actions/update-org-sections (:slug org-data) sections-list))]
    [:div.onboard-lander.lander-sections
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]
          [:button.mlb-reset.top-continue
            {:on-touch-start identity
             :on-click continue-fn
             :disabled disabled
             :aria-label "Start using Carrot"}
           "Start"]]
        [:div.title
          "Start with a few sections"]
        [:div.subtitle
          "What types of communication are shared with your team?"]
        [:div.steps.third-step
          [:div.step]
          [:div.step]
          [:div.step]]
        [:div.steps-separator]]
      [:div.onboard-form
        [:div.sections-list
          (if (seq sections-list)
            (for [idx (range (count sections-list))
                  :let [section (get sections-list idx)]]
              [:div.section
                {:key (str "sections-list-" (:name section))
                 :class (when (:selected section) "selected")
                 :on-click #(dis/dispatch! [:update [:sections-setup idx :selected] not])}
                (:name section)])
            (small-loading))]
        [:button.continue.start-using-carrot
          {:on-touch-start identity
           :on-click continue-fn
           :disabled disabled}
          "✨ Start using Carrot ✨"]]]))

(def default-invite-row
  {:user ""
   :type "email"
   :role :author
   :error false})

(defn- check-invite-row [invite]
  (assoc invite :error (and (seq (:user invite)) (not (utils/valid-email? (:user invite))))))

(defn- check-invites [s]
  (reset! (::invite-rows s) (vec (map check-invite-row @(::invite-rows s)))))

(def default-invite-note
  (str
    "Hey there, let's explore Carrot together. It's a place to make sure important "
    "announcements, updates, and decisions don't get lost in the noise."))

(rum/defcs lander-invite < rum/reactive
                           (drv/drv :org-data)
                           (drv/drv :invite-users)
                           (rum/local false ::inviting)
                           (rum/local nil ::invite-error)
                           (rum/local (rand 3) ::invite-rand)
                           (rum/local [] ::invite-rows)
                           (rum/local default-invite-note ::invite-note)
                           {:will-mount (fn [s]
                             (let [rows (if (responsive/is-tablet-or-mobile?) 2 3)]
                               (reset! (::invite-rows s) (vec (repeat rows default-invite-row))))
                             s)
                            :did-mount (fn [s]
                             ;; Load the list of teams if it's not already
                             (team-actions/teams-get-if-needed)
                             s)
                            :will-update (fn [s]
                             ;; Load the list of teams if it's not already
                             (team-actions/teams-get-if-needed)
                             (when @(::inviting s)
                               (let [invite-users @(drv/get-ref s :invite-users)
                                     invite-errors (filter :error invite-users)
                                     to-send (filter #(not (:error %)) invite-users)]
                                 (when (zero? (count to-send))
                                   (reset! (::inviting s) false)
                                   (if (pos? (count invite-errors))
                                     ;; There were errors inviting users, show them and let the user retry
                                     (do
                                       (reset! (::invite-rand s) (rand 3))
                                       (reset! (::invite-error s) "An error occurred inviting the following users, please try again.")
                                       (reset! (::invite-rows s) (vec invite-errors)))
                                     ;; All invites sent, redirect to dashboard
                                     (org-actions/signup-invite-completed @(drv/get-ref s :org-data))))))
                             s)}
  [s]
  (let [_ (drv/react s :invite-users)
        org-data (drv/react s :org-data)
        valid-rows (filter #(and (seq (:user %))
                                 (not (:error %))) @(::invite-rows s))
        error-rows (filter #(and (seq (:user %))
                                 (:error %)) @(::invite-rows s))
        continue-fn (fn []
                     (let [_ (check-invites s)
                           errors (filter :error @(::invite-rows s))]
                       (when (zero? (count errors))
                         (reset! (::inviting s) true)
                         (reset! (::invite-error s) nil)
                         (let [not-empty-invites (filter #(seq (:user %)) @(::invite-rows s))]
                           (team-actions/invite-users not-empty-invites "")))))
        continue-disabled (not (zero? (count error-rows)))]
    [:div.onboard-lander.lander-invite
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]]
        [:div.title
          "Invite your team"]
        [:div.subtitle
          "Invite some colleagues to explore Carrot with you."]
        [:div.steps.second-step
          [:div.step]
          [:div.step]
          [:div.step]]
        [:div.steps-separator]]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.field-label.invite-teammates
            "Invite teammates " [:span.info "(optional)"]
            [:button.mlb-reset.add-another-invite-row
              {:on-click #(reset! (::invite-rows s) (vec (conj @(::invite-rows s) default-invite-row)))}
              "+ add more"]]
          (when @(::invite-error s)
            [:div.error @(::invite-error s)])
          [:div.invite-rows.fs-hide
            (for [idx (range (count @(::invite-rows s)))
                  :let [invite (get @(::invite-rows s) idx)]]
              [:div.invite-row
                {:class (when (:error invite) "error")
                 :key (str "invite-row-" @(::invite-rand s) "-" idx)}
                [:input
                  {:type "text"
                   :placeholder "name@example.com"
                   :on-change (fn [e]
                               (reset! (::invite-rows s)
                                (vec
                                 (assoc-in @(::invite-rows s) [idx]
                                  (assoc invite :user (.. e -target -value)))))
                               (check-invites s))
                   :value (:user invite)}]])]
          [:button.continue
            {:on-touch-start identity
             :on-click continue-fn
             :class (when (or @(::inviting s)
                              continue-disabled) "disabled")}
            "Invite team"]
          [:button.mlb-reset.skip-for-now
            {:on-click #(org-actions/signup-invite-completed org-data)}
            "Skip for now"]]]]))

(defn dots-animation [s]
  (when-let [dots-node (rum/ref-node s :dots)]
    (let [dots (.-innerText dots-node)
          next-dots (case dots
                      "." ".."
                      ".." "..."
                      ".")]
      (set! (.-innerText dots-node) next-dots)
      (utils/after 800 #(dots-animation s)))))

(defn confirm-invitation-when-ready [s]
  (let [confirm-invitation @(drv/get-ref s :confirm-invitation)]
    (when (and (:auth-settings confirm-invitation)
               (not @(::exchange-started s)))
      (reset! (::exchange-started s) true)
      (user-actions/confirm-invitation (:token confirm-invitation)))))

(rum/defcs invitee-lander < rum/reactive
                            (drv/drv :confirm-invitation)
                            (rum/local false ::exchange-started)
                            {:will-mount (fn [s]
                              (confirm-invitation-when-ready s)
                              s)
                             :did-mount (fn [s]
                              (dots-animation s)
                              (confirm-invitation-when-ready s)
                              s)
                             :did-update (fn [s]
                              (confirm-invitation-when-ready s)
                              s)}
  [s]
  (let [confirm-invitation (drv/react s :confirm-invitation)]
    [:div.onboard-lander.invitee-lander
      [:div.main-cta
        [:div.title
          "Join your team on Carrot"]
        [:div.mobile-logo.mobile-only]
        (if (:invitation-error confirm-invitation)
          [:div.subtitle
            "An error occurred while confirming your invitation, please try again."]
          [:div.subtitle
            "Please wait" [:span.dots {:ref :dots} "."]])]]))

(rum/defcs invitee-lander-password < rum/reactive
                                     (drv/drv :collect-password)
                                     (rum/local false ::password-error)
                                     {:did-mount (fn [s]
                                       (delay-focus-field-with-ref s "password")
                                       s)}
  [s]
  (let [collect-password (drv/react s :collect-password)
        jwt (:jwt collect-password)
        collect-pswd (:collect-pswd collect-password)
        collect-pswd-error (:collect-pswd-error collect-password)
        invitation-confirmed (:invitation-confirmed collect-password)]
    [:div.onboard-lander.invitee-lander-password
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]]
        [:div.title
          "Join your team on Carrot"]
        [:div.subtitle
          "Signing up as " [:span.email-address.fs-hide (:email jwt)]]]
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
                          (user-actions/pswd-collect collect-pswd false))
             :on-touch-start identity}
            "Continue"]]]]))

(rum/defcs invitee-lander-profile < rum/reactive
                                    (drv/drv :edit-user-profile)
                                    (drv/drv :current-user-data)
                                    (drv/drv :orgs)
                                    (rum/local false ::saving)
                                    (rum/local nil ::temp-user-avatar)
                                    {:will-mount (fn [s]
                                      (user-actions/user-profile-reset)
                                      (let [avatar-with-cdn (:avatar-url (:user-data @(drv/get-ref s :edit-user-profile)))]
                                        (reset! (::temp-user-avatar s) avatar-with-cdn))
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
        current-user-data (drv/react s :current-user-data)
        user-data (:user-data edit-user-profile)
        temp-user-avatar @(::temp-user-avatar s)
        fixed-user-data (if (empty? (:avatar-url user-data))
                          (assoc user-data :avatar-url temp-user-avatar)
                          user-data)
        is-jelly-head-avatar (= (:avatar-url user-data) temp-user-avatar)]
    [:div.onboard-lander.invitee-lander-profile
      [:div.main-cta
        [:div.mobile-header.mobile-only
          [:div.mobile-logo]]
        [:div.title.about-yourself
          "Tell us a bit about you"]
        [:div.subtitle
          "This information will be visible to your team"]
        (when (:error edit-user-profile)
            [:div.subtitle.error
              "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.logo-upload-container.group.fs-hide
            {:on-click (fn []
                        (when (not= (:avatar-url user-data) temp-user-avatar)
                          (dis/dispatch! [:input [:edit-user-profile :avatar-url] temp-user-avatar]))
                        (iu/upload! user-utils/user-avatar-filestack-config
                          (fn [res]
                            (dis/dispatch! [:input [:edit-user-profile :avatar-url] (gobj/get res "url")]))
                          nil
                          (fn [_])
                          nil))}
            (if is-jelly-head-avatar
              [:div.empty-user-avatar-placeholder]
              (user-avatar-image fixed-user-data))
            [:div.add-picture-link
              "+ Upload a profile photo"]
            [:div.add-picture-link-subtitle
              "A 160x160 PNG or JPG works best"]]
          [:div.field-label
            "First name"]
          [:input.field.fs-hide
            {:type "text"
             :ref "first-name"
             :value (:first-name user-data)
             :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
          [:div.field-label
            "Last name"]
          [:input.field.fs-hide
            {:type "text"
             :value (:last-name user-data)
             :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
          [:button.continue
            {:disabled (or (and (empty? (:first-name user-data))
                                (empty? (:last-name user-data)))
                           (empty? (:avatar-url user-data)))
             :on-touch-start identity
             :on-click #(do
                          (reset! (::saving s) true)
                          (user-actions/user-profile-save current-user-data edit-user-profile))}
            "Continue"]]]]))

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
    [:div.onboard-email-container.email-wall
      [:div.email-wall-icon]
      "Please verify your email"
      [:div.email-wall-subtitle
        (str
         "Before you can join your team, we just need to verify your idetity. "
         "Please check your email, and continue the registration process from there.")]
      [:div.email-wall-sent-link
        "We have sent an email to"
        (if (seq email)
          ":"
          " ")
        (if (seq email)
          [:div.email-address.fs-hide email]
          "your email address")
        "."]]))

(defn exchange-token-when-ready [s]
  (when-let [auth-settings (:auth-settings @(drv/get-ref s :email-verification))]
    (when (and (not @(::exchange-started s))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (reset! (::exchange-started s) true)
      (user-actions/auth-with-token :email-verification))))

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
                              (nux-actions/new-user-registered "email")
                              (router/nav! oc-urls/confirm-invitation-profile))
                            (router/nav! (oc-urls/org (:slug org))))
                          (router/nav! oc-urls/login)))
           :on-touch-start identity}
          "Get Started"]]
      :else
      [:div.onboard-email-container.small.dot-animation
        "Verifying, please wait" [:span.dots {:ref :dots} "."]])))

(defn exchange-pswd-reset-token-when-ready [s]
  (when-let [auth-settings (:auth-settings @(drv/get-ref s :password-reset))]
    (when (and (not @(::exchange-started s))
               (utils/link-for (:links auth-settings) "authenticate" "GET" {:auth-source "email"}))
      (reset! (::exchange-started s) true)
      (user-actions/auth-with-token :password-reset))))

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
    :lander-sections (lander-sections)
    :lander-invite (lander-invite)
    :invitee-lander (invitee-lander)
    :invitee-lander-password (invitee-lander-password)
    :invitee-lander-profile (invitee-lander-profile)
    :email-wall (email-wall)
    :email-verified (email-verified)
    :password-reset-lander (password-reset-lander)
    [:div]))

(rum/defcs onboard-wrapper < rum/reactive
                             (drv/drv :ap-loading)
  [s component]
  [:div.onboard-wrapper-container
    (loading {:loading (drv/react s :ap-loading)})
    [:div.onboard-wrapper
      {:class (str "onboard-" (name component))}
      [:div.onboard-wrapper-left
        [:div.onboard-wrapper-logo]
        [:div.onboard-wrapper-left-inner
          [:div.onboard-wrapper-box]]]
      [:div.onboard-wrapper-right
        (get-component component)]]])