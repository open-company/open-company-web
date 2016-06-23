(ns open-company-web.components.topics-columns
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]))

;; Calc best topics layout based on heights

(defn add-topic? [owner]
  (let [data (om/get-props owner)
        company-data (:company-data data)
        sharing-mode (om/get-props owner :sharing-mode)]
    (and (not (:hide-add-topic data))
         (responsive/can-edit?)
         (not sharing-mode)
         (not (:read-only company-data)))))

(def inter-topic-gap 22)
(def add-a-topic-height 95)

(def topic-default-height-no-body 95)
(def topic-default-height-with-body 109)
(def data-topic-default-zero-height 105)
(def data-topic-default-one-height 282)
(def data-topic-default-more-height 388)
(def topic-body-height 29)
(def topic-header-image-max-height 196)
(def add-topic-height 94)

(defn headline-height [headline card-width]
  (if (clojure.string/blank? headline)
    0
    (let [$headline (js/$ (str "<div class=\"topic\">"
                                  "<div class=\"topic-anim\">"
                                    "<div>"
                                      "<div class=\"topic-internal\">"
                                        "<div class=\"topic-headline-inner\" style=\"width: " card-width "px;\">"
                                          (utils/emojify headline true)
                                        "</div>"
                                      "</div>"
                                    "</div>"
                                  "</div>"
                                "</div>"))]
      (.appendTo $headline (.-body js/document))
      (let [height (.height $headline)]
        (.detach $headline)
        height))))

(defn get-topic-height [topic-body]
  (if (clojure.string/blank? topic-body)
    topic-default-height-no-body
    topic-default-height-with-body))

(defn data-topic-height [owner topic topic-data]
  (if (= topic :finances)
    (cond
      (= (count (:data topic-data)) 0)
      data-topic-default-zero-height
      (= (count (:data topic-data)) 1)
      data-topic-default-one-height
      (> (count (:data topic-data)) 1)
      data-topic-default-more-height)
    (let [data (:data topic-data)
          selected-metric (or (om/get-props owner :selected-metric) (:slug (first (:metrics topic-data))))
          metric-data (filter #(= (:slug %) selected-metric) data)]
      (cond
        (= (count metric-data) 0)
        data-topic-default-zero-height
        (= (count metric-data) 1)
        data-topic-default-one-height
        (> (count metric-data) 1)
        data-topic-default-more-height))))

(defn calc-column-height [owner data topics]
  (let [card-width (om/get-props owner :card-width)
        company-data (:company-data data)]
    (for [topic topics
          :let [topic-kw (keyword topic)
                topic-data (get company-data topic-kw)
                is-data-topic (#{:finances :growth} topic-kw)]]
      (cond
        (= topic "add-topic")
        add-topic-height
        (#{:finances :growth} topic-kw)
        (let [headline-height (headline-height (:headline topic-data) card-width)
              body-height (if (clojure.string/blank? (:body (:notes topic-data)))
                            0
                            topic-body-height)
              start-height (data-topic-height owner topic topic-data)]
          (+ start-height headline-height body-height))
        :else
        (let [topic-body (:body topic-data)
              rendered-body (js/$ (str "<div>" topic-body "</div>"))
              img (.find rendered-body "img")
              temp-height (get-topic-height topic-body)
              headline (headline-height (:headline topic-data) card-width)
              image-height (if (and img (.get img 0))
                             (min (.data img "height") topic-header-image-max-height)
                             0)]
          (+ temp-height headline image-height))))))

(defn get-shortest-column [owner data current-layout]
  (let [columns-num (:columns-num data)
        frst-clmn (apply + (calc-column-height owner data (:1 current-layout)))
        scnd-clmn (apply + (calc-column-height owner data (:2 current-layout)))
        thrd-clmn (apply + (calc-column-height owner data (:3 current-layout)))
        min-height (if (= columns-num 3)
                    (min frst-clmn scnd-clmn thrd-clmn)
                    (min frst-clmn scnd-clmn))]
    (cond
      (= min-height frst-clmn)
      :1
      (= min-height scnd-clmn)
      :2
      (= min-height thrd-clmn)
      :3)))

(defn calc-layout [owner data]
  (let [columns-num (:columns-num data)
        show-add-topic (add-topic? owner)
        topics (to-array (:topics data))
        final-layout (loop [idx 3
                            layout (if (= columns-num 3)
                                      {:1 [(first topics)]
                                       :2 [(second topics)]
                                       :3 [(get topics 2)]}
                                      {:1 [(first topics)]
                                       :2 [(second topics)]})]
                        (let [shortest-column (get-shortest-column owner data layout)
                              new-column (conj (get layout shortest-column) (get topics idx))
                              new-layout (assoc layout shortest-column new-column)]
                          (if (< (inc idx) (count topics))
                            (recur (inc idx)
                                   new-layout)
                            new-layout)))
        clean-layout (apply merge (for [[k v] final-layout] {k (vec (remove nil? v))}))]
    clean-layout))

(defn render-topic [owner options section-name & [column]]
  (when section-name
    (let [props                 (om/get-props owner)
          sharing-mode          (:sharing-mode props)
          share-selected-topics (:share-selected-topics props)
          company-data          (:company-data props)
          topics                (:topics props)
          topic-click           (or (:topic-click options) identity)
          update-active-topics  (or (:update-active-topics options) identity)
          share-selected?       (utils/in? share-selected-topics section-name)]
      (if (= section-name "add-topic")
        (om/build topic {:loading false
                         :section "add-topic"
                         :add-topic true
                         :show-fast-editing false
                         :column column
                         :read-only-company false
                         :archived-topics (:archived company-data)
                         :section-data {:title "+ ADD A TOPIC"
                                        :body ""
                                        :updated-at 0
                                        :headline ""}
                          :currency (:currency company-data)
                          :active-topics (map name topics)}
                         {:opts {:section-name section-name
                                 :topic-click (partial topic-click section-name)
                                 :update-active-topics update-active-topics}})
        (let [sd (->> section-name keyword (get company-data))]
          (when-not (and (:read-only company-data) (:placeholder sd))
            (dom/div #js {:className "topic-row"
                          :data-topic (name section-name)
                          :ref section-name
                          :key (str "topic-row-" (name section-name))}
              (om/build topic {:loading (:loading company-data)
                               :section section-name
                               :show-fast-editing (om/get-props owner :show-fast-editing)
                               :section-data sd
                               :read-only-company (:read-only company-data)
                               :currency (:currency company-data)
                               :sharing-mode sharing-mode
                               :share-selected share-selected?}
                               {:opts {:section-name section-name
                                       :topic-click (partial topic-click section-name)}}))))))))

(defcomponent topics-columns [{:keys [columns-num
                                      sharing-mode
                                      content-loaded
                                      total-width
                                      card-width
                                      topics
                                      company-data] :as data} owner options]

  (did-mount [_]
    (when (> columns-num 1)
      (om/set-state! owner :best-layout (calc-layout owner data))))

  (will-receive-props [_ next-props]
    (when (or (not= (:topics next-props) (:topics data))
              (not= (:columns-num next-props) (:columns-num data)))
      (om/set-state! owner :best-layout (calc-layout owner next-props))))

  (render-state [_ {:keys [best-layout]}]
    (let [show-add-topic     (add-topic? owner)
          partial-render-topic (partial render-topic owner options)]
      ;; Topic list
      (dom/div {:class (utils/class-set {:topics-columns true
                                         :sharing-mode sharing-mode
                                         :group true
                                         :content-loaded content-loaded})}
        (cond
          ;; render 2 or 3 column layout
          (> columns-num 1)
          (dom/div {:class "topics-column-container group"
                    :style #js {:width total-width}}
            (for [kw (if (= columns-num 3) [:1 :2 :3] [:1 :2])]
              (let [column (get best-layout kw)]
                (dom/div {:class "topics-column"
                          :style #js {:width (str card-width "px")}}
                  (when (pos? (count column))
                    (for [idx (range (count column))
                      :let [section-kw (get column idx)
                            section-name (name section-kw)]]
                      (partial-render-topic section-name
                                            (when (= section-name "add-topic") (int (name kw))))))
                  (when (and show-add-topic
                             (= kw :1)
                             (= (count topics) 0))
                    (partial-render-topic "add-topic" 1))
                  (when (and show-add-topic
                             (= kw :2)
                             (or (and (= (count topics) 1)
                                      (= columns-num 3))
                                 (and (>= (count topics) 1)
                                      (= columns-num 2))))
                    (partial-render-topic "add-topic" 2))
                  (when (and show-add-topic
                             (= kw :3)
                             (>= (count topics) 2))
                    (partial-render-topic "add-topic" 3))))))
          ;; 1 column or default
          :else
          (dom/div {:class "topics-column-container columns-1 group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"}
              (for [section (vec topics)]
                (partial-render-topic (name section)))
              (when show-add-topic
                (partial-render-topic "add-topic" 1)))))))))