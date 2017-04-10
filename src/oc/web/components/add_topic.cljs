(ns oc.web.components.add-topic
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            ; [oc.web.lib.tooltip :as t]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.icon :as i]))

;; This should be defined as a derivative specifically suited to rendering the
;; topic adding component
(defn get-all-topics
  "Return all topics for the current board no matter it's state (archived, active, inactive)
   e.g {:slug a-string :title a-string}"
  [new-topics]
  (let [board-data (dis/board-data)]
    (into
      (:archived board-data)
      (for [sec (vals new-topics)]
        (select-keys sec [:title :slug :body-placeholder :links])))))

(defn get-categories [new-topic-categories]
  "Return the categories of the new available topics divided in columns and sorted by the
   specified order."
  (when-let [all-categories (vec (map :column new-topic-categories))]
    (apply merge
      (for [idx (range 1 (inc (apply max all-categories)))]
        (hash-map (keyword (str idx)) (sort #(compare (:order %1) (:order %2)) (filter #(= (:column %) idx) new-topic-categories)))))))

(rum/defcs custom-topic-input
  < (rum/local "" ::topic-title)
  [s custom-topic-data submit-fn]
  (let [add-disabled (clojure.string/blank? @(::topic-title s))]
    [:div.mt3
     [:input.custom-topic-input
      {:type "text",
       :value @(::topic-title s)
       :max-length 20
       :on-change #(reset! (::topic-title s) (.. % -target -value))
       :placeholder "Name your own topic"}]
     [:button.btn-reset.btn-solid.custom-topic-add
      {:disabled add-disabled
       :on-click #(let [topic-name     (str "custom-" (utils/my-uuid))
                        link           (utils/link-for (:links custom-topic-data) "create")
                        new-topic-data {:title @(::topic-title s)
                                        :topic topic-name
                                        :was-archived false
                                        :body-placeholder (:body-placeholder custom-topic-data)
                                        :links [(assoc link :href (clojure.string/replace (:href link) "custom-{4-char-UUID}" topic-name))]
                                        :placeholder true}]
                    (submit-fn topic-name new-topic-data))} "Add"]]))

(rum/defcs category < rum/static
  [s cat board-data update-active-topics-cb all-topics is-archived]
  (let [archived-topics (:archived board-data)
        all-topics (into {} (if is-archived
                              (for [a archived-topics]
                                [(keyword (:slug a)) a])
                              (for [s (vals all-topics)]
                                [(keyword (:slug s)) s])))]
    [:div.category
      [:span.block.mb1.all-caps.category-title
        (str (:name cat) (when (:icon cat) " "))]
      (for [sec (:topics cat)
            :when (or is-archived
                      (and (not (utils/in? (:topics board-data) sec))
                           (not (utils/in? archived-topics sec))))
            :let [topic-data (get all-topics (keyword sec))
                  topic-kw (keyword (:slug topic-data))]]
        [:div.mb1.btn-reset.yellow-line-hover-child
          {:key (str "topic-" sec)
           :on-click #(update-active-topics-cb (:slug topic-data) {:title (:title topic-data)
                                                                        :topic (:slug topic-data)
                                                                        :placeholder (not is-archived)
                                                                        :body-placeholder (:body-placeholder topic-data)
                                                                        :links (:links topic-data)
                                                                        :was-archived is-archived})}
          [:span.child.topic-title
            (:title topic-data)
            (when (#{:finances :growth} topic-kw)
              " ")
            (when (#{:finances :growth} topic-kw)
              [:i.fa.fa-line-chart])]])]))

(defn is-active-or-archived [topic active-topics archived-topics]
  (and (not (utils/in? active-topics topic))
       (not (utils/in? archived-topics topic))))

(rum/defcs add-topic < rum/reactive
                       (drv/drv :board-data)
                       (drv/drv :board-new-topics)
                       (drv/drv :board-new-categories)
                       ;Comment out onboard tooltips 
                       ; (rum/local "" ::tt-key)
                       ; {:did-mount (fn [s]
                       ;              (let [rum-comp (:rum/react-component s)
                       ;                    add-topic-node (js/ReactDOM.findDOMNode rum-comp)
                       ;                    $add-topic (js/$ add-topic-node)
                       ;                    add-topic-offset (.offset $add-topic)
                       ;                    add-topic-width (.width $add-topic)
                       ;                    add-topic-height (.height $add-topic)
                       ;                    coords [(.-left add-topic-offset)
                       ;                            (+ (.-top add-topic-offset) 60)]
                       ;                    board-data @(drv/get-ref s :board-data)
                       ;                    tt-key (str "first-add-topic-" (:slug board-data))
                       ;                    place {:place "left-bottom"}]
                       ;                (utils/after 500
                       ;                 #(when (and (= (count (:topics board-data)) 0)
                       ;                             (= (count (:archived board-data)) 0))
                       ;                  (t/tooltip coords {:config {:place "left-bottom"}
                       ;                                     :id tt-key
                       ;                                     :persistent true
                       ;                                     :desktop "Select a topic you want everyone to know about, or you can name your own."})
                       ;                  (t/show tt-key)))
                       ;                (assoc s ::tt-key tt-key)))
                       ; :will-unmount (fn [s]
                       ;                 (t/hide (::tt-key s))
                       ;                s)}
  [s update-active-topics-cb]
  (let [board-data (drv/react s :board-data)
        board-new-topics (drv/react s :board-new-topics)
        board-new-categories (drv/react s :board-new-categories)
        all-topics (into {} (for [s (get-all-topics board-new-topics)]
                                [(keyword (:slug s)) s]))
        categories (get-categories board-new-categories)
        topics (:topics board-data)
        archived (vec (map :slug (:archived board-data)))
        archived-category {:name "Archived" :order 2 :topics archived}]
      [:div.add-topic.group
        [:div.add-topic-title
          {:on-click (fn [_] (let [x 0]
                              (/ 2 x)))}
          (if (pos? (count topics))
            "Add another topic"
            "Choose a topic to get started")]
        [:hr]
        (when (pos? (count topics))
          [:span.close-add-topic
            {:on-click #(dis/dispatch! [:show-add-topic false])}
            (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 16 :stroke 8 :accent-color "rgba(78, 90, 107, 1.0)"})])
        [:div.mxn2.clearfix
          ;; column 1
          (for [column (keys categories)
                :let [all-categories (get categories column)
                      filter-categories (filter (fn [c]
                                                  (pos? (count (vec (filter
                                                                     #(is-active-or-archived % topics archived)
                                                                     (:topics c))))))
                                         all-categories)]
                :when (pos? (count filter-categories))]
            [:div.col.px2.col-4
              {:key (str "add-topic-col-" (name column))}
              (for [cat (get categories column)
                    :let [filtered-topics (vec (filter #(is-active-or-archived % topics archived) (:topics cat)))]
                    :when (pos? (count filtered-topics))]
                (rum/with-key (category (assoc cat :topics filtered-topics) board-data update-active-topics-cb all-topics false)
                 (str "col-" (:name cat))))])]
        ;; Archived topics
        [:div.mxn2.clearfix
          (when (pos? (count archived))
            [:div.col.px2.col-12
              (rum/with-key (category archived-category board-data update-active-topics-cb all-topics true)
               (str "col-Archived"))])]
        (custom-topic-input (get all-topics (keyword "custom-{4-char-UUID}")) #(update-active-topics-cb %1 %2))]))