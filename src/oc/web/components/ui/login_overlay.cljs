(ns oc.web.components.ui.login-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [clojure.string :as s]
            [goog.object :as gobj]
            [goog.style :as gstyle]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.icon :as i]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(defn close-overlay [e]
  (utils/event-stop e)
  (dis/dispatch! [:login-overlay-show false]))

(def dont-scroll
  {:will-mount (fn [s]
                (when-not (contains? @dis/app-state :auth-settings)
                  (utils/after 100 #(dis/dispatch! [:auth-settings-get])))
                s)
   :before-render (fn [s]
                    (if (responsive/is-mobile-size?)
                      (let [display-none #js {:display "none"}]
                        (when (sel1 [:div.main])
                          (gstyle/setStyle (sel1 [:div.main]) display-none))
                        (when (sel1 [:nav.navbar-bottom])
                          (gstyle/setStyle (sel1 [:nav.navbar-bottom]) display-none))
                        (when (sel1 [:nav.navbar-static-top])
                          (gstyle/setStyle (sel1 [:nav.navbar-static-top]) display-none))
                        (when (sel1 [:div.fullscreen-page])
                          (gstyle/setStyle (sel1 [:div.fullscreen-page]) display-none)))
                      (dommy/add-class! (sel1 [:body]) :no-scroll))
                    s)
   :will-unmount (fn [s]
                   (if (responsive/is-mobile-size?)
                    (let [display-block #js {:display "block"}]
                      (when (sel1 [:div.main])
                        (gstyle/setStyle (sel1 [:div.main]) display-block))
                      (when (sel1 [:nav.navbar-bottom])
                        (gstyle/setStyle (sel1 [:nav.navbar-bottom]) display-block))
                      (when (sel1 [:nav.navbar-static-top])
                        (gstyle/setStyle (sel1 [:nav.navbar-static-top]) display-block))
                      (when (sel1 [:div.fullscreen-page])
                          (gstyle/setStyle (sel1 [:div.fullscreen-page]) display-block)))
                    (dommy/remove-class! (sel1 [:body]) :no-scroll))
                   s)})

(rum/defcs login-signup-with-slack < rum/reactive
                                     dont-scroll
  [state]
  (let [action-title (if (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack) "Sign Up" "Sign In")
        slack-error [:span.block.red "There was an issue validating with Slack."]]
    [:div.login-overlay-container.group
      {:on-click (partial close-overlay)}
      [:button.carrot-modal-close.mlb-reset
        {:on-click (partial close-overlay)}]
      [:div.login-overlay.login-with-slack
        {:on-click #(utils/event-stop %)}
        [:div.login-overlay-cta.pl2.pr2.group
          [:div.sign-in-cta.left action-title]]
        [:div.login-overlay-content.pt2.pl3.pr3.group.center
          [:div
            (when (:access (:query-params @router/path)) slack-error)
            [:button.mlb-reset.mt2.login-button.slack-button
              {:on-click #(do
                            (.preventDefault %)
                            (when (:auth-settings @dis/app-state)
                              (dis/dispatch! [:login-with-slack])))}
              (str action-title " with ")
              [:span.slack "Slack"]
              (when-not (:auth-settings (rum/react dis/app-state))
                (small-loading))]]
          [:div.login-with-email.domine.underline.bold
            [:a {:on-click (fn [e]
                              (utils/event-stop e)
                              (if (= (:show-login-overlay @dis/app-state) :signup-with-slack)
                                (do
                                  (router/nav! oc-urls/sign-up)
                                  (utils/after 100 #(dis/dispatch! [:login-overlay-show :signup-with-email])))
                                (dis/dispatch! [:login-overlay-show :login-with-email])))}
              (cond
                (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
                "or Sign Up via email"
                :else
                "or Sign In via email")]]]
          [:div.login-overlay-footer.group
            (cond
                (= (:show-login-overlay (rum/react dis/app-state)) :signup-with-slack)
                [:a.left {:on-click #(dis/dispatch! [:login-overlay-show :login-with-slack])}
                  "Already have an account? "
                   [:span.blue-link "Sign In now."]]
                :else
                [:a.left
                  {:on-click #(dis/dispatch! [:login-overlay-show :signup-with-slack])}
                  "Don't have an account? "
                   [:span.blue-link "Sign Up now."]])]]]))

(rum/defcs login-with-email < rum/reactive
                              (merge dont-scroll
                                {:did-mount (fn [s] (.focus (sel1 [:input.email])) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:button.carrot-modal-close.mlb-reset
        {:on-click (partial close-overlay)}]
    [:div.login-overlay.login-with-email.group
      {:on-click #(utils/event-stop %)}
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Sign In"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl3.pr3.pb2.group
        (when-not (nil? (:login-with-email-error (rum/react dis/app-state)))
          (cond
            (= (:login-with-email-error (rum/react dis/app-state)) :verify-email)
            [:span.small-caps.green
              "Hey buddy, go verify your email, again, eh?"]
            (= (:login-with-email-error (rum/react dis/app-state)) 401)
            [:span.small-caps.red
              "The email or password you entered is incorrect."
              [:br]
              "Please try again, or "
              [:a.underline.red {:on-click #(dis/dispatch! [:login-overlay-show :password-reset])} "reset your password"]
              "."]
            :else
            [:span.small-caps.red
              "System troubles logging in."
              [:br]
              "Please try again, then "
              [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
              "."]))
        [:form.sign-in-form
          {:id "sign-in-form"}
          [:div.sign-in-label-container
            [:label.sign-in-label "Email"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.email
              {:value (:email (:login-with-email (rum/react dis/app-state)))
               :on-change #(dis/dispatch! [:input [:login-with-email :email] (.-value (sel1 [:input.email]))])
               :type "email"
               :id "sign-in-email"
               :auto-focus true
               :tabIndex 1
               :autoCapitalize "none"
               :name "email"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label "Password"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd
              {:value (:pswd (:login-with-email (rum/react dis/app-state)))
               :on-change #(dis/dispatch! [:input [:login-with-email :pswd] (.-value (sel1 [:input.pswd]))])
               :type "password"
               :id "sign-in-pswd"
               :tabIndex 2
               :name "pswd"}]]
          [:div.group.pb3.mt3
            [:div.left.forgot-password
              [:a {:on-click #(dis/dispatch! [:login-overlay-show :password-reset])} "Forgot Password?"]]
            [:div.right
              [:button.mlb-reset.mlb-default
                {:disabled (or (not (:auth-settings (rum/react dis/app-state)))
                               (nil? (utils/link-for (:links (:auth-settings (rum/react dis/app-state))) "authenticate" "GET" {:auth-source "email"})))
                 :on-click #(do
                              (.preventDefault %)
                              (dis/dispatch! [:login-with-email]))}
                "Sign In"]]]]]
      [:div.login-overlay-footer.group
        [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:login-overlay-show :signup-with-email]))}
          "Don't have an account? "
          [:span.blue-link "Sign Up now."]]]]])

; (rum/defcs signup-with-email < rum/reactive
;                                (merge dont-scroll
;                                  {:did-mount (fn [s] (.focus (sel1 [:input.firstname])) s)})
;   [state]
;   [:div.login-overlay-container.group
;     {:on-click (partial close-overlay)}
;     [:button.carrot-modal-close.mlb-reset
;        {:on-click (partial close-overlay)}]
;     [:div.login-overlay.signup-with-email.group
;       {:on-click #(utils/event-stop %)}
;       [:div.login-overlay-cta.pl2.pr2.group
;         [:div.sign-in-cta "Sign Up"
;           (when-not (:auth-settings (rum/react dis/app-state))
;             (small-loading))]]
;       [:div.pt2.pl3.pr3.pb2.group
;         (when-not (nil? (:signup-with-email-error (rum/react dis/app-state)))
;           (cond
;             (= (:signup-with-email-error (rum/react dis/app-state)) :verify-email)
;             [:span.small-caps.green
;               "Hey buddy, go verify your email, eh?"]
;             (= (:signup-with-email-error (rum/react dis/app-state)) 409)
;             [:span.small-caps.red
;               "This email address already has an account. "
;               [:a.underline.red {:on-click #(dis/dispatch! [:login-overlay-show :login-with-email])} "Would you like to sign in with that account?"]
;               [:br]
;               "Please try again, or "
;               [:a.underline.red {:on-click #(dis/dispatch! [:login-overlay-show :password-reset])} "reset your password"]
;               "."]
;             (= (:signup-with-email-error (rum/react dis/app-state)) 400)
;             [:span.small-caps.red
;               "An error occurred while processing your data, please check the fields and try again."]
;             :else
;             [:span.small-caps.red
;               "System troubles logging in."
;               [:br]
;               "Please try again, then "
;               [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
;               "."]))
;         [:form.sign-in-form
;           {:id "sign-up-form"
;            :action ""
;            :method "GET"}
;           [:div.sign-in-label-container
;             [:label.sign-in-label {:for "sign-up-firstname"} "Your Name"]]
;           [:div.sign-in-field-container.group
;             [:input.sign-in-field.firstname.half.left
;               {:value (:firstname (:signup-with-email (rum/react dis/app-state)))
;                :id "sign-up-firstname"
;                :auto-focus true
;                :on-change #(dis/dispatch! [:input [:signup-with-email :firstname] (.-value (sel1 [:input.firstname]))])
;                :placeholder "First name"
;                :type "text"
;                :tabIndex 1
;                :name "firstname"}]
;             [:input.sign-in-field.lastname.half.right
;               {:value (:lastname (:signup-with-email (rum/react dis/app-state)))
;                :id "sign-up-lastname"
;                :on-change #(dis/dispatch! [:input [:signup-with-email :lastname] (.-value (sel1 [:input.lastname]))])
;                :placeholder "Last name"
;                :type "text"
;                :tabIndex 2
;                :name "lastname"}]]
;           [:div.sign-in-label-container
;             [:label.sign-in-label {:for "sign-up-email"} "Email"]]
;           [:div.sign-in-field-container
;             [:input.sign-in-field.email
;               {:value (:email (:signup-with-email (rum/react dis/app-state)))
;                :id "sign-up-email"
;                :on-change #(dis/dispatch! [:input [:signup-with-email :email] (.-value (sel1 [:input.email]))])
;                :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
;                :placeholder "email@example.com"
;                :type "email"
;                :tabIndex 3
;                :autoCapitalize "none"
;                :name "email"}]]
;           [:div.sign-in-label-container
;             [:label.sign-in-label {:for "sign-up-pswd"} "Password"]]
;           [:div.sign-in-field-container
;             [:input.sign-in-field.pswd
;               {:value (:pswd (:signup-with-email (rum/react dis/app-state)))
;                :id "sign-up-pswd"
;                :on-change #(dis/dispatch! [:input [:signup-with-email :pswd] (.-value (sel1 [:input.pswd]))])
;                :pattern ".{8,}"
;                :placeholder "Minimum 8 characters"
;                :type "password"
;                :tabIndex 4
;                :name "pswd"}]]
;           [:div.group.pb3.mt3
;             [:div.left.forgot-password
;               [:a {:on-click #(dis/dispatch! [:login-overlay-show :password-reset])} "Forgot Password?"]]
;             [:div.right
;               [:button.mlb-reset.mlb-default
;                 {:disabled (or (not (:auth-settings (rum/react dis/app-state)))
;                                (and (s/blank? (:firstname (:signup-with-email (rum/react dis/app-state))))
;                                     (s/blank? (:lastname (:signup-with-email (rum/react dis/app-state)))))
;                                (gobj/get (gobj/get (sel1 [:input.email]) "validity") "patternMismatch")
;                                (< (count (:pswd (:signup-with-email (rum/react dis/app-state)))) 8))
;                  :on-click #(do
;                               (utils/event-stop %)
;                               (dis/dispatch! [:signup-with-email]))}
;                 "Sign Up"]]]]]
;       [:div.login-overlay-footer.group
;         [:a.left {:on-click #(do (utils/event-stop %) (dis/dispatch! [:login-overlay-show :login-with-slack]))}
;           "Already have an account? "
;           [:span.blue-link "Sign In now."]]]]])

(rum/defcs password-reset < rum/reactive
                            (merge dont-scroll
                              {:did-mount (fn [s] (.focus (sel1 [:div.sign-in-field-container.email])) s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click (partial close-overlay)}
    [:button.carrot-modal-close.mlb-reset
        {:on-click (partial close-overlay)}]
    [:div.login-overlay.password-reset
      {:on-click #(utils/event-stop %)}
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Password Reset"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl3.pr3.pb2.group
        (when (contains? (:password-reset (rum/react dis/app-state)) :success)
          (cond
            (:success (:password-reset (rum/react dis/app-state)))
            [:div.green "We sent you an email with the instructions to reset your account password."]
            :else
            [:div.red "An error occurred, please try again."]))
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label "Please enter your email address"]]
          [:div.sign-in-field-container.email
            [:input.sign-in-field
              {:value (:email (:password-reset (rum/react dis/app-state)))
               :tabIndex 1
               :type "email"
               :autoCapitalize "none"
               :auto-focus true
               :on-change #(dis/dispatch! [:input [:password-reset :email] (.. % -target -value )])
               :name "email"}]]
          (if (:success (:password-reset (rum/react dis/app-state)))
            [:div.group.pb3.mt3
              [:div.right
                [:dubtton.mlb-reset.mlb-default
                  {:on-click #(dis/dispatch! [:login-overlay-show nil])}
                  "Done"]]]
            [:div.group.pb3.mt3
              [:div.right.ml1
                [:button.mlb-reset.mlb-default
                  {:on-click #(dis/dispatch! [:password-reset])
                   :disabled (not (utils/valid-email? (:email (:password-reset @dis/app-state))))}
                  "Reset Password"]]
              [:div.right
                [:button.mlb-reset.mlb-link-black
                  {:on-click #(dis/dispatch! [:login-overlay-show nil])
                   :style {:margin-top "6px"}
                   :disabled (not (:auth-settings (rum/react dis/app-state)))}
                  "Cancel"]]])]]]])

(rum/defcs collect-name-password < rum/reactive
                                   (merge
                                     dont-scroll
                                     {:did-mount (fn [s]
                                                   ; initialise the keys to string to avoid jumps in UI focus
                                                   (utils/after 500
                                                      #(dis/dispatch! [:input [:collect-name-pswd] {:firstname (or (:first-name (:current-user-data @dis/app-state)) "")
                                                                                                    :lastname (or (:last-name (:current-user-data @dis/app-state)) "")
                                                                                                    :pswd (or (:pswd (:collect-name-pswd @dis/app-state)) "")}]))
                                                   (utils/after 100 #(.focus (sel1 [:input.firstname])))
                                                   s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click #(utils/event-stop %)}
    [:div.login-overlay.collect-name-pswd.group
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Provide Your Name and a Password"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl3.pr3.pb2.group
        (when-not (nil? (:collect-name-pswd-error (rum/react dis/app-state)))
          [:span.small-caps.red
            "System troubles logging in."
            [:br]
            "Please try again, then "
            [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
            "."])
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "collect-name-pswd-firstname"} "Your Name"]]
          [:div.sign-in-field-container.group
            [:input.sign-in-field.firstname.half.left
              {:value (:firstname (:collect-name-pswd (rum/react dis/app-state)))
               :id "collect-name-pswd-firstname"
               :on-change #(dis/dispatch! [:input [:collect-name-pswd :firstname] (.-value (sel1 [:input.firstname]))])
               :placeholder "First name"
               :type "text"
               :tabIndex 1
               :name "firstname"}]
            [:input.sign-in-field.lastname.half.right
              {:value (:lastname (:collect-name-pswd (rum/react dis/app-state)))
               :id "collect-name-pswd-lastname"
               :on-change #(dis/dispatch! [:input [:collect-name-pswd :lastname] (.-value (sel1 [:input.lastname]))])
               :placeholder "Last name"
               :type "text"
               :tabIndex 2
               :name "lastname"}]]
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "signup-pswd"} "Password"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd
              {:value (:pswd (:collect-name-pswd (rum/react dis/app-state)))
               :id "collect-name-pswd-pswd"
               :on-change #(dis/dispatch! [:input [:collect-name-pswd :pswd] (.-value (sel1 [:input.pswd]))])
               :pattern ".{8,}"
               :placeholder "Minimum 8 characters"
               :type "password"
               :tabIndex 4
               :name "pswd"}]]
          [:div.group.my3
            [:div.right
              [:button.mlb-reset.mlb-default
                {:disabled (or (and (s/blank? (:firstname (:collect-name-pswd (rum/react dis/app-state))))
                                    (s/blank? (:lastname (:collect-name-pswd (rum/react dis/app-state)))))
                               (< (count (:pswd (:collect-name-pswd (rum/react dis/app-state)))) 8))
                 :on-click #(do
                              (utils/event-stop %)
                              (dis/dispatch! [:name-pswd-collect]))}
                "Let Me In"]]]]]]])

(rum/defcs collect-password < rum/reactive
                              (merge
                                dont-scroll
                                {:did-mount (fn [s]
                                              ; initialise the keys to string to avoid jumps in UI focus
                                              (utils/after 500
                                                 #(dis/dispatch! [:input [:collect-pswd] {:pswd (or (:pswd (:collect-pswd @dis/app-state)) "")}]))
                                             (utils/after 1000 #(when-let [pswd-el (sel1 [:input.sign-in-field.pswd])]
                                                                  (.focus pswd-el)))
                                             s)})
  [state]
  [:div.login-overlay-container.group
    {:on-click #(utils/event-stop %)}
    [:div.login-overlay.collect-pswd.group
      [:div.login-overlay-cta.pl2.pr2.group
        [:div.sign-in-cta "Enter your new password"
          (when-not (:auth-settings (rum/react dis/app-state))
            (small-loading))]]
      [:div.pt2.pl3.pr3.pb2.group
        (when-not (nil? (:collect-password-error (rum/react dis/app-state)))
          [:span.small-caps.red
            "System troubles logging in."
            [:br]
            "Please try again, then "
            [:a.underline.red {:href oc-urls/contact-mail-to} "contact support"]
            "."])
        [:form.sign-in-form
          [:div.sign-in-label-container
            [:label.sign-in-label {:for "signup-pswd"} "Password"]]
          [:div.sign-in-field-container
            [:input.sign-in-field.pswd
              {:value (:pswd (:collect-pswd (rum/react dis/app-state)))
               :id "collect-pswd-pswd"
               :on-change #(dis/dispatch! [:input [:collect-pswd :pswd] (.-value (sel1 [:input.pswd]))])
               :pattern ".{8,}"
               :placeholder "Minimum 8 characters"
               :type "password"
               :tabIndex 4
               :name "pswd"}]]
          [:div.group.my3
            [:div.right
              [:button.mlb-reset.mlb-default
                {:disabled (< (count (:pswd (:collect-pswd (rum/react dis/app-state)))) 8)
                 :on-click #(do
                              (utils/event-stop %)
                              (dis/dispatch! [:pswd-collect true]))}
                "Let Me In"]]]]]]])

(rum/defcs login-overlays-handler < rum/static
                                    rum/reactive
                                    (drv/drv :show-login-overlay)
  [s]
  (cond
    ; login via email
    (= (drv/react s :show-login-overlay) :login-with-email)
    (login-with-email)
    ; signup via email
    (= (drv/react s :show-login-overlay) :signup-with-email)
    (do
      (utils/after 150 #(router/nav! oc-urls/sign-up))
      [:div])
    ; password reset
    (= (drv/react s :show-login-overlay) :password-reset)
    (password-reset)
    ; form to collect name and password
    (= (drv/react s :show-login-overlay) :collect-name-password)
    (collect-name-password)
    ; form to insert a new password
    (= (drv/react s :show-login-overlay) :collect-password)
    (collect-password)
    ; login via slack as default
    (or (= (drv/react s :show-login-overlay) :login-with-slack)
        (= (drv/react s :show-login-overlay) :signup-with-slack))
    (login-signup-with-slack)
    ; show nothing
    :else
    [:div.hidden]))