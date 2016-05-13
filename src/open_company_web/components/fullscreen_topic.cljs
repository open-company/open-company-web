(ns open-company-web.components.fullscreen-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.caches :as cache]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.ui.icon :refer (icon)]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]))

(defn show-fullscreen-topic [owner]
  (dommy/add-class! (sel1 [:body]) :no-scroll)
  (setStyle (sel1 [:div.company-dashboard]) #js {:height "90vh" :overflow "hidden"})
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defn hide-fullscreen-topic [options]
  (dommy/remove-class! (sel1 [:body]) :no-scroll)
  (setStyle (sel1 [:div.company-dashboard]) #js {:height "auto" :overflow "auto"})
  (let [fade-out (new Fade (sel1 :div.fullscreen-topic) 1 0 utils/oc-animation-duration)]
    (doto fade-out
      (.listen AnimationEventType/FINISH
        #((:close-overlay-cb options)))
      (.play))))

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
    (hide-fullscreen-topic options)))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency card-width] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :editing false
     :actual-as-of (:updated-at section-data)})

  (did-mount [_]
    (om/set-state! owner :esc-listener-key
      (events/listen js/document EventType/KEYUP (partial esc-listener owner options)))
    (show-fullscreen-topic owner))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :esc-listener-key)))

  (render-state [_ {:keys [as-of actual-as-of] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          revisions-list (section-kw (slug @cache/revisions))
          topic-data (utils/select-section-data section-data section-kw as-of)
          is-actual? (= as-of actual-as-of)
          opts (merge options {:rev-nav #(om/set-state! owner :as-of %)})]
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
        (when is-actual?
          (dom/div {:class "edit"
                    :on-click #(om/set-state! owner :editing true)}
            (icon :pencil)))
        (dom/div {:class "close"
                  :on-click #(hide-fullscreen-topic options)}
          (icon :simple-remove))
        (om/build fullscreen-topic-internal {:topic section
                                             :topic-data topic-data
                                             :selected-metric selected-metric
                                             :currency currency
                                             :card-width card-width
                                             :is-actual is-actual?
                                             :prev-rev prev-rev
                                             :next-rev next-rev}
                                            {:opts opts})))))