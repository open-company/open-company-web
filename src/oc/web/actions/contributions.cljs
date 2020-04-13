(ns oc.web.actions.contributions
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [clojure.string :as s]
            [defun.core :refer (defun)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(defn- is-currently-shown? [author-uuid]
  (= (router/current-contributor-id) author-uuid))

(defn- request-reads-count
  "Request the reads count data only for the items we don't have already."
  [author-uuid contrib-data]
  (let [member? (jwt/user-is-part-of-the-team (:team-id (dis/org-data)))]
    (when (and member?
               (seq (:items contrib-data)))
      (let [item-ids (map :uuid (:items contrib-data))
            cleaned-ids (au/clean-who-reads-count-ids item-ids (dis/activity-read-data))]
        (when (seq cleaned-ids)
          (api/request-reads-count cleaned-ids))))))

(defn- contributions-get-success [author-uuid contrib-data]
  (let [is-currently-shown (is-currently-shown? author-uuid)
        member? (jwt/user-is-part-of-the-team (:team-id (dis/org-data)))]
    (when is-currently-shown
      (when member?
        ;; only watch the currently visible board.
        ; only for logged in users and if the board is currently shown
        ; (watch-single-section section)
        ;; Retrieve reads count if there are items in the loaded section
        (request-reads-count author-uuid contrib-data)))
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
    (api/get-contributor contrib-link
     (fn [{:keys [status body success] :as resp}]
       (contributions-get-finish author-uuid resp))))))

(defn- contributions-more-finish [author-uuid direction {:keys [success body]}]
  (when success
    (request-reads-count author-uuid (json->cljs body)))
  (dis/dispatch! [:contributions-more/finish (router/current-org-slug) author-uuid
   direction (when success (:collection (json->cljs body)))]))

(defn contributions-more [more-link direction]
  (let [author-uuid (router/current-contributor-id)]
    (api/load-more-items more-link direction (partial contributions-more-finish author-uuid direction))
    (dis/dispatch! [:contributions-more (router/current-org-slug) author-uuid])))