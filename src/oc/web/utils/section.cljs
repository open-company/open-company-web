(ns oc.web.utils.section
  (:require [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.ws-change-client :as ws-cc]))

(defn request-reads-data [item-id]
  (ws-cc/who-read item-id))

(defn request-reads-count [item-ids]
  (let [activities-read-data (dispatcher/activities-read-data)
        all-items (set (keys activities-read-data))
        request-set (set item-ids)
        needed-ids (into [] (clojure.set/difference request-set all-items))]
    (ws-cc/who-read-count needed-ids)))