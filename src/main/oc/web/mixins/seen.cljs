(ns oc.web.mixins.seen
  (:require [rum.core :as rum]
            [oops.core :refer (oget ocall)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.activity :as aa]))

(defn container-nav-mixin []
  (let [container-slug (atom nil)
        sort-type (atom nil)]
    {:did-mount (fn [s]
      (reset! container-slug (if (dis/current-board-slug) (keyword (dis/current-board-slug)) (dis/current-contributions-id)))
      (reset! sort-type (dis/current-sort-type))
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :did-remount (fn [_ s]
      (aa/container-nav-in @container-slug @sort-type)
     s)
     :will-unmount (fn [s]
      (aa/container-nav-out @container-slug @sort-type)
     s)}))

(declare maybe-mark-element-seen)
(declare unobserve-element)

(defn- entry-uuid-from-entry [intersection-entry]
  (oget intersection-entry :target.?dataset.?entryUuid))

(defn- seen-observer-cb [intersection-entries _observer]
  (doseq [intersection-entry (vec intersection-entries)]
    (maybe-mark-element-seen intersection-entry)
    (when (or (-> intersection-entry entry-uuid-from-entry aa/unseen-item? not)
              (oget intersection-entry :isIntersecting))
      (unobserve-element (oget intersection-entry :target)))))

(defonce ^{:export true} --seen-intersection-observer (atom nil))

(defn- intersection-observer
  "Return the intersection observer singleton, creates it if it's not
   initialized yet."
  []
  (let [opts {:rootMargin (str responsive/navbar-height "px 0px 0px 0px")
              :threshold 1.0} ]
    (or @--seen-intersection-observer
        (reset! --seen-intersection-observer
                (js/IntersectionObserver. seen-observer-cb (clj->js opts))))))

(defn- unobserve-element
  "Remove an observed element from the list."
  [observable-element]
  (ocall (intersection-observer) :unobserve observable-element))

(defn- maybe-mark-element-seen
  "If the passed intersection entry is intersecting with the viewport and it has
   the unseen-item class."
  [intersection-entry]
  (let [entry-uuid (entry-uuid-from-entry intersection-entry)]
    (when (and (oget intersection-entry :isIntersecting)
               (aa/unseen-item? entry-uuid))
      (aa/send-item-seen entry-uuid))))

(defn- observe-element [observable-element]
  (ocall (intersection-observer) :observe observable-element))

(def item-visible-mixin
  {:did-mount (fn [s]
                (when (-> s :rum/args first :activity-data :unseen)
                  (observe-element (rum/dom-node s)))
                s)
   :will-unmount (fn [s]
                   (unobserve-element (rum/dom-node s))
                   s)})