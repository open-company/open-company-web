(ns oc.web.components.all-posts
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 5)
(def card-avg-height 372)

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  [s e]
  (let [scroll-top (.-scrollTop (.-scrollingElement js/document))
        direction (if (> @(::last-scroll s) scroll-top)
                    :up
                    (if (< @(::last-scroll s) scroll-top)
                      :down
                      :stale))
        min-scroll 60
        max-scroll (- (.-scrollHeight (.-body js/document)) (.-innerHeight js/window))]
    ;; scrolling up
    (when (and (not @(::top-loading s))
               @(::has-prev s)
               (not @(::scroll-to-entry s))
               (= direction :up)
               (<= scroll-top min-scroll))
      ;; Show a spinner at the top
      (reset! (::top-loading s) true)
      ;; if the user is close to the top margin, load more results if there is a link
      (cond
        (= (router/current-board-slug) "all-posts")
        (activity-actions/all-posts-more @(::has-prev s) :up)
        (= (router/current-board-slug) "must-see")
        (activity-actions/must-see-more @(::has-prev s) :up)))
    ;; scrolling down
    (when (and (not @(::bottom-loading s))
               @(::has-next s)
               (= direction :down)
               (>= scroll-top (- max-scroll (* scroll-card-threshold card-avg-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (cond
        (= (router/current-board-slug) "all-posts")
        (activity-actions/all-posts-more @(::has-next s) :down)
        (= (router/current-board-slug) "must-see")
        (activity-actions/must-see-more @(::has-next s) :down)))
    ;; Save the last scrollTop value
    (reset! (::last-scroll s) scroll-top)))

(defn- ap-seen-mixin-cb [_ item-uuid]
  (activity-actions/ap-seen-events-gate item-uuid))

(defn- wrt-stream-item-mixin-cb [_ item-uuid]
  (activity-actions/wrt-events-gate item-uuid))

(defn- sorted-posts [posts]
  (activity-utils/get-sorted-activities posts))

(defn check-pagination [s]
  (let [container-data @(drv/get-ref s :container-data)
        sorted-items (sorted-posts @(drv/get-ref s :filtered-posts))
        direction (:direction container-data)
        next-link (utils/link-for (:links container-data) "next")
        prev-link (utils/link-for (:links container-data) "previous")
        ap-initial-at @(drv/get-ref s :ap-initial-at)]
    ;; First load or subsequent load more with
    ;; different set of items
    (if (= direction :up)
      ;; did scrolled up, we need to scroll to the first of the old items
      ;; to not lose the previous position
      (let [saved-items (:saved-items container-data)
            last-new-entry-idx (dec (- (count sorted-items) saved-items))
            scroll-to-entry (get sorted-items last-new-entry-idx)]
        (reset! (::last-direction s) :up)
        (reset! (::scroll-to-entry s) scroll-to-entry))
      (when ap-initial-at
        (reset!
         (::scroll-to-entry s)
         (first (filter #(= (:published-at %) ap-initial-at) sorted-items)))))
    (reset! (::has-prev s) prev-link)
    (reset! (::has-next s) next-link)
    (if next-link
      (reset! (::show-all-caught-up-message s) false)
      (reset! (::show-all-caught-up-message s) (> (count sorted-items) 10)))))

(rum/defcs all-posts  < rum/reactive
                        ;; Derivatives
                        (drv/drv :ap-initial-at)
                        (drv/drv :filtered-posts)
                        (drv/drv :container-data)
                        (drv/drv :activities-read)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local 0 ::last-scroll)
                        (rum/local false ::has-next)
                        (rum/local false ::has-prev)
                        (rum/local nil ::scroll-to-entry)
                        (rum/local nil ::top-loading)
                        (rum/local nil ::bottom-loading)
                        (rum/local false ::show-all-caught-up-message)
                        (rum/local nil ::last-direction)
                        ;; Mixins
                        mixins/first-render-mixin
                        (mixins/ap-seen-mixin "div.ap-seen-item-headline" ap-seen-mixin-cb)
                        (mixins/wrt-stream-item-mixin "div.stream-item-body.item-ready:not(.truncated)"
                         wrt-stream-item-mixin-cb)
                        section-mixins/container-nav-in

                        {:will-mount (fn [s]
                          (check-pagination s)
                          s)
                         :did-remount (fn [s]
                          (when (or (= (router/current-board-slug) "all-posts")
                                    (= (router/current-board-slug) "must-see"))
                            (check-pagination s))
                         s)
                         :did-mount (fn [s]
                          (when (or (= (router/current-board-slug) "all-posts")
                                    (= (router/current-board-slug) "must-see"))
                            (reset! (::last-scroll s) (.-scrollTop (.-body js/document)))
                            (reset! (::scroll-listener s)
                             (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                            (check-pagination s))

                          s)
                         :after-render (fn [s]
                          (when-let [scroll-to @(::scroll-to-entry s)]
                            (when-let [entry-el (sel1 [(str "div.stream-item-" (:uuid scroll-to))])]
                              (utils/scroll-to-element entry-el 0 0))
                            (utils/after 100 #(do
                                               (reset! (::scroll-to-entry s) nil)
                                               (reset! (::last-direction s) nil))))
                          s)
                         :before-render (fn [s]
                          (let [container-data @(drv/get-ref s :container-data)]
                            (when-not (:loading-more container-data)
                              (when @(::top-loading s)
                                (reset! (::top-loading s) false))
                              (when @(::bottom-loading s)
                                (reset! (::bottom-loading s) false)
                                (reset! (::show-all-caught-up-message s) true))))
                          s)
                         :will-unmount (fn [s]
                          (when @(::scroll-listener s)
                            (events/unlistenByKey @(::scroll-listener s)))
                          s)}
  [s]
  (let [container-data (drv/react s :container-data)
        items (sorted-posts (drv/react s :filtered-posts))
        activities-read (drv/react s :activities-read)]
    [:div.all-posts.group
      [:div.all-posts-cards
        (when @(::top-loading s)
          [:div.loading-updates.top-loading
            "Loading more posts..."])
        [:div.all-posts-cards-inner.group
          (when (or @(::top-loading s)
                    (and (:loading-more container-data)
                         (not @(:first-render-done s)))
                    @(::scroll-to-entry s)
                    (= @(::last-direction s) :up))
            [:div.activities-overlay
              (loading {:loading true})
              (when (or @(::top-loading s)
                        (= @(::last-direction s) :up))
                [:div.top-loading-message "Retrieving earlier activity..."])])
          (for [e items
                :let [reads-data (get activities-read (:uuid e))]]
            (rum/with-key
             (stream-item e reads-data)
             (str "all-posts-entry-" (:uuid e) "-" (:updated-at e))))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            "Loading more posts..."])
        (when (and @(::show-all-caught-up-message s)
                   (responsive/is-mobile-size?))
          (all-caught-up))]]))