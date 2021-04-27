(ns oc.web.components.paginated-stream
  (:require [rum.core :as rum]
            [oops.core :refer (oget)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.rum :as rutils]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.seen :as seen-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.stream-item :refer (stream-item)]
            [oc.web.components.ui.activity-refresh-button :refer (activity-refresh-button)]
            [oc.web.actions.contributions :as contributions-actions]
            [oc.web.components.ui.all-caught-up :refer (caught-up-line)]
            [oc.web.components.stream-collapsed-item :refer (stream-collapsed-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            ["react-virtualized" :as react-virtualized :refer (Grid WindowScroller CellMeasurer CellMeasurerCache)]))

(def virtualized-grid (partial rutils/build Grid))
(def window-scroller (partial rutils/build WindowScroller))
(def cell-measurer (partial rutils/build CellMeasurer))
(def RVCellMeasurerCache CellMeasurerCache)

;; 800px from the end of the current rendered results as point to add more items in the batch
(def collapsed-foc-height 56)
(def foc-height 204)
(def mobile-foc-height 190)
(def mobile-collapsed-foc-height 131)

(defn- calc-card-height [mobile? collapsed?]
  (cond (and mobile? collapsed?)
        mobile-collapsed-foc-height
        mobile?
        mobile-foc-height
        collapsed?
        collapsed-foc-height
        :else
        foc-height))

(rum/defc wrapped-stream-item < rum/static
  [{:keys [item
           read-data
           org-data
           container-data
           editable-boards
           foc-layout
           is-mobile
           clear-cell-measure-cb
           current-user-data
           row-index
           foc-menu-open] :as props}]
  (let [member? (:member? org-data)
        replies? (= (:container-slug container-data) :replies)
        show-wrt? member?
        show-new-comments? replies?
        collapsed-item? replies?]
   [:div.virtualized-list-row
     {:class (utils/class-set {:collapsed-item collapsed-item?
                               :open-item (:open-item item)
                               :close-item (:close-item item)
                               :foc-menu-open foc-menu-open})}
     (cond
       collapsed-item?
       (stream-collapsed-item {:activity-data     item
                               :member?           member?
                               :read-data         read-data
                               :show-wrt?         show-wrt?
                               :editable-boards   editable-boards
                               :current-user-data current-user-data
                               :clear-cell-measure-cb clear-cell-measure-cb
                               :replies?           replies?
                               :foc-menu-open      foc-menu-open
                               :premium?          (:premium? org-data)})
       :else
       (stream-item {:activity-data item
                     :read-data          read-data
                     :show-wrt?          show-wrt?
                     :foc-menu-open      foc-menu-open
                     :show-new-comments? show-new-comments?
                     :replies?           replies?
                     :member?            member?
                     :clear-cell-measure-cb clear-cell-measure-cb
                     :editable-boards    editable-boards
                     :container-slug     (:container-slug container-data)
                     :foc-board          (not (activity-utils/board? container-data))
                     :current-user-data  current-user-data
                     :boards-count       (count (filter #(not= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))
                     :premium?           (:premium? org-data)}))]))

(rum/defc load-more < rum/static
  [{:keys [item]}]
  [:div.loading-updates.bottom-loading
    (:message item)])

(rum/defc closing-item < rum/static
  [{:keys [item]}]
  [:div.closing-item
    (:message item)])

(rum/defc separator-item < rum/static
  [{:keys [foc-layout item after-pins] :as props}]
  [:div.virtualized-list-separator
    {:class (utils/class-set {:expanded-list (not= foc-layout dis/other-foc-layout)
                              :after-pins after-pins})}
    (:label item)])

(rum/defc caught-up-wrapper < rum/static
  [{:keys [item]}]
  [:div.caught-up-wrapper
    (caught-up-line item)])

(rum/defcs list-item < rum/static
  [s {:keys [items
             activities-read
             foc-layout
             is-mobile?
             container-data
             current-user-data
             clear-cell-measure-cb
             row-index
             foc-menu-open]
    :as derivatives}
   {:keys [rowIndex key style isScrolling] :as row-props}
   props]
  (let [{:keys [registerChild measure] :as clj-props} (js->clj props :keywordize-keys true)
        item (get items rowIndex)
        entry? (activity-utils/entry? item)
        read-data (when entry?
                    (get activities-read (:uuid item)))
        replies? (= (:container-slug container-data) :replies)]
    [:div.virtualized-list-item
      {:key (str (name (:resource-type item)) "-" key "-" (if entry?
                                                            (cond replies?
                                                                  (str (:uuid item) "-" (:last-activity-at item) "-" (count (:replies-data item)))
                                                                  :else
                                                                  (clojure.string/join "-" (select-keys item [:uuid :created-at :published-at :updated-at])))
                                                            (str (:uuid item)
                                                                 (:sort-value item)
                                                                 (:last-activity-at item))))
       :ref registerChild
       :style style}
      (cond
        (activity-utils/resource-type? item :caught-up)
        (caught-up-wrapper {:item item})
        (activity-utils/resource-type? item :closing-item)
        (closing-item {:item item})
        (activity-utils/resource-type? item :loading-more)
        (load-more {:item item})
        (activity-utils/resource-type? item :separator)
        (separator-item {:item item :foc-layout foc-layout :after-pins (:after-pins item)})
        ; isScrolling
        ; [:div.virtualized-list-placeholder]
        :else
        (wrapped-stream-item (merge derivatives {:item item
                                                 :read-data read-data
                                                 :is-mobile is-mobile?
                                                 :foc-layout foc-layout
                                                 :container-data container-data
                                                 :clear-cell-measure-cb clear-cell-measure-cb
                                                 :row-index row-index
                                                 :foc-menu-open (boolean (= foc-menu-open (:uuid item)))})))]))

 (defn- unique-row-string [item]
  (let [entry? (activity-utils/entry? item)
        static-part (str (name (:resource-type item)) "-" (:uuid item))
        variable-part (cond entry?
                            (or (:updated-at item) (:created-at item))
                            :else
                            (:last-activity-at item))]
    (str static-part "-" variable-part)))

(defn- clear-cell-measure

  ([rum-state row-index] (clear-cell-measure rum-state row-index 0))

  ([rum-state row-index column-index]
   (when-let [cache @(::cache rum-state)]
     (.clear cache row-index column-index)
     (reset! (::force-re-render rum-state) (utils/activity-uuid)))))

(defn- clear-changed-cells-cache [s next-row-keys]
  (let [props (-> s :rum/args first)
        items (:items props)
        resource-types (::row-keys s)]
    (doseq [idx (range (count items))
            :let [old-resource-type (get @resource-types idx)
                  new-resource-type (get next-row-keys idx)]
            :when (not= old-resource-type new-resource-type)]
      (clear-cell-measure s idx 0))
    (reset! resource-types next-row-keys)))

(rum/defcs virtualized-stream < rum/static
                                (seen-mixins/container-nav-mixin)
                                (rum/local nil ::row-keys)
                                (rum/local nil ::cache)
                                (rum/local nil ::force-re-render)
                                mixins/mounted-flag
                                {:will-mount (fn [s]
                                  (let [props (-> s :rum/args s first)
                                        replies? (-> props :container-data :container-slug (= :replies))
                                        next-row-keys (mapv unique-row-string (:items props))]
                                    (reset! (::force-re-render s) (utils/activity-uuid))
                                    (reset! (::cache s)
                                     (RVCellMeasurerCache.
                                      (clj->js {:defaultHeight (calc-card-height (:is-mobile? props) (or (:foc-layout props) replies?))
                                                :minHeight 1
                                                :fixedWidth true})))
                                    (reset! (::row-keys s) next-row-keys))
                                  s)
                                 :did-remount (fn [_ s]
                                  (let [props (-> s :rum/args first)
                                        next-row-keys (mapv unique-row-string (:items props))]
                                    (when-not (= @(::row-keys s) next-row-keys)
                                      (clear-changed-cells-cache s next-row-keys)))
                                  s)}
  [s {:keys [items
             activities-read
             foc-layout
             is-mobile?
             container-data
             current-user-data
             foc-menu-open]
      :as derivatives}
     virtualized-props]
  (let [{:keys [height
                isScrolling
                onChildScroll
                scrollTop
                registerChild]} (js->clj virtualized-props :keywordize-keys true)
        key-prefix (if is-mobile? "mobile" foc-layout)
        cell-measurer-renderer (fn [{:keys [cache]} props]
                                 (let [{:keys [rowIndex key parent columnIndex] :as row-props} (js->clj props :keywordize-keys true)]
                                   (let [derived-props (assoc derivatives :clear-cell-measure-cb #(clear-cell-measure s rowIndex columnIndex) :row-index rowIndex)]
                                     (cell-measurer (clj->js {:cache cache
                                                              :columnIndex columnIndex
                                                              :rowIndex rowIndex
                                                              :index rowIndex
                                                              :key key
                                                              :parent parent})
                                      (partial list-item derived-props row-props)))))
        width (if is-mobile?
                js/window.innerWidth
                620)
        replies? (= (:container-slug container-data) :replies)]
    [:div.virtualized-list-container
      {:ref registerChild
       :data-render-key @(::force-re-render s)
       :key (str "virtualized-list-" key-prefix)
       :class (when replies? "collapsed")}
      (virtualized-grid {:autoHeight true
                         :ref :virtualized-list-comp
                         :deferredMeasurementCache @(::cache s)
                         :estimatedRowSize (if is-mobile? mobile-foc-height foc-height)
                         :height height
                         :width width
                         :columnWidth width
                         :isScrolling isScrolling
                         :onScroll onChildScroll
                         :rowCount (count items)
                         :rowHeight (oget @(::cache s) "rowHeight")
                         :cellRenderer (partial cell-measurer-renderer {:cache @(::cache s)})
                         :scrollTop scrollTop
                         ;; :overscanRowCount 20
                         :columnCount 1
                         :style {:outline "none"}})]))

(defonce last-scroll-top (atom 0))

; (def scroll-card-threshold 1)
; (def scroll-card-threshold-collapsed 5)

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  ([s] (did-scroll s (responsive/is-mobile-size?) nil))
  ([s mobile?] (did-scroll s mobile? nil))
  ([s mobile? e]
  (let [scroll-top (or (oget js/document "scrollingElement.?scrollTop") (oget js/window "pageYOffset"))
        direction (if (> @last-scroll-top scroll-top)
                    :up
                    (if (< @last-scroll-top scroll-top)
                      :down
                      :stale))
        win-height (dom-utils/viewport-height)
        max-scroll (- (.-scrollHeight (.-scrollingElement js/document)) win-height)

        ;; Calculate the Point of No Return based on the card height
        ; is-mobile? (if (nil? mobile?) (responsive/is-mobile-size?) mobile?)
        ; card-height (calc-card-height is-mobile? @(drv/get-ref s :foc-layout))
        ; scroll-threshold* (if (= card-height collapsed-foc-height) scroll-card-threshold-collapsed scroll-card-threshold)
        ; scroll-threshold (* scroll-threshold* card-height)
        ; pnr (- max-scroll scroll-threshold)

        ;; Let's use the viewport height as point of no return
        pnr (- max-scroll win-height)
        current-board-slug @(drv/get-ref s :board-slug)
        current-contributions-id @(drv/get-ref s :contributions-id)
        board-kw (keyword current-board-slug)]
    ;; scrolling down
    (when (and ;; not already loading more
               (not @(::bottom-loading s))
               ;; has a link to load more that can be used
               @(::has-next s)
               ;; scroll is moving down
               (or (= direction :down)
                   (= direction :stale))
               ;; and the threshold point has been reached
               (>= scroll-top pnr))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (cond
        (= board-kw :replies)
        (activity-actions/replies-more @(::has-next s) :down)
        (= board-kw :following)
        (activity-actions/following-more @(::has-next s) :down)
        (seq current-contributions-id)
        (contributions-actions/contributions-more @(::has-next s) :down)
        (= board-kw :inbox)
        (activity-actions/inbox-more @(::has-next s) :down)
        (= board-kw :all-posts)
        (activity-actions/all-posts-more @(::has-next s) :down)
        (= board-kw :bookmarks)
        (activity-actions/bookmarks-more @(::has-next s) :down)
        (= board-kw :unfollowing)
        (activity-actions/unfollowing-more @(::has-next s) :down)
        (not (dis/is-container? current-board-slug))
        (section-actions/section-more @(::has-next s) :down)))
    ;; Save the last scrollTop value
    (reset! last-scroll-top (max 0 scroll-top)))))

(defn check-pagination [s]
  (let [container-data @(drv/get-ref s :container-data)
        next-link (utils/link-for (:links container-data) "next")]
    (reset! (::has-next s) next-link)
    (did-scroll s)))

(rum/defcs paginated-stream  <
                        rum/static
                        rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :items-to-render)
                        (drv/drv :container-data)
                        (drv/drv :activities-read)
                        (drv/drv :editable-boards)
                        (drv/drv :current-user-data)
                        (drv/drv :board-slug)
                        (drv/drv :contributions-id)
                        (drv/drv :foc-menu-open)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local false ::has-next)
                        (rum/local nil ::bottom-loading)
                        ;; Mixins
                        mixins/first-render-mixin
                        section-mixins/container-nav-in
                        ; section-mixins/window-focus-auto-loader
                        ;; (section-mixins/load-entry-comments (fn [s]
                        ;;   (let [container-data @(drv/get-ref s :container-data)]
                        ;;     (when (= (:container-slug container-data) :replies)
                        ;;       container-data))))
                        {:will-mount (fn [s]
                          (reset! last-scroll-top (.. js/document -scrollingElement -scrollTop))
                          (reset! (::scroll-listener s)
                           (events/listen js/window EventType/SCROLL (partial did-scroll s (responsive/is-mobile-size?))))
                          s)
                         :did-mount (fn [s]
                          (reset! last-scroll-top (.. js/document -scrollingElement -scrollTop))
                          (check-pagination s)
                          s)
                         :did-remount (fn [_ s]
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
                            (events/unlistenByKey @(::scroll-listener s))
                            (reset! (::scroll-listener s) nil))
                          s)}
  [s]
  (let [org-data (drv/react s :org-data)
        _board-slug (drv/react s :board-slug)
        _contributions-id (drv/react s :contributions-id)
        editable-boards (drv/react s :editable-boards)
        container-data (drv/react s :container-data)
        items (drv/react s :items-to-render)
        activities-read (drv/react s :activities-read)
        current-user-data (drv/react s :current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        replies? (= (:container-slug container-data) :replies)
        foc-menu-open (drv/react s :foc-menu-open)]
    [:div.paginated-stream.group
      [:div.paginated-stream-cards
        [:div.paginated-stream-cards-inner.group
          (when replies?
            (rum/with-key
             (activity-refresh-button {:items-to-render items})
             (str "activity-refresh-button-" (:last-seen-at container-data))))
          (window-scroller
           {}
           (partial virtualized-stream {:org-data org-data
                                        :items items
                                        :container-data container-data
                                        :is-mobile? is-mobile?
                                        :current-user-data current-user-data
                                        :activities-read activities-read
                                        :editable-boards editable-boards
                                        :foc-menu-open foc-menu-open}))]]]));)]]]))
