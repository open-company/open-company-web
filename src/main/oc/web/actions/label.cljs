(ns oc.web.actions.label
  (:require-macros [if-let.core :refer (if-let*)])
  (:require [oc.web.dispatcher :as dis]
            [defun.core :refer (defun)]
            [oc.lib.hateoas :as hateoas]
            [cuerdas.core :as string]
            [taoensso.timbre :as timbre]
            [oc.lib.color :as lib-color]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]
            [cljs.reader :refer (read-string)]))

(def max-label-name-length 40)

;; Data parse

(defn hex->rgb [hex-color]
  (let [splitted-color (->> (string/split hex-color #"")
                            (remove #(or (string/empty-or-nil? %)
                                         (= "#" %)))
                            vec)
        colors (rest splitted-color)
        fixed-colors (if (= 3 (count colors))
                       (mapcat (partial repeat 2) colors)
                       colors)
        red (take 2 fixed-colors)
        green (take 2 (drop 2 fixed-colors))
        blue (take 2 (drop 4 fixed-colors))]
    (mapv #(let [conjed-c (conj % "0x")
                 joined-c (string/join conjed-c)]
             (read-string joined-c))
          [red green blue])))


(defn parse-label [label-map]
  
  (hex->rgb (:color label-map))

  (-> label-map
      (assoc :rgb-color (when (:color label-map)
                          (lib-color/hex->rgb (:color label-map))))
      (assoc :can-edit? (hateoas/link-for (:links label-map) "partial-update"))
      (assoc :can-delete? (hateoas/link-for (:links label-map) "delete"))))

(defn parse-labels [labels-resp]
  (->> labels-resp
      :collection
      :items
      (mapv parse-label)))

(defn user-labels [org-labels-list]
   (reverse (sort-by :count org-labels-list)))

(defn org-labels [org-labels-list]
  (sort-by :name org-labels-list))

(defn get-labels-finished
  ([org-slug resp] (get-labels-finished org-slug resp nil))
  ([org-slug finish-cb {:keys [body success]}]
   (let [response-body (when success (json->cljs body))
         parsed-labels (parse-labels response-body)
         org-labels-data (-> response-body
                             (dissoc :items)
                             (assoc :org-labels (org-labels parsed-labels))
                             (assoc :user-labels (user-labels parsed-labels)))]
     (timbre/infof "Labels load success %s, loaded %d labels" success (count (:org-labels org-labels-data)))
     (dis/dispatch! [:labels-loaded org-slug org-labels-data])
      (when (fn? finish-cb)
        (finish-cb (:org-labels org-labels-data))))))

(defn get-labels
  ([] (get-labels (dis/org-data) nil))
  ([org-data] (get-labels org-data nil))
  ([org-data finish-cb]
   (timbre/info "Loading labels")
   (let [labels-link (hateoas/link-for (:links org-data) "labels")]
     (api/get-labels labels-link (partial get-labels-finished (:slug org-data) finish-cb)))))

(defn new-label
  ([] (new-label ""))
  ([label-name]
   (let [org-data (dis/org-data)
         random-color (-> lib-color/default-css-color-names
                          vals
                          shuffle
                          first)]
     (timbre/infof "New label for org %s, random color %s" (:uuid org-data) random-color)
     (dis/dispatch! [:label-editor/start {:name label-name
                                          :color random-color
                                          :org-uuid (:uuid org-data)}]))))

(defn dismiss-label-editor []
  (timbre/info "Dismiss label editor")
  (dis/dispatch! [:label-editor/dismiss]))

(defn save-label []
  (let [org-data (dis/org-data)
        create-label-link (hateoas/link-for (:links org-data) "create-label")
        editing-label (:editing-label @dis/app-state)]
    (timbre/infof "Saving label with name %s and color %s" (:name editing-label) (:color editing-label))
    (api/create-label create-label-link editing-label
                      (fn [{:keys [body success]}]
                         (dismiss-label-editor)
                         (get-labels)
                         (let [label (when success (parse-label (json->cljs body)))]
                           (dis/dispatch! [:label-saved (dis/current-org-slug) label])
                           (when-not (:collapsed (dis/cmail-state))
                             (cmail-actions/add-cmail-label label)))))))

(defn edit-label [label]
  (timbre/infof "Editing label" (:uuid label))
  (dis/dispatch! [:label-editor/start label]))

(defn delete-label [label]
  (timbre/infof "Deleting label %s" (:uuid label))
  (let [delete-link (hateoas/link-for (:links label) "delete")]
    (dis/dispatch! [:delete-label (dis/current-org-slug) label])
    (api/delete-label delete-link #(get-labels))))

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