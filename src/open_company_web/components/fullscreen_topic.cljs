(ns open-company-web.components.fullscreen-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :refer (icon)]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]))

(defn show-fullscreen-topic [owner]
  (dommy/add-class! (sel1 [:body]) :no-scroll)
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defn hide-fullscreen-topic [owner options]
  (dommy/remove-class! (sel1 [:body]) :no-scroll)
  (let [fade-out (new Fade (sel1 :div.fullscreen-topic) 1 0 utils/oc-animation-duration)]
    (doto fade-out
      (.listen AnimationEventType/FINISH
        #((:close-overlay-cb options)))
      (.play))))

(defcomponent fullscreen-topic-internal [{:keys [topic topic-data] :as data} owner options]
  (render [_]
    (let []
      (dom/div {:class "fullscreen-topic-internal group"}
        (dom/div {:class "close"
                  :on-click #(hide-fullscreen-topic owner options)}
          (icon :circle-remove))
        (dom/div {:class "topic-title"} (:title topic-data))
        (dom/div {:class "topic-headline"} (:headline topic-data))
        (dom/div {:class "separator"})
        (dom/div {:class "topic-body"
                  :dangerouslySetInnerHTML (clj->js {"__html" (utils/get-topic-body topic-data topic)})})
        (dom/div {:class "topic-attribution"}
          (str "- " (:name (:author topic-data)) " / " (utils/date-string (js/Date. (:updated-at topic-data)) true)))))))

(defn esc-listener [owner options e]
  (when (= (.-keyCode e) 27)
    (hide-fullscreen-topic owner options)))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
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
          topic-data (utils/select-section-data section-data section-kw as-of)]
      (dom/div #js {:className "fullscreen-topic"
                    :ref "fullscreen-topic"}
        (om/build fullscreen-topic-internal {:topic section
                                             :topic-data topic-data
                                             :currency currency
                                             :prev-rev prev-rev
                                             :next-rev next-rev}
                                            {:opts options})))))