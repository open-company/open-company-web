(ns oc.web.lib.logging
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.cookies :as cook]
            [cuerdas.core :as s]))

(def carrot-log-level (atom nil))

(defn- valid-log-level? [log-level]
  (and log-level
       (#{:trace :debug :info :warn :error :fatal :report} (keyword log-level))))

(defn ^:export save-log-level [log-level]
  (when (valid-log-level? log-level)
    (timbre/debugf "Saving log-level %s" (name log-level))
    (cook/set-cookie! :log-level (name log-level) cook/default-cookie-expire)))

(defn ^:export remove-log-level []
  (timbre/debugf "Removing log-level")
  (cook/remove-cookie! :log-level))

(defn ^:export config-log-level!
  ([level] (config-log-level! level false))
  ([level save?]
   (let [level-kw (keyword (s/lower level))]
     (when (valid-log-level? level-kw)
       (when save?
         (save-log-level level))
       (timbre/info "Log level:" level-kw)
       (reset! carrot-log-level level-kw)
       (timbre/merge-config! {:min-level level-kw})))))

(defn dbg [& args]
  (when (= @carrot-log-level :debug)
    (apply js/console.log args)))