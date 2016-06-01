(ns open-company-web.components.topics-columns
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]))

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
                         :ref section-name
                         :key (str "topic-row-" (name section-name))}
              (om/build topic {:loading (:loading company-data)
                               :section section-name
                               :section-data sd
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
  (render [_]
    (let [add-topic?        (and (not (:hide-add-topic data))
                                 (responsive/can-edit?)
                                 (not sharing-mode)
                                 (not (:read-only company-data)))
          add-first-column?  (= (count topics) 0)
          add-second-column? (= (count topics) 1)
          add-third-column?  (>= (count topics) 2)
          partial-render-topic (partial render-topic owner options)]
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
              (for [idx (range (inc (quot (count topics) 3)))
                :while (< idx (inc (quot (count topics) 2)))
                :let [real-idx (* idx 3)
                      section-name (get (vec topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-first-column? add-topic?)
                (partial-render-topic "add-topic" 1)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count topics) 3)))
                :while (< idx (inc (quot (count topics) 2)))
                :let [real-idx (inc (* idx 3))
                      section-name (get (vec topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-second-column? add-topic?)
                (partial-render-topic "add-topic" 2)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count topics) 3)))
                :while (< idx (inc (quot (count topics) 2)))
                :let [real-idx (+ (* idx 3) 2)
                      section-name (get (vec topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-third-column? add-topic?)
                (partial-render-topic "add-topic" 3))))
          2
          (dom/div {:class "topics-column-container columns-2 group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count topics) 2)))
                :while (< idx (inc (quot (count topics) 2)))
                :let [real-idx (* idx 2)
                      section-name (get (vec topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and add-first-column? add-topic?)
                (partial-render-topic "add-topic" 1)))
            (dom/div {:class "topics-column"
                      :style #js {:width (str card-width "px")}}
              (for [idx (range (inc (quot (count topics) 2)))
                :while (< idx (inc (quot (count topics) 2)))
                :let [real-idx (inc (* idx 2))
                      section-name (get (vec topics) real-idx)]]
                (partial-render-topic section-name))
              (when (and (not add-first-column?)
                         add-topic?)
                (partial-render-topic "add-topic" 2))))
          ; 1 column or default
          (dom/div {:class "topics-column-container columns-1 group"
                    :style #js {:width total-width}}
            (dom/div {:class "topics-column"}
              (for [section-name topics]
                (partial-render-topic section-name))
              (when add-topic?
                (partial-render-topic "add-topic" 1)))))))))