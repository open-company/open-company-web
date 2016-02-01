(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.ui.charts :refer (column-chart)]))

(defn- get-body [section-data section]
  (cond
    (= section :finances)
    (:body (:notes section-data))

    (= section :growth)
    (:body (:notes section-data))

    :else
    (:body section-data)))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div {:class "topic-headline"} (:headline data))))

(defcomponent topic-headline-finances [data owner options]
  (render [_]
    (let [data (:data data)
          sorter (utils/sort-by-key-pred :period true)
          sorted-data (sort sorter data)
          actual (last sorted-data)
          currency (:currency options)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/format-value (:cash actual)))
          actual-label (str "as of " (finances-utils/get-as-of-string (:period actual)))]
      (dom/div {:class "topic-headline chart-header-container"}
        (dom/div {:class "target-actual-container"}
          (dom/div {:class "actual-container"}
            (dom/h3 {:class "actual green"} cash-val)
            (dom/h3 {:class "actual-label gray"} actual-label)))))))

(defcomponent topic-headline-growth [data owner options]
  (render [_]
    (let [metric-info (first (:metrics data))
          metric-data (filter #(= (:slug metric-info) (:slug %)) (:data data))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-metric (vec (sort sort-pred metric-data))
          interval (:interval metric-info)
          metric-unit (:unit metric-info)
          fixed-cur-unit (when (= metric-unit "currency")
                           (utils/get-symbol-for-currency-code (:currency options)))
          unit (when (= metric-unit "%") "%")]
      (dom/div {:class "topic-headline topic-headline-growth group"}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual blue"} (:name metric-info)))))
        (om/build column-chart
                  (growth-utils/get-chart-data sorted-metric
                                               fixed-cur-unit
                                               (:slug metric-info)
                                               (:name metric-info)
                                               unit
                                               interval)
                  {:opts {:chart-height 50 :chart-width 300 :chart-navigation false}})))))

(defcomponent topic [data owner options]

  (init-state [_]
    {:expanded false})

  (render [_]
    (let [company-data (:company-data data)
          section (keyword (:section-name options))
          section-data (company-data section)
          expanded (om/get-state owner :expanded)
          section-body (get-body section-data section)]
      (dom/div {:class "topic"
                ; :key (str "topic-" (name section))
                :on-click #(om/set-state! owner :expanded (not expanded))}

        ;; Topic title
        (dom/div {:class "topic-title"} (:title section-data))

        ;; Topic headline
        (cond
          (= section :finances)
          (om/build topic-headline-finances section-data {:opts {:currency (:currency company-data)}})

          (= section :growth)
          (om/build topic-headline-growth section-data {:opts {:currency (:currency company-data)}})

          :else
          (om/build topic-headline section-data))

        ;; Topic date
        (dom/div {:class "topic-date"} (utils/time-since (:updated-at section-data)))

        ;; Topic body
        (dom/div #js {:className (utils/class-set {:topic-body true
                                                   :expanded expanded})
                      :dangerouslySetInnerHTML (clj->js {"__html" section-body})})))))