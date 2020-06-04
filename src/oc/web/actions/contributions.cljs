(ns oc.web.actions.contributions
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [clojure.string :as s]
            [defun.core :refer (defun)]
            [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

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
  (= (router/current-contributions-id) author-uuid))

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

(defn- contributions-get-success [author-uuid contrib-data]
  (let [is-currently-shown (is-currently-shown? author-uuid)
        member? (:member? (dis/org-data))]
    (when is-currently-shown
      (when member?
        ;; only watch the boards of the posts of the contributor
        (when (= (router/current-contributions-id) (:author-id (:collection contrib-data)))
          ; (request-reads-count (->> contrib-data :collection :items (map :uuid)))
          (watch-boards (:items (:collection contrib-data))))
        ;; Retrieve reads count if there are items in the loaded section
        (request-reads-count author-uuid (:collection contrib-data))))
    (dis/dispatch! [:contributions-get/finish (router/current-org-slug) author-uuid contrib-data])))

(defn- contributions-get-finish [author-uuid {:keys [status body success]}]
  (if (= status 404)
    (router/redirect-404!)
    (contributions-get-success author-uuid (if success (json->cljs body) {}))))

(defn- contributions-link [org-data author-uuid]
  (when-let [partial-link (utils/link-for (:links org-data) "partial-contributions")]
    (utils/link-replace-href partial-link {:author-uuid author-uuid})))

(defn contributions-get
  ([author-uuid] (contributions-get (dis/org-data) author-uuid))

  ([org-data author-uuid]
  (when-let [contrib-link (contributions-link org-data author-uuid)]
    (api/get-contributions contrib-link
     (fn [{:keys [status body success] :as resp}]
       (contributions-get-finish author-uuid resp))))))

(defn- contributions-more-finish [author-uuid direction {:keys [success body]}]
  (let [contrib-data (when success (json->cljs body))]
    (when success
      (when (= (router/current-contributions-id) (:author-id (:collection contrib-data)))
        ;; only watch the boards of the posts of the contributor
        (watch-boards (:items (:collection contrib-data))))
      ;; Retrieve reads count if there are items in the loaded section
      (request-reads-count author-uuid (:collection contrib-data)))
    (dis/dispatch! [:contributions-more/finish (router/current-org-slug) author-uuid
     direction (when success (:collection contrib-data))])))

(defn contributions-more [more-link direction]
  (let [author-uuid (router/current-contributions-id)]
    (api/load-more-items more-link direction (partial contributions-more-finish author-uuid direction))
    (dis/dispatch! [:contributions-more (router/current-org-slug) author-uuid])))

;; Change service actions

(defn subscribe []
  (ws-cc/subscribe :item/change
    (fn [data]
      (when (router/current-contributions-id)
        (let [change-data (:data data)
              activity-uuid (:item-id change-data)
              change-type (:change-type change-data)
              activity-data (dis/activity-data (router/current-org-slug) activity-uuid)]
          ;; On update or delete of a post from the currently shown user
          (when (or (= change-type :add)
                    (and (#{:update :delete} change-type)
                         (= (router/current-contributions-id)
                            (-> activity-data :publisher :user-id))))
            (contributions-get (router/current-contributions-id))))))))