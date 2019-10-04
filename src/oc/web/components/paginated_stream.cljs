(ns oc.web.components.paginated-stream
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.react-utils :as rutils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            cljsjs.react-virtualized))

(def virtualized-list (partial rutils/build js/ReactVirtualized.List))
(def window-scroller (partial rutils/build js/ReactVirtualized.WindowScroller))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 1)
(def foc-height 184)

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  [s e]
  (let [scroll-top (.-scrollTop (.-scrollingElement js/document))
        direction (if (> @(::last-scroll s) scroll-top)
                    :up
                    (if (< @(::last-scroll s) scroll-top)
                      :down
                      :stale))
        max-scroll (- (.-scrollHeight (.-body js/document)) (.-innerHeight js/window))]
    ;; scrolling down
    (when (and ;; not already loading more
               (not @(::bottom-loading s))
               ;; has a link to load more that can be used
               @(::has-next s)
               ;; scroll is moving down
               (= direction :down)
               ;; and the threshold point has been reached
               (>= scroll-top (- max-scroll (* scroll-card-threshold foc-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (cond
        (= (router/current-board-slug) "all-posts")
        (activity-actions/all-posts-more @(::has-next s) :down)
        (= (router/current-board-slug) "follow-ups")
        (activity-actions/follow-ups-more @(::has-next s) :down)
        :else
        (section-actions/section-more @(::has-next s) :down)))
    ;; Save the last scrollTop value
    (when (not= scroll-top @(::last-scroll s))
      (reset! (::last-scroll s) scroll-top))))

(defn- ap-seen-mixin-cb [_ item-uuid]
  (activity-actions/ap-seen-events-gate item-uuid))

(defn check-pagination [s]
  (let [container-data @(drv/get-ref s :container-data)
        sorted-items @(drv/get-ref s :filtered-posts)
        next-link (utils/link-for (:links container-data) "next")]
    (reset! (::has-next s) next-link)
    (if next-link
      (reset! (::show-all-caught-up-message s) false)
      (reset! (::show-all-caught-up-message s) (> (count sorted-items) 10)))
    (did-scroll s nil)))

(rum/defc wrapped-stream-item < rum/static
  [{:keys [key style] :as row-props}
   {:keys [entry
           reads-data
           org-data
           comments-data
           editable-boards] :as props}]
  (let [show-wrt? (and (jwt/user-is-part-of-the-team (:team-id org-data))
                       (activity-utils/is-published? entry))]
   [:div
    {:style style}
    (stream-item {:activity-data entry
                  :comments-data comments-data
                  :read-data reads-data
                  :show-wrt? show-wrt?
                  :editable-boards editable-boards})]))

(rum/defc virtualized-stream < rum/static
  [{:keys [items
           activities-read]
    :as derivatives}
   virtualized-props]
  (let [{:keys [height
                isScrolling
                onChildScroll
                scrollTop
                registerChild]} (js->clj virtualized-props :keywordize-keys true)
        row-renderer (fn [row-props]
                       (let [{:keys [key
                                     index
                                     isScrolling
                                     isVisible
                                     style] :as row-props} (js->clj row-props :keywordize-keys true)
                             entry (nth items index)
                             reads-data (get activities-read (:uuid entry))]
                         (rum/with-key
                           (wrapped-stream-item row-props (merge derivatives
                                                                 {:entry entry
                                                                  :reads-data reads-data}))
                           (str "stream-item-" key))))]
    (virtualized-list {:autoHeight true
                       :height height
                       :width (if (responsive/is-tablet-or-mobile?)
                                js/window.innerWidth
                                720)
                       :isScrolling isScrolling
                       :onScroll onChildScroll
                       :rowCount (count items)
                       :rowHeight foc-height
                       :rowRenderer row-renderer
                       :scrollTop scrollTop
                       :ref registerChild
                       :overscanRowCount 10
                       :style {:outline "none"}
                       })))

(rum/defcs paginated-stream  < rum/static
                               rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :filtered-posts)
                        (drv/drv :container-data)
                        (drv/drv :activities-read)
                        (drv/drv :comments-data)
                        (drv/drv :editable-boards)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local (.-scrollTop (.-scrollingElement js/document)) ::last-scroll)
                        (rum/local false ::has-next)
                        (rum/local nil ::bottom-loading)
                        (rum/local false ::show-all-caught-up-message)
                        ;; Mixins
                        mixins/first-render-mixin
                        (mixins/ap-seen-mixin "div.ap-seen-item-headline" ap-seen-mixin-cb)
                        section-mixins/container-nav-in

                        {:will-mount (fn [s]
                          (check-pagination s)
                          s)
                         :did-remount (fn [_ s]
                          (check-pagination s)
                         s)
                         :did-mount (fn [s]
                          (reset! (::last-scroll s) (.-scrollTop (.-body js/document)))
                          (reset! (::scroll-listener s)
                           (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                          (check-pagination s)
                          s)
                         :before-render (fn [s]
                          (let [container-data @(drv/get-ref s :container-data)]
                            (when (and (not (:loading-more container-data))
                                       @(::bottom-loading s))
                              (reset! (::bottom-loading s) false)
                              (reset! (::show-all-caught-up-message s) true)
                              (check-pagination s)))
                          s)
                         :will-unmount (fn [s]
                          (when @(::scroll-listener s)
                            (events/unlistenByKey @(::scroll-listener s)))
                          s)}
  [s]
  (let [org-data (drv/react s :org-data)
        comments-data (drv/react s :comments-data)
        editable-boards (drv/react s :editable-boards)
        container-data (drv/react s :container-data)
        items (drv/react s :filtered-posts)
        activities-read (drv/react s :activities-read)]
    [:div.paginated-stream.group
      [:div.paginated-stream-cards
        [:div.paginated-stream-cards-inner.group
         (window-scroller
          {}
          (partial virtualized-stream {:org-data org-data
                                       :comments-data comments-data
                                       :items items
                                       :activities-read activities-read
                                       :editable-boards editable-boards}))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            "Loading more posts..."])
        (when (and @(::show-all-caught-up-message s)
                   (responsive/is-mobile-size?))
          (all-caught-up))]]))
