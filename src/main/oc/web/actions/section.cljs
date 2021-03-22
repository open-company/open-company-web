(ns oc.web.actions.section
  (:require-macros [if-let.core :refer (if-let* when-let*)])
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.utils.user :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.lib.json :refer (json->cljs)]))

(defn is-currently-shown? [section]
  (= (dis/current-board-slug)
     (:slug section)))

(defn watch-single-section [section]
  ;; only watch the currently visible board.
  (ws-ic/board-unwatch (fn [rep]
    (timbre/debug rep "Watching on socket " (:uuid section))
    (ws-ic/boards-watch [(:uuid section)]))))

(defn request-reads-count
  "Request the reads count data only for the items we don't have already."
  [section]
  (let [user-is-part-of-the-team (:member? (dis/org-data))]
    (when (and user-is-part-of-the-team
               (not= (:slug section) utils/default-drafts-board-slug)
               (seq (:entries section)))
      (let [item-ids (map :uuid (:entries section))
            cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
        (when (seq cleaned-ids)
          (api/request-reads-count cleaned-ids))))))

(defn section-get-finish
  [org-slug section-slug sort-type section]
  (let [is-currently-shown (is-currently-shown? section-slug)
        user-is-part-of-the-team (:member? (dis/org-data))]
    (when is-currently-shown
      (when user-is-part-of-the-team
        ;; only watch the currently visible board.
        ; only for logged in users and if the board is currently shown
        (when (= (dis/current-board-slug) section-slug)
          (watch-single-section section)
          ;; Retrieve reads count if there are items in the loaded section
          (request-reads-count section))))
    (dis/dispatch! [:section org-slug section-slug sort-type (assoc section :is-loaded is-currently-shown)])))

(defn load-other-sections
  [sections]
  (doseq [section sections
          :when (not (is-currently-shown? section))
          :let [board-link (utils/link-for (:links section) ["item" "self"] "GET")]]
    (api/get-board board-link
      (fn [{:keys [status body success]}]
        (when success
          (section-get-finish (:slug (dis/org-data)) (:slug section) dis/recently-posted-sort (json->cljs body)))))))

(defn section-get
  ([board-slug]
   (when-let* [section-data (dis/org-board-data board-slug)
               section-link (utils/link-for (:links section-data) ["item" "self"] "GET")]
     (section-get board-slug section-link)))
  ([board-slug link & [finish-cb]]
   (api/get-board link
    (fn [{:keys [status body success] :as resp}]
      (when success
        (section-get-finish (:slug (dis/org-data)) board-slug
         dis/recently-posted-sort (json->cljs body)))
      (when (fn? finish-cb)
        (finish-cb resp))))))

(defn board-get-by-uuid [board-uuid]
  (when-let [board-data (au/board-by-uuid board-uuid)]
    (section-get (:slug board-data))))

(defn section-refresh
  [board-slug]
  (if-let* [section-data (dis/board-data board-slug)
            refresh-link (utils/link-for (:links section-data) "refresh")]
    (section-get board-slug refresh-link)
    (section-get board-slug)))

(defn drafts-get []
  (let [drafts-board (dis/org-board-data utils/default-drafts-board-slug)
        drafts-link (utils/link-for (:links drafts-board) ["item" "self"] "GET")]
    (when drafts-link
      (section-get (:slug drafts-board) drafts-link))))

(defn section-change
  [section-uuid & [finish-cb]]
  (timbre/debug "Section change:" section-uuid)
  (utils/after 0 (fn []
    (let [current-board-data (dis/board-data)
          current-board-link (utils/link-for (:links current-board-data) ["item" "self"] "GET")
          drafts-board (dis/org-board-data utils/default-drafts-board-slug)
          drafts-board-link (utils/link-for (:links drafts-board) ["item" "self"] "GET")
          refresh-slug (cond
                        (= section-uuid (:uuid utils/default-drafts-board))
                        utils/default-drafts-board-slug
                        (= section-uuid (:uuid current-board-data))
                        (:slug current-board-data))
          refresh-link (cond
                        (= section-uuid (:uuid utils/default-drafts-board))
                        drafts-board-link
                        (= section-uuid (:uuid current-board-data))
                        current-board-link)]
      (when refresh-link
        (section-get refresh-slug refresh-link finish-cb)))))
  ;; Update change-data state that the board has a change
  (dis/dispatch! [:section-change section-uuid]))

(defn- refresh-org-data []
  (let [org-link (utils/link-for (:links (dis/org-data)) ["item" "self"] "GET")]
    (api/get-org org-link
                 (fn [{:keys [status body success]}]
                   (dis/dispatch! [:org-loaded (when success (json->cljs body))])))))

(defn section-delete [section-slug & [callback]]
  (let [section-data (dis/org-board-data (dis/current-org-slug) section-slug)
        delete-section-link (utils/link-for (:links section-data) "delete")]
    (api/delete-board delete-section-link section-slug
                      (fn [{:keys [status success body]}]
                        (if success
                          (let [org-slug (dis/current-org-slug)
                                last-used-section-slug (au/last-used-section)]
                            (when (= last-used-section-slug section-slug)
                              (au/save-last-used-section nil))
                            (when (fn? callback)
                              (callback section-slug))
                            (refresh-org-data)
                            (if (= section-slug (dis/current-board-slug))
                              (router/nav! (oc-urls/default-landing org-slug))
                              (dis/dispatch! [:section-delete org-slug section-slug])))
                          (.reload (.-location js/window)))))))

(defn section-save-error [status]
  ;; Board name exists or too short
  (dis/dispatch! [:update [:section-editing]
   #(-> %
     (assoc :section-name-error (when (= status 409) "Topic name already exists or isn't allowed"))
     (assoc :section-error (when-not (= status 409) "An error occurred, please retry."))
     (dissoc :loading))]))

(defn section-save
  ([section-data note] (section-save section-data note nil))
  ([section-data note success-cb]
    (section-save section-data note success-cb section-save-error))
  ([section-data note success-cb error-cb]
   (timbre/debug section-data)
   (let [org-slug (dis/current-org-slug)
         board-link (cond (contains? section-data :links)
                          (utils/link-for (:links section-data) "partial-update")
                          (= (:access section-data) "public")
                          (utils/link-for (:links (dis/org-data)) "create-public")
                          (= (:access section-data) "private")
                          (utils/link-for (:links (dis/org-data)) "create-private")
                          :else
                          (utils/link-for (:links (dis/org-data)) "create"))]
     (dis/dispatch! [:section-edit-save org-slug section-data])
     (if (empty? (:links section-data))
       (api/create-board board-link section-data note
                         (fn [{:keys [success status body]}]
                           (let [section-data (when success (json->cljs body))
                                 editable-board? (utils/link-for (:links section-data) "create")]
                             (if-not success
                               (when (fn? error-cb)
                                 (error-cb status))
                               (do
                                 (utils/after 100
                                              #(do
                                                 (when (and editable-board?
                                                            (:collapsed (dis/cmail-state)))
                                                   (dis/dispatch! [:input dis/cmail-data-key {:board-slug (:slug section-data)
                                                                                              :board-name (:name section-data)
                                                                                              :publisher-board (:publisher-board section-data)}])
                                                   (dis/dispatch! [:input (conj dis/cmail-state-key :key) (utils/activity-uuid)]))))
                                 (utils/after 500 refresh-org-data)
                                 (ws-cc/container-watch (:uuid section-data))
                                 (dis/dispatch! [:section-edit-save/finish org-slug section-data])
                                 (uu/load-follow-data)
                                 (when (fn? success-cb)
                                   (success-cb section-data)))))))
       (api/patch-board board-link section-data note
                        (fn [{:keys [success body status]}]
                          (let [section-data (when success (json->cljs body))]
                            (if-not success
                              (when (fn? error-cb)
                                (error-cb status))
                              (do
                                (refresh-org-data)
                                (dis/dispatch! [:section-edit-save/finish org-slug section-data])
                                (uu/load-follow-data)
                                (when (fn? success-cb)
                                  (success-cb section-data)))))))))))

(defn private-section-user-add
  [user user-type]
  (dis/dispatch! [:private-section-user-add user user-type]))

(defn private-section-user-remove
  [user]
  (dis/dispatch! [:private-section-user-remove user]))

(defn private-section-kick-out-self
  [user]
  (when (= (:user-id user) (jwt/user-id))
    (let [remove-link (utils/link-for (:links user) "remove")]
      (api/remove-user-from-private-board remove-link (fn [status success body]
        ;; Redirect to the first available board
        (let [org-data (dis/org-data)
              all-boards (:boards org-data)
              current-board-slug (dis/current-board-slug)
              except-this-boards (remove #(#{current-board-slug "drafts"} (:slug %)) all-boards)
              redirect-url (if-let [next-board (first except-this-boards)]
                             (oc-urls/board (:slug next-board))
                             (oc-urls/org (dis/current-org-slug)))]
          (refresh-org-data)
          (utils/after 0 #(router/nav! redirect-url))
          (dis/dispatch! [:private-section-kick-out-self/finish success])))))))

(defn ws-change-subscribe []
  (ws-cc/subscribe :container/status
    (fn [data]
      (let [status-by-uuid (group-by :container-id (:data data))
            clean-change-data (zipmap (keys status-by-uuid) (->> status-by-uuid
                                                                 vals
                                                                 ; remove the sequence of 1 from group-by
                                                                 (map first)))]
        (dis/dispatch! [:container/status clean-change-data]))))

  (ws-cc/subscribe :container/change
    (fn [data]
      (let [change-data (:data data)
            section-uuid (:item-id change-data)
            change-type (:change-type change-data)]
        ;; Refresh the section only in case of an update, let the org
        ;; handle the add and delete cases
        (when (= change-type :update)
          (section-change section-uuid)))))
  (ws-cc/subscribe :item/change
    (fn [data]
      (let [change-data (:data data)
            section-uuid (:container-id change-data)
            change-type (:change-type change-data)
            org-slug (dis/current-org-slug)
            item-id (:item-id change-data)]
        ;; Refresh the section only in case of items added or removed
        ;; let the activity handle the item update case
        (when (or (= change-type :add)
                  (= change-type :delete)
                  (= change-type :move))
          (section-change section-uuid))
        ;; On item/change :add let's add the UUID to the unseen list of
        ;; the specified container to make sure it's marked as seen
        (when (and (= change-type :add)
                   (not= (:user-id change-data) (jwt/user-id)))
          (dis/dispatch! [:item-add/unseen (dis/current-org-slug) change-data]))
        (when (= change-type :delete)
          (dis/dispatch! [:item-delete/unseen (dis/current-org-slug) change-data]))
        (when (= change-type :move)
          (dis/dispatch! [:item-move (dis/current-org-slug) change-data]))))))

;; Section editing

(def min-section-name-length 2)

(defn section-save-create [section-editing section-name success-cb]
  (if (< (count section-name) min-section-name-length)
    (dis/dispatch! [:section-edit/error (str "Name must be at least " min-section-name-length " characters.")])
    (let [next-section-editing (merge section-editing {:loading true
                                                       :name section-name})]
      (dis/dispatch! [:input [:section-editing] next-section-editing])
      (success-cb next-section-editing))))

(defn pre-flight-check [section-slug section-name]
  (dis/dispatch! [:update [:section-editing] #(merge % {:has-changes true
                                                               :pre-flight-loading true})])
  (let [org-data (dis/org-data)
        pre-flight-link (utils/link-for (:links org-data) "create-preflight")]
    (api/pre-flight-section-check pre-flight-link section-slug section-name
     (fn [{:keys [success body status]}]
       (when-not success
         (section-save-error 409))
       (dis/dispatch! [:input [:section-editing :pre-flight-loading] false])))))

(defn section-more-finish [org-slug board-slug sort-type direction {:keys [success body]}]
  (when success
    (request-reads-count (json->cljs body)))
  (dis/dispatch! [:section-more/finish org-slug board-slug sort-type
   direction (when success (json->cljs body))]))

(defn section-more [more-link direction]
  (let [org-data (dis/org-data)
        board-slug (dis/current-board-slug)]
    (api/load-more-items more-link direction (partial section-more-finish (:slug org-data) board-slug dis/recently-posted-sort direction))
    (dis/dispatch! [:section-more (:slug org-data) board-slug dis/recently-posted-sort])))

(defn setup-section-editing [section-slug]
  (when-let [board-data (dis/org-board-data section-slug)]
    (dis/dispatch! [:setup-section-editing board-data])))
