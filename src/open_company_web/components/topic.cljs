(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [open-company-web.local-settings :as ls]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]))

(defn- get-body [section-data section]
  (if (#{:finances :growth} section)
    (get-in section-data [:notes :body])
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

(defn topic-body-click [e owner options show-edit-button]
  (when e
    (.stopPropagation e))
  ((:toggle-edit-topic-cb options) (not show-edit-button) (:section-name options)))

(defn topic-click [data owner options expanded]
  (let [topic (om/get-ref owner "topic")
        topic-more (om/get-ref owner "topic-more")
        body-node (om/get-ref owner "topic-body")]
    (set! (.-height (.-style body-node)) "auto")
    (let [body-height (.-offsetHeight body-node)
          body-width (.-offsetWidth body-node)]
      (set! (.-height (.-style body-node)) (if expanded "auto" "0"))

      ;; animate finances headtitle
      (when-let [finances-children (sel1 topic ":scope > div.topic-headline > div.topic-headline-finances")]
        (let [finances-resize (new Resize
                                   finances-children
                                   (new js/Array body-width (if expanded 0 100))
                                   (new js/Array body-width (if expanded 100 0))
                                   utils/oc-animation-duration)
              finances-fade (new Fade
                                 finances-children
                                 (if expanded 0 1)
                                 (if expanded 1 0)
                                 utils/oc-animation-duration)]
          (.play finances-resize)
          (.play finances-fade)))

      ;; animate growth headtitle
      (when-let [growth-children (sel1 topic ":scope > div.topic-headline > div.topic-headline-growth")]
        (let [growth-resize (new Resize
                                 growth-children
                                 (new js/Array body-width (if expanded 0 100))
                                 (new js/Array body-width (if expanded 100 0))
                                 utils/oc-animation-duration)
              growth-fade (new Fade
                               growth-children
                               (if expanded 0 1)
                               (if expanded 1 0)
                               utils/oc-animation-duration)]
            (.play growth-resize)
            (.play growth-fade)))

      ;; Fade in/out 3 horizontal dots
      (.play
        (new Fade
             topic-more
             (if expanded 0 1)
             (if expanded 1 0)
             utils/oc-animation-duration))
      ;; Collapse the more dots
      (.play
        (new Resize
             topic-more
             (new js/Array body-width (if expanded 0 21))
             (new js/Array body-width (if expanded 21 0))
             utils/oc-animation-duration))

      ;; animate height
      (let [height-animation (new Resize
                                  body-node
                                  (new js/Array body-width (if expanded body-height 0))
                                  (new js/Array body-width (if expanded 0 body-height))
                                  utils/oc-animation-duration)]
        (events/listen height-animation
                       EventType/FINISH
                       #(om/update-state! owner :expanded not))
        (.play height-animation))

      (let [topic (om/get-ref owner "topic")
            body-scroll (.-scrollTop (.-body js/document))
            topic-scroll-top (utils/offset-top topic)]
        (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90)))

      (if-not expanded
        ;; show the edit button if the topic body is empty
        (let [section (keyword (:section-name options))
              section-data (get (:company-data data) section)
              body (get-body section-data section)]
          (when (clojure.string/blank? body)
            ((:force-edit-cb options) true)))
        ;; hide the edit button if necessary
        ((:force-edit-cb options) false)))))

(defn headline-component [section]
  (cond

    (= section :finances)
    topic-headline-finances

    (= section :growth)
    topic-headline-growth

    :else
    topic-headline))

(defcomponent topic [{:keys [company-data] :as data} owner {:keys [section-name navbar-editing-cb] :as options}]

  (init-state [_]
    {:expanded false})

  (did-mount [_]
    (utils/replace-svg))

  (did-update [_ _ _]
    (utils/replace-svg))

  (render-state [_ {:keys [editing expanded show-edit-button] :as state}]
    (let [section (keyword section-name)
          section-data (get company-data section)
          section-body (get-body section-data section)
          currency (:currency company-data)
          headline-options {:opts {:currency currency}}
          headline-data (assoc section-data :expanded expanded)]
      (dom/div #js {:className "topic"
                    :ref "topic"
                    :onClick #(topic-click data owner options expanded)}

        ;; Topic title
        (dom/div {:class "topic-header group"}
          (dom/img {:class (str "topic-image svg")
                    :src (str (:image section-data) "?" ls/deploy-key)})
          (dom/div {:class "topic-title oc-header"} (:title section-data)))

        ;; Topic headline
        (dom/div {:class "topic-headline"}
          (cond
            (= section :finances)
            (om/build topic-headline-finances headline-data headline-options)

            (= section :growth)
            (om/build topic-headline-growth headline-data headline-options)

            :else
            (om/build topic-headline section-data)))

        (dom/div #js {:className "topic-more"
                      :ref "topic-more"
                      :style #js {:opacity (if expanded 0 1)
                                  :height (if expanded "0px" "21px")}}
          (dom/img {:src "/img/3dots-horizontal.png"}))

        ;; Topic body
        (dom/div #js {:className "topic-body"
                      :ref "topic-body"
                      :onClick #(when-not (:read-only section-data)
                                  (topic-body-click % owner options show-edit-button))
                      :style #js {"height" (if expanded "auto" "0")}}
          (cond
            (= section :growth)
            (om/build growth {:section-data section-data
                              :section section
                              :currency currency
                              :actual-as-of (:updated-at section-data)
                              :read-only true}
                             {:opts {:show-title false
                                     :show-revisions-navigation false}})

            (= section :finances)
            (om/build finances {:section-data section-data
                                :section section
                                :currency currency
                                :actual-as-of (:updated-at section-data)
                                :read-only true}
                               {:opts {:show-title false
                                       :show-revisions-navigation false}})

            :else
            (dom/div #js {:className "topic-body-inner"
                          :dangerouslySetInnerHTML (clj->js {"__html" section-body})})))))))