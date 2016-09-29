(ns open-company-web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.login-button :as login]
            [open-company-web.components.ui.icon :as i]))

(defn close-overlay [e]
  (utils/event-stop e)
  (dis/dispatch! [:show-login-overlay false]))

(def dont-scroll
  {:will-mount (fn [s] (dommy/add-class! (sel1 [:body]) :no-scroll) s)
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
                {:on-click #(login/login! (:basic-scopes-url (:auth-settings @dis/app-state)) %)}
                "this link instead."]]]
          (:slack-access (rum/react dis/app-state))
          [:span.block.red
            "There is a temporary error validating with Slack. Please try again later."])
        [:button.btn-reset.mt2.login-button
          {:on-click #(login/login! (:extended-scopes-url (:auth-settings @dis/app-state)) %)}
          [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]]
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
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Sign In"]
      [:div.pt2.pl2.pr2.group
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "EMAIL"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.email {:value "" :type "text" :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "PASSWORD"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd {:value "" :type "password" :name "pswd"}]]
          [:div.group.pb2.my3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "FORGOT PASSWORD?"]]
            [:div.right
              [:button.btn-reset.btn-solid "SIGN IN"]]]]]]
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
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Sign In"]]
      [:div.pt2.pl2.pr2.pb2.group
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "YOUR NAME"]]
          [:div.sign-in-field-container.group
            [:input.sign-in-field.firstname.half.left {:value "" :placeholder "First name" :type "text" :name "firstname"}]
            [:input.sign-in-field.lastname.half.right {:value "" :placeholder "Last name" :type "text" :name "lastname"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "EMAIL"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.email {:value "" :placeholder "email@example.com" :type "text" :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "PASSWORD"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd {:value "" :placeholder "your secret" :type "password" :name "pswd"}]]
          [:div.group.pb2.my3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "FORGOT PASSWORD?"]]
            [:div.right
              [:button.btn-reset.btn-solid "SIGN IN"]]]]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:show-login-overlay :signin-with-email]))}
          "ALREADY HAVE AN ACCOUNT? "
          [:span.underline "SIGN IN NOW"]]]]])

(rum/defcs password-reset < rum/reactive
                            (merge dont-scroll
                              {:did-mount (fn [s] (.focus [:div.password-reset-field-container.email]) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.password-reset
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.password-reset-cta.mb3 "Password Reset"]
      [:div.pt2.pl2.pr2.group
        [:form.password-reset-form
          [:div.password-reset-label-container
            [:label.password-reset-label "PLEASE ENTER YOUR EMAIL ADDRESS"]]
          [:div.password-reset-field-container.email
            [:input.password-reset-field {:value "" :type "text" :name "email"}]]
          [:div.group.pb2.mt3
            [:div.right.ml1
              [:button.btn-reset.btn-solid
                "RESET PASSWORD"]]
            [:div.right
              [:button.btn-reset.btn-outline
                {:on-click #(dis/dispatch! [:show-login-overlay nil])}
                "CANCEL"]]]]]]]])

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