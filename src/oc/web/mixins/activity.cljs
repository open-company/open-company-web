(ns oc.web.mixins.activity
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(def truncate-body-mixin
  {:init (fn [s]
    (assoc s :truncated (atom false)))
   :after-render (fn [s]
    ; Truncate body text with dotdotdot
    (when (compare-and-set! (:truncated s) false true)
      (when-let [body-el (rum/ref-node s "activity-body")]
        (au/truncate-body body-el)))
    s)
   :did-remount (fn [o s]
    (let [old-activity-data (first (:rum/args o))
          new-activity-data (first (:rum/args s))]
      ;; If the body changed
      (when (not= (:body old-activity-data) (:body new-activity-data))
        ;; Force the truncate algorithm to re-run
        (reset! (:truncated s) false)))
    s)})

(def body-thumbnail-mixin
  {:init (fn [s]
    (assoc s :body-thumbnail (atom nil)))
   :will-mount (fn [s]
    (let [activity-data (first (:rum/args s))]
      (reset!
       (:body-thumbnail s)
       (au/get-first-body-thumbnail (:body activity-data))))
    s)
   :did-remount (fn [o s]
    (let [old-activity-data (first (:rum/args o))
          new-activity-data (first (:rum/args s))]
      (when (not= (:body old-activity-data) (:body new-activity-data))
        (reset!
         (:body-thumbnail s)
         (au/get-first-body-thumbnail (:body new-activity-data)))))
    s)})