(ns oc.web.actions.section
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(defn is-currently-shown? [section]
  (= (router/current-board-slug)
     (:slug section)))

(defn watch-single-section [section]
  ;; only watch the currently visible board.
  (ws-ic/board-unwatch (fn [rep]
    (timbre/debug rep "Watching on socket " (:uuid section))
    (ws-ic/boards-watch [(:uuid section)]))))

(defn section-seen
  [uuid]
  ;; Let the change service know we saw the board
  (ws-cc/container-seen uuid))

(defn request-reads-count
  "Request the reads count data only for the items we don't have already."
  [section]
  (let [user-is-part-of-the-team (:member? (dispatcher/org-data))]
    (when (and user-is-part-of-the-team
               (not= (:slug section) utils/default-drafts-board-slug)
               (seq (:entries section)))
      (let [item-ids (map :uuid (:entries section))
            cleaned-ids (au/clean-who-reads-count-ids item-ids (dispatcher/activity-read-data))]
        (when (seq cleaned-ids)
          (api/request-reads-count cleaned-ids))))))

(defn section-get-finish
  [section]
  (let [is-currently-shown (is-currently-shown? section)
        user-is-part-of-the-team (:member? (dispatcher/org-data))]
    (when is-currently-shown
      (when user-is-part-of-the-team
        ;; only watch the currently visible board.
        ; only for logged in users and if the board is currently shown
        (when (= (router/current-board-slug) (:slug section))
          (watch-single-section section)
          ;; Retrieve reads count if there are items in the loaded section
          (request-reads-count section))))
    (dispatcher/dispatch! [:section (assoc section :is-loaded is-currently-shown)])))

(defn load-other-sections
  [sections]
  (doseq [section sections
          :when (not (is-currently-shown? section))
          :let [board-link (utils/link-for (:links section) ["item" "self"] "GET")]]
    (api/get-board board-link
      (fn [{:keys [status body success]}]
        (when success
          (section-get-finish (json->cljs body)))))))

(declare refresh-org-data)

(defn section-change
  [section-uuid & [finish-cb]]
  (timbre/debug "Section change:" section-uuid)
  (utils/after 0 (fn []
    (let [current-section-data (dispatcher/board-data)]
      (when (= section-uuid (:uuid utils/default-drafts-board))
        (refresh-org-data))
      (if (= section-uuid (:uuid current-section-data))
        ;; Reload the current board data
        (api/get-board (utils/link-for (:links current-section-data) ["item" "self"] "GET")
                       (fn [{:keys [status body success] :as resp}]
                         (when success (section-get-finish (json->cljs body)))
                         (when (fn? finish-cb)
                           (finish-cb resp))))
        ;; Reload a secondary board data
        (let [sections (:boards (dispatcher/org-data))
              filtered-sections (filterv #(= (:uuid %) section-uuid) sections)]
          (load-other-sections filtered-sections))))))
  ;; Update change-data state that the board has a change
  (dispatcher/dispatch! [:section-change section-uuid]))

(defn section-get
  [link]
  (api/get-board link
    (fn [{:keys [status body success]}]
      (when success (section-get-finish (json->cljs body))))))

(defn section-delete [section-slug & callback]
  (let [section-data (dispatcher/board-data (router/current-org-slug) section-slug)
        delete-section-link (utils/link-for (:links section-data) "delete")]
    (api/delete-board delete-section-link section-slug (fn [status success body]
      (if success
        (let [org-slug (router/current-org-slug)
              last-used-section-slug (au/last-used-section)]
          (when (= last-used-section-slug section-slug)
            (au/save-last-used-section nil))
          (when (fn? callback)
           (callback section-slug))
          (if (= section-slug (router/current-board-slug))
            (do
              (router/nav! (oc-urls/default-landing org-slug))
              (let [org-link (utils/link-for (:links (dispatcher/org-data)) ["item" "self"] "GET")]
                (api/get-org org-link
                  (fn [{:keys [status body success]}]
                    (dispatcher/dispatch! [:org-loaded (json->cljs body)])))))
            (dispatcher/dispatch! [:section-delete org-slug section-slug])))
        (.reload (.-location js/window)))))))

(defn refresh-org-data []
  (let [org-link (utils/link-for (:links (dispatcher/org-data)) ["item" "self"] "GET")]
    (api/get-org org-link
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:org-loaded (json->cljs body)])))))

(defn section-save-error [status]
  ;; Board name exists or too short
  (dispatcher/dispatch! [:update [:section-editing]
   #(-> %
     (assoc :section-name-error (when (= status 409) "Team name already exists or isn't allowed"))
     (assoc :section-error (when-not (= status 409) "An error occurred, please retry."))
     (dissoc :loading))]))

(defn section-save
  ([section-data note] (section-save section-data note nil))
  ([section-data note success-cb]
    (section-save section-data note success-cb section-save-error))
  ([section-data note success-cb error-cb]
    (timbre/debug section-data)
    (if (empty? (:links section-data))
      (let [create-board-link (utils/link-for (:links (dispatcher/org-data)) "create")]
        (api/create-board create-board-link section-data note
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
                                (-> @dispatcher/app-state :cmail-state :collapsed))
                        (dispatcher/dispatch! [:input [:cmail-data] {:board-slug (:slug section-data)
                                                                     :board-name (:name section-data)
                                                                     :publisher-board (:publisher-board section-data)}])
                        (dispatcher/dispatch! [:input [:cmail-state :key] (utils/activity-uuid)]))
                     (router/nav! (oc-urls/board (router/current-org-slug) (:slug section-data)))))
                  (utils/after 500 refresh-org-data)
                  (ws-cc/container-watch (:uuid section-data))
                  (dispatcher/dispatch! [:section-edit-save/finish section-data])
                  (when (fn? success-cb)
                    (success-cb section-data))))))))
      (let [board-patch-link (utils/link-for (:links section-data) "partial-update")]
        (api/patch-board board-patch-link section-data note (fn [success body status]
          (let [section-data (when success (json->cljs body))]
            (if-not success
              (when (fn? error-cb)
                (error-cb status))
              (do
                (refresh-org-data)
                (dispatcher/dispatch! [:section-edit-save/finish ])
                (when (fn? success-cb)
                  (success-cb section-data)))))))))))

(defn private-section-user-add
  [user user-type]
  (dispatcher/dispatch! [:private-section-user-add user user-type]))

(defn private-section-user-remove
  [user]
  (dispatcher/dispatch! [:private-section-user-remove user]))

(defn private-section-kick-out-self
  [user]
  (when (= (:user-id user) (jwt/user-id))
    (let [remove-link (utils/link-for (:links user) "remove")]
      (api/remove-user-from-private-board remove-link (fn [status success body]
        ;; Redirect to the first available board
        (let [org-data (dispatcher/org-data)
              all-boards (:boards org-data)
              current-board-slug (router/current-board-slug)
              except-this-boards (remove #(#{current-board-slug "drafts"} (:slug %)) all-boards)
              redirect-url (if-let [next-board (first except-this-boards)]
                             (oc-urls/board (:slug next-board))
                             (oc-urls/org (router/current-org-slug)))]
          (refresh-org-data)
          (utils/after 0 #(router/nav! redirect-url))
          (dispatcher/dispatch! [:private-section-kick-out-self/finish success])))))))

(defn ws-change-subscribe []
  (ws-cc/subscribe :container/status
    (fn [data]
      (let [status-by-uuid (group-by :container-id (:data data))
            clean-change-data (zipmap (keys status-by-uuid) (->> status-by-uuid
                                                              vals
                                                              ; remove the sequence of 1 from group-by
                                                              (map first)))]
        (dispatcher/dispatch! [:container/status clean-change-data]))))

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
            org-slug (router/current-org-slug)
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
          (dispatcher/dispatch! [:item-add/unseen (router/current-org-slug) change-data]))
        (when (= change-type :delete)
          (dispatcher/dispatch! [:item-delete/unseen (router/current-org-slug) change-data]))
        (when (= change-type :move)
          (dispatcher/dispatch! [:item-move (router/current-org-slug) change-data]))))))

;; Section editing

(def min-section-name-length 2)

(defn section-save-create [section-editing section-name success-cb]
  (if (< (count section-name) min-section-name-length)
    (dispatcher/dispatch! [:section-edit/error (str "Name must be at least " min-section-name-length " characters.")])
    (let [next-section-editing (merge section-editing {:loading true
                                                       :name section-name})]
      (dispatcher/dispatch! [:input [:section-editing] next-section-editing])
      (success-cb next-section-editing))))

(defn pre-flight-check [section-slug section-name]
  (dispatcher/dispatch! [:update [:section-editing] #(merge % {:has-changes true
                                                               :pre-flight-loading true})])
  (let [org-data (dispatcher/org-data)
        pre-flight-link (utils/link-for (:links org-data) "pre-flight-create")]
    (api/pre-flight-section-check pre-flight-link section-slug section-name
     (fn [{:keys [success body status]}]
       (when-not success
         (section-save-error 409))
       (dispatcher/dispatch! [:input [:section-editing :pre-flight-loading] false])))))

(defn section-more-finish [board-slug direction {:keys [success body]}]
  (when success
    (request-reads-count (json->cljs body)))
  (dispatcher/dispatch! [:section-more/finish (router/current-org-slug) board-slug
   direction (when success (json->cljs body))]))

(defn section-more [more-link direction]
  (let [board-slug (router/current-board-slug)]
    (api/load-more-items more-link direction (partial section-more-finish board-slug direction))
    (dispatcher/dispatch! [:section-more (router/current-org-slug) board-slug])))

(defn setup-section-editing [section-slug]
  (when-let [board-data (dispatcher/board-data (router/current-org-slug) section-slug)]
    (dispatcher/dispatch! [:setup-section-editing board-data])))
