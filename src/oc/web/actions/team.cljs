(ns oc.web.actions.team
  (:require [clojure.string :as string]
            [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.utils.user :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.actions.org :as org-actions]
            [oc.web.lib.json :refer (json->cljs)]))

(defn roster-get [roster-link]
  (api/get-team roster-link
   (fn [{:keys [success body status]}]
     (let [fixed-body (when success (json->cljs body))]
       (if success
         (let [users (-> fixed-body :collection :items)
               fixed-roster-data {:team-id (:team-id fixed-body)
                                  :links (-> fixed-body :collection :links)
                                  :users users}]
           (dis/dispatch! [:team-roster-loaded (router/current-org-slug) fixed-roster-data])
           ;; The roster is also used by the WRT component to show the unseen, rebuild the unseen lists
           (let [activities-read (dis/activity-read-data)]
             (doseq [[activity-uuid read-data] activities-read]
               (dis/dispatch! [:activity-reads (router/current-org-slug) activity-uuid (:count read-data) (:reads read-data) fixed-roster-data])))))))))

(defn enumerate-channels-cb [team-id {:keys [success body status]}]
  (let [fixed-body (when success (json->cljs body))
        channels (-> fixed-body :collection :items)]
    (if success
      (dis/dispatch! [:channels-enumerate/success team-id channels]))))

(defn enumerate-channels [team-data]
  (let [org-data (dis/org-data)
        team-id (:team-id team-data)]
    (when team-id
      (let [enumerate-link (utils/link-for (:links team-data) "channels" "GET")]
        (api/enumerate-channels enumerate-link (partial enumerate-channels-cb team-id))
        (dis/dispatch! [:channels-enumerate team-id])))))

(defn team-get [team-link]
  (api/get-team team-link
    (fn [{:keys [success body status]}]
      (let [team-data (when success (json->cljs body))]
        (when success
          (dis/dispatch! [:team-loaded (router/current-org-slug) team-data])
          (utils/after 100 org-actions/maybe-show-integration-added-notification?)
          (enumerate-channels team-data))))))

(defn force-team-refresh [team-id]
  (when-let [team-data (dis/team-data team-id)]
    (when-let [team-link (utils/link-for (:links team-data) ["self" "item"] "GET")]
      (team-get team-link))
    (when-let [roster-link (utils/link-for (:links team-data) "roster")]
      (roster-get roster-link))))

(defn read-teams [teams]
  (let [current-panel (last (:panel-stack @dis/app-state))
        load-delay (if (#{:org :integrations :team :invite-picker :invite-email :invite-slack} current-panel)
                     0
                     2500)]
    (doseq [team teams
            :let [team-link (utils/link-for (:links team) "item")
                  channels-link (utils/link-for (:links team) "channels")
                  roster-link (utils/link-for (:links team) "roster")]]
      ; team link may not be present for non-admins, if so they can still get team users from the roster
      (if team-link
        (utils/maybe-after load-delay #(team-get team-link))
        (when channels-link
          (utils/maybe-after load-delay #(enumerate-channels team))))
      ;; Do not delay the roster load since it's needed for the mentions extention
      ;; that needs to be initialized with the rich-body-editor or the add-comment components
      (when roster-link
        (roster-get roster-link)))))

(defn teams-get-cb [{:keys [success body status]}]
  (let [fixed-body (when success (json->cljs body))]
    (if success
      (let [teams (-> fixed-body :collection :items)]
        (dis/dispatch! [:teams-loaded (-> fixed-body :collection :items)])
        (read-teams teams))
      ;; Reset the team-data-requested to restart the teams load
      (when (<= 500 status 599)
        (dis/dispatch! [:input [:team-data-requested] false])))))

(defn teams-get []
  (let [auth-settings (dis/auth-settings)]
    (when-let [enumerate-link (utils/link-for (:links auth-settings) "collection" "GET")]
      (api/get-teams enumerate-link teams-get-cb)
      (dis/dispatch! [:teams-get]))))

(defn teams-get-if-needed []
  (let [auth-settings (dis/auth-settings)
        teams-data-requested (dis/teams-data-requested)
        teams-data (dis/teams-data)]
    (when (and (empty? teams-data)
               auth-settings
               (not teams-data-requested))
      (teams-get))))

;; Invite users

;; Authors

(defn author-change-cb [{:keys [success]}]
  (when success
    (org-actions/get-org nil true)))

(defn remove-author [author]
  (let [remove-author-link (utils/link-for (:links author) "remove")]
    (api/remove-author remove-author-link author author-change-cb)))

(defn add-author [author]
  (let [add-author-link (utils/link-for (:links (dis/org-data)) "add")]
    (api/add-author add-author-link (:user-id author) author-change-cb)))

;; Admins

(defn admin-change-cb [user {:keys [success]}]
  (if success
    (do
      (teams-get)
      (dis/dispatch! [:invite-user/success user]))
    (dis/dispatch! [:invite-user/failed user])))

(defn add-admin [user]
  (let [add-admin-link (utils/link-for (:links user) "add")]
    (api/add-admin add-admin-link user (partial admin-change-cb user))))

(defn remove-admin [user]
  (let [remove-admin-link (utils/link-for (:links user) "remove" "DELETE"
                           {:ref "application/vnd.open-company.team.admin.v1"})]
    (api/remove-admin remove-admin-link user (partial admin-change-cb user))))

;; Invite user callbacks

(defn invite-user-failed [user-data]
  (dis/dispatch! [:invite-user/failed user-data]))

(defn invite-user-success [user-data]
  ; refresh the users list once the invitation succeded
  (teams-get)
  (dis/dispatch! [:invite-user/success user-data]))

;; Switch user-type

(defn switch-user-type-cb [user-data {:keys [success]}]
  (if success
    (invite-user-success user-data)
    (invite-user-failed user-data)))

(defn switch-user-type
  "Given an existing user switch user type"
  [complete-user-data old-user-type new-user-type user & [author-data]]
  (when (not= old-user-type new-user-type)
    (let [org-data           (dis/org-data)
          fixed-author-data  (or author-data
                              (utils/get-author (:user-id user) (:authors org-data)))
          add-admin?         (= new-user-type :admin)
          remove-admin?      (= old-user-type :admin)
          add-author?        (or (= new-user-type :author)
                                 (= new-user-type :admin))
          remove-author?     (= new-user-type :viewer)]
      ;; Add an admin call
      (when add-admin?
        (add-admin user))
      ;; Remove admin call
      (when remove-admin?
        (remove-admin user))
      ;; Add author call
      (when add-author?
        (add-author user))
      ;; Remove author call
      (when remove-author?
        (remove-author fixed-author-data)))))

;; Invite user

(defn send-invitation-cb [invite-data user-type {:keys [success body]}]
  (if success
    ;; On successfull invitation
    (let [new-user (json->cljs body)]
      ;; If user was admin or author add him to the org as author
      (when (or (= user-type :author)
                (= user-type :admin))
        (add-author new-user))
      (invite-user-success invite-data))
    (invite-user-failed invite-data)))

(defn invite-user [org-data team-data invite-data note]
  (let [invite-from (:type invite-data)
        email (:user invite-data)
        slack-user (:user invite-data)
        user-type (:role invite-data)
        parsed-email (when (= "email" invite-from) (utils/parse-input-email email))
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        ;; check if the user being invited by email is already present in the users list.
        ;; from slack is not possible to select a user already invited since they are filtered by status before
        user (when (= invite-from "email")
               (first (filter #(= (:email %) email-address) (:users team-data))))
        old-user-type (when user (utils/get-user-type user org-data))]
    ;; Send the invitation only if the user is not part of the team already
    ;; or if it's still pending, ie resend the invitation email
    (if (or (not user)
            (and user
                 (= (string/lower-case (:status user)) "pending")))
      (let [splitted-name (string/split email-name #"\s")
            name-size (count splitted-name)
            splittable-name? (= name-size 2)
            first-name (cond
                        (and (= invite-from "email") (= name-size 1)) email-name
                        (and (= invite-from "email") splittable-name?) (first splitted-name)
                        (and (= invite-from "slack") (seq (:first-name slack-user))) (:first-name slack-user)
                        :else "")
            last-name (cond
                        (and (= invite-from "email") splittable-name?) (second splitted-name)
                        (and (= invite-from "slack") (seq (:last-name slack-user))) (:last-name slack-user)
                        :else "")
            user-value (if (= invite-from "email") email-address slack-user)]
        ;; If the user is already in the list
        ;; but the type changed we need to change the user type too
        (when (and user
                  (not= old-user-type user-type))
          (switch-user-type invite-data old-user-type user-type user))
        (let [team-data (or (dis/team-data)
                            (first (filter #(= (:team-id org-data) (:team-id %))
                                           (dis/teams-data))))
              invitation-link (utils/link-for (:links team-data) "add" "POST"
                               {:content-type "application/vnd.open-company.team.invite.v1"})]
          (api/send-invitation invitation-link user-value invite-from user-type
           first-name last-name note (partial send-invitation-cb invite-data user-type)))))))

;; Invite user helpers

(defn valid-inviting-user? [user]
  (or (and (= "email" (:type user))
           (utils/valid-email? (:user user)))
      (and (= "slack" (:type user))
           (map? (:user user))
           (contains? (:user user) :slack-org-id)
           (contains? (:user user) :slack-id))))

(defn duplicated-email-addresses [user users-list]
  (when (= (:type user) "email")
    (> (count (filter #(= (:user %) (:address (utils/parse-input-email (:user user)))) users-list)) 1)))

(defn duplicated-team-user [user users-list]
  (when (= (:type user) "email")
    (let [parsed-email (utils/parse-input-email (:user user))
          dup-user (first (filter #(= (:email %) (:address parsed-email)) users-list))]
      (and dup-user
           (not= (string/lower-case (:status dup-user)) "pending")))))

;; Invite users

(defn invite-users [inviting-users & [note]]
  (let [note (or note "")
        org-data (dis/org-data)
        team-data (dis/team-data (:team-id org-data))
        filter-empty (filterv #(seq (:user %)) inviting-users)
        checked-users (for [user filter-empty]
                        (let [valid? (valid-inviting-user? user)
                              intive-duplicated? (duplicated-email-addresses user inviting-users)
                              team-duplicated? (duplicated-team-user user (:users team-data))]
                          (cond
                            (not valid?)
                            (merge user {:error true :success false})
                            team-duplicated?
                            (merge user {:error "User already active" :success false})
                            intive-duplicated?
                            (merge user {:error "Duplicated email address" :success false})
                            :else
                            (dissoc user :error))))
        cleaned-inviting-users (filterv #(not (:error %)) checked-users)]
    (when (<= (count cleaned-inviting-users) (count filter-empty))
      (doseq [user cleaned-inviting-users]
        (invite-user org-data team-data user note)))
    (dis/dispatch! [:invite-users (vec checked-users)])))

;; User actions

(defn user-action-cb [_]
  (teams-get))

(defn user-action [team-id invitation action method other-link-params payload & [finished-cb]]
  (let [team-data (dis/team-data team-id)
        idx (.indexOf (:users team-data) invitation)]
    (when (> idx -1)
      (api/user-action (utils/link-for (:links invitation) action method other-link-params) payload
       #(do
          (when (fn? finished-cb)
            (finished-cb team-id invitation action method other-link-params payload %))
          (user-action-cb %)))
      (dis/dispatch! [:user-action team-id idx]))))

;; Email domains

(defn email-domain-team-add-cb [{:keys [status body success]}]
  (when success
    (teams-get))
  (dis/dispatch! [:email-domain-team-add/finish (= status 204)]))

(defn- add-email-domain-link [team-data-links]
  (utils/link-for
   team-data-links
   "add"
   "POST"
   {:content-type "application/vnd.open-company.team.email-domain.v1+json"}))

(defn email-domain-team-add [domain cb]
  (when (utils/valid-domain? domain)
    (let [team-data (dis/team-data)
          add-email-domain-link (add-email-domain-link (:links team-data))
          fixed-domain (if (.startsWith domain "@") (subs domain 1) domain)]
      (api/add-email-domain add-email-domain-link fixed-domain
       (fn [{:keys [success] :as resp}]
        (email-domain-team-add-cb resp)
        (when (fn? cb)
         (cb success)))
       team-data))
    (dis/dispatch! [:email-domain-team-add])))

(defn can-add-email-domain? [& [team-data]]
  (let [fixed-team-data (or team-data (dis/team-data))]
    (seq (add-email-domain-link (:links fixed-team-data)))))

;; Slack team add

(defn slack-team-add [current-user-data & [redirect-to]]
  (let [org-data (dis/org-data)
        team-id (:team-id org-data)
        team-data (dis/team-data team-id)
        add-slack-team-link (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
        redirect (or redirect-to (router/get-token))
        with-add-team (js/encodeURIComponent
                        (if (> (.indexOf redirect "?") -1)
                          (str redirect "&add=team")
                          (str redirect "?add=team")))
        fixed-add-slack-team-link (uu/auth-link-with-state
                                   (:href add-slack-team-link)
                                   {:user-id (:user-id current-user-data)
                                    :team-id team-id
                                    :redirect with-add-team})]
    (when fixed-add-slack-team-link
      (router/redirect! fixed-add-slack-team-link))))

;; Remove team

(defn remove-team [team-links & [cb]]
  (api/user-action (utils/link-for team-links "remove" "DELETE") nil
   (fn [{:keys [status body success] :as resp}]
    (when (fn? cb)
      (cb success))
    (user-action-cb resp))))

;; Invite team link handling

(defn create-invite-token-link [create-token-link & [cb]]
  (when create-token-link
    (api/handle-invite-link create-token-link
     (fn [{:keys [body success status]}]
      (when success
        (dis/dispatch! [:team-loaded (router/current-org-slug) (json->cljs body)])
        (when (fn? cb)
          (cb success)))))))

(defn delete-invite-token-link [delete-invite-link & [cb]]
  (when delete-invite-link
    (api/handle-invite-link delete-invite-link
     (fn [{:keys [body success status]}]
      (when success
        (dis/dispatch! [:team-loaded (router/current-org-slug) (json->cljs body)])
        (when (fn? cb)
          (cb success)))))))