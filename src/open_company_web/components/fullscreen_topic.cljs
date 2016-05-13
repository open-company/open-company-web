(ns open-company-web.components.fullscreen-topic
  (:require [cljs.core.async :refer (chan put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.caches :as cache]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.topic-overlay-edit :refer (topic-overlay-edit)]
            [open-company-web.components.ui.icon :refer (icon)]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]
            [open-company-web.lib.oc-colors :as oc-colors]))

(defn show-fullscreen-topic [owner]
  (dommy/add-class! (sel1 [:body]) :no-scroll)
  (setStyle (sel1 [:div.company-dashboard]) #js {:height "90vh" :overflow "hidden"})
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defn hide-fullscreen-topic [owner options]
  ; if it's in editing mode
  (if (om/get-state owner :editing)
    ; dismiss the editing
    (om/set-state! owner :editing false)
    ; else dismiss the fullscreen topic
    (do
      (dommy/remove-class! (sel1 [:body]) :no-scroll)
      (setStyle (sel1 [:div.company-dashboard]) #js {:height "auto" :overflow "auto"})
      (let [fade-out (new Fade (sel1 :div.fullscreen-topic) 1 0 utils/oc-animation-duration)]
        (doto fade-out
          (.listen AnimationEventType/FINISH
            #((:close-overlay-cb options)))
          (.play))))))

(defcomponent fullscreen-topic-internal [{:keys [topic topic-data currency selected-metric card-width] :as data} owner options]
  (render [_]
    (let [ww (.-clientWidth (sel1 js/document :body))
          fullscreen-width (if (> ww 575)
                              575
                              (min card-width ww))
          chart-opts {:show-title false
                      :show-revisions-navigation false
                      :chart-size {:width  (- fullscreen-width 40)
                                   :height (if (responsive/is-mobile) 174 295)}}
          chart-data {:section-data topic-data
                      :section (keyword topic)
                      :currency currency
                      :actual-as-of (:updated-at topic-data)
                      :selected-metric selected-metric
                      :read-only true}]
      (dom/div {:class "fullscreen-topic-internal group"
                :style #js {:width (str (- fullscreen-width 20) "px")}}
        (dom/div {:class "topic-title"} (:title topic-data))
        (dom/div {:class "topic-headline"
                  :dangerouslySetInnerHTML (utils/emojify (:headline topic-data))})
        (dom/div {:class "separator"})
        (when (or (= topic "growth") (= topic "finances"))
          (dom/div {:class "topic-growth-finances"}
            (cond
              (= topic "growth")
              (om/build topic-growth chart-data {:opts chart-opts})
              (= topic "finances")
              (om/build topic-finances chart-data {:opts chart-opts}))
            (dom/div {:class "separator"})))
        (dom/div {:class "topic-body"
                  :dangerouslySetInnerHTML (utils/emojify (utils/get-topic-body topic-data topic))})
        (dom/div {:class "topic-attribution"}
          (str "- " (:name (:author topic-data)) " / " (utils/date-string (js/Date. (:updated-at topic-data)) true)))
        (dom/div {:class "topic-revisions"}
          (when (:prev-rev data)
            (dom/button {:class "prev"
                         :on-click #((:rev-nav options) (:updated-at (:prev-rev data)))}
              (if (:is-actual data) "VIEW EARLIER UPDATE" "EARLIER")))
          (when (:next-rev data)
            (dom/button {:class "next"
                         :on-click #((:rev-nav options) (:updated-at (:next-rev data)))}
              "LATER")))))))

(defn esc-listener [owner options e]
  (when (= (.-keyCode e) 27)
    (hide-fullscreen-topic owner options)))

(defn remove-topic-click [owner options e]
  (.stopPropagation e)
  (when (js/confirm "Archiving removes the topic from the dashboard, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this topic?")
    (let [section (om/get-props owner :section)]
      ((:remove-topic options) section))
    (hide-fullscreen-topic owner options)))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency card-width] :as data} owner options]

  (init-state [_]
    (utils/add-channel "fullscreen-topic-save" (chan))
    (utils/add-channel "fullscreen-topic-cancel" (chan))
    {:as-of (:updated-at section-data)
     :editing false
     :show-save-button false
     :actual-as-of (:updated-at section-data)})

  (did-mount [_]
    (om/set-state! owner :esc-listener-key
      (events/listen js/document EventType/KEYUP (partial esc-listener owner options)))
    (show-fullscreen-topic owner))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner :as-of (:updated-at (:section-data next-props)))
      (om/set-state! owner :actual-as-of (:updated-at (:section-data next-props)))))

  (will-unmount [_]
    (utils/remove-channel "fullscreen-topic-save")
    (utils/remove-channel "fullscreen-topic-cancel")
    (events/unlistenByKey (om/get-state owner :esc-listener-key)))

  (render-state [_ {:keys [as-of actual-as-of editing show-save-button] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          revisions-list (get (slug @cache/revisions) section-kw)
          topic-data (utils/select-section-data section-data section-kw as-of)
          is-actual? (= as-of actual-as-of)
          fullscreen-topic-opts (merge options {:rev-nav #(om/set-state! owner :as-of %)})
          edit-topic-opts (merge options {:show-save-button #(om/set-state! owner :show-save-button %)
                                          :dismiss-editing #(om/set-state! owner :editing false)})
          can-edit? (and (not (:read-only data))
                         (not (responsive/is-mobile)))]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className "fullscreen-topic"
                    :ref "fullscreen-topic"}
        (when (and can-edit?
                   is-actual?
                   (not editing))
          (dom/div {:class "edit-button"
                    :on-click #(om/update-state! owner :editing not)}
            (icon :pencil)))
        (when (and can-edit?
                   editing
                   show-save-button)
          (dom/div {:class "save-button"
                    :on-click #(when-let [ch (utils/get-channel "fullscreen-topic-save")]
                                 (put! ch {:click true :event %}))}
            "POST"))
        (dom/div {:class "close"
                  :on-click #(if editing
                              (when-let [ch (utils/get-channel "fullscreen-topic-cancel")]
                                 (put! ch {:click true :event %}))
                              (hide-fullscreen-topic owner options))}
          (icon :simple-remove))
        (if editing
          (om/build topic-overlay-edit {:topic section
                                        :topic-data topic-data
                                        :selected-metric selected-metric
                                        :currency currency
                                        :card-width card-width
                                        :is-actual is-actual?
                                        :prev-rev prev-rev
                                        :next-rev next-rev}
                                       {:opts edit-topic-opts})
          (om/build fullscreen-topic-internal {:topic section
                                               :topic-data topic-data
                                               :selected-metric selected-metric
                                               :currency currency
                                               :card-width card-width
                                               :is-actual is-actual?
                                               :prev-rev prev-rev
                                               :next-rev next-rev}
                                              {:opts fullscreen-topic-opts}))
        (when (and can-edit?
                   is-actual?
                   (not editing))
          (dom/div {:class "remove-button"
                    :on-click (partial remove-topic-click owner options)}
            (icon :alert {:size 15
                          :accent-color (oc-colors/get-color-by-kw :oc-gray-5)
                          :stroke (oc-colors/get-color-by-kw :oc-gray-5)})
            "ARCHIVE THIS TOPIC"))))))