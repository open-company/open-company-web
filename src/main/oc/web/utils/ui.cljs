(ns oc.web.utils.ui
  (:require [oc.web.lib.utils :as utils]
            [cuerdas.core :as string]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.actions.activity :as activity-actions]))

(defn resize-textarea [textarea]
  (when textarea
    (set! (.-height (.-style textarea)) "")
    (set! (.-height (.-style textarea)) (str (.-scrollHeight textarea) "px"))))

(defn ui-compose []
  (utils/remove-tooltips)
  (activity-actions/activity-edit))

(def watch-activity-copy "Watch this update for future activity") ; "Get notified about new post activity"
(def unwatch-activity-copy "Ignore future activity for this update") ; "Don't show replies to this update" ; "Ignore future activity unless mentioned"

(defn prepare-for-plaintext-content-editable [in-str]
  (if (string? in-str)
    (-> in-str
        (string/trim)
        (string/replace #"<" "&lt;")
        (string/replace #">" "&gt;"))
    in-str))

(defn content-editable-value []
  (if (or ua/firefox? ua/ie?) "true" "plaintext-only"))