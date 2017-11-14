(ns oc.web.mixins.activity
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.activity-utils :as au]))

(def truncate-body-mixin
  {:init (fn [s]
    (assoc s :truncated (atom false)))
   :after-render (fn [s]
    (when-let [body-el (rum/ref-node s "activity-body")]
      ; Truncate body text with dotdotdot
      (when (compare-and-set! (:truncated s) false true)
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
    (let [draft-data (first (:rum/args s))]
      (reset!
       (:body-thumbnail s)
       (au/get-first-body-thumbnail (:body draft-data))))
    s)
   :did-remount (fn [o s]
    (let [old-draft-data (first (:rum/args o))
          new-draft-data (first (:rum/args s))]
      (when (not= (:body old-draft-data) (:body new-draft-data))
        (reset!
         (:body-thumbnail s)
         (au/get-first-body-thumbnail (:body new-draft-data)))))
    s)})