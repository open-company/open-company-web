(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]))

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
          actual (first sorted-data)
          currency (:currency options)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/thousands-separator (:cash actual)))
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
          unit (when (= metric-unit "%") "%")
          last-value (utils/thousands-separator (:value (first metric-data)))
          last-value-label (str fixed-cur-unit last-value unit)]
      (dom/div {:class (utils/class-set {:topic-headline true
                                         :topic-headline-growth true
                                         :group true
                                         :collapse (:expanded data)})}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual blue"} (:name metric-info))
              (dom/h3 {:class "actual blue"} last-value-label))))))))

(def animation-duration 500)

(defn topic-click [owner expanded]
  (let [topic (om/get-ref owner "topic")
        topic-date-author (om/get-ref owner "topic-date-author")
        body-node (om/get-ref owner "topic-body")]
    (set! (.-height (.-style body-node)) "auto")
    (let [body-height (.-offsetHeight body-node)
          body-width (.-offsetWidth body-node)]
      (set! (.-height (.-style body-node)) (if expanded "auto" "0"))

      ; animate finances headtitle
      (when-let [finances-children (.querySelector topic ":scope > div.topic-headline > div.topic-headline-finances")]
        (let [finances-resize (new Resize
                                   finances-children
                                   (new js/Array body-width (if expanded 0 100))
                                   (new js/Array body-width (if expanded 100 0))
                                   500)
              finances-fade (new Fade
                                 finances-children
                                 (if expanded 0 1)
                                 (if expanded 1 0)
                                 500)]
          (.play finances-resize)
          (.play finances-fade)))

      ; animate growth headtitle
      (when-let [growth-children (.querySelector topic ":scope > div.topic-headline > div.topic-headline-growth")]
        (let [growth-resize (new Resize
                                 growth-children
                                 (new js/Array body-width (if expanded 0 100))
                                 (new js/Array body-width (if expanded 100 0))
                                 500)
              growth-fade (new Fade
                               growth-children
                               (if expanded 0 1)
                               (if expanded 1 0)
                               500)]
            (.play growth-resize)
            (.play growth-fade)))

      ; fade in/out author
      (.play
        (new Fade
             topic-date-author
             (if expanded 1 0)
             (if expanded 0 1)
             500))
      (let [height-animation (new Resize
                                  body-node
                                  (new js/Array body-width (if expanded body-height 0))
                                  (new js/Array body-width (if expanded 0 body-height))
                                  500)]
        (events/listen height-animation
                       EventType/FINISH
                       #(om/update-state! owner :expanded not))
        ; animate height
        (.play height-animation))

      (let [topic (om/get-ref owner "topic")
            body-scroll (.-scrollTop (.-body js/document))
            topic-scroll-top (utils/offset-top topic)]
        (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))))

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
                    :onClick #(topic-click owner expanded)}

        ;; Topic title
        (dom/div {:class "topic-title oc-header"} (:title section-data))

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