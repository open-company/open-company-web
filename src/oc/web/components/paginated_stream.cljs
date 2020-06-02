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
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.stream-item :refer (stream-item)]
            [oc.web.components.threads-list :refer (threads-list)]
            [oc.web.actions.contributions :as contributions-actions]
            [oc.web.components.ui.all-caught-up :refer (caught-up-line)]
            [oc.web.components.stream-collapsed-item :refer (stream-collapsed-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            cljsjs.react-virtualized))

(def virtualized-list (partial rutils/build js/ReactVirtualized.List))
(def window-scroller (partial rutils/build js/ReactVirtualized.WindowScroller))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 1)
(def scroll-card-threshold-collapsed 5)
(def collapsed-foc-height 56)
(def carrot-close-height 60)
(def foc-height 204)
(def mobile-foc-height 166)
(def foc-separators-height 58)

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
        max-scroll (- (.-scrollHeight (.-scrollingElement js/document)) (.-innerHeight js/window))
        card-height (calc-card-height (responsive/is-mobile-size?) @(drv/get-ref s :foc-layout))
        scroll-threshold (if (= card-height collapsed-foc-height) scroll-card-threshold-collapsed scroll-card-threshold)
        current-board-slug (router/current-board-slug)]
    ;; scrolling down
    (when (and ;; not already loading more
               (not @(::bottom-loading s))
               ;; has a link to load more that can be used
               @(::has-next s)
               ;; scroll is moving down
               (or (= direction :down)
                   (= direction :stale))
               ;; and the threshold point has been reached
               (>= scroll-top (- max-scroll (* scroll-threshold card-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (cond
        (seq (router/current-contributions-id))
        (contributions-actions/contributions-more @(::has-next s) :down)
        (= current-board-slug "inbox")
        (activity-actions/inbox-more @(::has-next s) :down)
        (= current-board-slug "threads")
        (activity-actions/threads-more @(::has-next s) :down)
        (= current-board-slug "all-posts")
        (activity-actions/all-posts-more @(::has-next s) :down)
        (= (router/current-board-slug) "bookmarks")
        (activity-actions/bookmarks-more @(::has-next s) :down)
        (= (router/current-board-slug) "following")
        (activity-actions/following-more @(::has-next s) :down)
        (= (router/current-board-slug) "unfollowing")
        (activity-actions/unfollowing-more @(::has-next s) :down)
        :else
        (section-actions/section-more @(::has-next s) :down)))
    ;; Save the last scrollTop value
    (when (not= scroll-top @(::last-scroll s))
      (reset! (::last-scroll s) scroll-top))))

(defn check-pagination [s]
  (let [container-data @(drv/get-ref s :container-data)
        next-link (utils/link-for (:links container-data) "next")]
    (reset! (::has-next s) next-link)
    (did-scroll s nil)))

(rum/defc wrapped-stream-item < rum/static
  [{:keys [style] :as row-props}
   {:keys [entry
           reads-data
           org-data
           comments-data
           editable-boards
           foc-layout
           is-mobile] :as props}]
  (let [member? (jwt/user-is-part-of-the-team (:team-id org-data))
        publisher? (activity-utils/is-publisher? entry)
        show-wrt? (and member?
                       publisher?
                       (activity-utils/is-published? entry))
        collapsed-item? (and (= foc-layout dis/other-foc-layout)
                             (not is-mobile))]
   [:div.virtualized-list-row
     {:class (utils/class-set {:collapsed-item collapsed-item?
                               :open-item (:open-item entry)
                               :close-item (:close-item entry)})
      :style style}
     (if collapsed-item?
       (stream-collapsed-item {:activity-data entry
                               :comments-data comments-data
                               :read-data reads-data
                               :show-wrt? show-wrt?
                               :member? member?
                               :editable-boards editable-boards})
       (stream-item {:activity-data entry
                     :comments-data comments-data
                     :read-data reads-data
                     :show-wrt? show-wrt?
                     :member? member?
                     :publisher? publisher?
                     :editable-boards editable-boards
                     :boards-count (count (filter #(not= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))}))]))

(rum/defc load-more < rum/static
  [{:keys [style]}]
  [:div.loading-updates.bottom-loading
    {:style style}
    "Loading more posts..."])

(rum/defc carrot-close < rum/static
  [{:keys [style]}]
  [:div.carrot-close
    {:style style}
    "🤠 You've reached the end, partner"])

(rum/defc separator-item < rum/static
  [{:keys [style foc-layout] :as row-props} {:keys [label] :as props}]
  [:div.virtualized-list-separator
    {:style style
     :class (when (= foc-layout dis/default-foc-layout) "expanded-list")}
    label])

(rum/defc caught-up-wrapper < rum/static
  [{:keys [style item]}]
  [:div.caught-up-wrapper
    {:style style}
    (caught-up-line item)])

(defn- get-item [items idx show-loading-more show-carrot-close]
  (let [loading-more? (and show-loading-more
                           (= idx (count items)))
        carrot-close? (and (not loading-more?)
                           show-carrot-close
                           (= idx (count items)))
        item (when (and (not loading-more?)
                        (not carrot-close?)
                        (<= 0 idx (dec (count items))))
               (nth items idx))
        separator-item? (and (not loading-more?)
                             (not carrot-close?)
                             (= (:content-type item) :separator))]
    (cond
     loading-more?
     {:content-type :loading-more}
     carrot-close?
     {:content-type :carrot-close}
     :else
     item)))

(rum/defcs virtualized-stream < rum/static
                                rum/reactive
                                (rum/local nil ::last-force-list-update)
                                (rum/local false ::mounted)
                               {:did-mount (fn [s]
                                 (reset! (::mounted s) true)
                                 s)
                                :did-remount (fn [o s]
                                 (when @(::mounted s)
                                   (when-let [force-list-update (-> s :rum/args first :force-list-update)]
                                     (when-not (= @(::last-force-list-update s) force-list-update)
                                       (reset! (::last-force-list-update s) force-list-update)
                                       (.recomputeRowHeights (rum/ref s :virtualized-list-comp)))))
                                 s)
                                :will-unmount (fn [s]
                                 (reset! (::mounted s) false)
                                 s)}
  [s {:keys [items
             activities-read
             foc-layout
             show-loading-more
             show-carrot-close
             is-mobile?
             force-list-update]
      :as derivatives}
     virtualized-props]
  (let [{:keys [height
                isScrolling
                onChildScroll
                scrollTop
                registerChild]} (js->clj virtualized-props :keywordize-keys true)
        key-prefix (if is-mobile? "mobile" foc-layout)
        rowHeight (fn [row-props]
                    (let [{:keys [index]} (js->clj row-props :keywordize-keys true)
                          item (get-item items index show-loading-more show-carrot-close)]
                      (case (:content-type item)
                        :caught-up
                        64
                        :separator
                        (if (= foc-layout dis/other-foc-layout)
                          foc-separators-height
                          (- foc-separators-height 8))
                        :loading-more
                        (if is-mobile? 44 60)
                        :carrot-close
                        carrot-close-height
                        ; else
                        (calc-card-height is-mobile? foc-layout))))
        row-renderer (fn [row-props]
                       (let [{:keys [key
                                     index
                                     isScrolling
                                     isVisible
                                     style] :as row-props} (js->clj row-props :keywordize-keys true)
                             item (get-item items index show-loading-more show-carrot-close)
                             reads-data (when (= (:content-type item) :entry)
                                          (get activities-read (:uuid item)))
                             row-key (str key-prefix "-" key)
                             next-item (get-item items (inc index) show-loading-more show-carrot-close)
                             prev-item (get-item items (dec index) show-loading-more show-carrot-close)]
                         (case (:content-type item)
                           :caught-up
                           (rum/with-key (caught-up-wrapper {:item item :style style}) (str "caught-up-" (:last-activity-at item)))
                           :carrot-close
                           (rum/with-key (carrot-close row-props) (str "carrot-close-" row-key))
                           :loading-more
                           (rum/with-key (load-more row-props) (str "loading-more-" row-key))
                           :separator
                           (rum/with-key (separator-item (assoc row-props :foc-layout foc-layout) item) (str "separator-item-" row-key))
                           ; else
                           (rum/with-key
                            (wrapped-stream-item row-props (merge derivatives
                                                                 {:entry item
                                                                  :reads-data reads-data
                                                                  :foc-layout foc-layout
                                                                  :is-mobile is-mobile?}))
                            row-key))))]
    [:div.virtualized-list-container
      {:ref registerChild
       :key (str "virtualized-list-" key-prefix)}
      (virtualized-list {:autoHeight true
                         :ref :virtualized-list-comp
                         :height height
                         :width (if is-mobile?
                                  js/window.innerWidth
                                  620)
                         :isScrolling isScrolling
                         :onScroll onChildScroll
                         :rowCount (if (or show-loading-more
                                           show-carrot-close)
                                     (inc (count items))
                                     (count items))
                         :rowHeight rowHeight
                         :rowRenderer row-renderer
                         :scrollTop scrollTop
                         :overscanRowCount 20
                         :style {:outline "none"}})]))

(rum/defcs paginated-stream  < rum/static
                               rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :items-to-render)
                        (drv/drv :container-data)
                        (drv/drv :activities-read)
                        (drv/drv :comments-data)
                        (drv/drv :editable-boards)
                        (drv/drv :foc-layout)
                        (drv/drv :current-user-data)
                        (drv/drv :force-list-update)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local (.. js/document -scrollingElement -scrollTop) ::last-scroll)
                        (rum/local false ::has-next)
                        (rum/local nil ::bottom-loading)
                        (rum/local nil ::last-foc-layout)
                        ;; Mixins
                        mixins/first-render-mixin
                        section-mixins/container-nav-in
                        ; section-mixins/window-focus-auto-loader

                        {:will-mount (fn [s]
                          (reset! (::last-foc-layout s) @(drv/get-ref s :foc-layout))
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
                         :after-render (fn [s]
                          (let [foc-layout @(drv/get-ref s :foc-layout)]
                            (when (not= @(::last-foc-layout s) foc-layout)
                              (reset! (::last-foc-layout s) foc-layout)
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
        items (drv/react s :items-to-render)
        activities-read (drv/react s :activities-read)
        foc-layout (drv/react s :foc-layout)
        current-user-data (drv/react s :current-user-data)
        force-list-update (drv/react s :force-list-update)
        viewport-height (dom-utils/viewport-height)
        is-mobile? (responsive/is-mobile-size?)
        card-height (calc-card-height is-mobile? foc-layout)
        show-carrot-close (> (* (count items) card-height) viewport-height)
        member? (jwt/user-is-part-of-the-team (:team-id org-data))]
    [:div.paginated-stream.group
      [:div.paginated-stream-cards
        [:div.paginated-stream-cards-inner.group
         (if (:no-virtualized-steam container-data)
           (threads-list {:items-to-render items
                          :org-data org-data
                          :current-user-data current-user-data
                          :loading-more @(::bottom-loading s)})
           (window-scroller
            {}
            (partial virtualized-stream {:org-data org-data
                                         :comments-data comments-data
                                         :items items
                                         :is-mobile? is-mobile?
                                         :force-list-update force-list-update
                                         :activities-read activities-read
                                         :editable-boards editable-boards
                                         :foc-layout foc-layout
                                         :show-loading-more @(::bottom-loading s)
                                         :show-carrot-close (and (not @(::bottom-loading s))
                                                                 (not @(::has-next s))
                                                                 (> (count items) 3))})))]]]))
