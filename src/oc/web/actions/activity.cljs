(ns oc.web.actions.activity
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [taoensso.timbre :as timbre]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.lib.user-cache :as uc]
            [oc.web.local-settings :as ls]
            [oc.web.actions.section :as sa]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.utils.comment :as comment-utils]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(def initial-revision (atom {}))

(defn watch-boards [posts-data]
  (when (jwt/jwt) ; only for logged in users
    (let [board-slugs (distinct (map :board-slug (vals posts-data)))
          org-data (dis/org-data)
          org-boards (:boards org-data)
          org-board-map (zipmap (map :slug org-boards) (map :uuid org-boards))]
      (ws-ic/board-unwatch (fn [rep]
        (let [board-uuids (map org-board-map board-slugs)]
          (timbre/debug "Watching on socket " board-slugs board-uuids)
          (ws-ic/boards-watch board-uuids)))))))

;; Reads data

(defn request-reads-data
  "Request the list of readers of the given item."
  [item-id]
  (api/request-reads-data item-id))

(defn request-reads-count
  "Request the reads count data only for the items we don't have already."
  [item-ids]
  (let [cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
    (when (seq cleaned-ids)
      (api/request-reads-count cleaned-ids))))

;; Follow-ups stream

(defn follow-ups-get-finish [sort-type {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          org (router/current-org-slug)
          posts-data-key (dis/posts-data-key org)
          follow-ups-data (when success (json->cljs body))
          fixed-follow-ups (au/fix-container (:collection follow-ups-data) (dis/change-data) org-data)]
      (when (= (router/current-board-slug) "follow-ups")
        (cook/set-cookie! (router/last-board-cookie org) "follow-ups" (* 60 60 24 365))
        (request-reads-count (keys (:fixed-items fixed-follow-ups)))
        (watch-boards (:fixed-items fixed-follow-ups)))
      (dis/dispatch! [:follow-ups-get/finish org sort-type fixed-follow-ups]))))

(defn- follow-ups-real-get [follow-ups-link sort-type org-slug finish-cb]
  (api/get-all-posts follow-ups-link
   (fn [resp]
     (follow-ups-get-finish sort-type resp)
     (when (fn? finish-cb)
       (finish-cb resp)))))

(defn follow-ups-get [org-data & [finish-cb]]
  (when-let [follow-ups-link (utils/link-for (:links org-data) "follow-ups")]
    (follow-ups-real-get follow-ups-link :recently-posted (:slug org-data) finish-cb)))

(defn recent-follow-ups-get [org-data & [finish-cb]]
  (when-let [recent-follow-ups-link (utils/link-for (:links org-data) "follow-ups")]
    (follow-ups-real-get recent-follow-ups-link :recent-activity (:slug org-data) finish-cb)))

(defn follow-ups-sort-get [org-data & [finish-cb]]
  (let [sort-type (router/current-sort-type)
        follow-ups-link-rel (if (= sort-type dis/default-sort-type) "follow-ups-activity" "follow-ups")
        follow-ups-link (utils/link-for (:links org-data) follow-ups-link-rel)]
    (when follow-ups-link
      (follow-ups-real-get follow-ups-link sort-type (:slug org-data) finish-cb))))

(defn follow-ups-more-finish [direction sort-type {:keys [success body]}]
  (when success
    (request-reads-count (map :uuid (:items (:collection (json->cljs body))))))
  (dis/dispatch! [:follow-ups-more/finish (router/current-org-slug) direction sort-type (when success (json->cljs body))]))

(defn follow-ups-more [more-link direction]
  (api/load-more-items more-link direction (partial follow-ups-more-finish direction (router/current-sort-type)))
  (dis/dispatch! [:follow-ups-more (router/current-org-slug) (router/current-sort-type)]))

;; All Posts

(defn all-posts-get-finish [sort-type {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          org (router/current-org-slug)
          posts-data-key (dis/posts-data-key org)
          all-posts-data (when success (json->cljs body))
          fixed-all-posts (au/fix-container (:collection all-posts-data) (dis/change-data) org-data)]
      (when (= (router/current-board-slug) "all-posts")
        (cook/set-cookie! (router/last-board-cookie org) "all-posts" (* 60 60 24 365))
        (request-reads-count (keys (:fixed-items fixed-all-posts)))
        (watch-boards (:fixed-items fixed-all-posts)))
      (dis/dispatch! [:all-posts-get/finish org sort-type fixed-all-posts]))))

(defn- activity-real-get [activity-link sort-type org-slug finish-cb]
  (api/get-all-posts activity-link
   (fn [resp]
     (all-posts-get-finish sort-type resp)
     (when (fn? finish-cb)
       (finish-cb resp)))))

(defn activity-get [org-data & [finish-cb]]
  (when-let [activity-link (utils/link-for (:links org-data) "entries")]
    (activity-real-get activity-link :recently-posted (:slug org-data) finish-cb)))

(defn recent-activity-get [org-data & [finish-cb]]
  (when-let [recent-activity-link (utils/link-for (:links org-data) "activity")]
    (activity-real-get recent-activity-link :recent-activity (:slug org-data) finish-cb)))

(defn all-posts-get [org-data & [finish-cb]]
  (let [sort-type (router/current-sort-type)
        activity-link-rel (if (= sort-type dis/default-sort-type) "activity" "entries")
        activity-link (utils/link-for (:links org-data) activity-link-rel)]
    (when activity-link
      (activity-real-get activity-link sort-type (:slug org-data) finish-cb))))

(defn all-posts-more-finish [direction sort-type {:keys [success body]}]
  (when success
    (request-reads-count (map :uuid (:items (:collection (json->cljs body))))))
  (dis/dispatch! [:all-posts-more/finish (router/current-org-slug) direction sort-type (when success (json->cljs body))]))

(defn all-posts-more [more-link direction]
  (api/load-more-items more-link direction (partial all-posts-more-finish direction (router/current-sort-type)))
  (dis/dispatch! [:all-posts-more (router/current-org-slug) (router/current-sort-type)]))

;; Must see
(defn must-see-get-finish
  [sort-type {:keys [success body]}]
    (when body
    (let [org-data (dis/org-data)
          org (router/current-org-slug)
          must-see-data (when success (json->cljs body))
          must-see-posts (au/fix-container (:collection must-see-data) (dis/change-data) org-data)]
      (when (= (router/current-board-slug) "must-see")
        (cook/set-cookie! (router/last-board-cookie org) "all-posts" cook/default-cookie-expire)
        (watch-boards (:fixed-items must-see-posts)))
      (dis/dispatch! [:must-see-get/finish org sort-type must-see-posts]))))

(defn must-see-get [org-data]
  (let [current-sort-type (router/current-sort-type)
        link-rel (if (= current-sort-type dis/default-sort-type) "activity" "entries")]
    (when-let [activity-link (utils/link-for (:links org-data) link-rel)]
      (let [activity-href (:href activity-link)
            must-see-filter (str activity-href "?must-see=true")
            must-see-link (assoc activity-link :href must-see-filter)]
        (api/get-all-posts must-see-link (partial must-see-get-finish current-sort-type))))))

(defn must-see-more-finish [direction sort-type {:keys [success body]}]
  (when success
    (request-reads-count (map :uuid (:items (:collection (json->cljs body))))))
  (dis/dispatch! [:must-see-more/finish (router/current-org-slug) direction sort-type (when success (json->cljs body))]))

(defn must-see-more [more-link direction]
  (let [more-href (:href more-link)
        more-must-see-filter (str more-href "&must-see=true")
        more-must-see-link (assoc more-link :href more-must-see-filter)
        current-sort-type (router/current-sort-type)]
    (api/load-more-items more-must-see-link direction (partial must-see-more-finish direction current-sort-type))
    (dis/dispatch! [:must-see-more (router/current-org-slug) current-sort-type])))

;; Referesh org when needed
(defn refresh-org-data-cb [{:keys [status body success]}]
  (let [org-data (json->cljs body)
        is-all-posts (= (router/current-board-slug) "all-posts")
        is-follow-ups (= (router/current-board-slug) "follow-ups")
        board-data (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data))
        sort-type (router/current-sort-type)
        board-rel (if (= sort-type :recent-activity) "activity" "self")]
    (dis/dispatch! [:org-loaded org-data])
    (cond
      is-all-posts
      (if (= sort-type :recent-activity)
        (recent-activity-get org-data)
        (activity-get org-data))
      is-follow-ups
      (follow-ups-sort-get org-data)
      :else
      (sa/section-get sort-type (utils/link-for (:links board-data) board-rel "GET")))))

(defn refresh-org-data []
  (let [org-link (utils/link-for (:links (dis/org-data)) ["item" "self"] "GET")]
    (api/get-org org-link refresh-org-data-cb)))

;; Entry

(defn entry-edit
  [initial-entry-data]
  (cook/set-cookie! (cmail-actions/edit-open-cookie)
   (or (str (:board-slug initial-entry-data) "/" (:uuid initial-entry-data)) true) (* 60 30))
  (cmail-actions/load-cached-item initial-entry-data :entry-editing))

(defn entry-edit-dismiss
  []
  ;; If the user was looking at the modal, dismiss it too
  (when (router/current-activity-id)
    (utils/after 1 #(let [is-all-posts (= (router/current-board-slug) "all-posts")
                          is-must-see (= (router/current-board-slug) "must-see")]
                      (router/nav!
                        (cond
                          is-all-posts ; AP
                          (oc-urls/all-posts (router/current-org-slug))
                          is-must-see
                          (oc-urls/must-see (router/current-org-slug))
                          :else
                          (oc-urls/board (router/current-org-slug) (router/current-board-slug)))))))
  ;; Add :entry-edit-dissmissing for 1 second to avoid reopening the activity modal after edit is dismissed.
  (utils/after 1000 #(dis/dispatch! [:input [:entry-edit-dissmissing] false]))
  (dis/dispatch! [:entry-edit/dismiss]))

(defn activity-modal-edit
  [activity-data activate]
  (if activate
    (do
      (cmail-actions/load-cached-item activity-data :modal-editing-data)
      (dis/dispatch! [:modal-editing-activate]))
    (dis/dispatch! [:modal-editing-deactivate])))


(declare entry-save)

(defn entry-save-on-exit
  [edit-key activity-data entry-body section-editing]
  (let [entry-map (assoc activity-data :body entry-body)
        cache-key (cmail-actions/get-entry-cache-key (:uuid activity-data))]
    ;; Save the entry in the local cache without auto-saving or
    ;; we it will be picked up it won't be autosaved
    (uc/set-item cache-key (dissoc entry-map :auto-saving)
     (fn [err]
       (when-not err
         ;; auto save on drafts that have changes
         (when (and (not= "published" (:status entry-map))
                    (:has-changes entry-map)
                    (not (:auto-saving entry-map)))
           ;; dispatch that you are auto saving
           (dis/dispatch! [:update [edit-key] #(merge % {:auto-saving true :has-changes false :body (:body entry-map)})])
           (entry-save edit-key entry-map section-editing
             (fn [entry-data-saved edit-key-saved {:keys [success body status]}]
               (if-not success
                 ;; If save fails let's make sure save will be retried on next call
                 (dis/dispatch! [:update [edit-key ] #(merge % {:auto-saving false :has-changes true})])
                 (let [json-body (json->cljs body)
                       board-data (if (:entries json-body)
                                    (au/fix-board json-body)
                                    false)
                       fixed-items (:fixed-items board-data)
                       entry-saved (if fixed-items
                                     ;; board creation
                                     (first (vals fixed-items))
                                     json-body)]
                   (cook/set-cookie! (cmail-actions/edit-open-cookie) (str (:board-slug entry-saved) "/" (:uuid entry-saved)) (* 60 60 24 365))
                   ;; remove the initial document cache now that we have a uuid
                   ;; uuid didn't exist before
                   (when (and (nil? (:uuid entry-map))
                              (:uuid entry-saved))
                     (cmail-actions/remove-cached-item (:uuid entry-map)))
                   ;; set the initial version number after the first auto save
                   ;; this is used to revert if user decides to lose the changes
                   (when (nil? (get @initial-revision (:uuid entry-saved)))
                     (swap! initial-revision assoc (:uuid entry-saved)
                            (or (:revision-id entry-map) -1)))
                   (when board-data
                     (dis/dispatch! [:entry-save-with-board/finish (router/current-org-slug)
                      (router/current-sort-type) board-data]))
                   ;; add or update the entry in the app-state list of posts
                   ;; also move the updated data to the entry editing
                   (dis/dispatch! [:entry-auto-save/finish entry-saved edit-key entry-map])))))
           (dis/dispatch! [:entry-toggle-save-on-exit false])))))))

(defn entry-toggle-save-on-exit
  [enable?]
  (dis/dispatch! [:entry-toggle-save-on-exit enable?]))

(declare send-item-read)

(defn entry-save-finish [board-slug activity-data initial-uuid edit-key]
  (let [org-slug (router/current-org-slug)
        is-published? (= (:status activity-data) "published")]
    (when (and (router/current-activity-id)
               (not= board-slug (router/current-board-slug)))
      (router/nav! (oc-urls/entry org-slug board-slug (:uuid activity-data))))
    (au/save-last-used-section board-slug)
    (refresh-org-data)
    ;; Remove saved cached item
    (cmail-actions/remove-cached-item initial-uuid)
    ;; reset initial revision after successful save.
    ;; need a new revision number on the next edit.
    (swap! initial-revision dissoc (:uuid activity-data))
    (dis/dispatch! [:entry-save/finish (assoc activity-data :board-slug board-slug) edit-key])
    ;; Send item read
    (when is-published?
      (send-item-read (:uuid activity-data))
      (notification-actions/show-notification {:title "Changes have been saved"
                                               :primary-bt-dismiss true
                                               :primary-bt-title "OK"
                                               :primary-bt-inline true
                                               :expire 3
                                               :id :entry-updated-notification}))))

(defn create-update-entry-cb [entry-data edit-key {:keys [success body status]}]
  (if success
    (entry-save-finish (:board-slug entry-data) (json->cljs body) (:uuid entry-data) edit-key)
    (dis/dispatch! [:entry-save/failed edit-key])))

(defn entry-modal-save-with-board-finish [activity-data response]
  (let [fixed-board-data (au/fix-board response)
        org-slug (router/current-org-slug)
        saved-activity-data (first (vals (:fixed-items fixed-board-data)))]
    (au/save-last-used-section (:slug fixed-board-data))
    (cmail-actions/remove-cached-item (:uuid activity-data))
    ;; reset initial revision after successful save.
    ;; need a new revision number on the next edit.
    (swap! initial-revision dissoc (:uuid activity-data))
    (refresh-org-data)
    (when-not (= (:slug fixed-board-data) (router/current-board-slug))
      ;; If creating a new board, start watching changes
      (ws-cc/container-watch (:uuid fixed-board-data)))
    (dis/dispatch! [:entry-save-with-board/finish org-slug (router/current-sort-type) fixed-board-data])
    (when (= (:status saved-activity-data) "published")
      (send-item-read (:uuid saved-activity-data)))))

(defn board-name-exists-error [edit-key]
  (dis/dispatch! [:update [edit-key] #(-> %
                                        (assoc :section-name-error utils/section-name-exists-error)
                                        (dissoc :loading))]))

(defn entry-modal-save [activity-data section-editing]
  (if (and (= (:board-slug activity-data) utils/default-section-slug)
           section-editing)
    (let [fixed-entry-data (dissoc activity-data :board-slug :board-name :invite-note)
          final-board-data (assoc section-editing :entries [fixed-entry-data])
          create-board-link (utils/link-for (:links (dis/org-data)) "create")]
      (api/create-board create-board-link final-board-data (:invite-note activity-data)
        (fn [{:keys [success status body]}]
          (if (= status 409)
            ;; Board name exists
            (board-name-exists-error :modal-editing-data)
            (let [board-data (when success (json->cljs body))]
              (when (router/current-activity-id)
                (router/nav! (oc-urls/entry (router/current-org-slug) (:slug board-data)
                              (:uuid activity-data))))
              (entry-modal-save-with-board-finish activity-data board-data))))))
    (let [patch-entry-link (utils/link-for (:links activity-data) "partial-update")]
      (api/patch-entry patch-entry-link activity-data :modal-editing-data create-update-entry-cb)))
  (dis/dispatch! [:entry-modal-save]))

(defn add-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-add-attachment dispatch-input-key attachment-data])
  (dis/dispatch! [:input [dispatch-input-key :has-changes] true]))

(defn remove-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-remove-attachment dispatch-input-key attachment-data])
  (dis/dispatch! [:input [dispatch-input-key :has-changes] true]))

(declare secure-activity-get)

(defn get-entry [entry-data]
  (if (router/current-secure-activity-id)
    (secure-activity-get)
    (let [entry-link (utils/link-for (:links entry-data) "self")]
      (api/get-entry entry-link
        (fn [{:keys [status success body]}]
          (if (and (= status 404)
                   (= (:uuid entry-data) (router/current-activity-id)))
            (do
              (dis/dispatch! [:activity-get/not-found (router/current-org-slug) (:uuid entry-data) nil])
              (routing-actions/maybe-404))
            (dis/dispatch! [:activity-get/finish status (router/current-org-slug) (json->cljs body)
             nil])))))))

(declare entry-revert)

(defn entry-clear-local-cache [item-uuid edit-key item]
  "Removes user local cache and also reverts any auto saved drafts."
  (cmail-actions/remove-cached-item item-uuid)
  ;; revert draft to old version
  (timbre/debug "Reverting to " @initial-revision item-uuid)
  (when (not= "published" (:status item))
    (when-let [revision-id (get @initial-revision item-uuid)]
      (entry-revert revision-id item)))
  (dis/dispatch! [:entry-clear-local-cache edit-key]))

(defn entry-save
  ([edit-key edited-data section-editing]
     (entry-save edit-key edited-data section-editing create-update-entry-cb))

  ([edit-key edited-data section-editing entry-save-cb]
     (let [fixed-edited-data (assoc-in edited-data [:status] (or (:status edited-data) "draft"))
           fixed-edit-key (or edit-key :entry-editing)
           org-data (dis/org-data)]
       (dis/dispatch! [:entry-save fixed-edit-key])
       (if (:links fixed-edited-data)
         (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
                  section-editing)
           ;; Save existing post to new board
           (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note)
                 final-board-data (assoc section-editing :entries [fixed-entry-data])
                 create-board-link (utils/link-for (:links org-data) "create")]
             (api/create-board create-board-link final-board-data (:invite-note fixed-edited-data)
               (fn [{:keys [success status body] :as response}]
                 (if (= status 409)
                   ;; Board name exists
                   (board-name-exists-error fixed-edit-key)
                   (entry-save-cb fixed-edited-data fixed-edit-key response)))))
           ;; Update existing post
           (let [patch-entry-link (utils/link-for (:links edited-data) "partial-update")]
             (api/patch-entry patch-entry-link fixed-edited-data fixed-edit-key entry-save-cb)))
         (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
                  section-editing)
           ;; Save new post to new board
           (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note)
                 final-board-data (assoc section-editing :entries [fixed-entry-data])
                 create-board-link (utils/link-for (:links org-data) "create")]
             (api/create-board create-board-link final-board-data (:invite-note fixed-edited-data)
               (fn [{:keys [success status body] :as response}]
                 (if (= status 409)
                   ;; Board name exists
                   (board-name-exists-error fixed-edit-key)
                   (entry-save-cb fixed-edited-data fixed-edit-key response)))))
           ;; Save new post to existing board
           (let [org-slug (router/current-org-slug)
                 entry-board-data (first (filter #(= (:slug %) (:board-slug fixed-edited-data)) (:boards org-data)))
                 entry-create-link (utils/link-for (:links entry-board-data) "create")]
             (api/create-entry entry-create-link fixed-edited-data fixed-edit-key entry-save-cb)))))))

(defn entry-publish-finish [initial-uuid edit-key board-slug activity-data]
  ;; Save last used section
  (au/save-last-used-section board-slug)
  (refresh-org-data)
  ;; Remove entry cached edits
  (cmail-actions/remove-cached-item initial-uuid)
  ;; reset initial revision after successful publish.
  ;; need a new revision number on the next edit.
  (swap! initial-revision dissoc (:uuid activity-data))
  (dis/dispatch! [:entry-publish/finish edit-key activity-data])
  ;; Send item read
  (send-item-read (:uuid activity-data))
  ;; Show the first post added tooltip if needed
  (nux-actions/show-post-added-tooltip (:uuid activity-data))
  ;; Refresh the drafts board on publish
  (let [drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) (:boards (dis/org-data))))
        drafts-link (utils/link-for (:links drafts-board) "self")]
    (when drafts-link
      (sa/section-get dis/other-sort-type drafts-link)))
  ;; Show follow-ups notifications if needed
  (when (pos? (count (:follow-ups activity-data)))
    (let [follow-ups (:follow-ups activity-data)
          activity-follow-ups (cmail-actions/follow-ups-for-activity activity-data (dis/team-roster))
          available-users-set (set (map #(-> % :assignee :user-id) activity-follow-ups))
          actual-follow-ups-set (set (map #(-> % :assignee :user-id) follow-ups))
          notification-message (if (or (= (count actual-follow-ups-set) (count available-users-set))
                                       (and (= (dec (count actual-follow-ups-set)) (count available-users-set))
                                            (not (utils/in? actual-follow-ups-set (jwt/user-id)))))
                                (str "You requested a follow-up from everyone in " (:board-name activity-data) ".")
                                (str "You've requested a follow-up from " (count actual-follow-ups-set)
                                 (if (= (count actual-follow-ups-set) 1)
                                  " person."
                                  " people.")))]
      (notification-actions/show-notification {:title "Follow-up requested"
                                               :description notification-message
                                               :primary-bt-dismiss true
                                               :primary-bt-title "OK"
                                               :primary-bt-inline true
                                               :expire 3
                                               :id :publish-follow-ups}))))

(defn entry-publish-cb [entry-uuid posted-to-board-slug edit-key {:keys [status success body]}]
  (if success
    (entry-publish-finish entry-uuid edit-key posted-to-board-slug (when success (json->cljs body)))
    (dis/dispatch! [:entry-publish/failed edit-key])))

(defn entry-publish-with-board-finish [entry-uuid edit-key new-board-data]
  (let [board-slug (:slug new-board-data)
        saved-activity-data (first (:entries new-board-data))]
    (au/save-last-used-section (:slug new-board-data))
    (cmail-actions/remove-cached-item entry-uuid)
    ;; reset initial revision after successful publish.
    ;; need a new revision number on the next edit.
    (swap! initial-revision dissoc entry-uuid)
    (refresh-org-data)
    (when-not (= (:slug new-board-data) (router/current-board-slug))
      ;; If creating a new board, start watching changes
      (ws-cc/container-watch (:uuid new-board-data)))
    (dis/dispatch! [:entry-publish-with-board/finish (router/current-sort-type) new-board-data edit-key])
    ;; Send item read
    (send-item-read (:uuid saved-activity-data))
    (nux-actions/show-post-added-tooltip (:uuid saved-activity-data))))

(defn entry-publish-with-board-cb [entry-uuid edit-key {:keys [status success body]}]
  (if (= status 409)
    ; Board name already exists
    (board-name-exists-error :section-editing)
    (entry-publish-with-board-finish entry-uuid edit-key (when success (json->cljs body)))))

(defn entry-publish [entry-editing section-editing & [edit-key]]
  (let [fixed-entry-editing (assoc entry-editing :status "published")
        fixed-edit-key (or edit-key :entry-editing)]
    (dis/dispatch! [:entry-publish fixed-edit-key])
    (if (and (= (:board-slug fixed-entry-editing) utils/default-section-slug)
             section-editing)
      (let [fixed-entry-data (dissoc fixed-entry-editing :board-slug :board-name :invite-note)
            final-board-data (assoc section-editing :entries [fixed-entry-data])
            create-board-link (utils/link-for (:links (dis/org-data)) "create")]
        (api/create-board create-board-link final-board-data (:invite-note section-editing)
         (partial entry-publish-with-board-cb (:uuid fixed-entry-editing) fixed-edit-key)))
      (let [entry-exists? (seq (:links fixed-entry-editing))
            org-slug (router/current-org-slug)
            board-data (dis/board-data @dis/app-state org-slug (:board-slug fixed-entry-editing))
            publish-entry-link (if entry-exists?
                                ;; If the entry already exists use the publish link in it
                                (utils/link-for (:links fixed-entry-editing) "publish")
                                ;; If the entry is new, use
                                (utils/link-for (:links board-data) "create"))]
        (api/publish-entry publish-entry-link fixed-entry-editing
         (partial entry-publish-cb (:uuid fixed-entry-editing) (:board-slug fixed-entry-editing) fixed-edit-key))))))

(defn activity-delete-finish []
  ;; Reload the org to update the number of drafts in the navigation
  (when (= (router/current-board-slug) utils/default-drafts-board-slug)
    (refresh-org-data)))

(defn activity-delete [activity-data]
  ;; Make sure the WRT sample is dismissed
  (nux-actions/dismiss-post-added-tooltip)
  (cmail-actions/remove-cached-item (:uuid activity-data))
  (when (:links activity-data)
    (let [activity-delete-link (utils/link-for (:links activity-data) "delete")]
      (api/delete-entry activity-delete-link activity-delete-finish)
      (dis/dispatch! [:activity-delete (router/current-org-slug) activity-data]))))

(defn activity-move [activity-data board-data]
  (let [fixed-activity-data (assoc activity-data :board-slug (:slug board-data))
        patch-entry-link (utils/link-for (:links activity-data) "partial-update")]
    (api/patch-entry patch-entry-link fixed-activity-data nil create-update-entry-cb)
    (dis/dispatch! [:activity-move activity-data (router/current-org-slug) board-data])))

(defn activity-share-show [activity-data & [element-id share-medium]]
  (dis/dispatch! [:activity-share-show activity-data element-id (or share-medium :url)]))

(defn activity-share-hide []
  (dis/dispatch! [:activity-share-hide]))

(defn activity-share-reset []
  (dis/dispatch! [:activity-share-reset]))

(defn activity-share-cb [{:keys [status success body]}]
  (dis/dispatch! [:activity-share/finish success (when success (json->cljs body))]))

(defn activity-share [activity-data share-data & [share-cb]]
  (let [share-link (utils/link-for (:links activity-data) "share")
        callback (if (fn? share-cb) share-cb activity-share-cb)]
    (api/share-entry share-link share-data callback)
    (dis/dispatch! [:activity-share share-data])))

(defn entry-revert [revision-id entry-editing]
  (when-not (nil? revision-id)
    (let [entry-exists? (seq (:links entry-editing))
          entry-version (assoc entry-editing :revision-id revision-id)
          org-slug (router/current-org-slug)
          board-data (dis/board-data @dis/app-state org-slug (:board-slug entry-editing))
          revert-entry-link (when entry-exists?
                              ;; If the entry already exists use the publish link in it
                              (utils/link-for (:links entry-editing) "revert"))]
      (if entry-exists?
        (api/revert-entry revert-entry-link entry-version
                          (fn [{:keys [success body]}]
                            (dis/dispatch! [:entry-revert entry-version])
                            (when success
                              (dis/dispatch! [:entry-revert/finish (json->cljs body)]))))
        (dis/dispatch! [:entry-revert false])))))

(defn activity-get-finish [status activity-data secure-uuid]
  (cond

   (some #{status} [401 404])
   ;; Force a 404 in case user is visiting a secure activity as anonymous
   ;; that don't exists, the secure activity are always visible
   (routing-actions/maybe-404 (and (not (jwt/jwt))
                                   (not (jwt/id-token))
                                   (some? (router/current-secure-activity-id))))

   ;; The id token will have a current activity id, shared urls will not.
   ;; if the ids don't match return a 404
   (and (some? (router/current-activity-id))
        (not= (:uuid activity-data)
              (router/current-activity-id)))
   (routing-actions/maybe-404)

   (and secure-uuid
        (jwt/jwt)
        (jwt/user-is-part-of-the-team (:team-id activity-data)))
   (router/redirect! (oc-urls/entry (router/current-org-slug) (:board-slug activity-data) (:uuid activity-data)))

   :default
   (dis/dispatch! [:activity-get/finish status (router/current-org-slug) activity-data secure-uuid])))

(defn org-data-from-secure-activity [secure-activity-data]
  (let [old-org-data (dis/org-data)]
    (-> secure-activity-data
      (select-keys [:org-uuid :org-name :org-slug :org-logo-url :org-logo-width :org-logo-height])
      (clojure.set/rename-keys {:org-uuid :uuid
                                :org-name :name
                                :org-slug :slug
                                :org-logo-url :logo-url
                                :org-logo-width :logo-width
                                :org-logo-height :logo-height})
      (merge old-org-data))))

(defn secure-activity-get-finish [{:keys [status success body]}]
  (let [secure-activity-data (if success (json->cljs body) {})
        org-data (org-data-from-secure-activity secure-activity-data)]
    (activity-get-finish status secure-activity-data (router/current-secure-activity-id))
    (dis/dispatch! [:org-loaded org-data])))

(defn get-org [org-data cb]
  (let [fixed-org-data (or org-data (dis/org-data))
        org-link (utils/link-for (:links fixed-org-data) ["item" "self"] "GET")]
    (api/get-org org-link (fn [{:keys [status body success]}]
      (let [org-data (json->cljs body)]
        (dis/dispatch! [:org-loaded org-data])
        (cb success))))))

(defn secure-activity-get [& [cb]]
  (api/get-secure-entry (router/current-org-slug) (router/current-secure-activity-id)
   (fn [resp]
     (secure-activity-get-finish resp)
     (when (fn? cb)
       (cb resp)))))

;; Change reaction

(defn activity-change [section-uuid activity-uuid]
  (let [org-data (dis/org-data)
        section-data (first (filter #(= (:uuid %) section-uuid) (:boards org-data)))
        activity-data (dis/activity-data (:slug org-data) activity-uuid)
        editing-activity-data (:cmail-data @dis/app-state)]
    (when (and ;; if we have the activity in the app-state
               activity-data
               ;; and we are not currently editing it (if we are editing
               ;; we don't need to refresh it on each change)
               (not= (:uuid activity-data) (:uuid editing-activity-data)))
      (get-entry activity-data))))

;; Change service actions

(defn ws-change-subscribe []
  (ws-cc/subscribe :container/change
    (fn [data]
      (let [change-data (:data data)
            section-uuid (:item-id change-data)
            change-type (:change-type change-data)]
        ;; Refresh AP if user is looking at it
        (when (= (router/current-board-slug) "all-posts")
          (all-posts-get (dis/org-data))))))
  (ws-cc/subscribe :item/change
    (fn [data]
      (let [change-data (:data data)
            activity-uuid (:item-id change-data)
            section-uuid (:container-id change-data)
            change-type (:change-type change-data)
            ;; In case another user is adding a new post mark it as unread
            ;; directly to avoid delays in the newly added post propagation
            org-data (dis/org-data)
            dispatch-unread (when (and (= change-type :add)
                                       (not= (:user-id change-data) (jwt/user-id)))
                              (fn [{:keys [success]}]
                                (when success
                                  (dis/dispatch! [:mark-unread (router/current-org-slug) {:uuid activity-uuid
                                                                                          :board-uuid section-uuid}]))))]
        (when (= change-type :delete)
          (dis/dispatch! [:activity-delete (router/current-org-slug) {:uuid activity-uuid}]))
        ;; Refresh the AP in case of items added or removed
        (when (or (= change-type :add)
                  (= change-type :delete))
          ;; Refresh the count of drafts and follow-ups
          (api/get-org (utils/link-for (:links org-data) "self") refresh-org-data-cb)
          ;; Refresh specific containers/sections
          (cond
            (= (router/current-board-slug) "all-posts")
            (all-posts-get org-data dispatch-unread)
            (= (router/current-board-slug) "follow-ups")
            (follow-ups-get org-data dispatch-unread)
            :else
            (sa/section-change section-uuid dispatch-unread)))
        ;; Refresh the activity in case of an item update
        (when (= change-type :update)
          (activity-change section-uuid activity-uuid)))))
  (ws-cc/subscribe :item/counts
    (fn [data]
      (dis/dispatch! [:activities-count (:data data)])))
  (ws-cc/subscribe :item/status
    (fn [data]
      (dis/dispatch! [:activity-reads (router/current-org-slug) (:item-id (:data data)) (count (:reads (:data data))) (:reads (:data data)) (dis/team-roster)]))))

;; AP Seen

(defn- send-item-seen
  "Actually send the seen. Needs to get the activity data from the app-state
  to read the published-at and make sure it's still inside the TTL."
  [activity-id]
  (when-let* [activity-data (dis/activity-data (router/current-org-slug) activity-id)
              publisher-id (:user-id (:publisher activity-data))
              container-id (:board-uuid activity-data)
              published-at-ts (.getTime (utils/js-date (:published-at activity-data)))
              today-ts (.getTime (utils/js-date))
              oc-seen-ttl-ms (* ls/oc-seen-ttl 24 60 60 1000)
              minimum-ttl (- today-ts oc-seen-ttl-ms)]
    (when (> published-at-ts minimum-ttl)
      ;; Send the seen because:
      ;; 1. item is published
      ;; 2. item is newer than TTL
      (ws-cc/item-seen publisher-id container-id activity-id))))

(def ap-seen-timeouts-list (atom {}))
(def ap-seen-wait-interval 3)

(defn ap-seen-events-gate
  "Gate to throttle too many seen call for the same UUID.
  Set a timeout to ap-seen-wait-interval seconds every time it's called with a new UUID,
  if there was already a timeout for that item remove the old one.
  Once the timeout finishes it means no other events were fired for it so we can send a seen.
  It will send seen every 3 seconds or more."
  [activity-id]
  ;; Discard everything if we are not on AP
  (when (= :all-posts (keyword (router/current-board-slug)))
    (let [wait-interval-ms (* ap-seen-wait-interval 1000)]
      ;; Remove the old timeout if there is
      (when-let [uuid-timeout (get @ap-seen-timeouts-list activity-id)]
        (.clearTimeout js/window uuid-timeout))
      ;; Set the new timeout
      (swap! ap-seen-timeouts-list assoc activity-id
       (utils/after wait-interval-ms
        (fn []
         (swap! ap-seen-timeouts-list dissoc activity-id)
         (send-item-seen activity-id)))))))

;; WRT read

(defn send-secure-item-seen-read []
  (when-let* [activity-data (dis/secure-activity-data)
              activity-id (:uuid activity-data)
              publisher-id (:user-id (:publisher activity-data))
              container-id (:board-uuid activity-data)
              token-data (jwt/get-id-token-contents)
              user-name (:name token-data)
              avatar-url (:avatar-url token-data)
              org-id (:uuid (dis/org-data))]
    (ws-cc/item-seen publisher-id container-id activity-id)
    (ws-cc/item-read org-id container-id activity-id user-name avatar-url)))

(defn send-item-read
  "Actually send the read. Needs to get the activity data from the app-state
  to read the published-id and the board uuid."
  [activity-id & [show-notification]]
  (when-let* [activity-key (dis/activity-key (router/current-org-slug) activity-id)
              activity-data (get-in @dis/app-state activity-key)
              org-id (:uuid (dis/org-data))
              container-id (:board-uuid activity-data)
              user-name (jwt/get-key :name)
              avatar-url (jwt/get-key :avatar-url)]
    (ws-cc/item-read org-id container-id activity-id user-name avatar-url)
    (dis/dispatch! [:mark-read (router/current-org-slug) activity-data])
    (when show-notification
      (notification-actions/show-notification {:title "Post marked as read"
                                               :dismiss true
                                               :expire 3
                                               :id :mark-read-success}))))

(def wrt-timeouts-list (atom {}))
(def wrt-wait-interval 3)

(defn wrt-events-gate
  "Gate to throttle too many wrt call for the same UUID.
  Set a timeout to wrt-wait-interval seconds every time it's called with a certain UUID,
  if there was already a timeout for that item remove it and reset it.
  Once the timeout finishes it means no other events were fired for it so we can send a seen.
  It will send seen every 3 seconds or more."
  [activity-id]
  (let [wait-interval-ms (* wrt-wait-interval 1000)]
    ;; Remove the old timeout if there is
    (if-let [uuid-timeout (get @wrt-timeouts-list activity-id)]
      ;; Remove the previous timeout if exists
      (.clearTimeout js/window uuid-timeout)
      ;; Send read if it's the first timeout for the current element
      (send-item-read activity-id))
    ;; Set the new timeout
    (swap! wrt-timeouts-list assoc activity-id
     (utils/after wait-interval-ms
      (fn []
       (swap! wrt-timeouts-list dissoc activity-id)
       (send-item-read activity-id))))))

;; Video handling

(defn uploading-video [video-id edit-key]
  (dis/dispatch! [:update [edit-key] #(merge % {:has-changes true})])
  (dis/dispatch! [:uploading-video (router/current-org-slug) video-id]))

(defn remove-video [edit-key activity-data]
  (let [has-changes (or (au/has-attachments? activity-data)
                        (au/has-text? activity-data))]
    (dis/dispatch! [:update [edit-key] #(merge % {:fixed-video-id nil
                                                  :video-id nil
                                                  :video-processed false
                                                  :video-error false
                                                  :has-changes has-changes})])))

(defn prompt-remove-video [edit-key activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "rerecord-video"
                    :message "Are you sure you want to delete the current video? This can’t be undone."
                    :link-button-title "Keep"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn []
                                      (remove-video edit-key activity-data)
                                      (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(defn video-started-recording-cb [edit-key video-token]
  (dis/dispatch! [:update [edit-key] #(merge % {:fixed-video-id video-token
                                                :video-id video-token
                                                ;; default video error to true
                                                :video-error true})]))

(defn video-processed-cb [edit-key video-token unmounted?]
  (when-not unmounted?
    (dis/dispatch! [:update [edit-key] #(merge % {:fixed-video-id video-token
                                                  :video-id video-token
                                                  ;; turn off video error since upload finished
                                                  :video-error false
                                                  :has-changes true})])))

;; Sample post handling

(defn delete-samples []
  ;; Make sure the WRT sample is dismissed
  (nux-actions/dismiss-post-added-tooltip)
  (let [org-data (dis/org-data)
        org-link (utils/link-for (:links org-data) ["item" "self"] "GET")
        delete-samples-link (utils/link-for (:links org-data) "delete-samples" "DELETE")]
    (when delete-samples-link
      (api/delete-samples delete-samples-link
       #(do
          (api/get-org org-link refresh-org-data-cb)
          (router/nav! (oc-urls/all-posts)))))))

(defn has-sample-posts? []
  (let [org-data (dis/org-data)]
    (utils/link-for (:links org-data) "delete-samples" "DELETE")))

(defn activity-edit
  ([]
    (let [cmail-state (:cmail-state @dis/app-state)
          cmail-data (:cmail-data @dis/app-state)
          next-cmail-data (if (and cmail-data
                                   cmail-state
                                   (not (:collapsed cmail-state)))
                             cmail-data
                             (cmail-actions/get-board-for-edit))]
      (activity-edit next-cmail-data)))
  ([activity-data]
    (let [fixed-activity-data (if-not (seq (:uuid activity-data))
                                (assoc activity-data :must-see (= (router/current-board-slug) "must-see"))
                                activity-data)
          is-published? (= (:status fixed-activity-data) "published")
          cmail-state {:fullscreen true :key (utils/activity-uuid)}]
      (cmail-actions/cmail-show fixed-activity-data cmail-state))))

(defn mark-unread [activity-data]
  (when-let [mark-unread-link (utils/link-for (:links activity-data) "mark-unread")]
    (dis/dispatch! [:mark-unread (router/current-org-slug) activity-data])
    (api/mark-unread mark-unread-link (:board-uuid activity-data)
     (fn [{:keys [success]}]
      (notification-actions/show-notification {:title (if success "Post marked as unread" "An error occurred")
                                               :description (when-not success "Please try again")
                                               :dismiss true
                                               :expire 3
                                               :id (if success :mark-unread-success :mark-unread-error)})))))

(defn saved-sort-type [org-slug]
  (if-let [sort-type-cookie (cook/get-cookie (router/last-sort-cookie org-slug))]
    (keyword sort-type-cookie)
    dis/default-sort-type))

(defn change-sort-type [type]
  (cook/set-cookie! (router/last-sort-cookie (router/current-org-slug)) (name type) cook/default-cookie-expire)
  (swap! router/path merge {:sort-type type}))

(defn complete-follow-up [entry-data assigned-follow-up]
  (let [self-follow-up-index (utils/index-of (:follow-ups entry-data) #(= (-> % :assignee :user-id) (jwt/user-id)))
        with-completed-follow-up (update-in entry-data [:follow-ups self-follow-up-index] merge
                                  {:completed? true
                                   :completed-at (utils/as-of-now)})
        complete-follow-up-link (utils/link-for (:links assigned-follow-up) "mark-complete" "POST")]
    (dis/dispatch! [:follow-up-complete (router/current-org-slug) with-completed-follow-up])
    (api/complete-follow-up complete-follow-up-link
     (fn [{:keys [success status body]}]
       (when success
         (dis/dispatch! [:activity-get/finish status (router/current-org-slug) (json->cljs body) nil]))
       (notification-actions/show-notification {:title (if success "Follow-up completed" "An error occurred")
                                               :description (when-not success "Please try again")
                                               :dismiss true
                                               :expire 3
                                               :id (if success :self-follow-up-completed
                                                    :self-follow-up-completed-error)})))))

(defn create-self-follow-up [entry-data create-follow-up-link]
  (let [org-data (dis/org-data)]
    (dis/dispatch! [:follow-up-create-self (:slug org-data) entry-data])
    (when create-follow-up-link
      (api/create-follow-ups create-follow-up-link {:self true}
       (fn [{:keys [status body success]}]
        (when success
          (dis/dispatch! [:activity-get/finish status (router/current-org-slug) (json->cljs body) nil]))
        (notification-actions/show-notification {:title (if success "Follow-up created" "An error occurred")
                                                 :description (when-not success "Please try again")
                                                 :dismiss true
                                                 :expire 3
                                                 :id (if success :self-follow-up-created
                                                      :self-follow-up-created-error)})
        (follow-ups-get org-data)
        (recent-follow-ups-get org-data))))))