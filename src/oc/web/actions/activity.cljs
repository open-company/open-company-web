(ns oc.web.actions.activity
  (:require-macros [if-let.core :refer (if-let* when-let*)])
  (:require [taoensso.timbre :as timbre]
            [defun.core :refer (defun)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.utils.poll :as pu]
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
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.utils.comment :as comment-utils]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.actions.contributions :as contrib-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(def initial-revision (atom {}))

(defn- watch-boards [posts-data]
  (when (jwt/jwt) ; only for logged in users
    (let [board-slugs (distinct (map :board-slug posts-data))
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

(defn- request-reads-count
  "Request the reads count data only for the items we don't have already."
  [item-ids]
  (let [cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
    (when (seq cleaned-ids)
      (api/request-reads-count cleaned-ids))))

;; bookmarks stream

(defn- bookmarks-get-finish
  [org-slug sort-type refresh? {:keys [body success]}]
  (when body
    (let [posts-data-key (dis/posts-data-key org-slug)
          bookmarks-data (when success (json->cljs body))]
      (when (= (dis/current-board-slug) "bookmarks")
        (cook/set-cookie! (router/last-board-cookie org-slug) "bookmarks" (* 60 60 24 365))
        (request-reads-count (->> bookmarks-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection bookmarks-data))))
      (if refresh?
        (dis/dispatch! [:bookmarks-refresh/finish org-slug sort-type bookmarks-data])
        (dis/dispatch! [:bookmarks-get/finish org-slug sort-type bookmarks-data])))))

(defn- bookmarks-real-get
  ([bookmarks-link org-slug sort-type finish-cb] (bookmarks-real-get bookmarks-link org-slug sort-type false finish-cb))
  ([bookmarks-link org-slug sort-type refresh? finish-cb]
  (api/get-all-posts bookmarks-link
   (fn [resp]
     (bookmarks-get-finish org-slug sort-type refresh? resp)
     (when (fn? finish-cb)
       (finish-cb resp))))))

(defn bookmarks-get [org-data & [finish-cb]]
  (when-let [bookmarks-link (utils/link-for (:links org-data) "bookmarks")]
    (bookmarks-real-get bookmarks-link (:slug org-data) dis/recently-posted-sort false finish-cb)))

(defn bookmarks-refresh
 "If the user is looking at the bookmarks view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
 ([] (bookmarks-refresh (dis/org-data)))
 ([org-data]
  (if-let* [bookmarks-data (dis/bookmarks-data)
              refresh-link (utils/link-for (:links bookmarks-data) "refresh")]
    (bookmarks-real-get refresh-link (:slug org-data) dis/recently-posted-sort true nil)
    (bookmarks-get org-data))))

(defn- bookmarks-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:bookmarks-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn bookmarks-more [more-link direction]
  (api/load-more-items more-link direction (partial bookmarks-more-finish (dis/current-org-slug) dis/recently-posted-sort direction))
  (dis/dispatch! [:bookmarks-more (dis/current-org-slug) dis/recently-posted-sort]))

;; All Posts

(defn- all-posts-get-finish [org-slug sort-type refresh? {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          posts-data-key (dis/posts-data-key org-slug)
          all-posts-data (when success (json->cljs body))]
      (when (= (dis/current-board-slug) "all-posts")
        (cook/set-cookie! (router/last-board-cookie org-slug) "all-posts" (* 60 60 24 365))
        (request-reads-count (->> all-posts-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection all-posts-data))))
      (if refresh?
        (dis/dispatch! [:all-posts-refresh/finish org-slug sort-type all-posts-data])
        (dis/dispatch! [:all-posts-get/finish org-slug sort-type all-posts-data])))))

(defn- activity-real-get
  ([activity-link org-slug sort-type finish-cb]
   (activity-real-get activity-link org-slug sort-type false finish-cb))
  ([activity-link org-slug sort-type refresh? finish-cb]
  (api/get-all-posts activity-link
   (fn [resp]
     (all-posts-get-finish org-slug sort-type refresh? resp)
     (when (fn? finish-cb)
       (finish-cb resp))))))

(defn all-posts-get [org-data & [finish-cb]]
  (when-let [activity-link (utils/link-for (:links org-data) "entries" "GET")]
    (activity-real-get activity-link (:slug org-data) dis/recently-posted-sort finish-cb)))

(defn recent-all-posts-get [org-data & [finish-cb]]
  (when-let [activity-link (utils/link-for (:links org-data) "activity" "GET")]
    (activity-real-get activity-link (:slug org-data) dis/recent-activity-sort  finish-cb)))

(defn all-posts-refresh
 "If the user is looking at the all-posts view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
 ([] (all-posts-refresh (dis/org-data)))
 ([org-data]
  (if-let* [all-posts-data (dis/all-posts-data)
              refresh-link (utils/link-for (:links all-posts-data) "refresh")]
    (activity-real-get refresh-link (:slug org-data) dis/recently-posted-sort true nil)
    (all-posts-get org-data))))

(defn- all-posts-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:all-posts-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn all-posts-more [more-link direction]
  (api/load-more-items more-link direction (partial all-posts-more-finish (dis/current-org-slug) (dis/current-sort-type) direction))
  (dis/dispatch! [:all-posts-more (dis/current-org-slug)]))

;; Inbox

(defn- inbox-get-finish [org-slug sort-type {:keys [body success]}]
  (when body
    (let [posts-data-key (dis/posts-data-key org-slug)
          inbox-data (when success (json->cljs body))]
      (when (= (dis/current-board-slug) "inbox")
        (cook/set-cookie! (router/last-board-cookie org-slug) "inbox" (* 60 60 24 365))
        (request-reads-count (->> inbox-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection inbox-data))))
      (dis/dispatch! [:inbox-get/finish org-slug sort-type inbox-data]))))

(defn inbox-get [org-data & [finish-cb]]
  (when-let [inbox-link (utils/link-for (:links org-data) "following-inbox")]
    (api/get-all-posts inbox-link
     (fn [resp]
       (inbox-get-finish (:slug org-data) dis/recently-posted-sort resp)
       (when (fn? finish-cb)
         (finish-cb resp))))))

(defn- inbox-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:inbox-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn inbox-more [more-link direction]
  (api/load-more-items more-link direction (partial inbox-more-finish (dis/current-org-slug) dis/recently-posted-sort direction))
  (dis/dispatch! [:inbox-more (dis/current-org-slug) dis/recently-posted-sort]))

;; Following stream

(defn- following-get-finish [org-slug sort-type keep-seen-at? refresh? {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          posts-data-key (dis/posts-data-key org-slug)
          following-data (when success (json->cljs body))
          current-board-slug (dis/current-board-slug)]
      (when (= current-board-slug "following")
        (cook/set-cookie! (router/last-board-cookie org-slug) "following" (* 60 60 24 365))
        (request-reads-count (->> following-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection following-data))))
      (if refresh?
        (dis/dispatch! [:following-get/finish org-slug sort-type current-board-slug keep-seen-at? following-data])
        (dis/dispatch! [:following-get/finish org-slug sort-type current-board-slug keep-seen-at? following-data])))))

(defn- following-real-get
  ([following-link org-slug sort-type keep-seen-at? finish-cb]
   (following-real-get following-link org-slug sort-type keep-seen-at? false finish-cb))
  ([following-link org-slug sort-type keep-seen-at? refresh? finish-cb]
  (api/get-all-posts following-link
   (fn [resp]
     (following-get-finish org-slug sort-type keep-seen-at? refresh? resp)
     (when (fn? finish-cb)
       (finish-cb resp))))))

(defn following-get
 ([] (following-get (dis/org-data) false nil))
 ([org-data] (following-get org-data false nil))
 ([org-data finish-cb] (following-get org-data false finish-cb))
 ([org-data keep-seen-at? finish-cb]
  (when-let [following-link (utils/link-for (:links org-data) "following")]
    (following-real-get following-link (:slug org-data) dis/recently-posted-sort keep-seen-at? finish-cb))))

(defn following-refresh
 "If the user is looking at the following view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
 ([] (following-refresh (dis/org-data) true))
 ([org-data] (following-refresh org-data true))
 ([org-data keep-seen-at?]
  (if-let* [following-data (dis/following-data)
              refresh-link (utils/link-for (:links following-data) "refresh")]
    (following-real-get refresh-link (:slug org-data) dis/recently-posted-sort keep-seen-at? true nil)
    (following-get org-data keep-seen-at? nil))))

(defn following-did-change []
  (let [current-board-slug (keyword (dis/current-board-slug))
        org-data (dis/org-data)]
    (if (= current-board-slug :following)
      (following-refresh org-data true)
      (following-get org-data true nil))))

(defn recent-following-get
 ([] (recent-following-get (dis/org-data) nil))
 ([org-data] (recent-following-get org-data nil))
 ([org-data finish-cb]
  (when-let [recent-following-link (utils/link-for (:links org-data) "recent-following")]
    (following-real-get recent-following-link (:slug org-data) dis/recent-activity-sort false finish-cb))))

(defn- following-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:following-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn following-more [more-link direction]
  (api/load-more-items more-link direction (partial following-more-finish (dis/current-org-slug) (dis/current-sort-type) direction))
  (dis/dispatch! [:following-more (dis/current-org-slug) (dis/current-sort-type)]))

;; Replies stream

(defn- replies-get-finish [org-slug sort-type keep-seen-at? refresh? {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          posts-data-key (dis/posts-data-key org-slug)
          replies-data (when success (json->cljs body))
          current-board-slug (dis/current-board-slug)]
      (when (= current-board-slug "replies")
        (cook/set-cookie! (router/last-board-cookie org-slug) "replies" (* 60 60 24 365))
        (request-reads-count (->> replies-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection replies-data))))
      (if refresh? 
        (dis/dispatch! [:replies-refresh/finish org-slug sort-type current-board-slug keep-seen-at? replies-data])
        (dis/dispatch! [:replies-get/finish org-slug sort-type current-board-slug keep-seen-at? replies-data])))))

(defn- replies-real-get
  ([replies-link org-slug sort-type keep-seen-at? finish-cb]
   (replies-real-get replies-link org-slug sort-type keep-seen-at? false finish-cb))
  ([replies-link org-slug sort-type keep-seen-at? refresh? finish-cb]
  (api/get-all-posts replies-link
   (fn [resp]
     (replies-get-finish org-slug sort-type keep-seen-at? refresh? resp)
     (when (fn? finish-cb)
       (finish-cb resp))))))

(defn replies-get
  ([] (replies-get (dis/org-data) false nil))
  ([org-data] (replies-get org-data false nil))
  ([org-data finish-cb] (replies-get org-data false finish-cb))
  ([org-data keep-seen-at? finish-cb]
   (when-let [replies-link (utils/link-for (:links org-data) "replies")]
     (replies-real-get replies-link (:slug org-data) dis/recent-activity-sort keep-seen-at? finish-cb))))

(defn replies-refresh
 ([] (replies-refresh (dis/org-data) true))
 ([org-data] (replies-refresh org-data true))
 ([org-data keep-seen-at?]
  (if-let* [replies-data (dis/following-data)
            refresh-link (utils/link-for (:links replies-data) "refresh")]
    (replies-real-get refresh-link (:slug org-data) dis/recently-posted-sort keep-seen-at? true nil)
    (replies-get org-data keep-seen-at? nil))))

(defn- replies-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:replies-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn replies-more [more-link direction]
  (api/load-more-items more-link direction (partial replies-more-finish (dis/current-org-slug) (dis/current-sort-type) direction))
  (dis/dispatch! [:replies-more (dis/current-org-slug) (dis/current-sort-type)]))

;; Unfollowing stream

(defn- unfollowing-get-finish [org-slug sort-type refresh? {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          posts-data-key (dis/posts-data-key org-slug)
          unfollowing-data (when success (json->cljs body))]
      (when (= (dis/current-board-slug) "unfollowing")
        (cook/set-cookie! (router/last-board-cookie org-slug) "unfollowing" (* 60 60 24 365))
        (request-reads-count (->> unfollowing-data :collection :items (map :uuid)))
        (watch-boards (:items (:collection unfollowing-data))))
      (if refresh?
        (dis/dispatch! [:unfollowing-refresh/finish org-slug sort-type unfollowing-data])
        (dis/dispatch! [:unfollowing-get/finish org-slug sort-type unfollowing-data])))))

(defn- unfollowing-real-get
  ([unfollowing-link org-slug sort-type finish-cb]
   (unfollowing-real-get unfollowing-link org-slug sort-type false finish-cb))
  ([unfollowing-link org-slug sort-type refresh? finish-cb]
  (api/get-all-posts unfollowing-link
   (fn [resp]
     (unfollowing-get-finish org-slug sort-type refresh? resp)
     (when (fn? finish-cb)
       (finish-cb resp))))))

(defn unfollowing-get [org-data & [finish-cb]]
  (when-let [unfollowing-link (utils/link-for (:links org-data) "unfollowing")]
    (unfollowing-real-get unfollowing-link (:slug org-data) dis/recently-posted-sort finish-cb)))

(defn recent-unfollowing-get [org-data & [finish-cb]]
  (when-let [recent-unfollowing-link (utils/link-for (:links org-data) "recent-unfollowing")]
    (unfollowing-real-get recent-unfollowing-link (:slug org-data) dis/recent-activity-sort finish-cb)))

(defn unfollowing-refresh
 "If the user is looking at the unfollowing view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
 ([] (unfollowing-refresh (dis/org-data) true))
 ([org-data] (unfollowing-refresh org-data true))
 ([org-data keep-seen-at?]
  (if-let* [unfollowing-data (dis/unfollowing-data)
            refresh-link (utils/link-for (:links unfollowing-data) "refresh")]
    (unfollowing-real-get refresh-link (:slug org-data) dis/recently-posted-sort true nil)
    (unfollowing-get org-data keep-seen-at? nil))))

(defn- unfollowing-more-finish [org-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (->> body json->cljs :collection :items (map :uuid))))
  (dis/dispatch! [:unfollowing-more/finish org-slug sort-type direction (when success (json->cljs body))]))

(defn unfollowing-more [more-link direction]
  (api/load-more-items more-link direction (partial unfollowing-more-finish (dis/current-org-slug) (dis/current-sort-type) direction))
  (dis/dispatch! [:unfollowing-more (dis/current-org-slug) (dis/current-sort-type)]))

;; Referesh org when needed
(defn- refresh-org-data-cb

  ([resp]
   (refresh-org-data-cb (dis/current-board-slug) (dis/current-sort-type) resp))

  ([board-slug sort-type {:keys [status body success]}]
  (let [org-data (json->cljs body)
        board-kw (keyword board-slug)
        is-all-posts (= board-kw :all-posts)
        is-bookmarks (= board-kw :bookmarks)
        is-inbox (= board-kw :inbox)
        is-following (= board-kw :following)
        is-unfollowing (= board-kw :unfollowing)
        is-replies (= board-kw :replies)
        is-drafts (= board-slug utils/default-drafts-board-slug)
        active-users (dis/active-users)
        is-contributions? (get active-users board-slug)
        board-data (dis/org-board-data org-data board-slug)
        board-link (when (and (not is-all-posts) (not is-bookmarks) (not is-inbox))
                     (utils/link-for (:links board-data) ["item" "self"] "GET"))]
    (dis/dispatch! [:org-loaded org-data])
    (cond
      ; is-inbox
      ; (inbox-get org-data)

      (and is-all-posts
           (= sort-type dis/recently-posted-sort))
      (all-posts-get org-data)

      (and is-all-posts
           (= sort-type dis/recent-activity-sort))
      (recent-all-posts-get org-data)

      is-bookmarks
      (bookmarks-get org-data)

      (and is-following
           (= sort-type dis/recently-posted-sort))
      (following-get org-data)

      (and is-following
           (= sort-type dis/recent-activity-sort))
      (recent-following-get org-data)

      (and is-unfollowing
           (= sort-type dis/recently-posted-sort))
      (unfollowing-get org-data)

      (and is-unfollowing
           (= sort-type dis/recent-activity-sort))
      (recent-unfollowing-get org-data)

      is-replies
      (replies-get org-data)

      is-contributions?
      (contrib-actions/contributions-get board-slug)

      (map? board-link)
      (sa/section-get board-slug board-link)))))

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
  (when (dis/current-activity-id)
    (utils/after 1 #(let [is-all-posts (= (dis/current-board-slug) "all-posts")
                          is-must-see (= (dis/current-board-slug) "must-see")]
                      (router/nav!
                        (cond
                          is-all-posts ; AP
                          (oc-urls/all-posts (dis/current-org-slug))
                          is-must-see
                          (oc-urls/must-see (dis/current-org-slug))
                          :else
                          (oc-urls/board (dis/current-org-slug) (dis/current-board-slug)))))))
  ;; Add :entry-edit-dissmissing for 1 second to avoid reopening the activity modal after edit is dismissed.
  (utils/after 1000 #(dis/dispatch! [:input [:entry-edit-dissmissing] false]))
  (dis/dispatch! [:entry-edit/dismiss]))

(declare entry-save)

(defn entry-save-on-exit
  ([edit-key entry-data entry-body section-editing]
   (entry-save-on-exit edit-key entry-data entry-body section-editing nil))

  ([edit-key entry-data entry-body section-editing callback]
  (let [entry-map (assoc entry-data :body entry-body)
        cache-key (cmail-actions/get-entry-cache-key (:uuid entry-data))]
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
           (dis/dispatch! [:update [edit-key] #(merge % {:auto-saving true :body (:body entry-map)})])
           (entry-save edit-key entry-map section-editing
             (fn [entry-data-saved edit-key-saved {:keys [success body status]}]
               (if-not success
                 ;; If save fails let's make sure save will be retried on next call
                 (dis/dispatch! [:update [edit-key ] #(merge % {:auto-saving false :has-changes true})])
                 (let [json-body (json->cljs body)
                       board-data (if (:entries json-body)
                                    (au/parse-board json-body)
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
                     (dis/dispatch! [:entry-save-with-board/finish (dis/current-org-slug) board-data]))
                   ;; add or update the entry in the app-state list of posts
                   ;; also move the updated data to the entry editing
                   (dis/dispatch! [:entry-auto-save/finish entry-saved edit-key entry-map])))
               (when (fn? callback)
                 (callback success))))
           (dis/dispatch! [:entry-toggle-save-on-exit false]))))))))

(defn entry-toggle-save-on-exit
  [enable?]
  (dis/dispatch! [:entry-toggle-save-on-exit enable?]))

(declare send-item-read)

(defn entry-save-finish [board-slug entry-data initial-uuid edit-key]
  (let [org-slug (dis/current-org-slug)
        is-published? (= (:status entry-data) "published")]
    (when (and (dis/current-activity-id)
               (not= board-slug (dis/current-board-slug)))
      (router/nav! (oc-urls/entry org-slug board-slug (:uuid entry-data))))
    (au/save-last-used-section board-slug)
    (when-not is-published?
      (sa/drafts-get))
    ;; Remove saved cached item
    (cmail-actions/remove-cached-item initial-uuid)
    ;; reset initial revision after successful save.
    ;; need a new revision number on the next edit.
    (swap! initial-revision dissoc (:uuid entry-data))
    (dis/dispatch! [:entry-save/finish (assoc entry-data :board-slug board-slug) edit-key])
    ;; Send item read
    (when is-published?
      (send-item-read (:uuid entry-data))
      (notification-actions/show-notification {:title "Changes have been saved"
                                               :primary-bt-dismiss true
                                               :primary-bt-title "OK"
                                               :primary-bt-inline true
                                               :expire 3
                                               :id :entry-updated-notification}))))

(defn create-update-entry-cb [entry-data edit-key {:keys [success body status]}]
  (if success
    (entry-save-finish (:board-slug entry-data) (when body (json->cljs body)) (:uuid entry-data) edit-key)
    (dis/dispatch! [:entry-save/failed edit-key])))

(defn- board-name-exists-error [edit-key]
  (dis/dispatch! [:update [edit-key] #(-> %
                                        (assoc :section-name-error utils/section-name-exists-error)
                                        (dissoc :loading))]))

(defn add-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-add-attachment dispatch-input-key attachment-data])
  (dis/dispatch! [:input [dispatch-input-key :has-changes] true]))

(defn remove-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-remove-attachment dispatch-input-key attachment-data])
  (dis/dispatch! [:input [dispatch-input-key :has-changes] true]))

(declare secure-activity-get)

(defn get-entry [entry-data]
  (if (dis/current-secure-activity-id)
    (secure-activity-get)
    (let [entry-link (utils/link-for (:links entry-data) "self")]
      (dis/dispatch! [:activity-get {:org-slug (dis/current-org-slug)
                                     :board-slug (:board-slug entry-data)
                                     :activity-uuid (:uuid entry-data)}])
      (api/get-entry entry-link
        (fn [{:keys [status success body]}]
          (if (and (= status 404)
                   (= (:uuid entry-data) (dis/current-activity-id)))
            (do
              (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) (:uuid entry-data) nil])
              (routing-actions/maybe-404))
            (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (json->cljs body) nil])))))))

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
     (when-not (payments-actions/show-paywall-alert? (dis/payments-data))
       (let [publisher-board (some #(when (:publisher-board %) %) (dis/editable-boards-data))
             fixed-edited-data (merge edited-data {:status (or (:status edited-data) "draft")
                                                   :board-slug (if (and publisher-board
                                                                        (:publisher-board edited-data))
                                                                 (:slug publisher-board)
                                                                 (:board-slug edited-data))})
             fixed-edit-key (or edit-key :entry-editing)
             org-data (dis/org-data)]
         (dis/dispatch! [:entry-save fixed-edit-key])
         (if (:links fixed-edited-data)
           (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
                    (:publisher-board fixed-edited-data))
             ;; Save existing post to new board
             (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note :publisher-board)
                   final-board-data {:name (:board-name fixed-edited-data)
                                     :entries [fixed-entry-data]
                                     :access (:board-access fixed-edited-data)
                                     :publisher-board (:publisher-board fixed-edited-data)}
                   create-board-link (utils/link-for (:links org-data) "create")]
               (api/create-board create-board-link final-board-data (:invite-note fixed-edited-data)
                 (fn [{:keys [success status body] :as response}]
                   (if (= status 409)
                     ;; Board name exists
                     (board-name-exists-error fixed-edit-key)
                     (let [response-board-data (when success (json->cljs body))
                           updated-entry-data (merge fixed-edited-data {:board-name (:name response-board-data)
                                                                        :board-slug (:slug response-board-data)
                                                                        :board-access (:access response-board-data)
                                                                        :publisher-board (:publisher-board response-board-data)
                                                                        :board-uuid (:uuid response-board-data)})]
                       (entry-save-cb updated-entry-data fixed-edit-key response))))))
             ;; Update existing post
             (let [patch-entry-link (utils/link-for (:links edited-data) "partial-update")]
               (api/patch-entry patch-entry-link fixed-edited-data fixed-edit-key entry-save-cb)))
           (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
                    (:publisher-board fixed-edited-data))
             ;; Save new post to new board
             (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note :publisher-board)
                   final-board-data {:name (:board-name fixed-edited-data)
                                     :entries [fixed-entry-data]
                                     :access (:board-access fixed-edited-data)
                                     :publisher-board (:publisher-board fixed-edited-data)}
                   create-board-link (utils/link-for (:links org-data) "create")]
               (api/create-board create-board-link final-board-data (:invite-note fixed-edited-data)
                 (fn [{:keys [success status body] :as response}]
                   (if (= status 409)
                     ;; Board name exists
                     (board-name-exists-error fixed-edit-key)
                     (let [response-board-data (when success (json->cljs body))
                           updated-entry-data (merge fixed-edited-data {:board-name (:name response-board-data)
                                                                        :board-slug (:slug response-board-data)
                                                                        :board-access (:access response-board-data)
                                                                        :publisher-board (:publisher-board response-board-data)
                                                                        :board-uuid (:uuid response-board-data)})]
                       (entry-save-cb updated-entry-data fixed-edit-key response))))))
             ;; Save new post to existing board
             (let [org-slug (dis/current-org-slug)
                   entry-board-data (dis/org-board-data org-data (:board-slug fixed-edited-data))
                   entry-create-link (utils/link-for (:links entry-board-data) "create")]
               (api/create-entry entry-create-link fixed-edited-data fixed-edit-key entry-save-cb))))))))

(defn- entry-publish-finish [initial-uuid edit-key org-slug board-slug entry-data]
  ;; Save last used section
  (au/save-last-used-section board-slug)
  ; (refresh-org-data)
  ;; Remove entry cached edits
  (cmail-actions/remove-cached-item initial-uuid)
  ;; reset initial revision after successful publish.
  ;; need a new revision number on the next edit.
  (swap! initial-revision dissoc (:uuid entry-data))
  (dis/dispatch! [:entry-publish/finish org-slug edit-key entry-data])
  ;; Send item read
  (send-item-read (:uuid entry-data))
  ;; Show the first post added tooltip if needed
  (nux-actions/show-post-added-tooltip (:uuid entry-data))
  ;; Refresh the drafts board on publish
  (let [drafts-board (dis/org-board-data utils/default-drafts-board-slug)
        drafts-link (utils/link-for (:links drafts-board) "self")]
    (when drafts-link
      (sa/section-get utils/default-drafts-board-slug drafts-link))))

(defn- entry-publish-cb [entry-uuid posted-to-board-slug edit-key {:keys [status success body]}]
  (if success
    (entry-publish-finish entry-uuid edit-key (dis/current-org-slug) posted-to-board-slug (when success (json->cljs body)))
    (dis/dispatch! [:entry-publish/failed edit-key])))

(defn- entry-publish-with-board-finish [entry-uuid edit-key new-board-data]
  (let [board-slug (:slug new-board-data)
        saved-entry-data (first (:entries new-board-data))]
    (au/save-last-used-section (:slug new-board-data))
    (cmail-actions/remove-cached-item entry-uuid)
    ;; reset initial revision after successful publish.
    ;; need a new revision number on the next edit.
    (swap! initial-revision dissoc entry-uuid)
    ; (refresh-org-data)
    (when-not (= (:slug new-board-data) (dis/current-board-slug))
      ;; If creating a new board, start watching changes
      (ws-cc/container-watch (:uuid new-board-data)))
    (dis/dispatch! [:entry-publish-with-board/finish new-board-data edit-key])
    ;; Send item read
    (send-item-read (:uuid saved-entry-data))
    (nux-actions/show-post-added-tooltip (:uuid saved-entry-data))))

(defn- entry-publish-with-board-cb [entry-uuid edit-key {:keys [status success body]}]
  (if (= status 409)
    ; Board name already exists
    (board-name-exists-error :section-editing)
    (entry-publish-with-board-finish entry-uuid edit-key (when success (json->cljs body)))))

(defn entry-publish [entry-editing section-editing & [edit-key]]
  (when-not (payments-actions/show-paywall-alert? (dis/payments-data))
    (let [fixed-edit-key (or edit-key :entry-editing)]
      (if (get-in dis/app-state [edit-key :auto-saving])
        (utils/after 1000 #(entry-publish entry-editing section-editing edit-key))
        (let [org-data (dis/org-data)
              fixed-entry-editing (assoc entry-editing :status "published")]
          (dis/dispatch! [:entry-publish fixed-edit-key])
          (if (and (= (:board-slug fixed-entry-editing) utils/default-section-slug)
                   section-editing)
            (let [fixed-entry-data (dissoc fixed-entry-editing :board-slug :board-name :invite-note :publisher-board)
                  final-board-data (assoc section-editing :entries [fixed-entry-data])
                  create-board-link (utils/link-for (:links org-data) "create")]
              (api/create-board create-board-link final-board-data (:invite-note section-editing)
               (partial entry-publish-with-board-cb (:uuid fixed-entry-editing) fixed-edit-key)))
            (let [entry-exists? (seq (:links fixed-entry-editing))
                  board-data (dis/org-board-data org-data (:board-slug fixed-entry-editing))
                  publish-entry-link (if entry-exists?
                                      ;; If the entry already exists use the publish link in it
                                      (utils/link-for (:links fixed-entry-editing) "publish")
                                      ;; If the entry is new, use
                                      (utils/link-for (:links board-data) "create"))]
              (api/publish-entry publish-entry-link fixed-entry-editing
               (partial entry-publish-cb (:uuid fixed-entry-editing) (:board-slug fixed-entry-editing) fixed-edit-key)))))))))

(defn- activity-delete-finish []
  ;; Reload the org to update the number of drafts in the navigation
  (when (= (dis/current-board-slug) utils/default-drafts-board-slug)
    (sa/drafts-get)))

(defn- real-activity-delete [activity-data]
  (let [activity-delete-link (utils/link-for (:links activity-data) "delete")]
    (api/delete-entry activity-delete-link activity-delete-finish)
    (dis/dispatch! [:activity-delete (dis/current-org-slug) activity-data])))

(defn activity-delete [activity-data]
  ;; Make sure the WRT sample is dismissed
  (nux-actions/dismiss-post-added-tooltip)
  (cmail-actions/remove-cached-item (:uuid activity-data))
  (if (:links activity-data)
    (real-activity-delete activity-data)
    (when (:auto-saving activity-data)
      (utils/after 1000 #(real-activity-delete (dis/cmail-data))))))

(defn activity-move [activity-data board-data]
  (let [fixed-activity-data (assoc activity-data :board-slug (:slug board-data))
        patch-entry-link (utils/link-for (:links activity-data) "partial-update")]
    (api/patch-entry patch-entry-link fixed-activity-data nil create-update-entry-cb)
    (dis/dispatch! [:activity-move activity-data (dis/current-org-slug) board-data])))

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

(defn- entry-revert [revision-id entry-editing]
  (when-not (nil? revision-id)
    (let [entry-exists? (seq (:links entry-editing))
          entry-version (assoc entry-editing :revision-id revision-id)
          org-slug (dis/current-org-slug)
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
                                   (some? (dis/current-secure-activity-id))))

   ;; The id token will have a current activity id, shared urls will not.
   ;; if the ids don't match return a 404
   (and (some? (dis/current-activity-id))
        (not= (:uuid activity-data)
              (dis/current-activity-id)))
   (routing-actions/maybe-404)

   (and secure-uuid
        (jwt/jwt)
        (:member? (dis/org-data)))
   (router/redirect! (oc-urls/entry (dis/current-org-slug) (:board-slug activity-data) (:uuid activity-data)))

   :default
   (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) activity-data secure-uuid])))

(defn- org-data-from-secure-activity [secure-activity-data]
  (let [old-org-data (dis/org-data)]
    (-> secure-activity-data
      (select-keys [:org-uuid :org-name :org-slug :org-logo-url :team-id])
      (clojure.set/rename-keys {:org-uuid :uuid
                                :org-name :name
                                :org-slug :slug
                                :org-logo-url :logo-url})
      (merge old-org-data))))

(defn- secure-activity-get-finish [{:keys [status success body]}]
  (let [secure-activity-data (if success (json->cljs body) {})
        org-data (org-data-from-secure-activity secure-activity-data)]
    (activity-get-finish status secure-activity-data (dis/current-secure-activity-id))
    (dis/dispatch! [:org-loaded org-data])))

(defn get-org [org-data cb]
  (let [fixed-org-data (or org-data (dis/org-data))
        org-link (utils/link-for (:links fixed-org-data) ["item" "self"] "GET")]
    (api/get-org org-link (fn [{:keys [status body success]}]
      (let [org-data (json->cljs body)]
        (dis/dispatch! [:org-loaded org-data])
        (cb success))))))

(defn- build-secure-activity-link [org-slug secure-activity-id]
  {:href (str "/orgs/" org-slug "/entries/" secure-activity-id)
   :method "GET"
   :rel ""
   :accept "application/vnd.open-company.entry.v1+json"})

(defn secure-activity-get
  ([] (secure-activity-get nil (dis/current-secure-activity-id)))
  ([cb] (secure-activity-get cb (dis/current-secure-activity-id)))
  ([cb secure-uuid]
   (let [link-with-replacements (utils/link-for (:links (dis/api-entry-point)) "partial-secure" {} {:org-slug (dis/current-org-slug) :secure-uuid secure-uuid})
         secure-link (or link-with-replacements (build-secure-activity-link (dis/current-org-slug) secure-uuid))]
     (api/get-secure-entry secure-link
      (fn [resp]
        (secure-activity-get-finish resp)
        (when (fn? cb)
          (cb resp)))))))

;; Change reaction

(defn- entry-change [org-slug entry-uuid]
  (when-let [entry-data (dis/activity-data org-slug entry-uuid)]
    (get-entry entry-data)))

;; Following badge

(defn check-entry-for-badges [container-id entry-uuid]
  ;; Reload the entry from the server and check if we need to turn on the badge for home
  (let [org-slug (dis/current-org-slug)
        current-board-slug (dis/current-board-slug)
        board-data (au/board-by-uuid container-id)]
    (when-not (= (:slug board-data) utils/default-drafts-board-slug)
      (cmail-actions/get-entry-with-uuid (:slug board-data) entry-uuid
       (fn [success status]
         (when success
           (let [entry-data (dis/activity-data entry-uuid)
                 follow-boards-list (dis/follow-boards-list org-slug)
                 following-data (dis/container-data @dis/app-state org-slug :following dis/recently-posted-sort)
                 replies-data (dis/container-data @dis/app-state org-slug :replies dis/recent-activity-sort)
                 is-following? (= (keyword current-board-slug) :following)
                 is-replies? (= (keyword current-board-slug) :replies)
                 is-published? (= (keyword (:status entry-data)) :published)
                 current-user-data (dis/current-user-data)
                 is-publisher? (= (-> entry-data :publisher :user-id) (:user-id current-user-data))
                 following-entry? (or ;; if unfollow link is present it means the user is explicitly
                                      ;; following the entry
                                      (utils/link-for (:links entry-data) "unfollow")
                                      ;; or if the board is followed by the user
                                      ((set (map :uuid follow-boards-list)) container-id))
                 following-item (some #(when (= (:uuid %) (:uuid entry-data)) %) (:posts-list following-data))
                 should-badge-following? (and ;; Show badge on following only if user is not looking at it
                                              (not is-following?)
                                              is-published?
                                              following-entry?
                                              ;; and the user has never read it
                                              (not is-publisher?)
                                              (or (:unseen following-item)
                                                  (au/entry-unseen? entry-data (:last-seen-at following-data))))
                 should-badge-replies? (and ;; Show badge on replies only when user is looking at it
                                            ;; since we don't want them to re-arrange when visible
                                            is-replies?
                                            is-published?
                                            following-entry?
                                            (-> entry-data :links (utils/link-for "comments") :count pos?)
                                            (au/comments-unseen? entry-data (:last-seen-at replies-data)))]
             (when should-badge-following?
               (dis/dispatch! [:following-badge/on org-slug]))
             (when should-badge-replies?
               (dis/dispatch! [:replies-badge/on org-slug])))))))))

;; Change service actions

(defn ws-change-subscribe []
  (ws-cc/subscribe :container/change
    (fn [data]
      (let [change-data (:data data)
            container-id (:item-id change-data)
            change-type (:change-type change-data)]
        ;; Refresh AP if user is looking at it
        ; (when (= (dis/current-board-slug) "all-posts")
        ;   (all-posts-get (dis/org-data)))
        (when (= (dis/current-board-slug) "bookmarks")
          (bookmarks-get (dis/org-data)))
        ; (when (= (dis/current-board-slug) "following")
        ;   (following-get (dis/org-data)))
        (when (= (dis/current-board-slug) "unfollowing")
          (unfollowing-get (dis/org-data)))
        ; (when (= (dis/current-board-slug) "inbox")
        ;   (inbox-get (dis/org-data)))
        )))
  (ws-cc/subscribe :entry/inbox-action
    (fn [data]
      ;; Only in case the event is from/to this user:
      (when (and (#{:dismiss :unread :follow :unfollow :comment-add} (:change-type (:data data)))
                 (= (-> data :data :user-id) (jwt/user-id)))
        (let [change-data (:data data)
              org-slug (dis/current-org-slug)
              container-id (:container-id change-data)
              board-data (au/board-by-uuid container-id)
              entry-uuid (:item-id change-data)
              change-type (:change-type change-data)
              inbox-action (:inbox-action change-data)
              current-board-slug (dis/current-board-slug)
              org-data (dis/org-data)]
          (cond
            ; (= change-type :dismiss)
            ; (do
            ;   (timbre/debug "Dismiss for" entry-uuid)
            ;   (dis/dispatch! [:inbox/dismiss org-slug entry-uuid])
            ;   (inbox-get org-data))
            ; (= change-type :unread)
            ; (do
            ;   (timbre/debug "Unread for" entry-uuid)
            ;   (dis/dispatch! [:inbox/unread org-slug (:slug board-data) entry-uuid])
            ;   (inbox-get org-data))
            (= change-type :follow)
            (do
              (timbre/debug "Follow for" entry-uuid)
              ;; Reload the entry from the server and check if we need to turn on the badge for home
              (check-entry-for-badges container-id entry-uuid)
              ; (inbox-get org-data)
              (when (not= (keyword current-board-slug) :replies)
                ;; Reload all replies
                (replies-get org-data))
              ;; Refresh following items
              (following-did-change))
            (= change-type :unfollow)
            (do
              (timbre/debug "Unfollow for" entry-uuid)
              ; (inbox-get org-data)
              ;; Reload the entry from the server and check if we need to turn on the badge for home
              (check-entry-for-badges container-id entry-uuid)
              ;; Reload Replies if user is not currently there
              (when (not= (keyword current-board-slug) :replies)
                (replies-get org-data))
              ;; Refresh following items
              (following-did-change)))))
      (when (and (utils/in? (-> data :data :users) (jwt/user-id))
                 (= :comment-add (:change-type (:data data))))
        (let [change-data (:data data)
              entry-uuid (:item-id change-data)
              container-id (:container-id change-data)
              change-type (:change-type change-data)
              inbox-action (:inbox-action change-data)
              org-data (dis/org-data)
              current-board-slug (dis/current-board-slug)]
          (timbre/debug "Comment added for" entry-uuid)
          ;; Reload the entry from the server and check if we need to turn on the badge for home
          (check-entry-for-badges container-id entry-uuid)
          ;; Reload Replies if user is not currently there
          (when (not= (keyword current-board-slug) :replies)
            (replies-get org-data))
          ;; Delay the inbox refresh to make sure follows have been added
          ;; for al mentioned users
          ; (utils/after 500 #(inbox-get org-data))
          ))))
  (ws-cc/subscribe :item/change
    (fn [data]
      (let [change-data (:data data)
            container-id (:container-id change-data)
            entry-uuid (:item-id change-data)
            container-id (:container-id change-data)
            change-type (:change-type change-data)
            item-status (keyword (:item-status change-data))
            org-data (dis/org-data)
            ;; In case another user is adding a new post mark it as unread
            ;; directly to avoid delays in the newly added post propagation
            dispatch-unread (when (and (= change-type :add)
                                       (not= (:user-id change-data) (jwt/user-id)))
                              (fn [{:keys [success]}]
                                (when success
                                  (dis/dispatch! [:mark-unread (dis/current-org-slug) {:uuid entry-uuid
                                                                                          :board-uuid container-id}]))))
            current-board-slug (dis/current-board-slug)]
        (if-not (au/is-published? item-status)
          ;; Refresh the count of drafts and bookmarks
          (sa/drafts-get)
          (do
            (when (not= change-type :delete)
              ;; Refresh the badge
              (check-entry-for-badges container-id entry-uuid))
            ;; Refresh the AP in case of items added or removed
            (when (or (= change-type :add)
                      (= change-type :delete))
            
              (bookmarks-get org-data dispatch-unread)
              ;; Refresh following items
              (following-did-change)
              ;; Refresh specific containers/sections
              (cond
                ; (= current-board-slug "inbox")
                ; (inbox-get org-data dispatch-unread)
                ; (= current-board-slug "all-posts")
                ; (all-posts-get org-data dispatch-unread)
                ; (= current-board-slug "bookmarks")
                ; (bookmarks-get org-data dispatch-unread)
                ; (= current-board-slug "following")
                ; (following-get org-data dispatch-unread)
                (= current-board-slug "unfollowing")
                (unfollowing-get org-data dispatch-unread)
                ;; If it's not one of the previous containers then load
                ;; a real board if needed
                (and (not (dis/is-container? current-board-slug))
                     (not (dis/current-contributions-id)))
                (sa/section-change container-id dispatch-unread)))
            (when (= change-type :delete)
              (dis/dispatch! [:activity-delete (dis/current-org-slug) {:uuid entry-uuid}]))
            ;; Refresh the entry in case of an item update
            (when (= change-type :update)
              (entry-change (:slug org-data) entry-uuid)))))))
  (ws-cc/subscribe :item/counts
    (fn [data]
      (dis/dispatch! [:activities-count (dis/current-org-slug) (:data data)])))
  (ws-cc/subscribe :item/status
    (fn [data]
      (let [read-data (:data data)]
        (dis/dispatch! [:activity-reads (dis/current-org-slug) (:item-id read-data) (count (:reads read-data)) (:reads read-data)])))))

;; AP Seen

(defn send-item-seen
  "Actually send the seen. Needs to get the entry data from the app-state
  to read the published-at."
  [entry-uuid]
  (when-let* [entry-data (dis/entry-data (dis/current-org-slug) entry-uuid)
              publisher-id (:user-id (:publisher entry-data))
              container-id (:board-uuid entry-data)
              org-id (:org-uuid entry-data)]
    (ws-cc/item-seen publisher-id org-id container-id entry-uuid)))

(defn- send-container-seen
  "Send a seen message for the given container-id to Change service."
  [container-id seen-at]
  (when-let* [org-data (dis/org-data)
              org-id (:uuid org-data)
              _seen-at seen-at]
    (timbre/info "Send seen for container:" container-id "at:" seen-at)
    (ws-cc/container-seen org-id container-id seen-at)
    (dis/dispatch! [:container-seen (:org-slug org-data) container-id seen-at])))

;; Seen - containers nav events

(defonce last-sent-seen (atom nil))

(defn container-nav-in [container-slug sort-type]
  (let [container-data (dis/container-data @dis/app-state (dis/current-org-slug) container-slug sort-type)
        next-sent-seen (str "last-sent-seen-" (:container-slug container-data) "-" (:next-seen-at container-data))]
    (when (and (:container-id container-data)
               (not= @last-sent-seen next-sent-seen))
      (send-container-seen (:container-id container-data) (:next-seen-at container-data))
      (reset! last-sent-seen next-sent-seen)
      (dis/dispatch! [:container-nav-in (dis/current-org-slug) container-slug sort-type]))))

(defn container-nav-out [container-slug sort-type]
  ;; No-op
  (dis/dispatch! [:container-nav-out (dis/current-org-slug) container-slug sort-type]))
  
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
    (ws-cc/item-seen publisher-id org-id container-id activity-id)
    (ws-cc/item-read org-id container-id activity-id user-name avatar-url)))

(defn send-item-read
  "Actually send the read. Needs to get the activity data from the app-state
  to read the published-id and the board uuid."
  [activity-id]
  (when-let* [activity-key (dis/activity-key (dis/current-org-slug) activity-id)
              activity-data (get-in @dis/app-state activity-key)
              org-id (:uuid (dis/org-data))
              container-id (:board-uuid activity-data)
              user-name (jwt/get-key :name)
              avatar-url (jwt/get-key :avatar-url)]
    (ws-cc/item-read org-id container-id activity-id user-name avatar-url)))

(declare inbox-dismiss)

(defun mark-read
  ([activity-uuid :guard string?]
   (mark-read (dis/activity-data activity-uuid)))

  ([activity-data]
   (when (and activity-data
              (not= activity-data :404)
              (au/entry? activity-data)
              (not (:loading activity-data)))
     (send-item-read (:uuid activity-data))
     (dis/dispatch! [:mark-read (dis/current-org-slug) activity-data (utils/as-of-now)])
     (inbox-dismiss (:uuid activity-data))
     true)))

;; Video handling

(defn- remove-video [edit-key activity-data]
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
          (router/nav! (oc-urls/default-landing)))))))

(defn has-sample-posts? []
  (let [org-data (dis/org-data)]
    (utils/link-for (:links org-data) "delete-samples" "DELETE")))

(defn activity-edit
  ([]
    (let [cmail-state (dis/cmail-state)
          cmail-data (dis/cmail-data)
          next-cmail-data (if (and cmail-data
                                   cmail-state)
                             cmail-data
                             (cmail-actions/get-board-for-edit))]
      (activity-edit next-cmail-data)))
  ([activity-data]
    (let [is-published? (= (:status activity-data) "published")
          cmail-state {:key (utils/activity-uuid)
                       :fullscreen true
                       :collapsed false}]
      (cmail-actions/cmail-show activity-data cmail-state))))

(defn add-bookmark [activity-data add-bookmark-link]
  (when add-bookmark-link
    (dis/dispatch! [:bookmark-toggle (dis/current-org-slug) (:uuid activity-data) true])
    (api/toggle-bookmark add-bookmark-link
     (fn [{:keys [status success body]}]
      (let [new-activity-data (if success (json->cljs body) {})]
        (activity-get-finish status new-activity-data nil))))))

(defn remove-bookmark [activity-data remove-bookmark-link]
  (when remove-bookmark-link
    (dis/dispatch! [:bookmark-toggle (dis/current-org-slug) (:uuid activity-data) false])
    (api/toggle-bookmark remove-bookmark-link
     (fn [{:keys [status success body]}]
      (let [new-activity-data (if success (json->cljs body) {})]
        (activity-get-finish status new-activity-data nil))))))

;; Sort type handling

(defn saved-sort-type [org-slug board-slug]
  (let [sort-type-cookie (cook/get-cookie (router/last-sort-cookie org-slug))]
    (if (and (dis/is-container-with-sort? board-slug)
             (string? sort-type-cookie))
      (keyword sort-type-cookie)
      (if (dis/is-recent-activity? board-slug)
        dis/recent-activity-sort
        dis/recently-posted-sort))))

(defn change-sort-type [type]
  (cook/set-cookie! (router/last-sort-cookie (dis/current-org-slug)) (name type) cook/default-cookie-expire)
  (dis/dispatch! [:route/rewrite :sort-type type]))

;; Refresh data

(defn refresh-board-data [to-slug]
  (when (and (not (dis/current-activity-id))
             to-slug)
    (let [org-data (dis/org-data)
          active-users (dis/active-users)
          is-container? (dis/is-container? to-slug)
          is-board? (dis/org-board-data org-data to-slug)
          is-contributions? (get active-users to-slug)
          board-data (cond
                       is-container?
                       (dis/container-data @dis/app-state (dis/current-org-slug) to-slug)
                       is-board?
                       (dis/board-data to-slug))]
       (cond

        (= to-slug "inbox")
        (inbox-get org-data)

        (= to-slug "replies")
        (replies-get org-data)

        (and (= to-slug "all-posts")
             (= (dis/current-sort-type) dis/recently-posted-sort))
        (all-posts-get org-data)

        (and (= to-slug "all-posts")
             (= (dis/current-sort-type) dis/recent-activity-sort))
        (recent-all-posts-get org-data)

        (= to-slug "bookmarks")
        (bookmarks-get org-data)

        (and (= to-slug "following")
             (= (dis/current-sort-type) dis/recently-posted-sort))
        (following-get org-data)

        (and (= to-slug "following")
             (= (dis/current-sort-type) dis/recent-activity-sort))
        (recent-following-get org-data)

        (and (= to-slug "unfollowing")
             (= (dis/current-sort-type) dis/recently-posted-sort))
        (unfollowing-get org-data)

        (and (= to-slug "unfollowing")
             (= (dis/current-sort-type) dis/recent-activity-sort))
        (recent-unfollowing-get org-data)

        (and (not board-data)
             is-contributions?)
        (contrib-actions/contributions-get to-slug)

        :default
        (when-let* [fixed-board-data (or board-data
                                         (dis/org-board-data org-data to-slug))
                    board-link (utils/link-for (:links fixed-board-data) ["item" "self"] "GET")]
          (sa/section-get (:slug fixed-board-data) board-link))))))

;; FOC Layout

(defn saved-foc-layout [org-slug]
  (if-let [foc-layout-cookie (cook/get-cookie (router/last-foc-layout-cookie org-slug))]
    (keyword foc-layout-cookie)
    dis/default-foc-layout))

(defn toggle-foc-layout []
  (let [current-value (:foc-layout @dis/app-state)
        next-value (if (= current-value dis/default-foc-layout) dis/other-foc-layout dis/default-foc-layout)]
    (cook/set-cookie! (router/last-foc-layout-cookie (dis/current-org-slug)) (name next-value) cook/default-cookie-expire)
    (dis/dispatch! [:input [:foc-layout] next-value])))

;; Inbox actions

(defn entry-follow [entry-uuid]
  (let [activity-data (dis/activity-data entry-uuid)
        follow-link (utils/link-for (:links activity-data) "follow")]
    (api/inbox-follow follow-link
     (fn [{:keys [status success body]}]
       (if (and (= status 404)
                (= (:uuid activity-data) (dis/current-activity-id)))
         (do
           (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) (:uuid activity-data) nil])
           (routing-actions/maybe-404))
         (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (json->cljs body) nil]))))))

(defn entry-unfollow [entry-uuid]
  (let [activity-data (dis/activity-data entry-uuid)
        unfollow-link (utils/link-for (:links activity-data) "unfollow")]
    (api/inbox-unfollow unfollow-link
     (fn [{:keys [status success body]}]
       (if (and (= status 404)
                (= (:uuid activity-data) (dis/current-activity-id)))
         (do
           (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) (:uuid activity-data) nil])
           (routing-actions/maybe-404))
         (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (json->cljs body) nil]))))))

(defn inbox-dismiss [entry-uuid]
  (let [dismiss-at (utils/as-of-now)
        activity-data (dis/activity-data entry-uuid)
        dismiss-link (utils/link-for (:links activity-data) "dismiss")]
    (when dismiss-link
      (dis/dispatch! [:inbox/dismiss (dis/current-org-slug) entry-uuid dismiss-at])
      (api/inbox-dismiss dismiss-link dismiss-at
       (fn [{:keys [status success body]}]
         (if (and (= status 404)
                  (= (:uuid activity-data) (dis/current-activity-id)))
           (do
             (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) (:uuid activity-data) nil])
             (routing-actions/maybe-404))
           (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (json->cljs body) nil]))
          ; (inbox-get (dis/org-data))
          )))))

(declare inbox-unread)

(defn mark-unread [activity-data]
  (inbox-unread activity-data)
  (when-let [mark-unread-link (utils/link-for (:links activity-data) "mark-unread")]
    (dis/dispatch! [:mark-unread (dis/current-org-slug) activity-data])
    (api/mark-unread mark-unread-link (:board-uuid activity-data)
     (fn [{:keys [success]}]
      (notification-actions/show-notification {:title (if success "Post marked as unread" "An error occurred")
                                               :description (when-not success "Please try again")
                                               :dismiss true
                                               :expire 3
                                               :id (if success :mark-unread-success :mark-unread-error)})))))

(defn inbox-unread [activity-data]
  (when-let [unread-link (utils/link-for (:links activity-data) "unread")]
    (dis/dispatch! [:inbox/unread (dis/current-org-slug) (dis/current-board-slug) (:uuid activity-data)])
    (api/inbox-unread unread-link
     (fn [{:keys [status success body]}]
       (if (and (= status 404)
                (= (:uuid activity-data) (dis/current-activity-id)))
         (do
           (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) (:uuid activity-data) nil])
           (routing-actions/maybe-404))
         (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (json->cljs body) nil]))
       ; (inbox-get (dis/org-data))
       ))))

(defn- inbox-real-dismiss-all []
  (let [dismiss-at (utils/as-of-now)
        inbox-data (dis/container-data @dis/app-state (dis/current-org-slug) "inbox")
        dismiss-all-link (utils/link-for (:links inbox-data) "dismiss-all")]
    (dis/dispatch! [:inbox/dismiss-all (dis/current-org-slug)])
    (api/inbox-dismiss-all dismiss-all-link dismiss-at
     (fn [{:keys [status success body]}]
       ; (inbox-get (dis/org-data))
       ))))

(defn inbox-dismiss-all []
  (let [alert-data {:action "dismiss-all"
                    :title "Dismiss all posts?"
                    :message " This action cannot be undone."
                    :link-button-title "No, keep them"
                    :link-button-style :green
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-title "Yes, dismiss them"
                    :solid-button-style :red
                    :solid-button-cb (fn []
                                      (inbox-real-dismiss-all)
                                      (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(defn foc-menu-open [v]
  (dis/dispatch! [:foc-menu-open v]))