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
            [oc.web.local-settings :as ls]
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
            [oc.web.actions.payments :as payments-actions]
            [oc.web.actions.contributions :as contributions-actions]
            [oc.web.actions.notifications :as notification-actions]))

;; User related functions
;; FIXME: these functions shouldn't be here but calling oc.web.actions.user from here is causing a circular dep

(defn- get-ap-url [org-slug]
  (let [first-ever-landing-name (router/first-ever-landing-cookie (jwt/user-id))
        first-ever-landing (cook/get-cookie first-ever-landing-name)]
    (if first-ever-landing
      (oc-urls/default-landing org-slug)
      (do
        (cook/remove-cookie! first-ever-landing-name)
        (oc-urls/first-ever-landing org-slug)))))

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

;; Active users

(def max-retry-count 3)

(defn load-active-users [active-users-link & [retry]]
  (when active-users-link
    (api/get-active-users active-users-link
     (fn [{:keys [status success body]}]
       (if success
         (let [resp (when success (json->cljs body))]
           (dis/dispatch! [:active-users (router/current-org-slug) resp]))
         (when (< retry max-retry-count)
           (utils/after 1000 #(load-active-users active-users-link (inc (or retry 0))))))))))

;; Org get
(defn check-org-404 []
  (let [orgs (dis/orgs-data)]
    ;; avoid infinite loop of the Go to digest button
    ;; by changing the value of the last visited slug
    (if (pos? (count orgs))
      (cook/set-cookie! (router/last-org-cookie) (:slug (first orgs)) cook/default-cookie-expire)
      (cook/remove-cookie! (router/last-org-cookie)))
    (routing-actions/maybe-404)))

(defn get-default-board [org-data]
  (let [last-board-slug oc-urls/default-board-slug]
    ; Replace default-board with the following to go back to the last visited board
    ; (or (cook/get-cookie (router/last-board-cookie (:slug org-data))) default-board)]
    (cond
      (and (= last-board-slug "all-posts")
           (utils/link-for (:links org-data) "entries"))
      {:slug "all-posts"}
      (and (= last-board-slug "following")
           (utils/link-for (:links org-data) "following"))
      {:slug "following"}
      (and (= last-board-slug "unfollowing")
           (utils/link-for (:links org-data) "unfollowing"))
      {:slug "unfollowing"}
      :else
      (let [boards (:boards org-data)
            board (first (filter #(= (:slug %) last-board-slug) boards))]
        (or
          ; Get the last accessed board from the saved cookie
          board
          (let [sorted-boards (vec (sort-by :name boards))]
            (first sorted-boards)))))))

(defn- redirect-to-ap? [org-data]
  (when-not (router/ap-redirect)
    (let [following-count (int (:following-count org-data))
          is-following? (= (router/current-board-slug) "following")]
      (when (and is-following?
                 (zero? following-count)
                 (not (router/current-activity-id))
                 (not (router/current-secure-activity-id))
                 (not (router/ap-redirect)))
        (timbre/info "Redirect to all-posts for empty following:" (:following-count org-data))
        (router/set-route! [(:slug org-data) "all-posts" "dashboard"]
         {:org (:slug org-data)
          :board "all-posts"
          :query-params (router/query-params)})
        (.pushState (.-history js/window) #js {} (.-title js/document) (oc-urls/all-posts (:slug org-data)))))
    (router/ap-redirect-done!)))

(def other-resources-delay 1000)

(defn org-loaded
  "Dispatch the org data into the app-state to be used by all the components.
   Do all the needed loading when the org data are loaded if complete-refresh? is true.
   The saved? flag is used as a strict boolean, if it's nil it means no org data PATCH happened, false
   means that the save went wrong, true went well."
  [org-data & [saved? email-domain complete-refresh?]]
  ;; Save the last visited org
  (when (and org-data
             (= (router/current-org-slug) (:slug org-data)))
    (redirect-to-ap? org-data)
    (cook/set-cookie! (router/last-org-cookie) (:slug org-data) cook/default-cookie-expire))
  ;; Check the loaded org
  (let [boards (:boards org-data)
        current-board-slug (router/current-board-slug)
        ; inbox-link (utils/link-for (:links org-data) "following-inbox")
        all-posts-link (utils/link-for (:links org-data) "entries")
        bookmarks-link (utils/link-for (:links org-data) "bookmarks")
        following-link (utils/link-for (:links org-data) "following")
        unfollowing-link (utils/link-for (:links org-data) "unfollowing")
        contrib-link (utils/link-for (:links org-data) "partial-contributions")
        drafts-board (some #(when (= (:slug %) utils/default-drafts-board-slug) %) boards)
        drafts-link (utils/link-for (:links drafts-board) ["self" "item"] "GET")
        ; is-inbox? (= current-board-slug "inbox")
        is-all-posts? (= current-board-slug "all-posts")
        is-bookmarks? (= (router/current-board-slug) "bookmarks")
        is-following? (= (router/current-board-slug) "following")
        is-unfollowing? (= (router/current-board-slug) "unfollowing")
        is-explore? (= (router/current-board-slug) "explore")
        is-threads? (= (router/current-board-slug) "threads")
        is-drafts? (= current-board-slug utils/default-drafts-board-slug)
        sort-type (router/current-sort-type)
        is-contributions? (seq (router/current-contributions-id))
        delay-count (atom 0)
        ; inbox-delay (if is-inbox? 0 (* other-resources-delay (swap! delay-count inc)))
        all-posts-delay (if (and is-all-posts? (= sort-type dis/recently-posted-sort)) 0 (* other-resources-delay (swap! delay-count inc)))
        bookmarks-delay (if is-bookmarks? 0 (* other-resources-delay (swap! delay-count inc)))
        following-delay (if (and is-following? (= sort-type dis/recently-posted-sort)) 0 (* other-resources-delay (swap! delay-count inc)))
        unfollowing-delay (if (and is-unfollowing? (= sort-type dis/recently-posted-sort)) 0 (* other-resources-delay (swap! delay-count inc)))
        drafts-delay (if is-drafts? 0 (* other-resources-delay (swap! delay-count inc)))
        contributions-delay (if is-contributions? 0 (* other-resources-delay (swap! delay-count inc)))
        active-users-link (utils/link-for (:links org-data) "active-users")]
    (when is-bookmarks?
      (dis/dispatch! [:bookmarks-nav/show (:slug org-data)]))
    (when is-drafts?
      (dis/dispatch! [:drafts-nav/show (:slug org-data)]))
    (when complete-refresh?
      ;; Load secure activity
      (if (router/current-secure-activity-id)
        (aa/secure-activity-get)
        (do
          ;; Load the active users
          (when active-users-link
            (load-active-users active-users-link))
          ;; Load the current activity
          (when (router/current-activity-id)
            (cmail-actions/get-entry-with-uuid current-board-slug (router/current-activity-id)))
          ;; Load inbox data
          ; (when (and ls/wut?
          ;            inbox-link)
          ;   (utils/maybe-after inbox-delay #(aa/inbox-get org-data)))
          ;; Load all posts data with recently posted sort
          (when all-posts-link
            (utils/maybe-after all-posts-delay #(aa/all-posts-get org-data)))
          ;; Preload bookmarks data
          (when bookmarks-link
            (utils/maybe-after bookmarks-delay #(aa/bookmarks-get org-data)))
          ;; Preload following data with recently posted sort
          (when following-link
            (utils/maybe-after following-delay #(aa/following-get org-data)))
          ;; Preload unfollowing data with recently posted sort
          (when unfollowing-link
            (utils/maybe-after unfollowing-delay #(aa/unfollowing-get org-data)))
          ;; Drafts
          (when drafts-link
            (utils/maybe-after drafts-delay #(sa/section-get drafts-link)))
          ;; contributions data
          (when (and contrib-link
                     (router/current-contributions-id))
            (utils/maybe-after contributions-delay #(contributions-actions/contributions-get org-data (router/current-contributions-id)))))))
    (cond
      (or is-explore?
          is-threads?)
      true
      ;; If it's all posts page or must see, loads AP and must see for the current org
      (dis/is-container? current-board-slug)
      (when (or ; (and is-inbox?
                ;     (not inbox-link))
                (and is-all-posts?
                     (not all-posts-link))
                (and is-bookmarks?
                     (not bookmarks-link))
                (and is-following?
                     (not following-link))
                (and is-unfollowing?
                     (not unfollowing-link)))
        (check-org-404))

      ; If there is a board slug let's load the board data
      current-board-slug
      (if-let [board-data (first (filter #(or (= (:slug %) current-board-slug)
                                              (= (:uuid %) current-board-slug)) boards))]
        ;; Load the board data except for drafts if there is a link in the boards list
        ;; except for drafts which is preloaded with the rest
        (when-not is-drafts?
          ;; Rewrite the URL in case it's using the board UUID instead of the slug
          (when (= (:uuid board-data) current-board-slug)
            (router/rewrite-board-uuid-as-slug current-board-slug (:slug board-data)))
          (when-let* [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
            (sa/section-get board-link)))
        ; The board wasn't found, showing a 404 page
        (if is-drafts?
          (utils/after 100 #(sa/section-get-finish utils/default-drafts-board))
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
           (not (utils/in? (:route @router/path) "secure-activity"))
           (not (router/current-contributions-id)))
      ;; Redirect to the first board if at least one is present
      (let [board-to (get-default-board org-data)]
        (router/nav!
          (if board-to
            (oc-urls/board (:slug org-data) (:slug board-to))
            (oc-urls/default-landing (:slug org-data)))))))

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

  (payments-actions/maybe-load-payments-data org-data complete-refresh?)

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
   #(router/nav! (oc-urls/default-landing (:slug org-data)))))

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

(defn- add-utm-data
  "
  Augment org data with utm values stored in cookies.

  Remove utm cookies if present.
  "
  [org-data]
  (let [source (cook/get-cookie "utm_source")
        term (cook/get-cookie "utm_term")
        medium (cook/get-cookie "utm_medium")
        campaign (cook/get-cookie "utm_campaign")]
    (doseq [c-name ["utm_source" "utm_term" "utm_medium" "utm_campaign"]]
      (js/OCStaticDeleteCookie c-name))
    (if (or source term medium campaign)
      (merge org-data {:utm-data {:utm-source (or source "")
                                  :utm-term (or term "")
                                  :utm-medium (or medium "")
                                  :utm-campaign (or campaign "")}})
      org-data)))

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
        (api/create-org create-org-link (add-utm-data clean-org-data) (partial org-create-cb email-domain))))))

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
              (router/nav! (oc-urls/default-landing (:slug org-data))))))))))

(defn signup-invite-completed [org-data]
  (router/nav! (oc-urls/default-landing (:slug org-data))))
