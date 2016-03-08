(ns open-company-web.components.expanded-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic-body :refer (topic-body)]
            [cljs-time.format :as f]
            [cljs-time.core :as t]
            [goog.style :refer (getStyle setStyle)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade Resize)]))

(defcomponent expanded-internal [{:keys [as-of section section-data currency expanded actual-as-of] :as data} owner options]
  (render [_]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          slug (keyword (:slug @router/path))
          revisions-list (section-kw (slug @cache/revisions))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          section-body (utils/get-topic-body section-data section-kw)
          js-date-upat (utils/js-date (:updated-at section-data))
          topic-updated-at (str (utils/month-string-int (inc (.getMonth js-date-upat))) " " (.getDate js-date-upat))]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div {:class "topic-expanded"}
        (dom/div {:class "topic-title"} (:title section-data))
        (dom/div #js {:className "topic-last-updated"}
          (str (:name (:author section-data)) " on " topic-updated-at))
        ;; topic body
        (om/build topic-body {:section section :section-data section-data :expanded expanded} {:opts options})
        (dom/div {:class "topic-navigation group"}
          (when prev-rev
            (dom/div {:class "previous"}
              (dom/a {:on-click #((:prev-cb options) (:updated-at prev-rev))} "< Previous")))
          (when next-rev
            (dom/div {:class "next"}
              (dom/a {:on-click #((:next-cb options) (:updated-at next-rev))} "Next >"))))))))

(defn perform-transition [owner]
  (when-let [transit-topic-expanded (om/get-ref owner "transit-topic-expanded")]
    (let [actual-topic-expanded (om/get-ref owner "actual-topic-expanded")
          topic-expanded-container (om/get-ref owner "topic-expanded-container")
          current-height (.-offsetHeight actual-topic-expanded)
          current-width (.-offsetWidth topic-expanded-container)]
      (setStyle transit-topic-expanded #js {:opacity 1 :height "auto"})
      (let [final-height (.-offsetHeight transit-topic-expanded)
            transit-fade (new Fade transit-topic-expanded 0 1 utils/oc-animation-duration)]
        (setStyle transit-topic-expanded #js {:opacity 0 :height current-height})
        (setStyle actual-topic-expanded #js {:position "absolute" :top 0})
        (.play (new Fade actual-topic-expanded 1 0 utils/oc-animation-duration))
        (.play (new Resize
                    topic-expanded-container
                    (new js/Array current-width current-height)
                    (new js/Array current-width final-height)
                    utils/oc-animation-duration))
        (doto transit-fade
          (events/listen EventType/FINISH
            (fn [e]
              (let [transit-as-of (om/get-state owner :transit-as-of)]
                (om/set-state! owner :as-of transit-as-of)
                (om/set-state! owner :transit-as-of nil)
                (.setTimeout js/window (fn []
                                        (setStyle actual-topic-expanded #js {:opacity 1 :position "relative"})) 1))))
          (.play))))))

(defcomponent expanded-topic [{:keys [section section-data currency expanded] :as data} owner options]

  (init-state [_]
   {:as-of (:updated-at section-data)
    :transit-as-of nil})

  (render-state [_ {:keys [as-of transit-as-of]}]
    (let [section-kw (keyword section)
          topic-data (utils/select-section-data section-data section-kw as-of)
          transit-topic-data (utils/select-section-data section-data section-kw transit-as-of)]
      (when transit-as-of
        (.setTimeout js/window #(perform-transition owner) 1))
      (dom/div #js {:className "topic-expanded-container"
                    :ref "topic-expanded-container"
                    :style #js {:overflow "hidden"}}
        (when transit-as-of
          (dom/div #js {:className "transit-topic-expanded topic-expanded-internal"
                        ; :key (:updated-at transit-topic-data)
                        :ref "transit-topic-expanded"
                        :style #js {:opacity 0}}
            (om/build expanded-internal {:section section
                                         :section-data transit-topic-data
                                         :as-of transit-as-of
                                         :actual-as-of (:updated-at section-data)
                                         :currency currency
                                         :expanded expanded
                                         :transit true})))
        (dom/div #js {:className "actual-topic-expanded topic-expanded-internal"
                      ; :key (:updated-at topic-data)
                      :ref "actual-topic-expanded"
                      :style #js {:opacity 1 :position "relative"}}
          (om/build expanded-internal {:section section
                                       :section-data topic-data
                                       :as-of as-of
                                       :actual-as-of (:updated-at section-data)
                                       :currency currency
                                       :expanded expanded}
                                      {:opts {:prev-cb #(om/set-state! owner :transit-as-of %)
                                              :next-cb #(om/set-state! owner :transit-as-of %)}}))))))