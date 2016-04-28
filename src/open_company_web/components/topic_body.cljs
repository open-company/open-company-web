(ns open-company-web.components.topic-body
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]))

(defcomponent topic-body [{:keys [section section-data currency expanded selected-metric] :as data} owner options]

  (render [_]
    (let [section-kw (keyword section)
          section-body (utils/get-topic-body section-data section-kw)]
      ;; Topic body
      (dom/div {:class "topic-body"}

        (cond
          (= section-kw :growth)
          (om/build topic-growth {:section-data section-data
                                  :section section-kw
                                  :currency currency
                                  :actual-as-of (:updated-at section-data)
                                  :selected-metric selected-metric
                                  :read-only true}
                                 {:opts {:show-title false
                                         :show-revisions-navigation false
                                         :chart-size {:width  (if (utils/is-mobile) 300 480)
                                                      :height (if (utils/is-mobile) 274 295)}}})

          (= section-kw :finances)
          (om/build topic-finances {:section-data section-data
                                    :section section-kw
                                    :currency currency
                                    :actual-as-of (:updated-at section-data)
                                    :selected-metric selected-metric
                                    :read-only true}
                                   {:opts {:show-title false
                                           :show-revisions-navigation false
                                           :chart-size {:width  (if (utils/is-mobile) 300 480)
                                                        :height (if (utils/is-mobile) 274 295)}}}))
        (dom/div #js {:className "topic-body-inner group"
                      :dangerouslySetInnerHTML (clj->js {"__html" (str section-body "<p style='height:1px;margin-top:0px;padding-top:0px;'> </p>")})})))))