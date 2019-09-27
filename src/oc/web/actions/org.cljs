(ns oc.web.actions.org
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.utils.user :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.actions.comment :as ca]
            [oc.web.actions.section :as sa]
            [oc.web.actions.activity :as aa]
            [oc.web.lib.fullstory :as fullstory]
            [oc.web.lib.chat :as chat]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.ws.notify-client :as ws-nc]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.notifications :as notification-actions]))

;; User related functions
;; FIXME: these functions shouldn't be here but calling oc.web.actions.user from here is causing a circular dep

(defn- get-ap-url [org-slug]
  (let [first-ever-ap-name (router/first-ever-ap-land-cookie (jwt/user-id))
        first-ever-ap (cook/get-cookie first-ever-ap-name)]
    (if first-ever-ap
      (oc-urls/all-posts org-slug)
      (do
        (cook/remove-cookie! first-ever-ap-name)
        (oc-urls/first-ever-all-posts org-slug)))))

(defn bot-auth [team-data user-data & [redirect-to]]
  (let [redirect (or redirect-to (router/get-token))
        auth-link (utils/link-for (:links team-data) "bot")
        fixed-auth-url (uu/auth-link-with-state (:href auth-link)
                                                {:user-id (:user-id user-data)
                                                 :team-id (:team-id team-data)
                                                 :redirect redirect})]
    (router/redirect! fixed-auth-url)))

(defn maybe-show-integration-added-notification? []
  ;; Do we need to show the add bot banner?
  (when-let* [org-data (dis/org-data)
              bot-access (dis/bot-access)]
    (when (= bot-access "bot")
      (notification-actions/show-notification {:title "Carrot Bot enabled"
                                                      :primary-bt-title "OK"
                                                      :primary-bt-dismiss true
                                                      :expire 5
                                                      :id :slack-bot-added}))
    (when (and (= bot-access "team")
               (not= (:new (router/query-params)) "true"))
      (notification-actions/show-notification {:title "Integration added"
                                                      :primary-bt-title "OK"
                                                      :primary-bt-dismiss true
                                                      :expire 5
                                                      :id :slack-team-added}))
    (dis/dispatch! [:input [:bot-access] nil])))

;; Org get
(defn check-org-404 []
  (let [orgs (dis/orgs-data)]
    ;; avoid infinite loop of the Go to digest button
    ;; by changing the value of the last visited slug
    (if (pos? (count orgs))
      (cook/set-cookie! (router/last-org-cookie) (:slug (first orgs)) cook/default-cookie-expire)
      (cook/remove-cookie! (router/last-org-cookie)))
    (routing-actions/maybe-404)))

(def default-board "all-posts")

(defn get-default-board [org-data]
  (let [last-board-slug default-board]
    ; Replace default-board with the following to go back to the last visited board
    ; (or (cook/get-cookie (router/last-board-cookie (:slug org-data))) default-board)]
    (if (and (= last-board-slug "all-posts")
             (utils/link-for (:links org-data) "activity"))
      {:slug "all-posts"}
      (let [boards (:boards org-data)
            board (first (filter #(= (:slug %) last-board-slug) boards))]
        (or
          ; Get the last accessed board from the saved cookie
          board
          (let [sorted-boards (vec (sort-by :name boards))]
            (first sorted-boards)))))))

(def other-resources-delay 10000)

(defn org-loaded
  "Dispatch the org data into the app-state to be used by all the components.
   Do all the needed loading when the org data are loaded if complete-refresh? is true.
   The saved? flag is used as a strict boolean, if it's nil it means no org data PATCH happened, false
   means that the save went wrong, true went well."
  [org-data & [saved? email-domain complete-refresh?]]
  ;; Save the last visited org
  (when (and org-data
             (= (router/current-org-slug) (:slug org-data)))
    (cook/set-cookie! (router/last-org-cookie) (:slug org-data) cook/default-cookie-expire))
  ;; Check the loaded org
  (let [boards (:boards org-data)
        activity-link (utils/link-for (:links org-data) "entries")
        recent-activity-link (utils/link-for (:links org-data) "activity")
        follow-ups-link (utils/link-for (:links org-data) "follow-ups")
        recent-follow-ups-link (utils/link-for (:links org-data) "follow-ups-activity")
        is-all-posts? (= (router/current-board-slug) "all-posts")
        is-follow-ups? (= (router/current-board-slug) "follow-ups")
        activity-delay (if is-all-posts?
                         0
                         other-resources-delay)
        section-delay (if (and (not is-all-posts?)
                                       (not is-follow-ups?))
                                0
                                other-resources-delay)
        follow-ups-delay (if is-follow-ups?
                           0
                           other-resources-delay)]
    (when complete-refresh?
      ;; Load secure activity
      (if (router/current-secure-activity-id)
        (aa/secure-activity-get)
        (do
          ;; Load the current activity
          (when (router/current-activity-id)
            (cmail-actions/get-entry-with-uuid (router/current-board-slug) (router/current-activity-id)))
          (utils/maybe-after section-delay #(sa/load-other-sections (:boards org-data)))
          ;; Preload all posts data
          (when activity-link
            (utils/maybe-after activity-delay #(aa/activity-get org-data)))
          (when recent-activity-link
            (utils/maybe-after activity-delay #(aa/recent-activity-get org-data)))
          ;; Preload follow-ups data
          (when follow-ups-link
            (utils/maybe-after follow-ups-delay #(aa/follow-ups-get org-data)))
          (when recent-follow-ups-link
            (utils/maybe-after follow-ups-delay #(aa/recent-follow-ups-get org-data))))))
    (cond
      ;; If it's all posts page or must see, loads AP and must see for the current org
      (or (= (router/current-board-slug) "all-posts")
          (= (router/current-board-slug) "follow-ups"))
      (when-not activity-link
        (check-org-404))

      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(or (= (:slug %) (router/current-board-slug))
                                              (= (:uuid %) (router/current-board-slug))) boards))]
        ; Load the board data since there is a link to the board in the org data
        (do
          ;; Rewrite the URL in case it's using the board UUID instead of the slug
          (when (= (:uuid board-data) (router/current-board-slug))
            (router/rewrite-board-uuid-as-slug (router/current-board-slug) (:slug board-data)))
          (when-let [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
            (utils/maybe-after section-delay #(sa/section-get :recently-posted board-link)))
          (when-let [recent-board-link (utils/link-for (:links board-data) "activity" "GET")]
            (utils/maybe-after section-delay #(sa/section-get :recent-activity recent-board-link))))
        ; The board wasn't found, showing a 404 page
        (if (= (router/current-board-slug) utils/default-drafts-board-slug)
          (utils/after 100 #(sa/section-get-finish (router/current-sort-type) utils/default-drafts-board))
          (when-not (router/current-activity-id) ;; user is not asking for a specific post
            (routing-actions/maybe-404))))
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "org-settings-invite"))
           (not (utils/in? (:route @router/path) "org-settings-team"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "email-verification"))
           (not (utils/in? (:route @router/path) "sign-up"))
           (not (utils/in? (:route @router/path) "email-wall"))
           (not (utils/in? (:route @router/path) "confirm-invitation"))
           (not (utils/in? (:route @router/path) "secure-activity")))
      ;; Redirect to the first board if at least one is present
      (let [board-to (get-default-board org-data)]
        (router/nav!
          (if board-to
            (oc-urls/board (:slug org-data) (:slug board-to))
            (oc-urls/all-posts (:slug org-data)))))))

  ;; Change service connection
  (when (or (jwt/jwt)
            (jwt/id-token)) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "changes")]
      (ws-cc/reconnect ws-link (jwt/user-id) (:slug org-data) (conj (map :uuid (:boards org-data)) (:uuid org-data)))))

  ;; Interaction service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "interactions")]
      (ws-ic/reconnect ws-link (jwt/user-id))))

  ;; Notify client
  (when (jwt/jwt)
    (when-let [ws-link (utils/link-for (:links org-data) "notifications")]
      (ws-nc/reconnect ws-link (jwt/user-id))))

  (dis/dispatch! [:org-loaded org-data saved? email-domain])
  (utils/after 100 maybe-show-integration-added-notification?)
  (fullstory/track-org org-data)
  (chat/identify) ; Intercom

  ;; Change page title when an org page is loaded
  (set! (.-title js/document) (str "Carrot | " (:name org-data))))

(defn get-org-cb [prevent-complete-refresh? {:keys [status body success]}]
  (let [org-data (json->cljs body)]
    (org-loaded org-data nil nil (not prevent-complete-refresh?))))

(defn get-org [& [org-data prevent-complete-refresh?]]
  (let [fixed-org-data (or org-data (dis/org-data))
        org-link (utils/link-for (:links fixed-org-data) ["item" "self"] "GET")]
    (api/get-org org-link (partial get-org-cb prevent-complete-refresh?))))

;; Org redirect

(defn org-redirect [org-data]
  (when org-data
    (let [org-slug (:slug org-data)]
      (utils/after 100 #(router/redirect! (get-ap-url (:slug org-data)))))))

;; Org create

(defn- org-created [org-data]
  (utils/after 0
   #(router/nav! (oc-urls/all-posts (:slug org-data)))))

(defn team-patch-cb [org-data {:keys [success body status]}]
  (when success
    (org-created org-data)))

(defn- handle-org-redirect [team-data org-data email-domain]
  (if (and (empty? (:name team-data))
           (utils/link-for (:links team-data) "partial-update"))
    ;; if the current team has no name and
    ;; the user has write permission on it
    ;; use the org name
    ;; for it and patch it back
    (let [team-id (:team-id org-data)
          team-data (dis/team-data team-id)
          team-patch-link (utils/link-for (:links team-data) "partial-update")]
      (api/patch-team team-patch-link team-id {:name (:name org-data)}
       (partial team-patch-cb org-data)))
    ;; if not redirect the user to the invite page
    (org-created org-data)))

(defn update-email-domains [email-domain org-data]
  (let [team-data (dis/team-data (:team-id org-data))
        redirect-cb #(handle-org-redirect team-data org-data email-domain)]
    (if (seq email-domain)
      (let [add-email-domain-link (utils/link-for
                                   (:links team-data)
                                   "add"
                                   "POST"
                                   {:content-type "application/vnd.open-company.team.email-domain.v1+json"})]
        (api/add-email-domain add-email-domain-link email-domain redirect-cb team-data))
      (redirect-cb))))

(defn pre-flight-email-domain [email-domain team-id cb]
  (let [team-data (or (dis/team-data team-id)
                      ;; Fallback for NUX: user has no team-id set from the org yet
                      ;; so the team data are not in the right position yet
                      (first (filter #(= (:team-id %) team-id) (dis/teams-data))))
        add-email-domain-link (utils/link-for
                                   (:links team-data)
                                   "add"
                                   "POST"
                                   {:content-type "application/vnd.open-company.team.email-domain.v1+json"})
        redirect-cb (fn [{:keys [status success body]}]
                      (cb success status))]
    (api/add-email-domain add-email-domain-link email-domain redirect-cb team-data true)))

(defn org-create-check-errors [status]
  (if (= status 409)
    ;; Redirect to the already available org
    (router/nav! (oc-urls/org (:slug (first (dis/orgs-data)))))
    (dis/dispatch! [:input [:org-editing :error] true])))

(defn org-create-cb [email-domain {:keys [success status body]}]
  (if success
    (when-let [org-data (when success (json->cljs body))]
      ;; rewrite history so when user come back here we load org data and patch them
      ;; instead of creating them
      (.replaceState js/history #js {} (.-title js/document) (oc-urls/sign-up-update-team (:slug org-data)))
      (org-loaded org-data nil email-domain)
      (dis/dispatch! [:org-create])
      (update-email-domains email-domain org-data))
    (org-create-check-errors status)))

(defn org-update-cb [email-domain {:keys [success status body]}]
  (if success
    (when-let [org-data (when success (json->cljs body))]
      (org-loaded org-data success email-domain)
      (update-email-domains email-domain org-data))
    (org-create-check-errors status)))

(defn- trunc
  "
  Truncate a string based on length
  "
  [s n]
  (subs s 0 (min (count s) n)))

(defn create-or-update-org [org-data]
  (dis/dispatch! [:input [:org-editing :error] false])
  (let [email-domain (:email-domain org-data)
        existing-org (dis/org-data)
        logo-org-data (if (seq (:logo-url org-data))
                          org-data
                          (dissoc org-data :logo-url :logo-width :logo-height))
        clean-org-data (assoc logo-org-data
                              :name
                              (trunc (:name logo-org-data) 127))]
    (if (seq (:slug existing-org))
      (let [org-patch-link (utils/link-for (:links (dis/org-data)) "partial-update")]
        (api/patch-org org-patch-link clean-org-data (partial org-update-cb email-domain)))
      (let [create-org-link (utils/link-for (dis/api-entry-point) "create")]
        (api/create-org create-org-link clean-org-data (partial org-create-cb email-domain))))))

;; Org edit

(defn org-edit-setup [org-data]
  (dis/dispatch! [:org-edit-setup org-data]))

(defn org-edit-save-cb [{:keys [success body status]}]
  (org-loaded (json->cljs body) success))

(defn org-edit-save [org-data]
  (let [org-patch-link (utils/link-for (:links (dis/org-data)) "partial-update")
        with-trimmed-name (assoc org-data :name (clojure.string/trim (:name org-data)))]
    (api/patch-org org-patch-link with-trimmed-name
      (fn [{:keys [success status] :as resp}]
        (if success
          (org-edit-save-cb resp)
          (when (= status 422)
            (dis/dispatch! [:input [:org-editing :error] true])))))))

(defn org-avatar-edit-save-cb [{:keys [success body status]}]
  (if success
    (do
      (notification-actions/show-notification
        {:title "Image update succeeded"
         :description "Your image was succesfully updated."
         :expire 3
         :dismiss true})
      (org-loaded (json->cljs body)))
    (do
      (dis/dispatch! [:org-avatar-update/failed])
      (notification-actions/show-notification
       {:title "Image upload error"
        :description "An error occurred while processing your company avatar. Please retry."
        :expire 3
        :id :org-avatar-upload-failed
        :dismiss true}))))

(defn org-avatar-edit-save [org-avatar-data]
  (let [org-patch-link (utils/link-for (:links (dis/org-data)) "partial-update")]
    (api/patch-org org-patch-link org-avatar-data org-avatar-edit-save-cb)))

(defn org-change [data org-data]
  (let [change-data (:data data)
        container-id (:container-id change-data)
        user-id (:user-id change-data)]
    (when (not= (jwt/user-id) user-id) ; no need to respond to our own events
      (when (= container-id (:uuid org-data))
        (utils/after 1000 get-org)))))

;; subscribe to websocket events
(defn subscribe []
  (ws-cc/subscribe :org/status
    (fn [data]
      (get-org)))
  (ws-cc/subscribe :container/change
    (fn [data]
      (let [change-data (:data data)
            change-type (:change-type change-data)
            org-data (dis/org-data)]
        ;; Handle section changes
        (org-change data org-data)
        ;; Nav away of the current section
        ;; if it's being deleted
        (when (and (= change-type :delete)
                   (= (:container-id change-data) (:uuid org-data)))
          (let [current-board-data (dis/board-data)]
            (when (= (:item-id change-data) (:uuid current-board-data))
              (router/nav! (oc-urls/all-posts (:slug org-data))))))))))

(defn signup-invite-completed [org-data]
  (router/nav! (oc-urls/all-posts (:slug org-data))))
