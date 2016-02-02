(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]))

(defn- get-body [section-data section]
  (if (#{:finances :growth} section)
    (get-in section-data [:body :notes])
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
      (dom/div {:class (utils/class-set {:topic-headline true
                                         :topic-headline-finances true
                                         :group true
                                         :collapse (:expanded data)})}
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
      (dom/div {:class (utils/class-set {:topic-headline true
                                         :topic-headline-growth true
                                         :group true
                                         :collapse (:expanded data)})}
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

(def animation-duration 500)

(defn topic-click []
  (let [$topic (.$ js/window (om/get-ref owner "topic"))
        $topic-date-author (.$ js/window (om/get-ref owner "topic-date-author"))
        $body-node (.$ js/window (om/get-ref owner "topic-body"))]
    (.css $body-node "height" "auto")
    (let [body-height (.height $body-node)]
      (.css $body-node "height" (if expanded "auto" "0"))
      (when-let [$finances-headtitle (.find $topic ".topic-headline-finances")]
        (.animate $finances-headtitle #js {"height" (if expanded "100" "0")
                                           "opacity" (if expanded "1" "0")} animation-duration))
      (when-let [$growth-headtitle (.find $topic ".topic-headline-growth")]
        (.animate $growth-headtitle #js {"height" (if expanded "100" "0")
                                         "opacity" (if expanded "1" "0")} animation-duration))
      (.animate $topic-date-author
                #js {"opacity" (if expanded "0" "1")}
                #js {"duration" animation-duration})
      (.animate $body-node
                #js {"height" (if expanded "0" body-height)}
                #js {"duration" animation-duration
                     "complete" (fn [](om/update-state! owner :expanded not))}))))

(defcomponent topic [data owner options]

  (init-state [_]
    {:expanded false})

  (render [_]
    (let [company-data (:company-data data)
          section (keyword (:section-name options))
          section-data (company-data section)
          expanded (om/get-state owner :expanded)
          section-body (get-body section-data section)]
      (dom/div #js {:className "topic"
                    :ref "topic"
                    :onClick topic-click}

        ;; Topic title
        (dom/div {:class "topic-title"} (:title section-data))

        ;; Topic headline
        (dom/div {:class "topic-headline"}
          (cond
            (= section :finances)
            (om/build topic-headline-finances (assoc section-data :expanded expanded) {:opts {:currency (:currency company-data)}})

            (= section :growth)
            (om/build topic-headline-growth (assoc section-data :expanded expanded) {:opts {:currency (:currency company-data)}})

            :else
            (om/build topic-headline section-data)))

        ;; Topic date
        (dom/div {:class "topic-date"}
                 (str (utils/time-since (:updated-at section-data)) " ")
                 (dom/label #js {:style #js {"opacity" (if expanded "1" "0")}
                                 :ref "topic-date-author"}
                            (str " by " (:name (:author section-data)))))

        ;; Topic body
        (dom/div #js {:className "topic-body"
                      :ref "topic-body"
                      :style #js {"height" (if expanded "auto" "0")}}
          (cond
            (= section :growth)
            (om/build growth {:section-data section-data
                              :section section
                              :currency (:currency company-data)
                              :actual-as-of (:updated-at section-data)
                              :read-only true}
                             {:opts {:show-title false
                                     :show-revisions-navigation false}})

            (= section :finances)
            (om/build finances {:section-data section-data
                                :section section
                                :currency (:currency company-data)
                                :actual-as-of (:updated-at section-data)
                                :read-only true}
                               {:opts {:show-title false
                                       :show-revisions-navigation false}})

            :else
            (dom/div #js {:className "topic-body-inner"
                          :dangerouslySetInnerHTML (clj->js {"__html" section-body})})))))))

