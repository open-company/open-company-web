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
            [open-company-web.components.ui.footer :as footer]
            [open-company-web.components.ui.login-required :refer (login-required)]
            [open-company-web.components.user-invitation :refer (user-invitation)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(rum/defcs user-management < (rum/local false ::public)
                             (rum/local false ::has-changes)
                             rum/reactive
                             (drv/drv :company-data)
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
                                               s)
                             :did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (dis/dispatch! [:input [:um-invite :email] ""]))
                                          (let [public (::public s)]
                                            (reset! public (not (not (:public (dis/company-data))))))
                                          s)}
  [{:keys [::has-changes ::public] :as s}]
  [:div.user-management.lg-col-5.md-col-7.col-11.mx-auto.p3.mt4.mb4.group
    {:style {:background-color "rgba(78, 90, 107, 0.05)"}}
    [:div.group
      [:div.vis-cta.pb2 "Visibility"]
      [:div.vis-input-line
        [:label {:for "private-company"} "Private - "]
        [:input {:type "checkbox" :id "private-company" :name "visibility" :checked (not @public) :on-change #(do (reset! has-changes true) (reset! public (not @public)))}]]
      [:p.vis-p
        "The board is private. Only people added to the board can view and edit it."]
      [:div.vis-input-line
        [:label {:for "public-company"} "Public - "]
        [:input {:type "checkbox" :id "public-company" :name "visibility" :checked @public :on-change #(do (reset! has-changes true) (reset! public (not @public)))}]]
      [:p.vis-p
        "The board is public and will show up in search engines like Google. Only people added to the board can edit it."]
      [:button.right.btn-reset.btn-solid
        {:disabled (not @has-changes)
         :on-click #(do
                      (reset! has-changes false)
                      (api/patch-company (router/current-company-slug) {:public @public :promoted (not @public)}))} "SAVE"]]
    [:div.mt3
      [:div.um-cta.pb2 "Users & Invitations"]
      [:div.um-description
        [:p.um-p
          "Users can add and edit topics, and they can share updates with others."]
        (when (= (jwt/get-key :auth-source) "slack")
          [:p.um-p
            "All members of your Slack organization (not guests) can authenticate as users. You can also invite users to join by email."])]
      [:div.my4.um-invite.group
        [:div.um-invite-label
          "INVITE USER BY EMAIL"]
          [:div
            [:div.group
              [:input.left.um-invite-field.email
                {:name "um-invite"
                 :type "email"
                 :autoCapitalize "none"
                 :value (:email (:um-invite (rum/react dis/app-state)))
                 :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                 :on-change #(dis/dispatch! [:invite-by-email-change (.. % -target -value)])
                 :placeholder "Email address"}]
              [:button.right.btn-reset.btn-solid.um-invite-send
                {:disabled (not (utils/valid-email? (:email (:um-invite (rum/react dis/app-state)))))
                 :on-click #(let [email (:email (:um-invite @dis/app-state))]
                              (if (utils/valid-email? email)
                                (dis/dispatch! [:invite-by-email email])
                                (dis/dispatch! [:input [:invite-by-email-error] true])))}
               "SEND INVITE"]]
          (when (:invite-by-email-error (rum/react dis/app-state))
            [:div
              (cond
                (and (= (:invite-by-email-error (rum/react dis/app-state)) :user-exists)
                     (:email (:um-invite @dis/app-state)))
                [:span.small-caps.red.mt1.left (str (:email (:um-invite @dis/app-state)) " is already a user.")]
                :else
                [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]]
      (when (pos? (count (:enumerate-users (rum/react dis/app-state))))
        (user-invitation (:enumerate-users (rum/react dis/app-state))))
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
            [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]])]])

(defcomponent visibility [data owner]
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
            (back-to-dashboard-btn {:title "Visibility & User Management"})
            (dom/div {:class "company-settings-container"}
              (user-management))))

        (om/build footer/footer data)))))