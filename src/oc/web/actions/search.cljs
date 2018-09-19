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
  (dispatcher/dispatch! [:search-reset]))

(defn inactive []
  (dispatcher/dispatch! [:search-inactive]))

(defn active []
  (dispatcher/dispatch! [:search-active]))

(defn query
  "Use the search service to query for results."
  [search-query]
  (when (> (count search-query) 1)
    (active)
    (api/query (:uuid (dispatcher/org-data)) search-query query-finished)))

(defn result-clicked [url]
  (dispatcher/dispatch! [:search-result-clicked])
  (utils/after 10 (router/nav! url)))

(defn focus []
  (dispatcher/dispatch! [:search-focus]))