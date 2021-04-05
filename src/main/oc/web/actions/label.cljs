(ns oc.web.actions.label
  (:require-macros [if-let.core :refer (if-let* when-let*)])
  (:require [oc.web.dispatcher :as dis]
            [defun.core :refer (defun)]
            [cuerdas.core :as cstr]
            [oc.lib.hateoas :as hateoas]
            [taoensso.timbre :as timbre]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.utils.label :as label-utils]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]))

(def max-label-name-length 40)

(defn user-labels [org-labels-list]
   (vec (reverse (sort-by :count org-labels-list))))

(defn org-labels [org-labels-list]
  (vec (sort-by (comp cstr/lower :name) org-labels-list)))

(defn get-labels-finished [org-slug {:keys [body success]}]
  (let [response-body (when success (json->cljs body))
        parsed-labels (label-utils/parse-labels response-body)
        retrieved-labels-data (-> response-body
                                  (dissoc :items)
                                  (assoc :org-labels (org-labels parsed-labels))
                                  (assoc :user-labels (user-labels parsed-labels)))]
    (timbre/infof "Labels load success %s, loaded %d labels" success (count (:org-labels retrieved-labels-data)))
    (dis/dispatch! [:labels-loaded org-slug retrieved-labels-data])
    ;; Need to return the labels here
    retrieved-labels-data))

(defn get-labels
  ([] (get-labels (dis/org-data)))
  ([org-data]
   (timbre/info "Loading labels")
   (let [labels-link (hateoas/link-for (:links org-data) "labels")]
     (api/get-labels labels-link (partial get-labels-finished (:slug org-data))))))

(defn new-label
  ([] (new-label "" {}))
  ([label-name extra]
   (let [org-data (dis/org-data)]
     (timbre/infof "New label for org %s" (:uuid org-data))
     (dis/dispatch! [:label-editor/start (merge (label-utils/new-label-data label-name (:uuid org-data))
                                                extra)]))))

(defn foc-picker-new-label []
  (new-label "" {:back-to :foc-picker}))

(defn dismiss-label-editor []
  (timbre/info "Dismiss label editor")
  (dis/dispatch! [:label-editor/dismiss]))

(declare entry-label-add)
(declare entry-label-remove)

(defn create-label-finished [org-slug {success :success :as resp}]
  (if-not success
    (get-labels)
    (let [updated-labels-list (get-labels-finished org-slug resp)
          new-added-label (last updated-labels-list)]
      (dismiss-label-editor)
      (dis/dispatch! [:label-create/finished org-slug updated-labels-list])
      ;; Add label to entry that has the picker currently opened
      (when-let [entry-uuid (dis/foc-labels-picker)]
        (entry-label-add entry-uuid (:uuid new-added-label)))
      ;; Add label to cmail if picker was opened from there
      (let [cmail-state (dis/cmail-state)]
        (when (or (:labels-inline-view cmail-state)
                  (:labels-floating-view cmail-state))
          (cmail-actions/add-cmail-label new-added-label))))))

(defn create-label
  ([label-data]
   (let [create-label-link (hateoas/link-for (:links (dis/org-data)) "create-label")]
     (create-label create-label-link label-data)))
  ([create-label-link editing-label]
   (timbre/infof "Creating label with name % with link %s" (:name editing-label) (:hread create-label-link))
   (let [org-slug (dis/current-org-slug)]
     (dis/dispatch! [:label-create org-slug editing-label])
     (api/create-label create-label-link editing-label (partial create-label-finished org-slug)))))

(defn update-label-finished [org-slug {success :success body :body}]
    (if success
      (let [updated-label (json->cljs body)]
        (dismiss-label-editor)
        (dis/dispatch! [:label-update/finished org-slug updated-label])
                             ;; Add label to entry that has the picker currently opened
        (when-let [entry-uuid (dis/foc-labels-picker)]
          (entry-label-add entry-uuid (:uuid updated-label)))
                             ;; Add label to cmail if picker was opened from there
        (let [cmail-state (dis/cmail-state)]
          (when (or (:labels-inline-view cmail-state)
                    (:labels-floating-view cmail-state))
            (cmail-actions/add-cmail-label updated-label)))
                             ;; Delay the complete refresh of the labels list since it's not necessary
        (utils/after 250 get-labels))
      (get-labels)))

(defn update-label
  ([label-data] (update-label (hateoas/link-for (:links label-data) "partial-update") label-data))
  ([update-label-link label-data]
   (timbre/infof "Updating label % with link %s" (:uuid label-data) (:hread update-label-link))
   (let [org-slug (dis/current-org-slug)]
     (dis/dispatch! [:label-update org-slug label-data])
     (api/update-label update-label-link label-data (partial update-label-finished org-slug)))))

(defn save-label []
  (let [editing-label (:editing-label @dis/app-state)]
    (timbre/debugf "Saving label with name %s" (:name editing-label))
    (if (:links editing-label)
      (update-label editing-label)
      (create-label editing-label))))

(defn edit-label
  ([label] (edit-label label {}))
  ([label extra]
   (timbre/infof "Editing label" (:uuid label))
   (dis/dispatch! [:label-editor/start (merge label extra)])))

(defn foc-picker-edit-label [label]
  (edit-label label {:back-to :foc-picker}))

(defn delete-label-finished [label {success :success}]
  (when success
    (when-let [entry-uuid (dis/foc-labels-picker)]
      (entry-label-remove entry-uuid (:uuid label))))
  (get-labels))

(defn delete-label [label]
  (timbre/infof "Deleting label %s - %s" (:name label) (:uuid label))
  (let [delete-link (hateoas/link-for (:links label) "delete")]
    (dis/dispatch! [:delete-label (dis/current-org-slug) label])
    (api/delete-label delete-link (partial delete-label-finished label))))

(defn show-labels-manager []
  (timbre/info "Open labels manager")
  (dis/dispatch! [:labels-manager/show]))

(defn hide-labels-manager []
  (timbre/info "Close labels manager")
  (dis/dispatch! [:labels-manager/hide]))

(defn label-editor-update [label-data]
  (dis/dispatch! [:label-editor/update label-data]))

(defun get-label-entries
  ([label] (get-label-entries label (dis/org-data) nil))
  ([label org-data] (get-label-entries label org-data nil))
  ([label :guard map? org-data cb] (get-label-entries label org-data nil))
  ([label-slug :guard string? org-data cb]
   (timbre/infof "Loading entries for label %s" label-slug)
   (let [label-entries-link (hateoas/link-for (:links org-data) "partial-label-entries" {} {:label-slug label-slug})]
     (api/get-entries label-entries-link
                      ()))))

;; Label entries list

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

(defn- is-currently-shown? [label-slug]
  (= (dis/current-label-slug) label-slug))

(defn- request-reads-count
  "Request the reads count data only for the items we don't have already."
  [label-slug label-entries-data]
  (let [member? (:member? (dis/org-data))]
    (when (and member?
               (seq (:items label-entries-data)))
      (let [item-ids (map :uuid (:items label-entries-data))
            cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
        (when (seq cleaned-ids)
          (api/request-reads-count cleaned-ids))))))

(defn- label-entries-get-success [org-slug label-slug sort-type label-entries-data]
  (let [is-currently-shown (is-currently-shown? label-slug)
        member? (:member? (dis/org-data))]
    (when is-currently-shown
      (when member?
        ;; only watch the boards of the posts actually shown
        (when (= (dis/current-label-slug) (:label-slug (:collection label-entries-data)))
          ; (request-reads-count (->> contrib-data :collection :items (map :uuid)))
          (watch-boards (:items (:collection label-entries-data))))
        ;; Retrieve reads count if there are items in the loaded section
        (request-reads-count label-slug (:collection label-entries-data))))
    (dis/dispatch! [:label-entries-get/finish org-slug label-slug sort-type label-entries-data])))

(defn- label-entries-get-finish [org-slug label-slug sort-type {:keys [status body success]}]
  (if (= status 404)
    (router/redirect-404!)
    (label-entries-get-success org-slug label-slug sort-type (if success (json->cljs body) {}))))

(defn- label-entries-real-get [org-data label-slug label-entries-link]
  (api/get-label-entries label-entries-link
                         (fn [resp]
                           (label-entries-get-finish (:slug org-data) label-slug dis/recently-posted-sort resp))))

(defn- label-entries-link [org-data label-slug]
  (hateoas/link-for (:links org-data) "partial-label-entries" {} {:label-slug label-slug}))

(defn label-entries-get
  ([label-slug] (label-entries-get (dis/org-data) label-slug))

  ([org-data label-slug]
   (when-let [label-entries-link (label-entries-link org-data label-slug)]
     (label-entries-real-get org-data label-slug label-entries-link))))

(defn label-entries-refresh
  "If the user is looking at a label entries view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
  ([label-slug] (label-entries-refresh (dis/org-data) label-slug))
  ([org-data label-slug]
   (if-let* [label-entries-data (dis/label-entries-data label-slug)
             refresh-link (hateoas/link-for (:links label-entries-data) "refresh")]
     (label-entries-real-get org-data label-slug refresh-link)
     (label-entries-get org-data label-slug))))

(defn- label-entries-more-finish [org-slug label-slug sort-type direction {:keys [success body]}]
  (let [label-entries-data (when success (json->cljs body))]
    (when success
      (when (= (dis/current-label-slug) (:label-slug (:collection label-entries-data)))
        ;; only watch the boards of the posts of the contributor
        (watch-boards (:items (:collection label-entries-data))))
      ;; Retrieve reads count if there are items in the loaded section
      (request-reads-count label-slug (:collection label-entries-data)))
    (dis/dispatch! [:label-entries-more/finish org-slug label-slug sort-type
                    direction (when success (:collection label-entries-data))])))

(defn label-entries-more [more-link direction]
  (let [org-slug (dis/current-org-slug)
        label-slug (dis/current-label-slug)]
    (api/load-more-items more-link direction (partial label-entries-more-finish org-slug label-slug dis/recently-posted-sort direction))
    (dis/dispatch! [:label-entries-more org-slug label-slug dis/recently-posted-sort])))

;; Changes to labels inside an entry

(defn entry-label-add [entry-uuid label-uuid]
  (when-let* [org-slug (dis/current-org-slug)
              entry-data (dis/entry-data entry-uuid)
              label-data (dis/label-data label-uuid)
              add-label-link (hateoas/link-for (:links entry-data)
                                               "partial-add-label" {}
                                               {:label-uuid label-uuid})]
             (dis/dispatch! [:entry-label/add org-slug entry-uuid label-data])
             (api/add-entry-label add-label-link
                                  (partial cmail-actions/get-entry-finished org-slug entry-uuid))))

(defn entry-label-remove [entry-uuid label-uuid]
  (when-let* [org-slug (dis/current-org-slug)
              entry-data (dis/entry-data entry-uuid)
              label-data (dis/entry-label-data entry-uuid label-uuid)
              remove-label-link (hateoas/link-for (:links entry-data)
                                                  "partial-remove-label" {}
                                                  {:label-uuid label-uuid})]
             (dis/dispatch! [:entry-label/remove org-slug entry-uuid label-data])
             (api/remove-entry-label remove-label-link
                                     (partial cmail-actions/get-entry-finished org-slug entry-uuid))))

(defn toggle-entry-label [entry-uuid label-uuid]
  (when (dis/entry-data entry-uuid)
    (if (dis/entry-label-data entry-uuid label-uuid)
      (entry-label-remove entry-uuid label-uuid)
      (entry-label-add entry-uuid label-uuid))))

(defn entry-label-changes [entry-uuid add-label-uuids remove-label-uuids]
  (when-let* [org-slug (dis/current-org-slug)
              entry-data (dis/entry-data entry-uuid)
              add-remove-labels-map {:add add-label-uuids
                                     :remove remove-label-uuids}
              label-changes-link (hateoas/link-for (:links entry-data) "label-changes")]
             (dis/dispatch! [:entry-label/label-changes org-slug entry-uuid add-remove-labels-map])
             (api/entry-label-changes label-changes-link add-remove-labels-map
                                      (partial cmail-actions/get-entry-finished org-slug entry-uuid))))

;; FoC Labels picker

(defn toggle-foc-labels-picker
  ([] (toggle-foc-labels-picker nil))
  ([entry-uuid]
   (js/console.log "DBG " (if entry-uuid (str "Showing labels picker for" entry-uuid) "Hide labels picker"))
   (dis/dispatch! [:toggle-foc-labels-picker entry-uuid])))

(defn hide-foc-labels-picker []
  (toggle-foc-labels-picker nil))

;; Change service actions

(defn subscribe []
  (ws-cc/subscribe :item/change
                   (fn [data]
                     (when (dis/current-contributions-id)
                       (let [change-data (:data data)
                             activity-uuid (:item-id change-data)
                             change-type (:change-type change-data)
                             activity-data (dis/activity-data (dis/current-org-slug) activity-uuid)
                             current-label-slug (dis/current-label-slug)
                             contains-current-label? (->> activity-data
                                                          :labels
                                                          (map :slug)
                                                          set
                                                          current-label-slug)]
                         ;; On update or delete of a post from the currently shown user
                         (when (or (= change-type :add)
                                   (and (#{:update :delete} change-type)
                                        contains-current-label?))
                           (label-entries-get current-label-slug)))))))