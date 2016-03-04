(ns open-company-web.components.topic-body
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]))

(defn topic-body-click [e owner options]
  (when e
    (.stopPropagation e))
  ((:toggle-edit-topic-cb options) (:section-name options)))

(defcomponent topic-body [{:keys [section section-data currency expanded] :as data} owner options]

  (init-state [_]
   {:as-of (:updated-at section-data)
    :original-as-of (:updated-at section-data)})

  (render-state [_ {:keys [as-of actual-as-of] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (:slug @router/path))
          revisions-list (section-kw (slug @cache/revisions))
          topic-data (utils/select-section-data section-data section-kw as-of)
          section-body (utils/get-topic-body topic-data section-kw)]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      ;; Topic body
      (dom/div #js {:className (utils/class-set {:topic-body true
                                                 :expanded expanded})
                    :ref "topic-body"
                    :onClick #(when-not (:read-only topic-data)
                                (topic-body-click % owner options))
                    :style #js {"height" (if expanded "auto" "0")}}
        (cond
          (= section-kw :growth)
          (om/build growth {:section-data topic-data
                            :section section-kw
                            :currency currency
                            :actual-as-of (:updated-at topic-data)
                            :read-only true}
                           {:opts {:show-title false
                                   :show-revisions-navigation false}})

          (= section-kw :finances)
          (om/build finances {:section-data topic-data
                              :section section-kw
                              :currency currency
                              :actual-as-of (:updated-at topic-data)
                              :read-only true}
                             {:opts {:show-title false
                                     :show-revisions-navigation false}})

          :else
          (dom/div #js {:className "topic-body-inner"
                        :dangerouslySetInnerHTML (clj->js {"__html" section-body})}))
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