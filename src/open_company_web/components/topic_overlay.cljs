(ns open-company-web.components.topic-overlay
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.topic-overlay-edit :refer (topic-overlay-edit)]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade Resize Scroll)]
            [goog.style :refer (setStyle)]
            [goog.fx.Animation.EventType :as EventType]))

(defonce overlay-top-margin 100)

(defn pencil-click [options topic e focus-field]
  (.stopPropagation e)
  ; remove the overlay
  ((:edit-topic-cb options) focus-field))

(defn circle-remove-click [options e]
  (.stopPropagation e)
  ((:close-overlay-cb options)))

(defcomponent topic-overlay-internal [{:keys [read-only as-of topic topic-data prev-rev next-rev currency selected-metric] :as data} owner options]

  (render [_]
    (let [topic-kw (keyword topic)
          js-date-upat (utils/js-date (:updated-at topic-data))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat))
          subtitle-string (str (:name (:author topic-data)) " on " topic-updated-at)
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (min (- 650 126) (- win-height 126))]
      (dom/div {:class "topic-overlay-internal"
                :on-click #(.stopPropagation %)}
        (dom/button {:class "right mr2 mt2"
                     :on-click #(circle-remove-click options %)}
                    (i/icon :circle-remove))
        (when-not read-only
          (dom/button {:class "right mr2 mt2"
                       :on-click #(pencil-click options topic % "headline")}
                      (i/icon :pencil)))
        (dom/div {:class "topic-overlay-header"}
          (dom/div {:class "topic-overlay-title"} (:title topic-data))
          (dom/div {:class "topic-overlay-date"} subtitle-string))
        (dom/div #js {:className "topic-overlay-content"
                      :ref "topic-overlay-content"
                      :style #js {:maxHeight (str max-height "px")}}
          (dom/div {:class "topic-overlay-headline"
                    :dangerouslySetInnerHTML (utils/emojify (:headline topic-data))})
          (dom/div {}
            (when (= topic "finances")
              (om/build topic-finances {:section-data topic-data
                                        :section topic-kw
                                        :currency currency
                                        :selected-metric selected-metric
                                        :read-only true}
                                       {:opts {:show-title false
                                               :show-revisions-navigation false
                                               :chart-size {:height (if (utils/is-mobile) 200 290)
                                                            :width (if (utils/is-mobile) 320 480)}}}))
            (when (= topic "growth")
              (om/build topic-growth   {:section-data topic-data
                                        :section topic-kw
                                        :currency currency
                                        :selected-metric selected-metric
                                        :read-only true}
                                       {:opts {:show-title false
                                               :show-revisions-navigation false
                                               :switch-metric-cb (:switch-metric-cb options)
                                               :chart-size {:height (if (utils/is-mobile) 200 290)
                                                            :width (if (utils/is-mobile) 320 480)}}})))
          (dom/div {:class "topic-overlay-body"
                    :dangerouslySetInnerHTML (utils/emojify section-body)})
          (dom/div {:class "topic-overlay-navigation topic-navigation group"}
            (when prev-rev
              (dom/div {:class "arrow previous"}
                (dom/button {:on-click (fn [e]
                                    (let [bt (.-target e)]
                                      (set! (.-disabled bt) "disabled")
                                      (.stopPropagation e)
                                      ((:prev-cb options) (:updated-at prev-rev))
                                      (.setTimeout js/window
                                        #(set! (.-disabled bt) false) 1000)))} "< Previous")))
            (when next-rev
              (dom/div {:class "arrow next"}
                (dom/button {:on-click (fn [e]
                                        (let [bt (.-target e)]
                                          (set! (.-disabled bt) "disabled")
                                          (.stopPropagation e)
                                          ((:next-cb options) (:updated-at next-rev))
                                          (.setTimeout js/window
                                            #(set! (.-disabled bt) false) 1000)))} "Next >")))))
        (dom/div {:class "gradient"})))))

(defn animate-topic-overlay [owner show]
  (when-not (utils/is-test-env?)
    (when-let [topic-overlay (om/get-ref owner "topic-overlay")]
      (.play
        (new Fade
             topic-overlay
             (if show 0 1)
             (if show 1 0)
             utils/oc-animation-duration)))))

(defn close-overlay [owner options]
  (animate-topic-overlay owner false)
  (.setTimeout js/window
    #((:close-overlay-cb options)) utils/oc-animation-duration))

(defn start-editing [owner focus]
  (om/set-state! owner :field-focus focus)
  (om/set-state! owner :editing true))

(defn animate-transition [owner]
  (let [cur-topic (om/get-ref owner "cur-topic")
        tr-topic (om/get-ref owner "tr-topic")
        current-state (om/get-state owner)
        appear-animation (Fade. tr-topic 0 1 utils/oc-animation-duration)
        cur-size (js/getComputedStyle cur-topic)
        tr-size (js/getComputedStyle tr-topic)
        topic-overlay (om/get-ref owner "topic-overlay-transition")
        topic-overlay-size (js/getComputedStyle topic-overlay)
        topic-overlay-content (sel1 [:div.topic-overlay-content])
        scroll-top (.-scrollTop topic-overlay-content)]
    ; scroll to top
    (when (and topic-overlay-content
               (pos? scroll-top))
      (.play (Scroll. topic-overlay-content
                      #js [0 scroll-top]
                      #js [0 0]
                      utils/oc-animation-duration)))
    ; resize the light box
    (.play (Resize. topic-overlay
                    #js [(js/parseFloat (.-width cur-size)) (js/parseFloat (.-height topic-overlay-size))]
                    #js [(js/parseFloat (.-width cur-size)) (js/parseFloat (.-height tr-size))]
                    utils/oc-animation-duration))
    ; fade out current topic
    (.play (Fade. cur-topic 1 0 utils/oc-animation-duration))
    ; fade in the new topic
    (doto appear-animation
      (events/listen
        EventType/FINISH
        #(om/set-state! owner (merge current-state
                                    {:as-of (:transition-as-of current-state)
                                     :transition-as-of nil})))
      (.play))))

(defcomponent topic-overlay [{:keys [section section-data currency selected-metric force-editing] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :growth-metric-focus selected-metric
     :field-focus nil
     :transition-as-of nil
     :editing force-editing})

  (did-mount [_]
    (animate-topic-overlay owner true)
    ; prevent the window from scrolling
    (dommy/add-class! (sel1 [:body]) "no-scroll"))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner :as-of (:updated-at (:section-data next-props)))))

  (will-unmount [_]
    ; let the window scroll
    (dommy/remove-class! (sel1 [:body]) "no-scroll"))

  (did-update [_ _ _]
    (when (om/get-state owner :transition-as-of)
      (animate-transition owner)))

  (render-state [_ {:keys [as-of editing growth-metric-focus field-focus transition-as-of]}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          slug (keyword (router/current-company-slug))
          revisions-list (section-kw (@cache/revisions slug))
          topic-data (utils/select-section-data section-data section-kw as-of)
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          actual-as-of (:updated-at section-data)
          win-height (.-clientHeight (.-body js/document))
          content-max-height (if (< win-height utils/overlay-max-win-height)
                               (- win-height 20)
                               (- utils/overlay-max-win-height 20))
          needs-fix? (< win-height (+ content-max-height (* overlay-top-margin 2)))
          calc-top-margin (+ (/ (- content-max-height (min win-height utils/overlay-max-win-height)) 2) 10)
          top-margin (if needs-fix?
                       (max 10 calc-top-margin)
                       100)
          max-height (if needs-fix?
                       (- win-height 20)
                       650)]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className "topic-overlay"
                    :ref "topic-overlay"
                    :onClick #(when-not editing
                                (close-overlay owner options))
                    :key (name section)}
        (dom/div #js {:className "topic-overlay-box"
                      :ref "topic-overlay-box"
                      :style #js {:marginTop (str top-margin "px")
                                  :maxHeight (str max-height "px")}
                      :on-click #(.stopPropagation %)}
          (if-not editing
            (dom/div #js {:className "topic-overlay-transition group"
                          :ref "topic-overlay-transition"}
              (dom/div #js {:className "topic-overlay-as-of group"
                            :ref "cur-topic"
                            :key (str "cur-" as-of)
                            :style #js {:opacity 1}}
                (om/build topic-overlay-internal {:topic-data topic-data
                                                  :as-of as-of
                                                  :topic section
                                                  :currency currency
                                                  :selected-metric growth-metric-focus
                                                  :read-only (or (:read-only section-data) (not= as-of (:updated-at section-data)))
                                                  :prev-rev prev-rev
                                                  :next-rev next-rev}
                                                 {:opts {:close-overlay-cb #(close-overlay owner options)
                                                         :edit-topic-cb #(start-editing owner %)
                                                         :switch-metric-cb #(om/set-state! owner :growth-metric-focus %)
                                                         :prev-cb #(om/set-state! owner :transition-as-of %)
                                                         :next-cb #(om/set-state! owner :transition-as-of %)}}))
              (when transition-as-of
                (dom/div #js {:className "topic-overlay-tr-as-of group"
                              :ref "tr-topic"
                              :key (str "tr-" transition-as-of)
                              :style #js {:opacity 0}}
                  (let [tr-topic-data (utils/select-section-data section-data section-kw transition-as-of)
                        tr-prev-rev (utils/revision-prev revisions transition-as-of)
                        tr-next-rev (utils/revision-next revisions transition-as-of)]
                    (om/build topic-overlay-internal {:topic-data tr-topic-data
                                                      :as-of transition-as-of
                                                      :topic section
                                                      :currency currency
                                                      :selected-metric growth-metric-focus
                                                      :read-only true
                                                      :prev-rev tr-prev-rev
                                                      :next-rev tr-next-rev}
                                                     {:opts {:close-overlay-cb #()
                                                             :switch-metric-cb #()
                                                             :prev-cb #()
                                                             :next-cb #()}})))))
            (om/build topic-overlay-edit {:topic-data section-data
                                          :topic section
                                          :focus field-focus
                                          :growth-metric-focus growth-metric-focus
                                          :currency currency}
                                         {:opts {:dismiss-editing-cb #(om/set-state! owner :editing false)}})))))))