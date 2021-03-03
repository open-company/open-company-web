(ns oc.web.actions.contributions
  (:require-macros [if-let.core :refer (if-let*)])
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.lib.json :refer (json->cljs)]))

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

(defn- is-currently-shown? [author-uuid]
  (= (dis/current-contributions-id) author-uuid))

(defn- request-reads-count
  "Request the reads count data only for the items we don't have already."
  [author-uuid contrib-data]
  (let [member? (:member? (dis/org-data))]
    (when (and member?
               (seq (:items contrib-data)))
      (let [item-ids (map :uuid (:items contrib-data))
            cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
        (when (seq cleaned-ids)
          (api/request-reads-count cleaned-ids))))))

(defn- contributions-get-success [org-slug author-uuid sort-type contrib-data]
  (let [is-currently-shown (is-currently-shown? author-uuid)
        member? (:member? (dis/org-data))]
    (when is-currently-shown
      (when member?
        ;; only watch the boards of the posts of the contributor
        (when (= (dis/current-contributions-id) (:author-id (:collection contrib-data)))
          ; (request-reads-count (->> contrib-data :collection :items (map :uuid)))
          (watch-boards (:items (:collection contrib-data))))
        ;; Retrieve reads count if there are items in the loaded section
        (request-reads-count author-uuid (:collection contrib-data))))
    (dis/dispatch! [:contributions-get/finish org-slug author-uuid sort-type contrib-data])))

(defn- contributions-get-finish [org-slug author-uuid sort-type {:keys [status body success]}]
  (if (= status 404)
    (router/redirect-404!)
    (contributions-get-success org-slug author-uuid sort-type (if success (json->cljs body) {}))))

(defn- contributions-real-get [org-data author-uuid contrib-link]
  (api/get-contributions contrib-link
   (fn [{:keys [status body success] :as resp}]
     (contributions-get-finish (:slug org-data) author-uuid dis/recently-posted-sort resp))))

(defn- contributions-link [org-data author-uuid]
  (utils/link-for (:links org-data) "partial-contributions" {} {:author-uuid author-uuid}))

(defn contributions-get
  ([author-uuid] (contributions-get (dis/org-data) author-uuid))

  ([org-data author-uuid]
  (when-let [contrib-link (contributions-link org-data author-uuid)]
    (contributions-real-get org-data author-uuid contrib-link))))

(defn contributions-refresh
 "If the user is looking at a contributions view we need to reload all the items that are visible right now.
  Instead, if the user is looking at another view we can just reload the first page."
 ([author-uuid] (contributions-refresh (dis/org-data) author-uuid))
 ([org-data author-uuid]
  (if-let* [contrib-data (dis/contributions-data author-uuid)
            refresh-link (utils/link-for (:links contrib-data) "refresh")]
    (contributions-real-get org-data author-uuid refresh-link)
    (contributions-get org-data author-uuid))))

(defn- contributions-more-finish [org-slug author-uuid sort-type direction {:keys [success body]}]
  (let [contrib-data (when success (json->cljs body))]
    (when success
      (when (= (dis/current-contributions-id) (:author-id (:collection contrib-data)))
        ;; only watch the boards of the posts of the contributor
        (watch-boards (:items (:collection contrib-data))))
      ;; Retrieve reads count if there are items in the loaded section
      (request-reads-count author-uuid (:collection contrib-data)))
    (dis/dispatch! [:contributions-more/finish org-slug author-uuid sort-type
     direction (when success (:collection contrib-data))])))

(defn contributions-more [more-link direction]
  (let [org-slug (dis/current-org-slug)
        author-uuid (dis/current-contributions-id)]
    (api/load-more-items more-link direction (partial contributions-more-finish org-slug author-uuid dis/recently-posted-sort direction))
    (dis/dispatch! [:contributions-more org-slug author-uuid dis/recently-posted-sort])))

;; Change service actions

(defn subscribe []
  (ws-cc/subscribe :item/change
    (fn [data]
      (when (dis/current-contributions-id)
        (let [change-data (:data data)
              activity-uuid (:item-id change-data)
              change-type (:change-type change-data)
              activity-data (dis/activity-data (dis/current-org-slug) activity-uuid)]
          ;; On update or delete of a post from the currently shown user
          (when (or (= change-type :add)
                    (and (#{:update :delete} change-type)
                         (= (dis/current-contributions-id)
                            (-> activity-data :publisher :user-id))))
            (contributions-get (dis/current-contributions-id))))))))