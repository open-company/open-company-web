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
            [cljs-time.core :as t]))

(defcomponent expanded-topic [{:keys [section section-data currency expanded] :as data} owner options]

  (init-state [_]
   {:as-of (:updated-at section-data)
    :original-as-of (:updated-at section-data)})

  (render-state [_ {:keys [as-of actual-as-of]}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (:slug @router/path))
          revisions-list (section-kw (slug @cache/revisions))
          topic-data (utils/select-section-data section-data section-kw as-of)
          section-body (utils/get-topic-body topic-data section-kw)
          js-date-upat (utils/js-date (:updated-at topic-data))
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
        (dom/div {:class "topic-title"} (:title topic-data))
        (dom/div #js {:className "topic-last-updated"}
          (str (:name (:author topic-data)) " on " topic-updated-at))
        ;; topic body
        (om/build topic-body {:section section :section-data topic-data :expanded expanded} {:opts options})
        (dom/div {:class "topic-navigation group"}
          (when prev-rev
            (dom/div {:class "previous"}
              (dom/a {:on-click (fn [e]
                                  (om/set-state! owner :as-of (:updated-at prev-rev))
                                  (.stopPropagation e))} "< Previous")))
          (when next-rev
            (dom/div {:class "next"}
              (dom/a {:on-click (fn [e]
                                  (om/set-state! owner :as-of (:updated-at next-rev))
                                  (.stopPropagation e))} "Next >"))))))))