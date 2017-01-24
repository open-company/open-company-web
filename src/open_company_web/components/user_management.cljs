(ns open-company-web.components.user-management
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.urls :as oc-urls]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dis]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :as footer]
            [open-company-web.components.ui.login-required :refer (login-required)]
            [open-company-web.components.user-invitation :refer (user-invitation)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.team-disclaimer-popover :refer (team-disclaimer-popover)]
            [open-company-web.components.ui.popover :as popover :refer (add-popover-with-rum-component hide-popover)]))

(defn show-team-disclaimer-popover []
  (add-popover-with-rum-component team-disclaimer-popover {:hide-popover-cb #(hide-popover nil "team-disclaimer-popover")
                                                           :width 422
                                                           :height 230
                                                           :hide-on-click-out true
                                                           :z-index-popover 0
                                                           :container-id "team-disclaimer-popover"}))

(rum/defcs user-management < rum/static
                             rum/reactive
                             (drv/drv :user-management)
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
                                               s)
                             :did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (dis/dispatch! [:input [:um-invite :email] ""]))
                                          s)}
  [s]
  (let [{:keys [um-invite enumerate-users invite-by-email-error] :as user-man} (drv/react s :user-management)
        ro-user-man @(drv/get-ref s :user-management)
        user-type (:user-type um-invite)
        valid-email? (utils/valid-email? (:email um-invite))]
    [:div.user-management.mx-auto.p3.my4.group
      [:div
        [:div.mb3.um-invite.group
          [:div.um-invite-label
            "INVITE TEAM MEMBERS"]
            [:div
              [:div.group
                [:input.left.um-invite-field.email
                  {:name "um-invite"
                   :type "email"
                   :autoCapitalize "none"
                   :value (:email um-invite)
                   :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                   :on-change #(dis/dispatch! [:invite-by-email-change :email (.. % -target -value)])
                   :placeholder "Email address"}]
                [:div.user-type-picker
                  (when (= user-type :viewer)
                    [:span.user-type-disc.viewer
                      {:on-click #(show-team-disclaimer-popover)}
                      "VIEWER " [:i.fa.fa-question-circle]])
                  [:button.user-type-picker-btn.btn-reset
                    {:class (if (= user-type :viewer) "active" "")
                     :on-click #(dis/dispatch! [:invite-by-email-change :user-type :viewer])}
                    [:i.fa.fa-user]]
                  (when (= user-type :author)
                    [:span.user-type-disc.author
                      {:on-click #(show-team-disclaimer-popover)}
                      "AUTHOR " [:i.fa.fa-question-circle]])
                  [:button.user-type-picker-btn.btn-reset
                    {:class (if (= user-type :author) "active" "")
                     :on-click #(dis/dispatch! [:invite-by-email-change :user-type :author])}
                    [:i.fa.fa-pencil]]
                  (when (= user-type :admin)
                    [:span.user-type-disc.admin
                      {:on-click #(show-team-disclaimer-popover)}
                      "ADMIN " [:i.fa.fa-question-circle]])
                  [:button.user-type-picker-btn.btn-reset
                    {:class (if (= user-type :admin) "active" "")
                     :on-click #(dis/dispatch! [:invite-by-email-change :user-type :admin])}
                    [:i.fa.fa-gear]]]
                [:button.right.btn-reset.btn-solid.um-invite-send
                  {:disabled (or (not valid-email?)
                                 (not user-type))
                   :on-click #(let [email (:email (:um-invite ro-user-man))]
                                (if (utils/valid-email? email)
                                  (dis/dispatch! [:invite-by-email email])
                                  (dis/dispatch! [:input [:invite-by-email-error] true])))}
                 "SEND INVITE"]]
            (when invite-by-email-error
              [:div
                (cond
                  (and (= invite-by-email-error :user-exists)
                       (:email um-invite))
                  [:span.small-caps.red.mt1.left (str (:email um-invite) " is already a user.")]
                  :else
                  [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]]
      (when-not (responsive/is-mobile-size?)
        [:div.um-invite.group
          [:div.um-invite-label
              "TEAM MEMBERS"]
          (when (jwt/is-slack-org?)
            [:div.um-invite-label-2
              "Members of your " [:img {:src "/img/Slack_Icon.png" :width 14 :height 14}] " Slack team (not guests)."])
          (when (pos? (count enumerate-users))
            (user-invitation enumerate-users))])
      (comment
        [:div.my2.um-byemail-container.group
          [:div.group
            [:span.left.ml1.um-byemail-anyone-span
              "ANYONE WITH THIS EMAIL DOMAIN HAS USER ACCESS"]]
          [:div.mt2.um-byemail.group
            [:span.left.um-byemail-at "@"]
            [:input.left.um-byemail-email
              {:type "text"
               :name "um-byemail-email"
               :placeholder "your email domain without @"}]
            [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]])]]))

(defcomponent user-management-wrapper [data owner]
  (render [_]
    (let [company-data (dis/company-data data)]

      (when (:read-only company-data)
        (router/redirect! (oc-urls/company)))

      (dom/div {:class "main-company-settings fullscreen-page"}

        (cond
          ;; the data is still loading
          (:loading data)
          (dom/div (dom/h4 "Loading data..."))

          (get-in data [(keyword (router/current-company-slug)) :error])
          (login-required)

          ;; Company profile
          :else
          (dom/div {}
            (back-to-dashboard-btn {:title (if (responsive/is-mobile-size?) "Invite" "Invite and Manage Team")})
            (dom/div {:class "company-settings-container"}
              (user-management))))

        (let [columns-num (responsive/columns-num)
              card-width (responsive/calc-card-width)]
         (om/build footer/footer (assoc data :footer-width (responsive/total-layout-width-int card-width columns-num))))))))