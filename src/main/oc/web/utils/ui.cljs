(ns oc.web.utils.ui
  (:require [cuerdas.core :as string]
            [oc.lib.cljs.useragent :as ua]))

(defn resize-textarea [textarea]
  (when textarea
    (set! (.-height (.-style textarea)) "")
    (set! (.-height (.-style textarea)) (str (.-scrollHeight textarea) "px"))))

(defn remove-tooltips []
  (.remove (js/$ "div.tooltip")))

(def watch-activity-copy "Watch this update for future activity") ; "Get notified about new post activity"
(def unwatch-activity-copy "Ignore future activity for this update") ; "Don't show replies to this update" ; "Ignore future activity unless mentioned"

(defn ios-copy-to-clipboard [el]
  (let [old-ce (.-contentEditable el)
        old-ro (.-readOnly el)
        rg (.createRange js/document)]
    (set! (.-contentEditable el) true)
    (set! (.-readOnly el) false)
    (.selectNodeContents rg el)
    (let [s (.getSelection js/window)]
      (.removeAllRanges s)
      (.addRange s rg)
      (.setSelectionRange el 0 (.. el -value -length))
      (set! (.-contentEditable el) old-ce)
      (set! (.-readOnly el) old-ro))))

(defn copy-to-clipboard [el]
  (try
    (when ua/ios?
      (ios-copy-to-clipboard el))
    (.execCommand js/document "copy")
    (catch :default _
      false)))
