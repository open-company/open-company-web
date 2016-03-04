(ns open-company-web.components.topic-body
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]))

(defn topic-body-click [e owner options]
  (when e
    (.stopPropagation e))
  ((:toggle-edit-topic-cb options) (:section-name options)))

(defcomponent topic-body [{:keys [section section-data currency expanded] :as data} owner options]

  (render [_]
    (let [section-kw (keyword section)
          section-body (utils/get-topic-body section-data section-kw)]
      ;; Topic body
      (dom/div #js {:className (utils/class-set {:topic-body true
                                                 :expanded expanded})
                    :onClick #(when-not (:read-only section-data)
                                (topic-body-click % owner options))
                    :style #js {"height" (if expanded "auto" "0")}}
        (cond
          (= section-kw :growth)
          (om/build growth {:section-data section-data
                            :section section-kw
                            :currency currency
                            :actual-as-of (:updated-at section-data)
                            :read-only true}
                           {:opts {:show-title false
                                   :show-revisions-navigation false}})

          (= section-kw :finances)
          (om/build finances {:section-data section-data
                              :section section-kw
                              :currency currency
                              :actual-as-of (:updated-at section-data)
                              :read-only true}
                             {:opts {:show-title false
                                     :show-revisions-navigation false}})

          :else
          (dom/div #js {:className "topic-body-inner"
                        :dangerouslySetInnerHTML (clj->js {"__html" section-body})}))))))