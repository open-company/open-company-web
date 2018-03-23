(ns oc.web.actions.team
  (:require [clojure.string :as string]
            [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.json :refer (json->cljs)]))

(defn roster-get [roster-link]
  (api/get-team roster-link
   (fn [{:keys [success body status]}]
     (let [fixed-body (when success (json->cljs body))]
       (if success
         (dis/dispatch! [:team-roster-loaded fixed-body]))))))

(defn enumerate-channels-cb [team-id {:keys [success body status]}]
  (let [fixed-body (when success (json->cljs body))
        channels (-> fixed-body :collection :items)]
    (if success
      (dis/dispatch! [:channels-enumerate/success team-id channels]))))

(defn enumerate-channels [team-data]
  (let [org-data (dis/org-data)
        team-id (:team-id team-data)]
    (when (= (:team-id org-data) team-id)
      (api/enumerate-channels team-id (partial enumerate-channels-cb team-id))
      (dis/dispatch! [:channels-enumerate team-id]))))

(defn team-get [team-link]
  (api/get-team team-link
    (fn [{:keys [success body status]}]
      (let [team-data (when success (json->cljs body))]
        (when success
          (enumerate-channels team-data)
          (dis/dispatch! [:team-loaded team-data]))))))

(defn read-teams [teams]
  (doseq [team teams
          :let [team-link (utils/link-for (:links team) "item")
                roster-link (utils/link-for (:links team) "roster")]]
    ; team link may not be present for non-admins, if so they can still get team users from the roster
    (when team-link
      (team-get team-link))
    (when roster-link
      (roster-get roster-link))))

(defn teams-get-cb [{:keys [success body status]}]
  (let [fixed-body (when success (json->cljs body))]
    (if success
      (let [teams (-> fixed-body :collection :items)]
        (read-teams teams)
        (dis/dispatch! [:teams-loaded (-> fixed-body :collection :items)]))
      ;; Reset the team-data-requested to restart the teams load
      (when (and (>= status 500)
                 (<= status 599))
        (dis/dispatch! [:input [:team-data-requested] false])))))

(defn teams-get [auth-settings]
  (when (utils/link-for (:links auth-settings) "collection")
    (api/get-teams auth-settings teams-get-cb)
    (dis/dispatch! [:teams-get])))

;; Invite users

(defn author-change-cb [{:keys [success]}]
  (when success
    ;; TODO: replace with action creator for get org
    (api/get-org (dis/org-data))))

(defn invite-user-success [user-data]
  ; refresh the users list once the invitation succeded
  (teams-get (dis/auth-settings))
  (dis/dispatch! [:invite-user/success user-data]))

(defn send-invitation-cb [invite-data user-type {:keys [success body]}]
  (if success
    ;; On successfull invitation
    (let [new-user (json->cljs body)]
      ;; If user was admin or author add him to the org as author
      (when (or (= user-type :author)
                (= user-type :admin))
        (api/add-author (:user-id new-user) author-change-cb))
      (invite-user-success invite-data))
    (dis/dispatch! [:invite-user/failed invite-data])))

(defn invite-user [org-data team-data invite-data]
  (let [invite-from (:type invite-data)
        email (:user invite-data)
        slack-user (:user invite-data)
        user-type (:role invite-data)
        parsed-email (when (= "email" invite-from) (utils/parse-input-email email))
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        ;; check if the user being invited by email is already present in the users list.
        ;; from slack is not possible to select a user already invited since they are filtered by status before
        user  (when (= invite-from "email")
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
                        :else "")]
        ;; If the user is already in the list
        ;; but the type changed we need to change the user type too
        (when (and user
                  (not= old-user-type user-type))
          (api/switch-user-type
           invite-data
           old-user-type
           user-type
           user
           (utils/get-author (:user-id user) (:authors org-data))))
        (api/send-invitation
         invite-data
         (if (= invite-from "email") email-address slack-user)
         invite-from user-type first-name last-name
         (partial send-invitation-cb invite-data user-type))
        {:success true})
      {:error "User already active" :success false})))

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

(defn invite-users [inviting-users]
  (let [org-data (dis/org-data)
        team-data (dis/team-data (:team-id org-data))
        checked-users (for [user inviting-users]
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
    (when (= (count cleaned-inviting-users) (count inviting-users))
      (doseq [user cleaned-inviting-users]
        (invite-user org-data team-data user)))
    (dis/dispatch! [:invite-users (vec checked-users)])))

;; User actions

(defn user-action-cb [_]
  (teams-get (dis/auth-settings)))

(defn user-action [team-id invitation action method other-link-params payload]
  (let [team-data (dis/team-data team-id)
        idx (.indexOf (:users team-data) invitation)]
    (when (> idx -1)
      (api/user-action (utils/link-for (:links invitation) action method other-link-params) payload user-action-cb)
      (dis/dispatch! [:user-action team-id idx]))))

;; Email domains

(defn email-domain-team-add-cb [{:keys [status body success]}]
  (when success
    (teams-get (dis/auth-settings)))
  (dis/dispatch! [:email-domain-team-add/finish (= status 204)]))

(defn email-domain-team-add [domain]
  (when (utils/valid-domain? domain)
    (api/add-email-domain (if (.startsWith domain "@") (subs domain 1) domain) email-domain-team-add-cb)
    (dis/dispatch! [:email-domain-team-add])))

;; Slack team add

(defn slack-team-add [current-user-data]
  (let [org-data (dis/org-data)
        team-id (:team-id org-data)
        team-data (dis/team-data team-id)
        add-slack-team-link (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
        fixed-add-slack-team-link (utils/slack-link-with-state
                                   (:href add-slack-team-link)
                                   (:user-id current-user-data)
                                   team-id
                                   (router/get-token))]
    (when fixed-add-slack-team-link
      (router/redirect! fixed-add-slack-team-link))))

;; Remove team

(defn remove-team [team-links]
  (api/user-action (utils/link-for team-links "remove" "DELETE") nil user-action-cb))