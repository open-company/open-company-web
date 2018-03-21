(ns oc.web.actions.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.user-cache :as uc]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.ws-change-client :as ws-cc]))

(defn save-last-used-section [section-slug]
  (let [org-slug (router/current-org-slug)
        last-board-cookie (router/last-used-board-slug-cookie org-slug)]
    (cook/set-cookie! last-board-cookie section-slug (* 60 60 24 365))))

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
  [activity-data & [editing item-load-cb]]
  (let [org (router/current-org-slug)
        board (:board-slug activity-data)
        activity-uuid (:uuid activity-data)
        to-url (oc-urls/entry board activity-uuid)]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org board activity-uuid "activity"]
     {:org org
      :board board
      :activity activity-uuid
      :query-params (dissoc (:query-params @router/path) :ap-initial-at)
      :from-all-posts (= (router/current-board-slug) "all-posts")}))
  (when editing
    (utils/after 100 #(load-cached-item activity-data :modal-editing-data item-load-cb)))
  (dis/dispatch! [:activity-modal-fade-in activity-data editing]))

(defn activity-modal-fade-out
  [board-slug]
  (let [from-all-posts (:from-all-posts @router/path)
        to-board (if from-all-posts "all-posts" board-slug)
        org (router/current-org-slug)
        to-url (if from-all-posts
                (oc-urls/all-posts org)
                (oc-urls/board org board-slug))]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org to-board (if from-all-posts "all-posts" "dashboard")]
     {:org org
      :board to-board
      :activity nil
      :query-params (:query-params @router/path)
      :from-all-posts false}))
  (dis/dispatch! [:activity-modal-fade-out board-slug]))

(defn entry-edit
  [initial-entry-data]
  (load-cached-item initial-entry-data :entry-editing))

(defn activity-edit
  [activity-data]
  (if (or (responsive/is-tablet-or-mobile?)
          (not= (:status activity-data) "published"))
    (load-cached-item activity-data :entry-editing)
    (activity-modal-fade-in activity-data true (fn [] (dis/dispatch! [:modal-editing-activate])))))

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

(defn entry-save-finish [activity-data initial-uuid edit-key]
  (let [is-all-posts (or (:from-all-posts @router/path)
                         (= (router/current-board-slug) "all-posts"))
        org-slug (router/current-org-slug)]
    (save-last-used-section (:board-slug activity-data))
    ;; FIXME: refresh the last loaded all-posts link
    (when-not is-all-posts
      (api/get-board (utils/link-for (:links (dis/board-data)) ["item" "self"] "GET")))
    (api/get-org (dis/org-data))
    ; Remove saved cached item
    (remove-cached-item initial-uuid)
    (dis/dispatch! [:entry-save/finish activity-data edit-key])))

(defn create-update-entry-cb [entry-data edit-key {:keys [success body status]}]
  (if success
    (entry-save-finish (json->cljs body) (:uuid entry-data) edit-key)
    (dis/dispatch! [:entry-save/failed edit-key])))

(defn entry-modal-save [activity-data]
  (api/update-entry activity-data :modal-editing-data create-update-entry-cb)
  (dis/dispatch! [:entry-modal-save]))

(defn nux-next-step [next-step]
  (let [next-url (str (oc-urls/board (router/current-org-slug) (router/current-board-slug)) "#nux" (name next-step))]
    (.pushState (.-history js/window) (.. js/window -history -state) (.-title js/document) next-url))
  (dis/dispatch! [:nux-next-step next-step]))

(defn show-add-post-tooltip []
  (dis/dispatch! [:input [:show-add-post-tooltip] true]))

(defn hide-add-post-tooltip []
  (cook/remove-cookie! (router/show-add-post-tooltip-cookie))
  (dis/dispatch! [:input [:show-add-post-tooltip] false]))

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
                    (= (:user-id first-post-author) "0000-0000-0000"))
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
  (dis/dispatch! [:nux-end])
  (let [next-url (oc-urls/board (router/current-org-slug) (router/current-board-slug))]
    (.pushState (.-history js/window) (.. js/window -history -state) (.-title js/document) next-url)))

(defn add-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-add-attachment dispatch-input-key attachment-data]))

(defn remove-attachment [dispatch-input-key attachment-data]
  (dis/dispatch! [:activity-remove-attachment dispatch-input-key attachment-data]))

(defn get-entry [entry-data]
  (api/get-entry entry-data
    (fn [{:keys [status success body]}]
      (if success
        (dis/dispatch! [:entry (:uuid entry-data) (clj->js body)])))))

(defn entry-clear-local-cache [item-uuid edit-key]
  (remove-cached-item item-uuid)
  (dis/dispatch! [:entry-clear-local-cache edit-key]))

(defn entry-save [edited-data]
  (if (:links edited-data)
    (api/update-entry edited-data :entry-editing create-update-entry-cb)
    (let [org-slug (router/current-org-slug)
          entry-board-data (dis/board-data @dis/app-state org-slug (:board-slug edited-data))
          entry-create-link (utils/link-for (:links entry-board-data) "create")]
      (api/create-entry edited-data :entry-editing entry-create-link create-update-entry-cb)))
  (dis/dispatch! [:entry-save]))

(defn entry-publish-finish [initial-uuid edit-key activity-data]
  (let [board-slug (:board-slug activity-data)]
    ;; Save last used section
    (save-last-used-section board-slug)
    (api/get-org (dis/org-data))
    ;; Remove entry cached edits
    (remove-cached-item initial-uuid)
    (dis/dispatch! [:entry-publish/finish edit-key activity-data])))

(defn entry-publish-cb [entry-uuid {:keys [status success body]}]
  (if success
    (entry-publish-finish entry-uuid :modal-editing-data (when success (json->cljs body)))
    (dis/dispatch! [:entry-publish/failed  :modal-editing-data])))

(defn entry-publish-with-board-finish [entry-uuid new-board-data]
  (let [board-slug (:slug new-board-data)]
    (save-last-used-section (:slug new-board-data))
    (remove-cached-item entry-uuid)
    (api/get-org (dis/org-data))
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
  (if (and (= (:board-slug entry-editing) utils/default-section-slug)
           section-editing)
    (let [fixed-entry-data (dissoc entry-editing :board-slug :board-name)
          final-board-data (assoc section-editing :entries [fixed-entry-data])]
      (api/create-board final-board-data (partial entry-publish-with-board-cb (:uuid entry-editing))))
    (let [entry-exists? (seq (:links entry-editing))
          org-slug (router/current-org-slug)
          board-data (dis/board-data @dis/app-state org-slug (:board-slug entry-editing))
          publish-entry-link (if entry-exists?
                              ;; If the entry already exists use the publish link in it
                              (utils/link-for (:links entry-editing) "publish")
                              ;; If the entry is new, use
                              (utils/link-for (:links board-data) "create"))]
      (api/publish-entry entry-editing publish-entry-link (partial entry-publish-cb (:uuid entry-editing)))))
  (dis/dispatch! [:entry-publish]))

(defn activity-delete-finish []
  (api/get-board (utils/link-for (:links (dis/board-data)) ["item" "self"] "GET"))
  ;; Reload the org to update the number of drafts in the navigation
  (when (= (router/current-board-slug) utils/default-drafts-board-slug)
    (api/get-org (dis/org-data))
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
  (dis/dispatch! [:activity-delete activity-data]))

(defn activity-move [activity-data board-data]
  (let [fixed-activity-data (assoc activity-data :board-slug (:slug board-data))]
    (api/update-entry fixed-activity-data nil create-update-entry-cb)
    (dis/dispatch! [:activity-move activity-data board-data])))

(defn activity-share-show [activity-data]
  (dis/dispatch! [:activity-share-show activity-data]))

(defn activity-share-hide []
  (dis/dispatch! [:activity-share-hide]))

(defn activity-share-reset []
  (dis/dispatch! [:activity-share-reset]))

(defn activity-share-cb [{:keys [status success body]}]
  (dis/dispatch! [:activity-share/finish success (when success (json->cljs body))]))

(defn activity-share [activity-data share-data]
  (api/share-activity activity-data share-data activity-share-cb)
  (dis/dispatch! [:activity-share share-data]))

(defn activity-get-finish [status activity-data]
  (when (= status 404)
    (router/redirect-404!))
  (when (and (router/current-secure-activity-id)
             (jwt/jwt)
             (jwt/user-is-part-of-the-team (:team-id activity-data)))
    (router/nav! (oc-urls/entry (router/current-org-slug) (:board-slug activity-data) (:uuid activity-data))))
  (dis/dispatch! [:activity-get/finish status activity-data]))

(defn secure-activity-get-finish [{:keys [status success body]}]
  (activity-get-finish status (if success (json->cljs body) {})))

(defn secure-activity-get []
  (api/get-secure-activity (router/current-org-slug) (router/current-secure-activity-id) secure-activity-get-finish))