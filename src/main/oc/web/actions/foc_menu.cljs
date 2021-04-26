(ns oc.web.actions.foc-menu
  (:require [oc.web.dispatcher :as dis]))

(defn toggle-foc-show-menu
  ([] (toggle-foc-show-menu nil))
  ([v]
   (dis/dispatch! [:foc-show-menu v])))

(defn toggle-foc-menu-open
  ([] (toggle-foc-menu-open nil))
  ([v]
   (dis/dispatch! [:foc-menu-open v])))

(defn toggle-foc-activity-move
  ([] (toggle-foc-activity-move nil))
  ([v]
   (dis/dispatch! [:foc-activity-move v])))

(defn toggle-foc-labels-picker
  ([] (toggle-foc-labels-picker nil))
  ([v]
   (dis/dispatch! [:foc-labels-picker v])))

(defn toggle-foc-share-entry
  ([] (toggle-foc-share-entry nil))
  ([v]
   (dis/dispatch! [:foc-share-entry v])))

(defn hide-foc-menu-if-needed []
  (let [foc-menu-data (dis/foc-menu-data)]
    (when (some second foc-menu-data)
      (dis/dispatch! [:foc-show-menu nil]))))