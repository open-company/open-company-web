(ns oc.web.actions.search
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.api :as api]))

(defn query-finished
  [result]
  (dispatcher/dispatch! [:search-query/finish result]))

(defn reset []
  (dispatcher/dispatch! [:search-query/finish {:success true
                                               :error false
                                               :body []}]))

(defn query
  "Use the search service to query for results."
  [search-query]
  (timbre/debug search-query)
  (if (> (count search-query) 1)
    (api/query (:uuid (dispatcher/org-data)) search-query query-finished)
    (reset)))

(defn result-clicked [url] (utils/after 10 (router/nav! url)))