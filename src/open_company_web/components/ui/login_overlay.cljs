(ns open-company-web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [clojure.string :as s]
            [goog.object :as gobj]
            [open-company-web.urls :as oc-url]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(defn close-overlay [e]
  (utils/event-stop e)
  (dis/dispatch! [:show-login-overlay false]))

(def dont-scroll
  {:will-mount (fn [s] (dommy/add-class! (sel1 [:body]) :no-scroll)
                       (when-not (:auth-settings @dis/app-state)
                          (dis/dispatch! [:get-auth-settings])) s)
   :will-unmount (fn [s] (dommy/remove-class! (sel1 [:body]) :no-scroll) s)})

(rum/defcs login-signup-with-slack < rum/reactive
                                     dont-scroll
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.login-with-slack.center
      {:on-click #(utils/event-stop %)}
      [:div.login-overlay-cta.pl2.pr2.group
        (cond
          (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
          [:div.sign-in-cta.left "Sign Up"]
          :else
          [:div.sign-in-cta.left "Sign In"])]
      [:div.pt2.pl2.pr2.group
        [:button.close {:on-click (partial close-overlay)}
          (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"})]
        (cond
          (= (:slack-access (rum/react dis/app-state)) "denied")
          [:div.block.red
            "OpenCompany requires verification with your Slack team. Please allow access."
            [:p.my2.h5 "If Slack did not allow you to authorize OpenCompany, try "
              [:button.p0.btn-reset.underline
                {:on-click #(do (utils/event-stop %)
                                (dis/dispatch! [:login-with-slack (:basic-scopes-url (:auth-settings @dis/app-state))]))}
                "this link instead."]]]
          (:slack-access (rum/react dis/app-state))
          [:span.block.red
            "There is a temporary error validating with Slack. Please try again later."])
        [:button.btn-reset.mt2.login-button
          {:on-click #(do
                        (.preventDefault %)
                        (dis/dispatch! [:login-with-slack (:extended-scopes-url (:slack (:auth-settings @dis/app-state)))]))}
          (if (:auth-settings (rum/react dis/app-state))
            [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]
            (small-loading))]
        [:div.login-with-email.domine.underline.bold
          [:a {:on-click #(do (utils/event-stop %)
                              (dis/dispatch! [:show-login-overlay (if (= (:show-login-overlay @dis/app-state) :signup-with-slack) :signup-with-email :login-with-email)]))}
            (cond
              (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
              "OR SIGN UP VIA EMAIL"
              :else
              "OR SIGN IN VIA EMAIL")]]]
        [:div.login-overlay-footer.p2.mt1.group
          (cond
              (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
              [:a.left {:on-click #(dis/dispatch! [:show-login-overlay :signin-with-slack])}
                "ALREADY HAVE AN ACCOUNT? "
                 [:span.underline "SIGN IN NOW"]]
              :else
              [:a.left {:on-click #(dis/dispatch! [:show-login-overlay :signup-with-slack])}
                "DON’T HAVE AN ACCOUNT? "
                 [:span.underline "SIGN UP NOW"]])]]])

(rum/defcs login-with-email < rum/reactive
                              (merge dont-scroll
                                {:did-mount (fn [s] (.focus (sel1 [:input.email])) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.login-with-email.group
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)}
          (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"})]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Sign In"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl2.pr2.pb2.group
        (when-not (nil? (:login-with-email-error (rum/react dis/app-state)))
          (cond
            (= (:login-with-email-error (rum/react dis/app-state)) 401)
            [:span.error-message.red
              "The email or password you entered is incorrect."
              [:br]
              "Please try again, or "
              [:a.underline.red {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "reset your password"]
              "."]
            :else
            [:span.error-message.red
              "System troubles logging in."
              [:br]
              "Please try again, then "
              [:a.underline.red {:href oc-url/contact-mail-to} "contact support"]
              "."]))
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "EMAIL"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.email
              {:value (:email (:login-with-email (rum/react dis/app-state)))
               :on-change #(dis/dispatch! [:login-with-email-change :email (.-value (sel1 [:input.email]))])
               :type "text"
               :tabIndex 1
               :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "PASSWORD"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd
              {:value (:pswd (:login-with-email (rum/react dis/app-state)))
               :on-change #(dis/dispatch! [:login-with-email-change :pswd (.-value (sel1 [:input.pswd]))])
               :type "password"
               :tabIndex 2
               :name "pswd"}]]
          [:div.group.pb2.my3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "FORGOT PASSWORD?"]]
            [:div.right
              [:button.btn-reset.btn-solid
                {:disabled (nil? (:email (:auth-settings (rum/react dis/app-state))))
                 :on-click #(do
                              (.preventDefault %)
                              (dis/dispatch! [:login-with-email (:auth-url (:email (:auth-settings @dis/app-state)))]))}
                "SIGN IN"]]]]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:show-login-overlay :signup-with-email]))}
          "DON’T HAVE AN ACCOUNT? "
          [:span.underline "SIGN UP NOW"]]]]])

(rum/defcs signup-with-email < rum/reactive
                               (merge dont-scroll
                                 {:did-mount (fn [s] (.focus (sel1 [:input.firstname])) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.signup-with-email.group
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)}
          (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"})]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Sign Up"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl2.pr2.pb2.group
        (when-not (nil? (:signup-with-email-error (rum/react dis/app-state)))
          (cond
            (= (:signup-with-email-error (rum/react dis/app-state)) 409)
            [:span.error-message.red
              "The email or password you entered is incorrect."
              [:br]
              "Please try again, or "
              [:a.underline.red {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "reset your password"]
              "."]
            (= (:signup-with-email-error (rum/react dis/app-state)) 400)
            [:span.error-message.red
              "An error occurred while processing your data, please check the fields and try again."]
            :else
            [:span.error-message.red
              "System troubles logging in."
              [:br]
              "Please try again, then "
              [:a.underline.red {:href oc-url/contact-mail-to} "contact support"]
              "."]))
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "signup-firstname"} "YOUR NAME"]]
          [:div.sign-in-field-container.group
            [:input.sign-in-field.firstname.half.left
              {:value (:firstname (:signup-with-email (rum/react dis/app-state)))
               :id "signup-firstname"
               :on-change #(dis/dispatch! [:signup-with-email-change :firstname (.-value (sel1 [:input.firstname]))])
               :placeholder "First name"
               :type "text"
               :tabIndex 1
               :name "firstname"}]
            [:input.sign-in-field.lastname.half.right
              {:value (:lastname (:signup-with-email (rum/react dis/app-state)))
               :id "signup-lastname"
               :on-change #(dis/dispatch! [:signup-with-email-change :lastname (.-value (sel1 [:input.lastname]))])
               :placeholder "Last name"
               :type "text"
               :tabIndex 2
               :name "lastname"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "signup-email"} "EMAIL"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.email
              {:value (:email (:signup-with-email (rum/react dis/app-state)))
               :id "signup-email"
               :on-change #(dis/dispatch! [:signup-with-email-change :email (.-value (sel1 [:input.email]))])
               :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
               :placeholder "email@example.com"
               :type "text"
               :tabIndex 3
               :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "signup-pswd"} "PASSWORD"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd
              {:value (:pswd (:signup-with-email (rum/react dis/app-state)))
               :id "signup-pswd"
               :on-change #(dis/dispatch! [:signup-with-email-change :pswd (.-value (sel1 [:input.pswd]))])
               :pattern ".{5,}"
               :placeholder "your secret"
               :type "password"
               :tabIndex 4
               :name "pswd"}]]
          [:div.group.pb2.my3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "FORGOT PASSWORD?"]]
            [:div.right
              [:button.btn-reset.btn-solid
                {:disabled (or (s/blank? (:firstname (:signup-with-email (rum/react dis/app-state))))
                               (s/blank? (:lastname (:signup-with-email (rum/react dis/app-state))))
                               (gobj/get (gobj/get (sel1 [:input.email]) "validity") "patternMismatch")
                               (<= (count (:pswd (:signup-with-email (rum/react dis/app-state)))) 5))
                 :on-click #(do
                              (utils/event-stop %)
                              (dis/dispatch! [:signup-with-email "/email/users"]))}
                "SIGN UP"]]]]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:show-login-overlay :login-with-email]))}
          "ALREADY HAVE AN ACCOUNT? "
          [:span.underline "SIGN IN NOW"]]]]])

(rum/defcs password-reset < rum/reactive
                            (merge dont-scroll
                              {:did-mount (fn [s] (.focus [:div.sign-in-field-container.email]) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.password-reset
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)}
          (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"})]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Password reset"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl2.pr2.pb2.group
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "PLEASE ENTER YOUR EMAIL ADDRESS"]]
          [:div.sign-in-field-container.email
            [:input.sign-in-field {:value "" :tabIndex 1 :type "text" :name "email"}]]
          [:div.group.pb2.mt3
            [:div.right.ml1
              [:button.btn-reset.btn-solid
                "RESET PASSWORD"]]
            [:div.right
              [:button.btn-reset.btn-outline
                {:on-click #(dis/dispatch! [:show-login-overlay nil])}
                "CANCEL"]]]]]]])

(defn login-overlays-handler [s]
  (when (:show-login-overlay s)
    (cond
      ; login via email
      (= (:show-login-overlay s) :login-with-email)
      (login-with-email)
      ; signup via email
      (= (:show-login-overlay s) :signup-with-email)
      (signup-with-email)
      ; password reset
      (= (:show-login-overlay s) :password-reset)
      (password-reset)
      ; login via slack as default
      :else
      (login-signup-with-slack))))