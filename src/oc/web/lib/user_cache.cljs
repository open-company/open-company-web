(ns oc.web.lib.user-cache
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [clojure.walk :refer (keywordize-keys stringify-keys)]
            [cljsjs.localforage]))

(defn- get-key [data-key]
  (str (or (jwt/user-id) "anonymous") "-" data-key))

(defn set-item
  "Add an item to the localforage for later use."
  [data-key data-value & [completed-cb]]
  (let [fixed-key (get-key data-key)]
    (timbre/debug "set-item for" fixed-key)
    (.setItem js/localforage
     fixed-key
     (clj->js (stringify-keys data-value))
     (fn [err]
       (timbre/debug "   - set-item error" err)
       (when (fn? completed-cb)
         (completed-cb err))))))

(defn remove-item
  "Remove an item from the localforage."
  [data-key]
  (let [fixed-key (get-key data-key)]
    (timbre/debug "remove-item for" fixed-key)
    (.removeItem js/localforage fixed-key)))

(defn get-item
  "Get an item from the localforage and return it."
  [data-key get-item-cb]
  (let [fixed-key (get-key data-key)]
    (timbre/debug "get-item for" fixed-key)
    (.getItem js/localforage
     fixed-key
     (fn [err value]
       (timbre/debug "   - get-item for" fixed-key value err)
       (when (fn? get-item-cb)
         (let [clj-value (if value
                           (keywordize-keys (js->clj value))
                           value)]
           (get-item-cb clj-value err)))))))