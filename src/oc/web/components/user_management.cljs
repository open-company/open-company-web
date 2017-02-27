(ns oc.web.components.user-management
  (:require [rum.core :as rum]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.users-list :refer (users-list)]
            [oc.web.components.ui.footer :as footer]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.components.ui.user-type-picker :refer (user-type-picker)]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(rum/defcs user-management < rum/reactive
                             (drv/drv :user-management)
                             (drv/drv :org-data)
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
                                               (when (and (nil? (:user-type (:um-invite @(drv/get-ref s :user-management))))
                                                          (utils/valid-email? (:email (:um-invite @(drv/get-ref s :user-management)))))
                                                  (dis/dispatch! [:input [:um-invite :user-type] :viewer]))
                                               s)
                             :did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (dis/dispatch! [:input [:um-invite] {:email ""
                                                                                 :user-type nil
                                                                                 :error nil
                                                                                 :domain ""}]))
                                          s)}
  [s]
  (let [{:keys [um-invite um-domain-invite enumerate-users add-email-domain-team-error] :as user-man} (drv/react s :user-management)
        ro-user-man @(drv/get-ref s :user-management)
        org-data @(drv/get-ref s :org-data)
        user-type (:user-type um-invite)
        valid-email? (utils/valid-email? (:email um-invite))
        valid-domain-email? (utils/valid-domain? (:domain um-domain-invite))
        team-id (:team-id org-data)
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
               :on-change #(dis/dispatch! [:input [:um-invite :email] (.. % -target -value)])
               :placeholder "Email address"}]
            (user-type-picker user-type valid-email? #(dis/dispatch! [:input [:um-invite :user-type] %]) true)
            [:button.right.btn-reset.btn-solid.um-invite-send
              {:disabled (or (not valid-email?)
                             (not user-type))
               :on-click #(let [email (:email (:um-invite ro-user-man))]
                            (if (and (utils/valid-email? email)
                                     (not (nil? (:user-type (:um-invite ro-user-man)))))
                              (dis/dispatch! [:invite-by-email])
                              (dis/dispatch! [:input [:um-invite :error] true])))}
             "SEND INVITE"]]
          (when (:error um-invite)
            [:div
              (cond
                (and (= (:error um-invite) :user-exists)
                     (:email um-invite))
                [:span.small-caps.red.mt1.left (str (:email um-invite) " is already a user.")]
                :else
                [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]]
      (when-not (responsive/is-mobile-size?)
        [:div.mb3.um-invite.group
          [:div.um-invite-label "TEAM MEMBERS"]
          (when (contains? enumerate-users team-id)
            (users-list team-id (:users (get enumerate-users team-id)) (:authors org-data)))])
      (when-not (responsive/is-mobile-size?)
        [:div.mb3.um-invite.group
          [:div.um-invite-label
              "SLACK TEAMS"]
          [:div.um-invite-label-2
            "Connect with Slack to seamlessly onboard your Slack teammates as viewers."]
          [:div.team-list
            (for [team (:slack-orgs team-data)]
              [:div.slack-domain.group
                {:key (str "slack-org-" (:slack-org-id team))}
                [:span (:name team)]
                (when-let [add-bot-link (utils/link-for (:links team) "bot" "GET" {:auth-source "slack"})]
                  (let [fixed-add-bot-link (clojure.string/replace (:href add-bot-link) team-id (str team-id ":" (:slug org-data)))]
                    [:button.btn-reset
                      {:on-click #(router/redirect! fixed-add-bot-link)
                       :title "Add Slack bot to this team"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"}
                       [:i.fa.fa-slack]]))
                [:button.btn-reset
                  {:on-click #(api/user-action (utils/link-for (:links team) "remove" "DELETE") nil)
                   :title "Remove Slack team"
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"}
                  [:i.fa.fa-trash]]])]
          [:div.group
            [:button.btn-reset.mt2.add-slack-team.slack-button
                {:on-click #(dis/dispatch! [:add-slack-team])}
                "Add "
                [:span.slack "Slack"]
                " Team"]
            (when (contains? user-man :slack-access)
              [:div
                (cond
                  (= (keyword (:slack-access user-man)) :team-exists)
                  [:span.small-caps.red.mt1.left "This team was already added."]
                  (or (not (:slack-access user-man))
                      (= (:slack-access user-man) "failed"))
                  [:span.small-caps.red.mt1.left "An error occurred, please try again."]
                  :else
                  [:span.small-caps.green.mt1.left "Bot succesfully added."])])]])
      (when-not (responsive/is-mobile-size?)
        [:div.mb3.um-invite.group
          [:div.um-invite-label
              "TEAM EMAIL DOMAINS"]
          [:div.um-invite-label-2
            "Anyone who signs up with this email domain will be a viewer on your team."]
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
    (let [company-data (dis/board-data data)]

      (when (:read-only company-data)
        (router/redirect! (oc-urls/board)))

      (dom/div {:class "main-company-settings fullscreen-page"}

        (cond
          ;; the data is still loading
          (:loading data)
          (dom/div (dom/h4 "Loading data..."))

          (get-in data (conj (dis/board-data-key (router/current-org-slug) (router/current-board-slug)) :error))
          (login-required)

          ;; Company profile
          :else
          (dom/div {}
            (back-to-dashboard-btn {:title (if (responsive/is-mobile-size?) "Invite" "Invite and Manage Team")})
            (dom/div {:class "company-settings-container"}
              (user-management))))

        (let [columns-num (responsive/columns-num)
              card-width (responsive/calc-card-width)]
         (footer/footer (responsive/total-layout-width-int card-width columns-num)))))))