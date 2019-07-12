(ns oc.web.actions.search
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as aa]
            [oc.web.dispatcher :as dispatcher]
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

(defn query
  "Use the search service to query for results."
  [search-query]
  (if (> (count search-query) 1)
    (do
      (active)
      (api/query (:uuid (dispatcher/org-data)) search-query query-finished))
    (reset)))

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
                                                     "The selected post was moved to another section or deleted."
                                                     "An error occurred while loading the selected post. Please try again.")
                                          :solid-button-style :red
                                          :solid-button-title "Ok"
                                          :solid-button-cb alert-modal/hide-alert}]
                          (alert-modal/show-alert alert-data))))]
    (if post-loaded?
      (open-post-cb true nil)
      (aa/get-entry-with-uuid (:board-slug entry-result) (:uuid entry-result) open-post-cb))))

(defn focus []
  (dispatcher/dispatch! [:search-focus]))