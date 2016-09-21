(ns open-company-web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.login-button :as login]))

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
      [:div.group
        (cond
          (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
          [:div.sign-in-cta.m2.left "Sign Up"]
          :else
          [:div])]
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:button.btn-reset.mt2.login-button
        {:on-click #(login/login! (:extended-scopes-url (:auth-settings @dis/app-state)) %)}
        [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]]
      [:div.login-with-email
        [:a {:on-click #(do (utils/event-stop %) (dis/dispatch! [:show-login-overlay :login-with-email]))}
          (cond
            (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
            "OR SIGN UP VIA EMAIL"
            :else
            "OR SIGN IN VIA EMAIL")]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {:on-click #(dis/dispatch! [:show-login-overlay :signup-with-slack])} "DON’T HAVE AN ACCOUNT? SIGN UP NOW"]]]])

(rum/defcs login-with-email < rum/reactive
                              dont-scroll
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.login-with-email.group
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:div.p2.group
        [:div.sing-in-cta.mb3 "Sign in"]
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "EMAIL"]]
          [:div.sign-in-field-container
            [:input.sign-in-field {:value "" :type "text" :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "PASSWORD"]]
          [:div.sign-in-field-container
            [:input.sign-in-field {:value "" :type "password" :name "pswd"}]]
          [:div.group.pb2.my3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:show-login-overlay :password-reset])} "FORGOT PASSWORD?"]]
            [:div.right
              [:button.btn-reset.btn-solid "SIGN IN"]]]]]
      [:div.login-overlay-footer.p2.mt1.group
        [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:show-login-overlay :signup-with-email]))} "DON’T HAVE AN ACCOUNT? SIGN UP NOW"]]]])

(rum/defcs password-reset < rum/reactive
                            dont-scroll
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:div.login-overlay.password-reset
      {:on-click #(utils/event-stop %)}
      [:button.close {:on-click (partial close-overlay)} [:i.fa.fa-times]]
      [:div.p2.group
        [:div.password-reset-cta.mb3 "Password Reset"]
        [:form.password-reset-form
          [:div.password-reset-label-container
            [:label.password-reset-label "PLEASE ENTER YOUR EMAIL ADDRESS"]]
          [:div.password-reset-field-container
            [:input.password-reset-field {:value "" :type "text" :name "email"}]]
          [:div.group.pb2.mt3
            [:div.right.ml1
              [:button.btn-reset.btn-solid
                "RESET PASSWORD"]]
            [:div.right
              [:button.btn-reset.btn-outline
                {:on-click #(dis/dispatch! [:show-login-overlay nil])}
                "CANCEL"]]]]]]])