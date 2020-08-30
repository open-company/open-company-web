(ns oc.web.actions.search
  (:require [cuerdas.core :as s]
            [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.actions.cmail :as cmail-actions]        
            [oc.web.components.ui.alert-modal :as alert-modal]))


(defn query-finished
  [result]
  (dispatcher/dispatch! [:search-query/finish result]))

(defn reset []
  (dispatcher/dispatch! [:search-reset]))

(defn inactive []
  (dispatcher/dispatch! [:search-inactive]))

(defn active []
  (dispatcher/dispatch! [:search-active]))

(def search-history-cookie (str "search-history-" (jwt/user-id)))

(def search-history-length 5)

(defn search-history []
  (let [res (cook/get-cookie search-history-cookie)]
    (if (seq res)
      (->> res js/$.parseJSON js->clj vec)
      #{})))

(defn query
  "Use the search service to query for results.
   Keep tracl of the last "
  [search-query auto-search?]
  (let [trimmed-query (utils/trim search-query)]
    (if (seq trimmed-query)
      (do
        (when (or (not auto-search?)
                  (> (count trimmed-query) 2))
          (let [temp-history (utils/vec-dissoc (search-history) trimmed-query)
                last-search (last temp-history)
                temp-history* (if (and auto-search?
                                     (or ; User added one letter to the beginning
                                         (->> trimmed-query rest (s/join "") (= last-search))
                                         ; User added one letter to the end
                                         (->> trimmed-query butlast (s/join "") (= last-search))
                                         ; User removed one letter from the beginning
                                         (->> last-search rest (s/join "") (= trimmed-query))
                                         ; User removed one letter from the end
                                         (->> last-search butlast (s/join "") (= trimmed-query))))
                                (butlast temp-history)
                                temp-history)
                with-new (conj temp-history* trimmed-query)
                history  (->> with-new (take-last search-history-length) clj->js js/JSON.stringify)]
            (cook/set-cookie! search-history-cookie history cook/default-cookie-expire)))
        (active)
        (dispatcher/dispatch! [:search-query/start trimmed-query])
        (api/query (:uuid (dispatcher/org-data)) trimmed-query query-finished))
      (reset))))

(defn result-clicked [entry-result url]
  (let [post-loaded? (dispatcher/activity-data (:uuid entry-result))
        open-post-cb (fn [success status]
                       (if success
                        (do
                          (dispatcher/dispatch! [:search-result-clicked])
                          (utils/after 10 (router/nav! url)))
                        (let [is-404? (= status 404)
                              alert-data {:icon "/img/ML/trash.svg"
                                          :action "search-result-load-failed"
                                          :title (if is-404? "Post moved or deleted" "An error occurred")
                                          :message (if is-404?
                                                     "The selected update was moved to another team or deleted."
                                                     "An error occurred while loading the selected post. Please try again.")
                                          :solid-button-style :red
                                          :solid-button-title "Ok"
                                          :solid-button-cb alert-modal/hide-alert}]
                          (alert-modal/show-alert alert-data))))]
    (if post-loaded?
      (open-post-cb true nil)
      (cmail-actions/get-entry-with-uuid (:board-slug entry-result) (:uuid entry-result) open-post-cb))))