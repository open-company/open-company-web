(ns oc.web.utils.section
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.ws-change-client :as ws-cc]))

(defn request-read-counts [item-ids]
  (let [read-counts-data (dispatcher/read-counts-data)
        all-items (set (keys read-counts-data))
        request-set (set item-ids)
        needed-ids (clojure.set/difference request-set all-items)]
    (ws-cc/who-read-count (into [] needed-ids))))