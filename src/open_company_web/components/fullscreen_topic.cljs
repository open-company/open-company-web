(ns open-company-web.components.fullscreen-topic
  "
  Display a topic in full screen in either display, or transitioning (history nav.) modes.

  Handle back button, and history nav transitions.
  "
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.caches :as cache]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.topic-attribution :refer (topic-attribution)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.ui.icon :as i]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade Resize Scroll)]))

(defn- show-fullscreen-topic [owner]
  (utils/disable-scroll)
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defn- window-height []
  (gobj/get (gobj/get js/document "documentElement") "clientHeight"))

(defcomponent fullscreen-topic-internal [{:keys [topic
                                                 topic-data
                                                 currency
                                                 selected-metric
                                                 card-width
                                                 hide-history-navigation
                                                 hide-fullscreen-topic-cb
                                                 prev-rev
                                                 next-rev
                                                 is-actual] :as data} owner options]

  (init-state [_]
    {:wh (window-height)
     :resize-listener nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (let [win-height (window-height)
            resize-listener (events/listen js/window EventType/RESIZE #(om/set-state! owner :wh (window-height)))]
        (om/set-state! owner {:wh win-height
                              :resize-listener resize-listener}))))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [wh] :as state}]
    (let [fullscreen-width (responsive/fullscreen-topic-width card-width)
          chart-reduction (if (responsive/is-mobile-size?) 100 250)
          chart-width (- fullscreen-width chart-reduction)
          chart-opts {:show-title false
                      :show-revisions-navigation false
                      :switch-metric-cb (:switch-metric-cb options)
                      :chart-size {:width (- chart-width 20)}}
          chart-data {:section-data (if (= topic "finances") (utils/fix-finances topic-data) topic-data)
                      :section (keyword topic)
                      :currency currency
                      :actual-as-of (:updated-at topic-data)
                      :selected-metric selected-metric
                      :read-only true}
          topic-body (if (:placeholder topic-data) (:body-placeholder topic-data) (:body topic-data))]
      (dom/div {:class "fullscreen-topic-internal group"}
        ;; Close button
        (dom/div {:class "fullscreen-topic-mobile-navigation"
                  :on-click #(hide-fullscreen-topic-cb)}
          (dom/div {:class "fullscreen-topic-mobile-navigation-title"} (:title topic-data))
          (dom/button {:class "btn-reset close-fullscreen-topic-btn"
                       :on-click #(hide-fullscreen-topic-cb)}
            (dom/i {:class "fa fa-angle-left"})))
        (dom/div {:class "fullscreen-topic-top-box"}
          (dom/div {:class "fullscreen-topic-top-box-overflow"}
          ;; Image
          (when (:image-url topic-data)
            (dom/div {:class "topic-header-image"}
              (dom/img {:src (:image-url topic-data)})))
          
          ;; Title
          (when-not (responsive/is-mobile-size?)
            (dom/div {:class "topic-title-container group"}
              (dom/div {:class "topic-title left"} (:title topic-data))))

          ;; Headline
          (dom/div {:class "topic-headline"
                    :dangerouslySetInnerHTML (utils/emojify (:headline topic-data))})

          ;; Chart
          (when (or (= topic "growth") (= topic "finances"))
            (dom/div {:class "topic-growth-finances"}
              (cond
                (= topic "growth")
                (om/build topic-growth chart-data {:opts chart-opts})
                (= topic "finances")
                (om/build topic-finances chart-data {:opts chart-opts}))))

          ;; Body
          (dom/div {:class (str "topic-body" (when (:placeholder topic-data) " italic"))
                  :dangerouslySetInnerHTML (utils/emojify topic-body)})
          
          ;; Attribution
          (when (and (not hide-history-navigation)
                     (not (:placeholder topic-data)))
            (om/build topic-attribution (assoc data :close-cb #(hide-fullscreen-topic-cb)) {:opts options}))))))))

(defn- hide-fullscreen-topic [owner options & [force-fullscreen-dismiss]]
  (utils/enable-scroll)
  (let [fade-out (new Fade (sel1 :div.fullscreen-topic) 1 0 utils/oc-animation-duration)]
    (doto fade-out
      (.listen AnimationEventType/FINISH
        #((:close-overlay-cb options)))
      (.play))))

(defn- esc-listener [owner options e]
  (when (= (.-keyCode e) 27)
    (hide-fullscreen-topic owner options)))

(defn- revision-navigation [owner as-of]
  (let [actual-as-of (om/get-state owner :actual-as-of)
        section (name (om/get-props owner :section))]
    (if (= as-of actual-as-of)
      (.pushState js/history nil section (oc-urls/company-section (router/current-company-slug) (om/get-props owner :section)))
      (.pushState js/history nil (str section " revision " as-of) (oc-urls/company-section-revision (router/current-company-slug) (om/get-props owner :section) as-of)))))

(defn- animate-transition [owner]
  (let [cur-topic (om/get-ref owner "cur-topic")
        tr-topic (om/get-ref owner "tr-topic")
        current-state (om/get-state owner)
        appear-animation (Fade. tr-topic 0 1 utils/oc-animation-duration)
        cur-size (js/getComputedStyle cur-topic)
        tr-size (js/getComputedStyle tr-topic)
        fullscreen-topic-tran (om/get-ref owner "fullscreen-topic-transition")
        fullscreen-topic-size (js/getComputedStyle fullscreen-topic-tran)
        fullscreen-topic (om/get-ref owner "fullscreen-topic")
        scroll-top (.-scrollTop fullscreen-topic)]
    ; scroll to top
    (when (and fullscreen-topic-internal
               (pos? scroll-top))
      (.play (Scroll. fullscreen-topic
                      #js [0 scroll-top]
                      #js [0 0]
                      utils/oc-animation-duration)))
    ; resize the light box
    (.play (Resize. fullscreen-topic-tran
                    #js [(js/parseFloat (.-width cur-size)) (js/parseFloat (.-height fullscreen-topic-size))]
                    #js [(js/parseFloat (.-width cur-size)) (js/parseFloat (.-height tr-size))]
                    utils/oc-animation-duration))
    ; fade out current topic
    (.play (Fade. cur-topic 1 0 utils/oc-animation-duration))
    ; fade in the new topic
    (doto appear-animation
      (events/listen
        AnimationEventType/FINISH
        (fn []
          (revision-navigation owner (:transition-as-of current-state))
          (om/set-state! owner (merge current-state
                                    {:as-of (:transition-as-of current-state)
                                     :transition-as-of nil}))
          (utils/after 100 #(utils/remove-tooltips))))
      (.play))))

(defn- rev-click [owner e revision]
  (om/set-state! owner :transition-as-of (:updated-at revision)))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency card-width
                                        hide-history-navigation] :as data} owner options]

  (init-state [_]
    (let [company-data (dis/company-data)
          su-data      (dis/stakeholder-update-data)]
      (when (and company-data
                 (not (contains? company-data (keyword section)))
                 (not (contains? su-data (keyword section))))
        (router/redirect-404!)))
    (let [actual-as-of (:updated-at section-data)
          current-as-of (or (router/current-as-of) actual-as-of)]
      {:as-of current-as-of
       :transition-as-of nil
       :data-posted false
       :show-save-button false
       :last-selected-metric selected-metric
       :actual-as-of actual-as-of
       :source (:src (router/query-params))}))

  (did-mount [_]
    (om/set-state! owner :esc-listener-key
      (events/listen js/document EventType/KEYUP (partial esc-listener owner options)))
    (when (:animate data)
      (show-fullscreen-topic owner))
    (om/set-state! owner :selected-topic-click
      (events/listen (sel1 [:div.selected-topic]) EventType/CLICK #(when-not (utils/event-inside? % (sel1 [:div.fullscreen-topic-internal]))
                                                                      (hide-fullscreen-topic owner options)))))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (when (om/get-state owner :data-posted)
        (hide-fullscreen-topic owner options))
      (om/set-state! owner :data-posted false)
      (when-not (= (:updated-at (:section-data next-props)) (:updated-at section-data))
        (om/set-state! owner :as-of (:updated-at (:section-data next-props)))
        (om/set-state! owner :actual-as-of (:updated-at (:section-data next-props))))))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :esc-listener-key))
    (events/unlistenByKey (om/get-state owner :selected-topic-click)))

  (did-update [_ _ _]
    (when (om/get-state owner :transition-as-of)
      (animate-transition owner)))

  (render-state [_ {:keys [as-of transition-as-of actual-as-of source show-save-button data-posted last-selected-metric] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          revisions-list (get (slug @cache/revisions) section-kw)
          topic-data (utils/select-section-data section-data section-kw as-of)
          is-actual? (= as-of actual-as-of)
          fullscreen-topic-opts (merge options {:rev-click (partial rev-click owner)
                                                :switch-metric-cb #(om/set-state! owner :last-selected-metric %)})
          fullscreen-width (responsive/fullscreen-topic-width card-width)]
      ; preload previous revision
      (when (and prev-rev
                 (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className (str "fullscreen-topic" (when (:animate data) " initial"))
                    :ref "fullscreen-topic"}
        (dom/div #js {:className "fullscreen-topic-transition group"
                      :ref "fullscreen-topic-transition"
                      :style #js {:height (when-not transition-as-of "auto")}}
          (dom/div #js {:className "fullscreen-topic-as-of group"
                        :ref "cur-topic"
                        :key (str "cur-" as-of)
                        :style #js {:opacity 1}}
            (om/build fullscreen-topic-internal {:topic section
                                                 :topic-data topic-data
                                                 :selected-metric last-selected-metric
                                                 :currency currency
                                                 :card-width card-width
                                                 :is-actual is-actual?
                                                 :hide-history-navigation hide-history-navigation
                                                 :hide-fullscreen-topic-cb (partial hide-fullscreen-topic owner options)
                                                 :prev-rev prev-rev
                                                 :next-rev next-rev}
                                                {:opts fullscreen-topic-opts}))
          (when transition-as-of
            (dom/div #js {:className "fullscreen-topic-tr-as-of group"
                          :ref "tr-topic"
                          :key (str "tr-" transition-as-of)
                          :style #js {:opacity 0}}
              (let [tr-topic-data (utils/select-section-data section-data section-kw transition-as-of)
                    tr-prev-rev (utils/revision-prev revisions transition-as-of)
                    tr-next-rev (utils/revision-next revisions transition-as-of)]
                (om/build fullscreen-topic-internal {:topic-data tr-topic-data
                                                     :topic section
                                                     :selected-metric selected-metric
                                                     :currency currency
                                                     :card-width card-width
                                                     :hide-history-navigation hide-history-navigation
                                                     :hide-fullscreen-topic-cb (partial hide-fullscreen-topic owner options)
                                                     :prev-rev tr-prev-rev
                                                     :next-rev tr-next-rev}
                                                    {:opts fullscreen-topic-opts})))))))))