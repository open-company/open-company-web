(ns open-company-web.components.topics-columns
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.lib.combinatronics :as combo]))

;; Calc best topics layout based on heights

(def inter-topic-gap 22)
(def add-a-topic-height 95)

(defn add-topic? [owner]
  (let [data (om/get-props owner)
        company-data (:company-data data)
        sharing-mode (om/get-props owner :sharing-mode)]
    (and (not (:hide-add-topic data))
         (responsive/can-edit?)
         (not sharing-mode)
         (not (:read-only company-data)))))

(defn calc-total-height
  "Calculate the total height for each column as the sum of all the topics in that column, including the gaps between topics."
  [partition]
  {:total-heights (map #(reduce + %) (->> partition
                     (map #(map :height %))
             (map #(conj % (* inter-topic-gap (- (count %) 1))))))
   :arrangement partition})

(defn calc-with-add-a-topic
  "Account for the size of the 'Add a Topic' button in the last column of this particular arrangement of topics into columns."
  [partition]
  (let [heights (:total-heights partition)
        last-height (+ (last heights) inter-topic-gap add-a-topic-height)]
    {:total-heights (conj (vec (drop-last heights)) last-height)
     :arrangement (:arrangement partition)}))

(defn calc-delta-height
  "Calculate the delta between the shortest and tallest column, for this particular arrangement of topics into columns."
  [partition]
  {:delta (- (apply max (:total-heights partition)) (apply min (:total-heights partition)))
   :arrangement (:arrangement partition)})

(defn calc-partitions [owner]
  (let [columns-num (om/get-props owner :columns-num)
        topics-row (sel [:div.topic-row])
        topics (vec (map #(hash-map :topic (keyword (.-topic (.-dataset %))) :height (.-clientHeight %)) topics-row))
        add-topic (add-topic? owner)
        partitions (combo/partitions topics :max columns-num :min columns-num)
        heights (map calc-total-height partitions)
        fixed-heights (if add-topic
                        (map calc-with-add-a-topic heights)
                        heights)
        deltas (map calc-delta-height fixed-heights)
        best-layout (apply min-key :delta deltas)
        out (map #(map :topic %) (:arrangement best-layout))
        max-lenght (apply max (map count out))]
    (apply concat (map (fn [x] (apply conj [] (map (fn [v] (get (vec v) x)) out))) (range max-lenght)))))

(defn img-onload-cb [owner src]
  (when-not (utils/in? (om/get-state owner :imgs) src)
    (om/update-state! owner :imgs #(concat % [src]))
    (om/set-state! owner :best-partition (calc-partitions owner))))


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
                         :column column
                         :archived-topics (:archived company-data)
                         :section-data {:title "+ ADD A TOPIC"
                                        :body ""
                                        :updated-at 0
                                        :headline ""}
                          :currency (:currency company-data)
                          :active-topics topics}
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
                               :section-data sd
                               :currency (:currency company-data)
                               :sharing-mode sharing-mode
                               :share-selected share-selected?}
                               {:opts {:section-name section-name
                                       :topic-click (partial topic-click section-name)
                                       :img-onload-cb (partial img-onload-cb owner)}}))))))))

(defcomponent topics-columns [{:keys [columns-num
                                      sharing-mode
                                      content-loaded
                                      total-width
                                      card-width
                                      topics
                                      company-data] :as data} owner options]

  (did-update [_ _ _]
    (if (> columns-num 1)
      (when-not (om/get-state owner :best-partition)
        (om/set-state! owner :best-partition (calc-partitions owner)))
      (om/set-state! owner :best-partition (vec topics))))

  (render-state [_ {:keys [best-partition]}]
    (let [show-add-topic     (add-topic? owner)
          add-first-column?  (= (count topics) 0)
          add-second-column? (= (count topics) 1)
          add-third-column?  (>= (count topics) 2)
          partial-render-topic (partial render-topic owner options)
          render-topics      (if best-partition (vec best-partition) (vec topics))]
      ;; Topic list
      (dom/div {:class (utils/class-set {:topics-columns true
                                         :sharing-mode sharing-mode
                                         :group true
                                         :content-loaded content-loaded})}
        (case columns-num
          3
          (dom/div {:class "topics-column-container group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count render-topics) 3)))
                :while (< idx (inc (quot (count render-topics) 2)))
                :let [real-idx (* idx 3)
                      section-name (get (vec render-topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-first-column? show-add-topic)
                (partial-render-topic "add-topic" 1)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count render-topics) 3)))
                :while (< idx (inc (quot (count render-topics) 2)))
                :let [real-idx (inc (* idx 3))
                      section-name (get (vec render-topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-second-column? show-add-topic)
                (partial-render-topic "add-topic" 2)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count render-topics) 3)))
                :while (< idx (inc (quot (count render-topics) 2)))
                :let [real-idx (+ (* idx 3) 2)
                      section-name (get (vec render-topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-third-column? show-add-topic)
                (partial-render-topic "add-topic" 3))))
          2
          (dom/div {:class "topics-column-container columns-2 group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count render-topics) 2)))
                :while (< idx (inc (quot (count render-topics) 2)))
                :let [real-idx (* idx 2)
                      section-name (get (vec render-topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-first-column? show-add-topic)
                (partial-render-topic "add-topic" 1)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count render-topics) 2)))
                :while (< idx (inc (quot (count render-topics) 2)))
                :let [real-idx (inc (* idx 2))
                      section-name (get (vec render-topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and (not add-first-column?)
                         show-add-topic)
                (partial-render-topic "add-topic" 2))))
          ; 1 column or default
          (dom/div {:class "topics-column-container columns-1 group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"}
              (for [section-name topics]
                (partial-render-topic section-name))
              (when show-add-topic
                (partial-render-topic "add-topic" 1)))))))))