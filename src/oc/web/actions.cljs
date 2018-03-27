(ns oc.web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [taoensso.timbre :as timbre]
            [oc.lib.time :as oc-time]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.actions.user :as ua]
            [oc.web.actions.team :as ta]
            [oc.web.actions.activity :as aa]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.user-cache :as uc]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.ws-interaction-client :as ws-ic]
            [oc.web.lib.ws-change-client :as ws-cc]))

;; ---- Generic Actions Dispatch
;; This is a small generic abstraction to handle "actions".
;; An `action` is a transformation on the app state.
;; The return value of an action will be used as the new app-state.

;; The extended multimethod `action` is defined in the dispatcher
;; namespace to avoid cyclical dependencies between namespaces

(defn- log [& args]
  (timbre/info (apply pr-str args)))

(defmethod dispatcher/action :default [db payload]
  (timbre/warn "No handler defined for" (str (first payload)))
  (timbre/info "Full event: " (pr-str payload))
  db)

(defmethod dispatcher/action :org
  [db [_ org-data saved?]]
  ;; Save the last visited org
  (when (and org-data
             (= (router/current-org-slug) (:slug org-data)))
    (cook/set-cookie! (router/last-org-cookie) (:slug org-data) (* 60 60 24 6)))
  (let [boards (:boards org-data)]

    (cond
      ;; If it's all posts page, loads all posts for the current org
      (or (= (router/current-board-slug) "all-posts")
          (:ap-initial-at db))
      (if (utils/link-for (:links org-data) "activity")
        ;; Load all posts only if not coming from a digest url
        ;; in that case do not load since we already have the results we need
        (api/get-all-posts (utils/link-for (:links org-data) "activity") (:ap-initial-at db)
         (partial aa/all-posts-get-finish (:ap-intiial-at db)))
        (router/redirect-404!))
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        ; Load the board data since there is a link to the board in the org data
        (when-let [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
          (api/get-board board-link))
        ; The board wasn't found, showing a 404 page
        (if (= (router/current-board-slug) utils/default-drafts-board-slug)
          (utils/after 100 #(dispatcher/dispatch! [:board utils/default-drafts-board]))
          (router/nav! (oc-urls/org (router/current-org-slug)))))
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-settings-invite"))
           (not (utils/in? (:route @router/path) "org-settings-team"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "email-verification"))
           (not (utils/in? (:route @router/path) "sign-up"))
           (not (utils/in? (:route @router/path) "email-wall"))
           (not (utils/in? (:route @router/path) "confirm-invitation"))
           (not (utils/in? (:route @router/path) "secure-activity")))

      (when (>= (count boards) 1)
        ;; Redirect to the first board if at least one is present
        (let [board-to (utils/get-default-board org-data)]
          (utils/after 10
            #(router/nav!
               (if board-to
                 (oc-urls/board (:slug org-data) (:slug board-to))
                 (oc-urls/all-posts (:slug org-data)))))))))

  ;; Change service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "changes")]
      (ws-cc/reconnect ws-link (jwt/get-key :user-id) (:slug org-data) (map :uuid (:boards org-data)))))
  ;; Interaction service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "interactions")]
      (ws-ic/reconnect ws-link (jwt/get-key :user-id))))
  (-> db
    (assoc-in (dispatcher/org-data-key (:slug org-data)) (utils/fix-org org-data))
    (assoc :org-editing (-> (:org-editing db)
                            (assoc :saved saved?)
                            (dissoc :has-changes)))))

(defmethod dispatcher/action :boards-load-other
  [db [_ boards]]
  (doseq [board boards
          :when (not= (:slug board) (router/current-board-slug))]
    (api/get-board (utils/link-for (:links board) ["item" "self"] "GET")))
  db)

(defmethod dispatcher/action :board-get
  [db [_ link]]
  (api/get-board link)
  db)

(defn- update-change-data [db board-uuid property timestamp]
  (let [change-data-key (dispatcher/change-data-key (router/current-org-slug))
        change-data (get-in db change-data-key)
        change-map (or (get change-data board-uuid) {})
        new-change-map (assoc change-map property timestamp)
        new-change-data (assoc change-data board-uuid new-change-map)]
    (assoc-in db change-data-key new-change-data)))

(defn- board-change [db board-uuid change-at]
  (timbre/debug "Board change:" board-uuid "at:" change-at)
  (utils/after 1000 (fn []
    (let [current-board-data (dispatcher/board-data)]
      (if (= board-uuid (:uuid current-board-data))
        ;; Reload the current board data
        (dispatcher/dispatch! [:board-get (utils/link-for (:links current-board-data) "self")])
        ;; Reload a secondary board data
        (let [boards (:boards (dispatcher/org-data db))
              filtered-boards (filter #(= (:uuid %) board-uuid) boards)]
          (dispatcher/dispatch! [:boards-load-other filtered-boards]))))))
  ;; Update change-data state that the board has a change
  (update-change-data db board-uuid :change-at change-at))

(defn- org-change [db org-uuid change-at]
  (timbre/debug "Org change:" org-uuid "at:" change-at)
  (utils/after 1000 (fn [] (api/get-org (dispatcher/org-data))))
  db)

(defmethod dispatcher/action :container/change
  [db [_ {container-uuid :container-id change-at :change-at user-id :user-id}]]
  (timbre/debug "Container change:" container-uuid "at:" change-at "by:" user-id)
  (when (not= (jwt/user-id) user-id) ; no need to respond to our own events
    (if (= container-uuid (:uuid (dispatcher/org-data)))
      (org-change db container-uuid change-at)
      (board-change db container-uuid change-at))))

(defmethod dispatcher/action :board-seen
  [db [_ {board-uuid :board-uuid}]]
  (timbre/debug "Board seen:" board-uuid)
  ;; Let the change service know we saw the board
  (ws-cc/container-seen board-uuid)
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;; Do not update the seen-at if coming from the modal view
      next-db
      ;; Update change-data state that we nav'd to the board
      (update-change-data next-db board-uuid :nav-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :board-nav-away
  [db [_ {board-uuid :board-uuid}]]
  (timbre/debug "Board nav away:" board-uuid)
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;;  Do not update seen-at if navigating to an activity modal of the current board
      next-db
      ;; Update change-data state that we saw the board
      (update-change-data next-db board-uuid :seen-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :board
  [db [_ board-data]]
  (let [org (router/current-org-slug)
        is-currently-shown (= (router/current-board-slug) (:slug board-data))
        fixed-board-data (utils/fix-board board-data)
        db-loading (if (and is-currently-shown
                            (router/current-activity-id)
                            (contains? (:fixed-items fixed-board-data) (router/current-activity-id)))
                     (dissoc db :loading)
                     db)]
    (when is-currently-shown

      (when (and (router/current-activity-id)
                 (not (contains? (:fixed-items fixed-board-data) (router/current-activity-id))))
        (router/nav! (oc-urls/board (router/current-org-slug) (:slug board-data))))

      ;; Tell the container service that we are seeing this board,
      ;; and update change-data to reflect that we are seeing this board
      (when-let [board-uuid (:uuid fixed-board-data)]
        (utils/after 10 #(dispatcher/dispatch! [:board-seen {:board-uuid board-uuid}])))
      ;; only watch the currently visible board.
      (when (jwt/jwt) ; only for logged in users
        (ws-ic/board-unwatch (fn [rep]
          (timbre/debug rep "Watching on socket " (:uuid fixed-board-data))
          (ws-ic/board-watch (:uuid fixed-board-data)))))

      ;; Load the other boards
      (utils/after 2000 #(dispatcher/dispatch! [:boards-load-other (:boards (dispatcher/org-data db))])))
    (let [old-board-data (get-in db (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))))
          with-current-edit (if (and is-currently-shown
                                     (:entry-editing db))
                              old-board-data
                              fixed-board-data)
          next-db (assoc-in db-loading
                   (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data)))
                   with-current-edit)
          without-loading (if is-currently-shown
                            (dissoc next-db :loading)
                            next-db)]
      without-loading)))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod dispatcher/action :update [db [_ path value-fn]]
  (if (fn? value-fn)
    (update-in db path value-fn)
    db))

(defmethod dispatcher/action :org-create
  [db [_]]
  (let [org-data (:org-editing db)]
    (when-not (string/blank? (:name org-data))
      (api/create-org (:name org-data) (:logo-url org-data) (:logo-width org-data) (:logo-height org-data))))
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :board-delete
  [db [_ board-slug]]
  (api/delete-board board-slug)
  (dissoc db :latest-entry-point))

(defmethod dispatcher/action :org-edit
  [db [_ org-data]]
  (assoc db :org-editing org-data))

(defmethod dispatcher/action :org-edit-save
  [db [_]]
  (when (:org-editing db)
    (api/patch-org (:org-editing db)))
  db)

(defmethod dispatcher/action :container/status
  [db [_ status-data]]
  (timbre/debug "Change status received:" status-data)
  (let [org-data (dispatcher/org-data db)
        old-status-data (dispatcher/change-data db)
        status-by-uuid (group-by :container-id status-data)
        clean-status-data (zipmap (keys status-by-uuid) (->> status-by-uuid
                                                          vals
                                                          ; remove the sequence of 1 from group-by
                                                          (map first)
                                                          ; dup seen-at as nav-at
                                                          (map #(assoc % :nav-at (:seen-at %)))))
        new-status-data (merge old-status-data clean-status-data)]
    (timbre/debug "Change status data:" new-status-data)
    (assoc-in db (dispatcher/change-data-key (:slug org-data)) new-status-data)))

(defmethod dispatcher/action :org-redirect
  [db [_ org-data]]
  ;; Show NUX for first ever user when the dashboard is loaded
  (cook/set-cookie!
   (router/show-nux-cookie (jwt/user-id))
   (:first-ever-user router/nux-cookie-values)
   (* 60 60 24 7))
  (when org-data
    (let [org-slug (:slug org-data)]
      (utils/after 100 #(router/redirect! (oc-urls/all-posts org-slug)))))
  db)

(defmethod dispatcher/action :section-edit-save
  [db [_]]
  (let [section-data (:section-editing db)]
    (if (empty? (:links section-data))
      (api/create-board section-data
       (fn [{:keys [success status body]}]
         (let [section-data (when success (json->cljs body))]
           (if (= status 409)
             ; Board name exists
             (dispatcher/dispatch!
              [:input
               [:section-editing :section-name-error]
               "Section name already exists or isn't allowed"])
             (dispatcher/dispatch! [:section-edit-save/finish section-data])))))
      (api/patch-board section-data)))
  db)

(defmethod dispatcher/action :section-edit-save/finish
  [db [_ section-data]]
  (let [org-slug (router/current-org-slug)
        section-slug (:slug section-data)
        board-key (dispatcher/board-data-key org-slug (:slug section-data))
        fixed-section-data (utils/fix-board section-data)]
    (api/get-org (dispatcher/org-data))
    (if (not= (:slug section-data) (router/current-board-slug))
      ;; If creating a new board, redirect to that board page, and watch the new board
      (do
        (utils/after 100 #(router/nav! (oc-urls/board (router/current-org-slug) (:slug section-data))))
        (ws-cc/container-watch [(:uuid section-data)]))
      ;; If updating an existing board, refresh the org data
      (api/get-org (dispatcher/org-data)))
  (-> db
    (assoc-in board-key fixed-section-data)
    (dissoc :section-editing))))

(defmethod dispatcher/action :section-edit/dismiss
  [db [_]]
  (dissoc db :section-editing))

(defmethod dispatcher/action :private-section-user-add
  [db [_ user user-type]]
  (let [section-data (:section-editing db)
        current-notifications (filterv #(not= (:user-id %) (:user-id user))
                                       (:private-notifications section-data))
        current-authors (filterv #(not= % (:user-id user)) (:authors section-data))
        current-viewers (filterv #(not= % (:user-id user)) (:viewers section-data))
        next-authors (if (= user-type :author)
                       (vec (conj current-authors (:user-id user)))
                       current-authors)
        next-viewers (if (= user-type :viewer)
                       (vec (conj current-viewers (:user-id user)))
                       current-viewers)
        next-notifications (vec (conj current-notifications user))]
    (assoc db :section-editing
           (merge section-data {:authors next-authors
                                :viewers next-viewers
                                :private-notifications next-notifications}))))

(defmethod dispatcher/action :private-section-user-remove
  [db [_ user]]
  (let [section-data (:section-editing db)
        private-notifications (filterv #(not= (:user-id %) (:user-id user))
                                       (:private-notifications section-data))
        next-authors (filterv #(not= % (:user-id user)) (:authors section-data))
        next-viewers (filterv #(not= % (:user-id user)) (:viewers section-data))]
    (assoc db :section-editing
           (merge section-data {:authors next-authors
                                :viewers next-viewers
                                :private-notifications private-notifications}))))

(defmethod dispatcher/action :private-section-kick-out-self
  [db [_ user]]
  ;; If the user is self (same user-id) kick out from the current private section
  (when (= (:user-id user) (jwt/user-id))
    (api/remove-user-from-private-board user))
  db)

(defmethod dispatcher/action :private-section-kick-out-self/finish
  [db [_ success]]
  (if success
    ;; Redirect to the first available board
    (let [org-data (dispatcher/org-data db)
          all-boards (:boards org-data)
          current-board-slug (router/current-board-slug)
          except-this-boards (remove #(#{current-board-slug "drafts"} (:slug %)) all-boards)
          redirect-url (if-let [next-board (first except-this-boards)]
                         (oc-urls/board (:slug next-board))
                         (oc-urls/org (router/current-org-slug)))]
     (api/get-org org-data)
     (utils/after 0 #(router/nav! redirect-url))
     ;; Force board editing dismiss
     (dissoc db :section-editing))
    ;; An error occurred while kicking the user out, no-op to let the user retry
    db))