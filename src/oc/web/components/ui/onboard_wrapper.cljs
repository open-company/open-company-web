(ns oc.web.components.ui.onboard-wrapper
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as string]
            [oc.web.expo :as expo]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.image-upload :as iu]
            [oc.web.utils.org :as org-utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.jwt :as jwt-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.shared.useragent :as ua]
            [goog.dom :as gdom]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn- clean-org-name [org-name]
  (string/trim org-name))

(defn- delay-focus-field-with-ref
  "Given a Rum state and a ref, async focus the filed if it exists."
  [s r]
  (utils/after 0
   #(when-let [field (rum/ref-node s r)]
      (.focus field))))

(rum/defcs lander < rum/static
                    rum/reactive
                    (drv/drv user-store/signup-with-email)
                    (drv/drv :auth-settings)
                    (drv/drv :expo-deep-link-origin)
                    (rum/local false ::email-error)
                    (rum/local false ::password-error)
                    (rum/local "" ::email)
                    (rum/local "" ::pswd)
                    {:will-mount (fn [s]
                      (user-actions/signup-with-email-reset-errors)
                      s)}
  [s]
  (let [signup-with-email (drv/react s user-store/signup-with-email)
        auth-settings (drv/react s :auth-settings)
        deep-link-origin (drv/react s :expo-deep-link-origin)
        continue-disabled? (or (not (utils/valid-email? @(::email s)))
                               (<= (count @(::pswd s)) 7))
        continue-fn (if continue-disabled?
                      (fn [_]
                        (when-not (utils/valid-email? @(::email s))
                          (reset! (::email-error s) true))
                        (when (<= (count @(::pswd s)) 7)
                          (reset! (::password-error s) true)))
                      #(user-actions/signup-with-email {:email @(::email s) :pswd @(::pswd s)}))]
    [:div.onboard-lander.lander.group
      [:header.main-cta
        [:button.mlb-reset.top-back-button
          {:on-touch-start identity
           :on-click #(router/history-back!)}
          "Back"]
        [:div.title.main-lander
          "Let’s get started!"]
        [:button.mlb-reset.top-continue
          {:class (when continue-disabled? "disabled")
           :on-click continue-fn}
          "Continue"]]
      [:div.onboard-form
        [:div.form-title
          "Sign up"]
        [:div.signup-buttons.group
          [:button.mlb-reset.signup-with-slack
            {:on-touch-start identity
             :on-click #(do
                         (.preventDefault %)
                         (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                               {:auth-source "slack"})]
                           (user-actions/login-with-slack auth-link
                                                          (when ua/mobile-app?
                                                            {:redirect-origin deep-link-origin}))))}
            [:div.slack-icon
              {:aria-label "slack"}]
            [:div.slack-text "Slack"]]
         [:button.mlb-reset.signup-with-google
           {:on-touch-start identity
            :on-click #(do
                         (.preventDefault %)
                         (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                                              {:auth-source "google"})]
                           (user-actions/login-with-google auth-link
                                                           (when ua/mobile-app?
                                                             {:redirect-origin deep-link-origin}))))}
            [:div.google-icon
              {:aria-label "google"}]
            [:div.google-text "Google"]]]
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
          [:input.field.oc-input
            {:type "email"
             :class (utils/class-set {:error (= (:error signup-with-email) 409)
                                      utils/hide-class true})
             :pattern utils/valid-email-pattern
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
          [:input.field.oc-input
            {:type "password"
             :pattern ".{8,}"
             :value @(::pswd s)
             :placeholder "Minimum 8 characters"
             :on-change #(let [v (.. % -target -value)]
                           (reset! (::password-error s) false)
                           (reset! (::email-error s) false)
                           (reset! (::pswd s) v))}]
          [:button.continue
            {:class (when continue-disabled? "disabled")
             :on-touch-start identity
             :on-click continue-fn}
            "Continue"]
          [:div.field-description
            "By clicking continue, you agree to our "
            [:a
              {:href oc-urls/terms}
              "terms of service"]
            " and "
            [:a
              {:href oc-urls/privacy}
              "privacy policy"]
            "."]]]
        [:div.footer-link
          "Already have an account?"
          [:div
            [:a {:href (if ua/pseudo-native?
                         oc-urls/native-login
                         oc-urls/login)
                 :on-click (fn [e]
                             (utils/event-stop e)
                             (router/nav! (if ua/pseudo-native?
                              oc-urls/native-login
                              oc-urls/login)))}
             "Sign in here"]]]]))

(defn- profile-setup-team-data
  ""
  [s]
  ;; Load the list of teams if it's not already
  (team-actions/teams-get-if-needed)
  (let [org-editing @(drv/get-ref s :org-editing)
        teams-data @(drv/get-ref s :teams-data)]
    (if (and (zero? (count (:name org-editing)))
             (seq teams-data))
      (let [first-team (select-keys
                        (first teams-data)
                        [:name :logo-url])]
        (dis/dispatch! [:update [:org-editing] #(merge % first-team)])
        (when (seq (:logo-url first-team))
          (let [img (gdom/createDom "img")]
            (set! (.-onload img)
             (fn []
               (dis/dispatch! [:update [:org-editing] #(merge % {:logo-width (.-width img)
                                                                 :logo-height (.-height img)})])
               (gdom/removeNode img)))
            (set! (.-onerror img)
             (fn []
               (dis/dispatch! [:update [:org-editing] #(dissoc % :logo-url)])
               (gdom/removeNode img)))
            (gdom/append (.-body js/document) img)
            (set! (.-src img) (:logo-url first-team))))))))

(defn- update-tooltip [s]
  (utils/after 100
   #(let [header-avatar (rum/ref-node s "user-profile-avatar")
          $header-avatar (js/$ header-avatar)
          edit-user-profile-avatar (-> s (drv/get-ref :edit-user-profile) deref :user-data :avatar-url)
          title (if (string/includes? edit-user-profile-avatar "/img/ML/happy_face_")
                  "Add a photo"
                  "Change photo")]
      (.tooltip $header-avatar #js {:title title
                                    :trigger "hover focus"
                                    :position "top"
                                    :container "body"}))))

(defn error-cb [s res error]
  (notification-actions/show-notification
    {:title "Image upload error"
     :description "An error occurred while processing your image. Please retry."
     :expire 3
     :dismiss true})
  (update-tooltip s))

(defn success-cb
  [s res]
  (let [url (gobj/get res "url")]
    (if-not url
      (error-cb s nil nil)
      (dis/dispatch! [:input [:edit-user-profile :user-data :avatar-url] url]))
    (update-tooltip s)))

(defn progress-cb [res progress])

(defn upload-user-profile-picture-clicked [s]
  (iu/upload! user-utils/user-avatar-filestack-config (partial success-cb s) progress-cb (partial error-cb s)))

(rum/defcs lander-profile < rum/reactive
                            (drv/drv :edit-user-profile)
                            (drv/drv :current-user-data)
                            (drv/drv :teams-data)
                            (drv/drv :org-editing)
                            (drv/drv :orgs)
                            (rum/local false ::saving)
                            {:will-mount (fn [s]
                              (dis/dispatch! [:input [:org-editing :name] ""])
                              (user-actions/user-profile-reset)
                              s)
                             :did-mount (fn [s]
                              (profile-setup-team-data s)
                              (delay-focus-field-with-ref s "first-name")
                              (update-tooltip s)
                             s)
                             :did-remount (fn [o s]
                              (update-tooltip s)
                              s)
                             :will-update (fn [s]
                              (profile-setup-team-data s)
                              (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
                                    org-editing @(drv/get-ref s :org-editing)]
                                (when (and @(::saving s)
                                           (or (:error edit-user-profile)
                                               (:error org-editing)))
                                  (reset! (::saving s) false)))
                             s)}
  [s]
  (let [has-org? (pos? (count (drv/react s :orgs)))
        edit-user-profile (drv/react s :edit-user-profile)
        current-user-data (drv/react s :current-user-data)
        teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)
        user-data (:user-data edit-user-profile)
        continue-disabled (or @(::saving s)
                              (and (empty? (:first-name user-data))
                                   (empty? (:last-name user-data)))
                              (and (not has-org?)
                                   (-> org-editing :name clean-org-name count (<= 1))))
        is-mobile? (responsive/is-tablet-or-mobile?)
        continue-fn #(when-not continue-disabled
                       (reset! (::saving s) true)
                       (dis/dispatch! [:update [:org-editing :name] clean-org-name])
                       (user-actions/onboard-profile-save current-user-data edit-user-profile :org-editing))]
    [:div.onboard-lander.lander-profile
      [:header.main-cta
        [:div.title.about-yourself
          (if has-org?
           "Tell us about you"
           "Create your team")]]
      (when (:error edit-user-profile)
        [:div.subtitle.error
          "An error occurred while saving your data, please try again"])
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.form-title
            "Sign up"]
          [:button.mlb-reset.user-profile-avatar
            {:on-click #(upload-user-profile-picture-clicked s)
             :ref "user-profile-avatar"
             :data-toggle (when-not is-mobile? "")}
            (user-avatar-image user-data)]
          [:div.field-label.name-fields
              "First name"]
          [:input.field.oc-input
            {:class utils/hide-class
             :type "text"
             :ref "first-name"
             :placeholder "First name..."
             :max-length user-utils/user-name-max-lenth
             :value (or (:first-name user-data) "")
             :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
          [:div.field-label
            "Last name"]
          [:input.field.oc-input
            {:class utils/hide-class
             :type "text"
             :placeholder "Last name..."
             :value (or (:last-name user-data) "")
             :max-length user-utils/user-name-max-lenth
             :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
          (when-not has-org?
            [:div.field-label.company-name
              "Company name"])
          (when-not has-org?
            [:input.field.oc-input
              {:type "text"
               :ref "org-name"
               :placeholder "Enter a team name..."
               :class (utils/class-set {:error (:error org-editing)
                                        utils/hide-class true})
               :max-length org-utils/org-name-max-length
               :value (:name org-editing)
               :on-change #(let [new-name (.. % -target -value)
                                 clean-org-name (subs new-name 0 (min (count new-name)
                                                 org-utils/org-name-max-length))]
                             (dis/dispatch! [:input [:org-editing] (merge org-editing {:error nil
                                                                                       :name clean-org-name
                                                                                       ;; Enforce a change in the app-state
                                                                                       ;; to make sure the name is truncated
                                                                                       :rand (rand 1000)})]))}])
          (when (:error org-editing)
            [:div.error "Must be between 3 and 50 characters"])
          [:button.continue
            {:class (when continue-disabled "disabled")
             :on-touch-start identity
             :on-click continue-fn}
            "Create team"]]]
        [:div.logout-cancel
          "Need to start over? "
          [:button.mlb-reset.logout-cancel
            {:on-click #(jwt-actions/logout oc-urls/sign-up)}
            "Click here"]]]))

(defn- setup-team-data
  ""
  [s]
  ;; Load the list of teams if it's not already
  (team-actions/teams-get-if-needed)
  (let [org-editing @(drv/get-ref s :org-editing)
        teams-data @(drv/get-ref s :teams-data)]
    (if (and (zero? (count (:name org-editing)))
             (zero? (count (:logo-url org-editing)))
             (seq teams-data))
      (let [first-team (select-keys
                        (first teams-data)
                        [:name :logo-url :logo-width :logo-height])]
        (dis/dispatch!
         [:update
          [:org-editing]
          #(merge % first-team)])
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
            (set! (.-src img) (:logo-url first-team))))))))

(rum/defcs lander-team < rum/reactive
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
                           (setup-team-data s)
                           s)}
  [s]
  (let [teams-data (drv/react s :teams-data)
        org-editing (drv/react s :org-editing)
        is-mobile? (responsive/is-tablet-or-mobile?)
        continue-disabled (-> org-editing :name clean-org-name count (< 3))
        continue-fn #(when-not continue-disabled
                       (let [org-name (clean-org-name (:name org-editing))]
                         (dis/dispatch! [:input [:org-editing :name] org-name])
                         (if (and (seq org-name)
                                  (>= (count org-name) 2))
                           ;; Create org and show setup screen
                           (org-actions/create-or-update-org @(drv/get-ref s :org-editing))
                           (dis/dispatch! [:input [:org-editing :error] true]))))]
    [:div.onboard-lander.lander-team
      [:header.main-cta
        [:div.title.company-setup
          "Set up your company"]]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          (when-not is-mobile?
            [:div.logo-upload-container.org-logo.group
              {:class utils/hide-class
               :on-click (fn [_]
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
            "Company name"]
          [:input.field.oc-input
            {:type "text"
             :ref "org-name"
             :class (utils/class-set {:error (:error org-editing)
                                      utils/hide-class true})
             :value (:name org-editing)
             :on-change #(dis/dispatch! [:input [:org-editing]
               (merge org-editing {:error nil :name (.. % -target -value)})])}]
          (when (:error org-editing)
            [:div.error "Must be between 3 and 50 characters"])
          [:button.continue
            {:class (when continue-disabled "disabled")
             :on-touch-start identity
             :on-click continue-fn}
            "Continue"]]]]))

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
    "Hey there, let's explore Wut together. It's a place to make sure important "
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
      [:header.main-cta
        [:div.title
          "Invite your team"]
        [:div.subtitle
          "Invite some colleagues to explore Wut with you."]]
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
          [:div.invite-rows
            {:class utils/hide-class}
            (for [idx (range (count @(::invite-rows s)))
                  :let [invite (get @(::invite-rows s) idx)]]
              [:div.invite-row
                {:class (when (:error invite) "error")
                 :key (str "invite-row-" @(::invite-rand s) "-" idx)}
                [:input.oc-input
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

(rum/defcs invitee-team-lander < rum/reactive
                                 (drv/drv :team-invite)
                                 (drv/drv user-store/signup-with-email)
                                 (rum/local false ::email-error)
                                 (rum/local false ::password-error)
                                 (rum/local "" ::email)
                                 (rum/local "" ::pswd)
                                 (rum/local false ::auth-settings-loaded)
                                 {:did-mount (fn [s]
                                    (dots-animation s)
                                  s)
                                  :will-update (fn [s]
                                    (let [auth-settings (:auth-settings @(drv/get-ref s :team-invite))]
                                      (when (and (not @(::auth-settings-loaded s))
                                                 auth-settings
                                                 (not (:team auth-settings)))
                                        (reset! (::auth-settings-loaded s) true)))
                                  s)}
  [s]
  (let [team-invite-drv (drv/react s :team-invite)
        auth-settings (:auth-settings team-invite-drv)
        email-signup-link (utils/link-for (:links auth-settings) "create" "POST" {:auth-source "email"})
        team-data (:team auth-settings)
        signup-with-email (drv/react s user-store/signup-with-email)]
    [:div.onboard-lander.invitee-team-lander
      [:header.main-cta
        (when-not ua/mobile-app?
          [:button.mlb-reset.top-back-button
            {:on-touch-start identity
             :on-click #(router/history-back!)
             :aria-label "Back"}])
        (if auth-settings
          (if (:team auth-settings)
            [:div.title
              "Join Wut"]
            [:div.title
              "Oh oh..."])
          [:div.title
            "Please wait"])]
        (if auth-settings
          (if (:team auth-settings)
            [:div.onboard-form
              [:form
                {:on-submit (fn [e]
                              (.preventDefault e))}
                [:div.title-container
                  (when (seq (:logo-url team-data))
                    [:div.team-logo-container
                      (org-avatar team-data false :never)])
                  [:div.title.main-lander
                    "Join " (:name team-data) " on Wut"]]
                [:div.field-label.email-field
                  "Work email"
                  (cond
                    (= (:error signup-with-email) 409)
                    [:span.error "Email already exists"]
                    @(::email-error s)
                    [:span.error "Email is not valid"])]
                [:input.field.oc-input
                  {:type "email"
                   :class (utils/class-set {:error (= (:error signup-with-email) 409)
                                            utils/hide-class true})
                   :pattern utils/valid-email-pattern
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
                [:input.field.oc-input
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
                                (user-actions/signup-with-email {:email @(::email s) :pswd @(::pswd s)} true))}
                  (str "Join " (:name team-data))]]]
            [:div.invite-token-container.token-error
              [:div.title
                (str "The invite link you’re trying to access "
                     "has been deactivated by your account admin "
                     "and is no longer valid.")]])
          [:div.invite-token-container
            [:div.subtitle.checking-invitation
              "Checking invitation link" [:span.dots {:ref :dots} "."]]])]))

(defn confirm-invitation-when-ready [s]
  (let [confirm-invitation @(drv/get-ref s :confirm-invitation)]
    (when (and (:auth-settings confirm-invitation)
               (not @(::exchange-started s)))
      (reset! (::exchange-started s) true)
      (user-actions/confirm-invitation (:token confirm-invitation)))))

(rum/defcs invitee-lander < rum/reactive
                            (drv/drv :confirm-invitation)
                            (rum/local false ::exchange-started)
                            (rum/local false ::exchange-ended)
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
      [:header.main-cta
        [:div.invite-container
          [:div.title
            "Join your team on Wut"]
          (if (:invitation-error confirm-invitation)
            [:div.subtitle
              "An error occurred while confirming your invitation, please try again."]
            [:div.subtitle.checking-invitation
              "Checking invitation link" [:span.dots {:ref :dots} "."]])]]]))

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
      [:header.main-cta
        [:div.title
          "Set a password"]
        [:div.subtitle
          "Joining as: "
          [:span.email-address
            {:class utils/hide-class}
            (:email jwt)]]]
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
          [:input.field.oc-input
            {:type "password"
             :class (when collect-pswd-error "error")
             :value (or (:pswd collect-pswd) "")
             :ref "password"
             :on-change #(do
                           (reset! (::password-error s) false)
                           (dis/dispatch! [:input [:collect-pswd :pswd] (.. % -target -value)]))
             :placeholder "Minimum 8 characters"
             :pattern ".{8,}"}]
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
                                    {:will-mount (fn [s]
                                      (user-actions/user-profile-reset)
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
        user-data (:user-data edit-user-profile)]
    [:div.onboard-lander.invitee-lander-profile
      [:header.main-cta
        [:div.title.about-yourself
          "Tell us about you"]
        (when (:error edit-user-profile)
            [:div.subtitle.error
              "An error occurred while saving your data, please try again"])]
      [:div.onboard-form
        [:form
          {:on-submit (fn [e]
                        (.preventDefault e))}
          [:div.field-label
            "First name"]
          [:input.field.oc-input
            {:class utils/hide-class
             :type "text"
             :ref "first-name"
             :placeholder "First name..."
             :value (:first-name user-data)
             :max-length user-utils/user-name-max-lenth
             :on-change #(dis/dispatch! [:input [:edit-user-profile :first-name] (.. % -target -value)])}]
          [:div.field-label
            "Last name"]
          [:input.field.oc-input
            {:class utils/hide-class
             :type "text"
             :placeholder "Last name..."
             :value (:last-name user-data)
             :max-length user-utils/user-name-max-lenth
             :on-change #(dis/dispatch! [:input [:edit-user-profile :last-name] (.. % -target -value)])}]
          [:button.continue.start-using-carrot
            {:disabled (and (empty? (:first-name user-data))
                            (empty? (:last-name user-data)))
             :on-touch-start identity
             :on-click #(do
                          (reset! (::saving s) true)
                          (user-actions/onboard-profile-save current-user-data edit-user-profile))}
            "Start using Wut"]]]]))

(rum/defcs email-wall < rum/reactive
                        (drv/drv :query-params)
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
          [:div.email-address
            {:class utils/hide-class}
            email]
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
        "This link is no longer valid."]
      (:error email-verification)
      [:div.onboard-email-container.error
        "An error occurred, please try again."]
      (:success email-verification)
      [:div.onboard-email-container
        "Thanks for verifying"
        [:button.mlb-reset.continue
          {:on-click #(let [org (utils/get-default-org orgs)]
                        (router/nav!
                         (if org
                           (if (and (empty? (jwt/get-key :first-name))
                                    (empty? (jwt/get-key :last-name)))
                             oc-urls/confirm-invitation-profile
                             (oc-urls/org (:slug org)))
                          oc-urls/sign-up-profile)))
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
        "This link is no longer valid."]
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
    :lander-invite (lander-invite)
    :invitee-lander (invitee-lander)
    :invitee-lander-password (invitee-lander-password)
    :invitee-lander-profile (invitee-lander-profile)
    :invitee-team-lander (invitee-team-lander)
    :email-wall (email-wall)
    :email-verified (email-verified)
    :password-reset-lander (password-reset-lander)
    [:div]))

(rum/defcs onboard-wrapper < rum/reactive
                             (drv/drv :ap-loading)
                             (drv/drv :current-user-data)
  [s component]
  (let [ap-loading (drv/react s :ap-loading)
        user-data (drv/react s :current-user-data)
        loading? (or ap-loading
                     (and (jwt/jwt)
                          (not user-data)))]
    [:div.onboard-wrapper-container
      {:class (when loading? "loading")}
      (if loading?
        (loading {:loading true})
        [:div.onboard-wrapper
          {:class (str "onboard-" (name component))}
          (get-component component)])]))
