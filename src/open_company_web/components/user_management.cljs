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
            [open-company-web.components.users-list :refer (users-list)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.team-disclaimer-popover :refer (team-disclaimer-popover)]
            [open-company-web.components.ui.popover :as popover :refer (add-popover-with-rum-component hide-popover)]))

(defn show-team-disclaimer-popover [e]
  (.stopPropagation e)
  (add-popover-with-rum-component team-disclaimer-popover {:hide-popover-cb #(hide-popover nil "team-disclaimer-popover")
                                                           :width 422
                                                           :height 230
                                                           :hide-on-click-out true
                                                           :z-index-popover 0
                                                           :container-id "team-disclaimer-popover"}))

(rum/defcs user-management < (rum/local nil ::last-user-type)
                             rum/reactive
                             (drv/drv :user-management)
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
                                               (when (and (nil? (:user-type (:um-invite @(drv/get-ref s :user-management))))
                                                          (utils/valid-email? (:email (:um-invite @(drv/get-ref s :user-management)))))
                                                  (dis/dispatch! [:invite-by-email-change :user-type :viewer]))
                                               s)
                             :did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (dis/dispatch! [:input [:um-invite :email] ""])
                                            (dis/dispatch! [:input [:um-invite :user-type] nil])
                                            (dis/dispatch! [:input [:um-domain-invite :domain] ""]))
                                          s)}
  [s]
  (let [{:keys [um-invite um-domain-invite enumerate-users add-email-domain-team-error add-slack-team-error invite-by-email-error] :as user-man} (drv/react s :user-management)
        ro-user-man @(drv/get-ref s :user-management)
        user-type (:user-type um-invite)
        valid-email? (utils/valid-email? (:email um-invite))
        valid-domain-email? (utils/valid-domain? (:domain um-domain-invite))
        team-id (router/current-team-id)
        team-data (get enumerate-users team-id)]
    [:div.user-management.mx-auto.p3.my4.group
      [:div.um-invite.group.mb3
        [:div.um-invite-label
          "INVITE TEAM MEMBERS"]
        [:div
          [:div.group
            [:input.left.um-invite-field.email
              {:name "um-invite"
               :type "text"
               :autoCapitalize "none"
               :value (:email um-invite)
               :on-change #(dis/dispatch! [:invite-by-email-change :email (.. % -target -value)])
               :placeholder "Email address"}]
            [:div.user-type-picker
              {:on-mouse-out #(let [el (or (.-toElement %) (.-relatedTarget %))]
                                ; mouseOut event is triggerend also when the mouse enter a child so we need to
                                ; check that it's not entering a child of this
                                (when (not (utils/is-parent? (sel1 [:div.user-type-picker]) el))
                                  ; reset the user-type to what was before the user enter this div with the mouse
                                  (dis/dispatch! [:invite-by-email-change :user-type @(::last-user-type s)])))}
              [:button.user-type-picker-btn.btn-reset.viewer
                {:class (if (= user-type :viewer) "active" "")
                 :on-mouse-over #(dis/dispatch! [:invite-by-email-change :user-type :viewer])
                 :on-click #(do (reset! (::last-user-type s) :viewer) (dis/dispatch! [:invite-by-email-change :user-type :viewer]))}
                (when (= user-type :viewer)
                  [:span.user-type-disc.viewer
                    {:on-click #(show-team-disclaimer-popover %)}
                    "VIEWER " [:i.fa.fa-question-circle]])
                [:i.fa.fa-user]]
              [:button.user-type-picker-btn.btn-reset.author
                {:class (if (= user-type :author) "active" "")
                 :on-mouse-over #(dis/dispatch! [:invite-by-email-change :user-type :author])
                 :on-click #(do (reset! (::last-user-type s) :author) (dis/dispatch! [:invite-by-email-change :user-type :author]))}
                (when (= user-type :author)
                  [:span.user-type-disc.author
                    {:on-click #(show-team-disclaimer-popover %)}
                    "AUTHOR " [:i.fa.fa-question-circle]])
                [:i.fa.fa-pencil]]
              [:button.user-type-picker-btn.btn-reset.admin
                {:class (if (= user-type :admin) "active" "")
                 :on-mouse-over #(dis/dispatch! [:invite-by-email-change :user-type :admin])
                 :on-click #(do (reset! (::last-user-type s) :admin) (dis/dispatch! [:invite-by-email-change :user-type :admin]))}
                (when (= user-type :admin)
                  [:span.user-type-disc.admin
                    {:on-click #(show-team-disclaimer-popover %)}
                    "ADMIN " [:i.fa.fa-question-circle]])
                [:i.fa.fa-gear]]]
            [:button.right.btn-reset.btn-solid.um-invite-send
              {:disabled (or (not valid-email?)
                             (not user-type))
               :on-click #(let [email (:email (:um-invite ro-user-man))]
                            (if (and (utils/valid-email? email)
                                     (not (nil? (:user-type (:um-invite ro-user-man)))))
                              (dis/dispatch! [:invite-by-email])
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
        [:div.mb3.um-invite.group
          [:div.um-invite-label
              "TEAM MEMBERS"]
          (when (jwt/is-slack-org?)
            [:div.um-invite-label-2
              "Members of your " [:img {:src "/img/Slack_Icon.png" :width 14 :height 14}] " Slack team (not guests)."])
          (let [first-team (first (jwt/get-key :teams))]
            (when (contains? enumerate-users first-team)
              (users-list team-id (:users (get enumerate-users first-team)))))])
      (when-not (responsive/is-mobile-size?)
        [:div.mb3.um-invite.group
          [:div.um-invite-label
              "SLACK TEAMS"]
          [:div.um-invite-label-2
            "Connect with Slack to seamlessly onboard your Stack teammates as viewers."]
          [:div.team-list
            (for [team (:slack-teams team-data)]
              [:div.slack-domain.group
                [:span (:name team)]
                [:button.btn-reset
                  {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)
                   :title "Remove Slack team"}
                  [:i.fa.fa-trash]]])]
          [:div.group
            [:button.btn-reset.mt2.add-slack-team.slack-button
                {:on-click #(dis/dispatch! [:add-slack-team])}
                "Add "
                [:span.slack "Slack"]
                " Team"]
            (when add-slack-team-error
              [:div
                (cond
                  (= add-slack-team-error :team-exists)
                  [:span.small-caps.red.mt1.left "This team was already added."]
                  :else
                  [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]])
      (when-not (responsive/is-mobile-size?)
        [:div.mb3.um-invite.group
          [:div.um-invite-label
              "TEAM EMAIL DOMAINS"]
          [:div.um-invite-label-2
            "People with email addresses in the specified domains can join your team as viewers."]
          [:div.team-list
            (for [team (:email-domains team-data)]
              [:div.email-domain.group
                [:span (str "@" (:domain team))]
                [:button.btn-reset
                  {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)
                   :title "Remove email domain team"}
                  [:i.fa.fa-trash]]])]
          [:div.group
            [:input.left.um-invite-field.email
              {:name "um-domain-invite"
               :type "text"
               :autoCapitalize "none"
               :value (:domain um-domain-invite)
               :pattern "@?[a-z0-9.-]+\\.[a-z]{2,4}$"
               :on-change #(dis/dispatch! [:input [:um-domain-invite :domain] (.. % -target -value)])
               :placeholder "Email domain"}]
            [:button.right.btn-reset.btn-solid.um-invite-send
              {:disabled (not valid-domain-email?)
               :on-click #(let [domain (:domain (:um-domain-invite ro-user-man))]
                            (if (utils/valid-domain? domain)
                              (dis/dispatch! [:add-email-domain-team])
                              (dis/dispatch! [:input [:add-email-domain-team-error] true])))}
             "ADD"]
            (when add-email-domain-team-error
              [:div
                (cond
                  (and (= add-email-domain-team-error :domain-exists)
                       (:domain um-domain-invite))
                  [:span.small-caps.red.mt1.left (str (:domain um-domain-invite) " was already added.")]
                  :else
                  [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]])
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
            [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]])]))

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