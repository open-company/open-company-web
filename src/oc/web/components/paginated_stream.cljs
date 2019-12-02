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
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            cljsjs.react-virtualized))

(def virtualized-list (partial rutils/build js/ReactVirtualized.List))
(def window-scroller (partial rutils/build js/ReactVirtualized.WindowScroller))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 1)
(def scroll-card-threshold-collapsed 5)
(def collapsed-foc-height 56)
(def foc-height 188)
(def mobile-foc-height 172)

(defn- calc-card-height [mobile? foc-layout]
  (cond
    mobile?
    mobile-foc-height
    (= foc-layout dis/other-foc-layout)
    collapsed-foc-height
    :else
    foc-height))

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  [s e]
  (let [scroll-top (.. js/document -scrollingElement -scrollTop)
        direction (if (> @(::last-scroll s) scroll-top)
                    :up
                    (if (< @(::last-scroll s) scroll-top)
                      :down
                      :stale))
        max-scroll (- (.-scrollHeight (.-body js/document)) (.-innerHeight js/window))
        card-height (calc-card-height (responsive/is-mobile-size?) @(drv/get-ref s :foc-layout))
        scroll-threshold (if (= card-height collapsed-foc-height) scroll-card-threshold-collapsed scroll-card-threshold)
        current-board-slug (router/current-board-slug)]
    ;; scrolling down
    (when (and ;; not already loading more
               (not @(::bottom-loading s))
               ;; has a link to load more that can be used
               @(::has-next s)
               ;; scroll is moving down
               (= direction :down)
               ;; and the threshold point has been reached
               (>= scroll-top (- max-scroll (* scroll-threshold card-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (cond
        (= current-board-slug "all-posts")
        (activity-actions/all-posts-more @(::has-next s) :down)
        (= current-board-slug "follow-ups")
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
   [:div.virtualized-list-row
    {:style style}
    (stream-item {:activity-data entry
                  :comments-data comments-data
                  :read-data reads-data
                  :show-wrt? show-wrt?
                  :editable-boards editable-boards})]))

(rum/defc load-more < rum/static
  [{:keys [style]}]
  [:div.loading-updates.bottom-loading
    {:style style}
    "Loading more posts..."])

(rum/defc virtualized-stream < rum/static
  [{:keys [items
           activities-read
           foc-layout
           show-loading-more]
    :as derivatives}
   virtualized-props]
  (let [{:keys [height
                isScrolling
                onChildScroll
                scrollTop
                registerChild]} (js->clj virtualized-props :keywordize-keys true)
        is-mobile? (responsive/is-mobile-size?)
        key-prefix (if is-mobile? "mobile" foc-layout)
        row-renderer (fn [row-props]
                       (let [{:keys [key
                                     index
                                     isScrolling
                                     isVisible
                                     style] :as row-props} (js->clj row-props :keywordize-keys true)
                             loading-more? (and show-loading-more
                                                (= index (count items)))
                             entry (when-not loading-more? (nth items index))
                             reads-data (get activities-read (:uuid entry))]
                         (if loading-more?
                           (rum/with-key
                             (load-more row-props)
                             key)
                           (rum/with-key
                            (wrapped-stream-item row-props (merge derivatives
                                                                 {:entry entry
                                                                  :reads-data reads-data}))
                            (str key-prefix "-" key)))))]
    [:div.virtualized-list-container
      {:ref registerChild
       :key (str "virtualized-list-" key-prefix)}
      (virtualized-list {:autoHeight true
                         :height height
                         :width (if is-mobile?
                                  js/window.innerWidth
                                  720)
                         :isScrolling isScrolling
                         :onScroll onChildScroll
                         :rowCount (if show-loading-more (inc (count items)) (count items))
                         :rowHeight (fn [params]
                                      (let [{:keys [index]} (js->clj params :keywordize-keys true)]
                                        (if (and show-loading-more
                                                 (= index (count items)))
                                          60
                                          (calc-card-height is-mobile? foc-layout))))
                         :rowRenderer row-renderer
                         :scrollTop scrollTop
                         :overscanRowCount 20
                         :style {:outline "none"}})]))

(rum/defcs paginated-stream  < rum/static
                               rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :filtered-posts)
                        (drv/drv :container-data)
                        (drv/drv :activities-read)
                        (drv/drv :comments-data)
                        (drv/drv :editable-boards)
                        (drv/drv :foc-layout)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local (.. js/document -scrollingElement -scrollTop) ::last-scroll)
                        (rum/local false ::has-next)
                        (rum/local nil ::bottom-loading)
                        ;; Mixins
                        mixins/first-render-mixin
                        section-mixins/container-nav-in
                        section-mixins/window-focus-auto-loader

                        {:will-mount (fn [s]
                          (check-pagination s)
                          s)
                         :did-remount (fn [_ s]
                          (check-pagination s)
                         s)
                         :did-mount (fn [s]
                          (reset! (::last-scroll s) (.. js/document -scrollingElement -scrollTop))
                          (reset! (::scroll-listener s)
                           (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                          (check-pagination s)
                          s)
                         :before-render (fn [s]
                          (let [container-data @(drv/get-ref s :container-data)]
                            (when (and (not (:loading-more container-data))
                                       @(::bottom-loading s))
                              (reset! (::bottom-loading s) false)
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
        activities-read (drv/react s :activities-read)
        foc-layout (drv/react s :foc-layout)]
    [:div.paginated-stream.group
      [:div.paginated-stream-cards
        [:div.paginated-stream-cards-inner.group
         (window-scroller
          {}
          (partial virtualized-stream {:org-data org-data
                                       :comments-data comments-data
                                       :items items
                                       :activities-read activities-read
                                       :editable-boards editable-boards
                                       :foc-layout foc-layout
                                       :show-loading-more @(::bottom-loading s)}))]]]))
