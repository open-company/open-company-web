(ns open-company-web.components.fullscreen-topic
  (:require [cljs.core.async :refer (chan put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.caches :as cache]
            [open-company-web.router :as router]
            [open-company-web.urls :as oc-urls]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.fullscreen-topic-edit :refer (fullscreen-topic-edit)]
            [open-company-web.components.ui.icon :refer (icon)]
            [open-company-web.components.ui.small-loading :refer (small-loading)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.small-loading :as loading]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade Resize Scroll)]
            [open-company-web.lib.oc-colors :as oc-colors]))

(defn show-fullscreen-topic [owner]
  (utils/disable-scroll)
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defcomponent fullscreen-topic-internal [{:keys [topic topic-data currency selected-metric card-width hide-history-navigation can-edit is-actual] :as data} owner options]
  (render [_]
    (let [fullscreen-width (responsive/fullscreen-topic-width card-width)
          chart-opts {:show-title false
                      :show-revisions-navigation false
                      :switch-metric-cb (:switch-metric-cb options)
                      :chart-size {:width  (- fullscreen-width 100)
                                   :height (if (responsive/is-mobile) 174 295)}}
          chart-data {:section-data topic-data
                      :section (keyword topic)
                      :currency currency
                      :actual-as-of (:updated-at topic-data)
                      :selected-metric selected-metric
                      :read-only true}]
      (dom/div {:class "fullscreen-topic-internal group"
                :style #js {:width (str (- fullscreen-width 20) "px")}}
        (dom/div {:class "fullscreen-topic-top-box"}
          (when (:image-url topic-data)
            (dom/div {:class "topic-header-image"}
              (dom/img {:src (:image-url topic-data)})))
          (dom/div {:class "group"}
            (dom/div {:class "topic-title left"} (:title topic-data))
            (when (and can-edit
                       is-actual)
              (dom/div {:class "edit-button"
                        :on-click #((:start-editing options))}
                (dom/i {:class "fa fa-pencil"}))))
          (dom/div {:class "topic-headline"
                    :dangerouslySetInnerHTML (utils/emojify (:headline topic-data))})
          (when (or (= topic "growth") (= topic "finances"))
            (dom/div {:class "topic-growth-finances"}
              (cond
                (= topic "growth")
                (om/build topic-growth chart-data {:opts chart-opts})
                (= topic "finances")
                (om/build topic-finances chart-data {:opts chart-opts}))
              (when-not (clojure.string/blank? (:snippet topic-data))
                (dom/div {:class "separator"}))))
          (dom/div {:class "topic-snippet"
                    :dangerouslySetInnerHTML (utils/emojify (:snippet topic-data))}))
        (dom/div {:class "topic-body"
                  :dangerouslySetInnerHTML (utils/emojify (utils/get-topic-body topic-data topic))})
        (when (:author topic-data)
          (dom/div {:class "topic-attribution"}
            (str "- " (:name (:author topic-data)) " / " (utils/date-string (js/Date. (:updated-at topic-data)) true))))
        (when-not hide-history-navigation
          (dom/div {:class "topic-revisions group"}
            (when (:prev-rev data)
              (dom/button {:class "prev"
                           :on-click #((:rev-nav options) (:updated-at (:prev-rev data)))}
                (if (:is-actual data) "VIEW EARLIER UPDATE" "EARLIER")))
            (when (:next-rev data)
              (dom/button {:class "next"
                           :on-click #((:rev-nav options) (:updated-at (:next-rev data)))}
                "LATER"))))))))

(defn start-editing [owner options]
  (let [editing (om/get-state owner :editing)
        section (om/get-props owner :section)]
    (.pushState js/history nil (str "Edit " (name section)) (oc-urls/company-section-edit (router/current-company-slug) (name section)))
    (when-not editing
      ((:topic-navigation options) false)
      (om/set-state! owner :editing true))))

(defn exit-editing [owner options]
  (let [editing (om/get-state owner :editing)
        section (om/get-props owner :section)]
    (.pushState js/history nil (name section) (oc-urls/company-section (router/current-company-slug) (name section)))
    (when editing
      ((:topic-navigation options) true)
      (om/set-state! owner :editing false))))

(defn hide-fullscreen-topic [owner options & [force-fullscreen-dismiss]]
  ; if it's in editing mode
  (let [editing (om/get-state owner :editing)]
    (exit-editing owner options)
    (when (or (not editing)
              force-fullscreen-dismiss
              (om/get-props owner :fullscreen-force-edit))
      (utils/enable-scroll)
      (let [fade-out (new Fade (sel1 :div.fullscreen-topic) 1 0 utils/oc-animation-duration)]
        (doto fade-out
          (.listen AnimationEventType/FINISH
            #((:close-overlay-cb options)))
          (.play))))))

(defn esc-listener [owner options e]
  (when (= (.-keyCode e) 27)
    (hide-fullscreen-topic owner options)))

(defn remove-topic-click [owner options e]
  (.stopPropagation e)
  (when (js/confirm "Archiving removes the topic from the dashboard, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this topic?")
    (let [section (om/get-props owner :section)]
      (dis/dispatch! [:topic-archive section]))
    (hide-fullscreen-topic owner options true)))

(defn revision-navigation [owner as-of]
  (let [actual-as-of (om/get-state owner :actual-as-of)
        section (name (om/get-props owner :section))]
    (if (= as-of actual-as-of)
      (.pushState js/history nil section (oc-urls/company-section (router/current-company-slug) (om/get-props owner :section)))
      (.pushState js/history nil (str section " revision " as-of) (oc-urls/company-section-revision (router/current-company-slug) (om/get-props owner :section) as-of)))))

(defn animate-transition [owner]
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
                                     :transition-as-of nil}))))
      (.play))))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency card-width hide-history-navigation show-first-edit-tooltip] :as data} owner options]

  (init-state [_]
    (when (:fullscreen-force-edit data)
      (dis/dispatch! [:force-fullscreen-edit nil])
      ((:topic-navigation options) false))
    (when (and (router/section-editing?)
               (:read-only section-data))
      (utils/after 100 #(router/nav! (oc-urls/company-section))))
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
       :editing (and (:fullscreen-force-edit data) (not (:read-only section-data)))
       :data-posted false
       :show-save-button false
       :last-selected-metric selected-metric
       :actual-as-of actual-as-of
       :edit-rand (rand 4)}))

  (did-mount [_]
    (om/set-state! owner :esc-listener-key
      (events/listen js/document EventType/KEYUP (partial esc-listener owner options)))
    (when (:animate data)
      (show-fullscreen-topic owner)))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (when (om/get-state owner :data-posted)
        (hide-fullscreen-topic owner options))
      (om/set-state! owner :data-posted false)
      (when-not (= (:updated-at (:section-data next-props)) (:updated-at section-data))
        (om/set-state! owner :as-of (:updated-at (:section-data next-props)))
        (om/set-state! owner :actual-as-of (:updated-at (:section-data next-props))))))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :esc-listener-key)))

  (did-update [_ _ _]
    (when (om/get-state owner :transition-as-of)
      (animate-transition owner)))

  (render-state [_ {:keys [as-of transition-as-of actual-as-of editing show-save-button data-posted last-selected-metric edit-rand] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          revisions-list (get (slug @cache/revisions) section-kw)
          topic-data (utils/select-section-data section-data section-kw as-of)
          is-actual? (= as-of actual-as-of)
          fullscreen-topic-opts (merge options {:rev-nav #(om/set-state! owner :transition-as-of %)
                                                :switch-metric-cb #(om/set-state! owner :last-selected-metric %)
                                                :start-editing #(start-editing owner options)})
          edit-topic-opts (merge options {:show-save-button #(om/set-state! owner :show-save-button %)
                                          :dismiss-editing (partial hide-fullscreen-topic owner options)})
          can-edit? (and (responsive/can-edit?)
                         (not (:read-only data)))
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
        (when-not editing
          (dom/div {:class "btd-container"
                    :style {:width (str (+ fullscreen-width 20) "px")}}
            (back-to-dashboard-btn {:click-cb #(hide-fullscreen-topic owner options true)})))
        (dom/div {:style #js {:display (when-not editing "none")}
                  :key (str as-of edit-rand)}
          (om/build fullscreen-topic-edit {:topic section
                                           :topic-data topic-data
                                           :visible editing
                                           :selected-metric selected-metric
                                           :currency currency
                                           :card-width card-width
                                           :is-actual is-actual?
                                           :prev-rev prev-rev
                                           :next-rev next-rev
                                           :show-first-edit-tooltip show-first-edit-tooltip}
                                          {:opts edit-topic-opts}))
        (dom/div #js {:className "fullscreen-topic-transition group"
                      :ref "fullscreen-topic-transition"
                      :style #js {:height (when-not transition-as-of "auto")
                                  :display (when editing "none")}}
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
                                                 :can-edit can-edit?
                                                 :hide-history-navigation hide-history-navigation
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
                                                     :prev-rev tr-prev-rev
                                                     :next-rev tr-next-rev}
                                                    {:opts fullscreen-topic-opts})))))))))