(ns oc.web.actions.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.actions.section :as sa]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.user-cache :as uc]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.ws-change-client :as ws-cc]
            [oc.web.lib.ws-interaction-client :as ws-ic]))

(defn save-last-used-section [section-slug]
  (let [org-slug (router/current-org-slug)
        last-board-cookie (router/last-used-board-slug-cookie org-slug)]
    (cook/set-cookie! last-board-cookie section-slug (* 60 60 24 365))))

(defn watch-boards [posts-data]
  (when (jwt/jwt) ; only for logged in users
    (let [board-slugs (distinct (map :board-slug
                                     (map second (:fixed-items posts-data))))
          org-data (dis/org-data)
          org-boards (:boards org-data)
          org-board-map (zipmap (map :slug org-boards) (map :uuid org-boards))]
      (ws-ic/board-unwatch (fn [rep]
        (doseq [board-slug board-slugs]
          (timbre/debug "Watching on socket " board-slug (org-board-map board-slug))
          (ws-ic/board-watch (org-board-map board-slug))))))))

;; All Posts
(defn all-posts-get-finish [from {:keys [body success]}]
  (when body
    (let [org-data (dis/org-data)
          org (router/current-org-slug)
          all-posts-key (dis/all-posts-key org)
          all-posts-data (when success (json->cljs body))
          fixed-all-posts (au/fix-all-posts (:collection all-posts-data))
          should-404? (and from
                           (router/current-activity-id)
                           (not (get (:fixed-items fixed-all-posts) (router/current-activity-id))))]
      (when should-404?
        (router/redirect-404!))
      (when (and (not should-404?)
                 (= (router/current-board-slug) "all-posts"))
        (save-last-used-section "all-posts"))
      (watch-boards fixed-all-posts)
      (dis/dispatch! [:all-posts-get/finish org fixed-all-posts]))))

(defn all-posts-get [org-data ap-initial-at]
  (when-let [activity-link (utils/link-for (:links org-data) "activity")]
    (api/get-all-posts activity-link ap-initial-at (partial all-posts-get-finish ap-initial-at))))

(defn all-posts-more-finish [direction {:keys [success body]}]
  (dis/dispatch! [:all-posts-more/finish (router/current-org-slug) direction (when success (json->cljs body))]))

(defn all-posts-more [more-link direction]
  (api/load-more-all-posts more-link direction (partial all-posts-more-finish direction))
  (dis/dispatch! [:all-posts-more (router/current-org-slug)]))

;; Must see
(defn must-see-get-finish
  [{:keys [success body]}]
    (when body
    (let [org-data (dis/org-data)
          org (router/current-org-slug)
          must-see-data (when success (json->cljs body))
          must-see-posts (au/fix-all-posts (:collection must-see-data))]
      (when (= (router/current-board-slug) "must-see")
        (save-last-used-section "must-see"))
      (watch-boards must-see-posts)
      (dis/dispatch! [:must-see-get/finish org must-see-posts]))))

(defn must-see-get [org-data]
  (when-let [activity-link (utils/link-for (:links org-data) "activity")]
    (let [activity-href (:href activity-link)
          must-see-filter (str activity-href "?must-see=true")
          must-see-link (assoc activity-link :href must-see-filter)]
      (api/get-all-posts must-see-link nil (partial must-see-get-finish)))))

;; Referesh org when needed
(defn refresh-org-data-cb [{:keys [status body success]}]
  (let [org-data (json->cljs body)
        is-all-posts (or (:from-all-posts @router/path)
                         (= (router/current-board-slug) "all-posts"))
        board-data (some #(when (= (:slug %) (router/current-board-slug)) %) (:boards org-data))]
    (dis/dispatch! [:org-loaded org-data])
    (if is-all-posts
      (all-posts-get org-data (:ap-initial-at @dis/app-state))
      (sa/section-get (utils/link-for (:links board-data) "self" "GET")))))

(defn refresh-org-data []
  (api/get-org (dis/org-data) refresh-org-data-cb))

;; Entry
(defn get-entry-cache-key
  [entry-uuid]
  (str (or
        entry-uuid
        (router/current-org-slug))
   "-entry-edit"))

(defn remove-cached-item
  [item-uuid]
  (uc/remove-item (get-entry-cache-key item-uuid)))

(defn load-cached-item
  [entry-data edit-key & [completed-cb]]
  (let [cache-key (get-entry-cache-key (:uuid entry-data))]
    (uc/get-item cache-key
     (fn [item err]
       (if (and (not err)
                (map? item)
                (= (:updated-at entry-data) (:updated-at item)))
         (let [entry-to-save (merge item (select-keys entry-data [:links :board-slug :board-name]))]
           (dis/dispatch! [:input [edit-key] entry-to-save]))
         (do
           ;; If we got an item remove it since it won't be used
           ;; since we have an updated version of it already
           (when item
             (remove-cached-item (:uuid entry-data)))
           (dis/dispatch! [:input [edit-key] entry-data])))
       (when (fn? completed-cb)
         (completed-cb))))))

(defn activity-modal-fade-in
  [activity-data & [editing item-load-cb dismiss-on-editing-end]]
  (let [org (router/current-org-slug)
        board (:board-slug activity-data)
        activity-uuid (:uuid activity-data)
        to-url (oc-urls/entry board activity-uuid)]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org board activity-uuid "activity"]
     {:org org
      :board board
      :activity activity-uuid
      :previous-board (router/current-board-slug)
      :query-params (dissoc (:query-params @router/path) :ap-initial-at)
      :from-all-posts (= (router/current-board-slug) "all-posts")}))
  (when editing
    (utils/after 100 #(load-cached-item activity-data :modal-editing-data item-load-cb)))
  (dis/dispatch! [:activity-modal-fade-in activity-data editing dismiss-on-editing-end]))

(defn activity-modal-fade-out
  [activity-board-slug]
  (let [from-all-posts (:from-all-posts @router/path)
        previous-board-slug (:previous-board @router/path)
        to-board (cond
                   ;; If user was in AP go back there
                   from-all-posts "all-posts"
                   ;; if the previous position is set use it
                   (seq previous-board-slug) previous-board-slug
                   ;; use the passed activity board slug if present
                   (seq activity-board-slug) activity-board-slug
                   ;; fallback to the current board slug
                   :else (router/current-board-slug))
        org (router/current-org-slug)
        to-url (if from-all-posts
                (oc-urls/all-posts org)
                (oc-urls/board org to-board))]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org to-board (if from-all-posts "all-posts" "dashboard")]
     {:org org
      :board to-board
      :activity nil
      :previous-board nil
      :query-params (:query-params @router/path)
      :from-all-posts false}))
  (dis/dispatch! [:activity-modal-fade-out activity-board-slug]))

(defn entry-edit
  [initial-entry-data]
  (load-cached-item initial-entry-data :entry-editing))

(defn activity-edit
  [activity-data]
  (if (or (responsive/is-tablet-or-mobile?)
          (not= (:status activity-data) "published"))
    (load-cached-item activity-data :entry-editing)
    (activity-modal-fade-in activity-data true (fn [] (dis/dispatch! [:modal-editing-activate]))
     (not (router/current-activity-id)))))

(defn entry-edit-dismiss
  []
  ;; If the user was looking at the modal, dismiss it too
  (when (router/current-activity-id)
    (utils/after 1 #(let [from-all-posts (or
                                          (:from-all-posts @router/path)
                                          (= (router/current-board-slug) "all-posts"))]
                      (router/nav!
                        (if from-all-posts ; AP
                          (oc-urls/all-posts (router/current-org-slug))
                          (oc-urls/board (router/current-org-slug) (router/current-board-slug)))))))
  ;; Add :entry-edit-dissmissing for 1 second to avoid reopening the activity modal after edit is dismissed.
  (utils/after 1000 #(dis/dispatch! [:input [:entry-edit-dissmissing] false]))
  (dis/dispatch! [:entry-edit/dismiss]))

(defn activity-modal-edit
  [activity-data activate]
  (if activate
    (do
      (load-cached-item activity-data :modal-editing-data)
      (dis/dispatch! [:modal-editing-activate]))
    (dis/dispatch! [:modal-editing-deactivate])))

(defn entry-save-on-exit
  [edit-key activity-data entry-body]
  (let [entry-map (assoc activity-data :body entry-body)
        cache-key (get-entry-cache-key (:uuid activity-data))]
    (uc/set-item cache-key entry-map
     (fn [err]
       (when-not err
         (dis/dispatch! [:entry-toggle-save-on-exit false]))))))

(defn entry-toggle-save-on-exit
  [enable?]
  (dis/dispatch! [:entry-toggle-save-on-exit enable?]))

(defn entry-save-finish [board-slug activity-data initial-uuid edit-key]
  (let [org-slug (router/current-org-slug)
        board-key (if (= (:status activity-data) "published")
                   (dis/current-board-key)
                   (dis/board-data-key org-slug utils/default-drafts-board-slug))]
    (save-last-used-section board-slug)
    (refresh-org-data)
    ; Remove saved cached item
    (remove-cached-item initial-uuid)

    (dis/dispatch! [:entry-save/finish activity-data edit-key board-key])))

(defn create-update-entry-cb [entry-data edit-key {:keys [success body status]}]
  (if success
    (entry-save-finish (:board-slug entry-data) (json->cljs body) (:uuid entry-data) edit-key)
    (dis/dispatch! [:entry-save/failed edit-key])))

(defn entry-modal-save-with-board-finish [activity-data response]
  (let [fixed-board-data (au/fix-board response)
        org-slug (router/current-org-slug)]
    (save-last-used-section (:slug fixed-board-data))
    (remove-cached-item (:uuid activity-data))
    (refresh-org-data)
    (when-not (= (:slug fixed-board-data) (router/current-board-slug))
      ;; If creating a new board, start watching changes
      (ws-cc/container-watch [(:uuid fixed-board-data)]))
    (dis/dispatch! [:entry-save-with-board/finish org-slug fixed-board-data])))

(defn board-name-exists-error [edit-key]
  (dis/dispatch!
   [:input
    [edit-key :section-name-error]
    "Board name already exists or isn't allowed"]))

(defn entry-modal-save [activity-data board-slug section-editing]
  (if (and (= (:board-slug activity-data) utils/default-section-slug)
           section-editing)
    (let [fixed-entry-data (dissoc activity-data :board-slug :board-name :invite-note)
          final-board-data (assoc section-editing :entries [fixed-entry-data])]
      (api/create-board final-board-data (:invite-note activity-data)
        (fn [{:keys [success status body]}]
          (if (= status 409)
            ;; Board name exists
            (board-name-exists-error :modal-editing-data)
            (entry-modal-save-with-board-finish activity-data (when success (json->cljs body)))))))
    (api/update-entry activity-data :modal-editing-data create-update-entry-cb))
  (dis/dispatch! [:entry-modal-save]))

(defn nux-next-step [next-step]
  (dis/dispatch! [:nux-next-step next-step]))

(defn show-invite-people-tooltip []
  (dis/dispatch! [:input [:show-invite-people-tooltip] true]))

(defn hide-invite-people-tooltip []
  (dis/dispatch! [:input [:show-invite-people-tooltip] false]))

(defn remove-invite-people-tooltip []
  (cook/remove-cookie! (router/show-invite-people-tooltip-cookie))
  (hide-invite-people-tooltip))

(defn show-add-post-tooltip []
  (dis/dispatch! [:input [:show-add-post-tooltip] true]))

(defn should-show-invite-people-tooltip []
  (let [org-data (dis/org-data)
        team-data (dis/team-data (:team-id org-data))
        board-data (dis/board-data)
        posts (vals (:fixed-items board-data))]
    (and ;; cookie is set
         (cook/get-cookie (router/show-invite-people-tooltip-cookie))
         ;; user is alone in the team
         (= (count (:users team-data)) 1)
         ;; has at least 1 user added post
         (> (count posts) 1))))

(defn check-invite-people-tooltip []
  (if (should-show-invite-people-tooltip)
    (show-invite-people-tooltip)
    (hide-invite-people-tooltip)))

(defn hide-add-post-tooltip []
  (let [add-post-cookie-name (router/show-add-post-tooltip-cookie)
        show-add-post-tooltip (cook/get-cookie add-post-cookie-name)
        team-data (dis/team-data (:team-id (dis/org-data)))]
    (cook/remove-cookie! (router/show-add-post-tooltip-cookie))
    (dis/dispatch! [:input [:show-add-post-tooltip] false])
    ;; Show the invite people tooltip if
    (when (and ;; was showing the add post tooltip
               show-add-post-tooltip
               ;; the team has only the current user
               (= (count (:users team-data)) 1))
      (cook/set-cookie! (router/show-invite-people-tooltip-cookie) true (* 60 60 24 365))
      (when (should-show-invite-people-tooltip)
        (check-invite-people-tooltip)))))

(defn should-show-add-post-tooltip
  "Check if we need to show the add post tooltip."
  []
  (let [org-data (dis/org-data)]
    (and ;; The cookie is set
         (cook/get-cookie (router/show-add-post-tooltip-cookie))
         ;; user has edit permission
         (utils/link-for (:links org-data) "create")
         ;; has only one board
         (= (count (:boards org-data)) 1)
         ;; and the board
         (let [board-data (dis/board-data)
               first-post (first (vals (:fixed-items board-data)))
               first-post-author (when first-post
                                   (if (map? (:author first-post))
                                      (:author first-post)
                                      (first (:author first-post))))]
           ;; or
           (or (and ;; has only one post
                    (= (count (:fixed-items board-data)) 1)
                    first-post
                    ;; from CarrotHQ
                    (= (:user-id first-post-author) "1111-1111-1111"))
                ;; has no posts
               (zero? (count (:fixed-items board-data))))))))

(defn check-add-post-tooltip []
  (if (should-show-add-post-tooltip)
    (show-add-post-tooltip)
    (hide-add-post-tooltip)))

(defn nux-end []
  ;; Add the cookie to show the add post tooltip
  (cook/set-cookie! (router/show-add-post-tooltip-cookie) true (* 60 60 24 365))
  (check-add-post-tooltip)
  (cook/remove-cookie! (router/show-nux-cookie (jwt/user-id)))
  (dis/dispatch! [:nux-end]))

(defn add-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-add-attachment dispatch-input-key attachment-data]))

(defn remove-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-remove-attachment dispatch-input-key attachment-data]))

(defn get-entry [entry-data]
  (api/get-entry entry-data
    (fn [{:keys [status success body]}]
      (if success
        (dis/dispatch! [:entry (dis/current-board-key) (:uuid entry-data) (json->cljs body)])))))

(defn entry-clear-local-cache [item-uuid edit-key]
  (remove-cached-item item-uuid)
  (dis/dispatch! [:entry-clear-local-cache edit-key]))

(defn entry-save [edited-data & [section-editing]]
  (let [fixed-edited-data (assoc edited-data :status (or (:status edited-data) "draft"))]
    (if (:links fixed-edited-data)
      (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
               section-editing)
        (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note)
              final-board-data (assoc section-editing :entries [fixed-entry-data])]
          (api/create-board final-board-data (:invite-note fixed-edited-data)
            (fn [{:keys [success status body] :as response}]
              (if (= status 409)
                ;; Board name exists
                (board-name-exists-error :entry-editing)
                (create-update-entry-cb fixed-edited-data :entry-editing response)))))
        (api/update-entry fixed-edited-data :entry-editing create-update-entry-cb))
      (if (and (= (:board-slug fixed-edited-data) utils/default-section-slug)
               section-editing)
        (let [fixed-entry-data (dissoc fixed-edited-data :board-slug :board-name :invite-note)
              final-board-data (assoc section-editing :entries [fixed-entry-data])]
          (api/create-board final-board-data (:invite-note fixed-edited-data)
            (fn [{:keys [success status body] :as response}]
              (if (= status 409)
                ;; Board name exists
                (board-name-exists-error :entry-editing)
                (create-update-entry-cb fixed-edited-data :entry-editing response)))))
        (let [org-slug (router/current-org-slug)
              entry-board-data (dis/board-data @dis/app-state org-slug (:board-slug fixed-edited-data))
              entry-create-link (utils/link-for (:links entry-board-data) "create")]
          (api/create-entry fixed-edited-data :entry-editing entry-create-link create-update-entry-cb))))
    (dis/dispatch! [:entry-save])))

(defn entry-publish-finish [initial-uuid edit-key board-slug activity-data]
  ;; Save last used section
  (save-last-used-section board-slug)
  (refresh-org-data)
  ;; Remove entry cached edits
  (remove-cached-item initial-uuid)
  (dis/dispatch! [:entry-publish/finish edit-key activity-data]))

(defn entry-publish-cb [entry-uuid posted-to-board-slug {:keys [status success body]}]
  (if success
    (entry-publish-finish entry-uuid :entry-editing posted-to-board-slug (when success (json->cljs body)))
    (dis/dispatch! [:entry-publish/failed  :entry-editing])))

(defn entry-publish-with-board-finish [entry-uuid new-board-data]
  (let [board-slug (:slug new-board-data)]
    (save-last-used-section (:slug new-board-data))
    (remove-cached-item entry-uuid)
    (refresh-org-data)
    (when-not (= (:slug new-board-data) (router/current-board-slug))
      ;; If creating a new board, start watching changes
      (ws-cc/container-watch [(:uuid new-board-data)]))
    (dis/dispatch! [:entry-publish-with-board/finish new-board-data])))

(defn entry-publish-with-board-cb [entry-uuid {:keys [status success body]}]
  (if (= status 409)
    ; Board name already exists
    (dis/dispatch! [:section-edit/error "Board name already exists or isn't allowed"])
    (entry-publish-with-board-finish entry-uuid (when success (json->cljs body)))))

(defn entry-publish [entry-editing section-editing]
  (let [fixed-entry-editing (assoc entry-editing :status "published")]
    (if (and (= (:board-slug fixed-entry-editing) utils/default-section-slug)
             section-editing)
      (let [fixed-entry-data (dissoc fixed-entry-editing :board-slug :board-name :invite-note)
            final-board-data (assoc section-editing :entries [fixed-entry-data])]
        (api/create-board final-board-data (:invite-note section-editing)
         (partial entry-publish-with-board-cb (:uuid fixed-entry-editing))))
      (let [entry-exists? (seq (:links fixed-entry-editing))
            org-slug (router/current-org-slug)
            board-data (dis/board-data @dis/app-state org-slug (:board-slug fixed-entry-editing))
            publish-entry-link (if entry-exists?
                                ;; If the entry already exists use the publish link in it
                                (utils/link-for (:links fixed-entry-editing) "publish")
                                ;; If the entry is new, use
                                (utils/link-for (:links board-data) "create"))]
        (api/publish-entry fixed-entry-editing publish-entry-link
         (partial entry-publish-cb (:uuid fixed-entry-editing) (:board-slug fixed-entry-editing)))))
    (dis/dispatch! [:entry-publish])))

(defn activity-delete-finish []
  ;; Reload the org to update the number of drafts in the navigation
  (when (= (router/current-board-slug) utils/default-drafts-board-slug)
    (refresh-org-data)
    (let [org-slug (router/current-org-slug)
          org-data (dis/org-data)
          boards-no-draft (sort-by :name (filterv #(not= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))
          board-key (dis/board-data-key org-slug (router/current-board-slug))
          board-data (get-in @dis/app-state board-key)]
      (when (zero? (count (:fixed-items board-data)))
        (utils/after
         100
         #(router/nav!
            (if (pos? (count boards-no-draft))
              ;; If there is at least one board redirect to it
              (oc-urls/board org-slug (:slug (first boards-no-draft)))
              ;; If not boards are available redirect to the empty org
              (oc-urls/org org-slug))))))))

(defn activity-delete [activity-data]
  (api/delete-activity activity-data activity-delete-finish)
  (dis/dispatch! [:activity-delete (dis/current-board-key) activity-data]))

(defn activity-share-show [activity-data & [element-id]]
  (dis/dispatch! [:activity-share-show activity-data element-id]))

(defn activity-share-hide []
  (dis/dispatch! [:activity-share-hide]))

(defn activity-share-reset []
  (dis/dispatch! [:activity-share-reset]))

(defn activity-share-cb [{:keys [status success body]}]
  (dis/dispatch! [:activity-share/finish success (when success (json->cljs body))]))

(defn activity-share [activity-data share-data]
  (api/share-activity activity-data share-data activity-share-cb)
  (dis/dispatch! [:activity-share share-data]))

(defn activity-get-finish [status activity-data secure-uuid]
  (when (= status 404)
    (router/redirect-404!))
  (when (and secure-uuid
             (jwt/jwt)
             (jwt/user-is-part-of-the-team (:team-id activity-data)))
    (router/nav! (oc-urls/entry (router/current-org-slug) (:board-slug activity-data) (:uuid activity-data))))
  (dis/dispatch! [:activity-get/finish status activity-data secure-uuid]))

(defn secure-activity-get-finish [{:keys [status success body]}]
  (activity-get-finish status (if success (json->cljs body) {}) (router/current-secure-activity-id)))

(defn secure-activity-get []
  (api/get-secure-activity (router/current-org-slug) (router/current-secure-activity-id) secure-activity-get-finish))

(defn toggle-must-see [activity-data]
  (let [must-see (:must-see activity-data)
        must-see-toggled (assoc activity-data :must-see (not must-see))
        org-data (dis/org-data)
        must-see-count (:must-see-count dis/org-data)
        new-must-see-count (if (not must-see)
                              (+ must-see-count 1)
                              (- must-see-count 1))]
    (dis/dispatch! [:org-loaded
                    (assoc org-data :must-see-count new-must-see-count)
                    false])
    (dis/dispatch! [:entry
                    (dis/current-board-key)
                    (:uuid activity-data)
                    must-see-toggled])
    (api/update-entry must-see-toggled :must-see
                      (fn [entry-data edit-key {:keys [success body status]}]
                        (if success
                          (api/get-org org-data
                            (fn [{:keys [status body success]}]
                              (let [api-org-data (json->cljs body)]
                                (dis/dispatch! [:org-loaded api-org-data false]))))
                          (dis/dispatch! [:entry
                                          (dis/current-board-key)
                                          (:uuid activity-data)
                                          (json->cljs body)]))))))
