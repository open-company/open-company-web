(ns open-company-web.components.add-topic
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.caches :as caches]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.lib.tooltip :as t]))

;; This should be defined as a derivative specifically suited to rendering the
;; topic adding component
(defn get-all-sections
  "Return all sections for the current company no matter it's state (archived, active, inactive)
   e.g {:section a-string :title a-string}"
  []
  (let [board-data (dis/board-data)
        org-slug   (keyword (router/current-org-slug))
        board-slug (keyword (router/current-board-slug))]
    (into
      (:archived board-data)
      (for [sec (:new-sections (board-slug (org-slug @caches/new-sections)))]
        (-> sec
            (assoc :section (:section-name sec))
            (select-keys [:title :section :body-placeholder :links]))))))

(defn get-categories []
  "Return the categories of the new available sections divided in columns and sorted by the
   specified order."
  (when-let* [org-slug       (keyword (router/current-org-slug))
              board-slug     (keyword (router/current-board-slug))
              categories     (:new-sections-categories (board-slug (org-slug @caches/new-sections)))
              all-categories (vec (map :column categories))]
    (apply merge
      (for [idx (range 1 (inc (apply max all-categories)))]
        (hash-map (keyword (str idx)) (sort #(compare (:order %1) (:order %2)) (filter #(= (:column %) idx) categories)))))))

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
                                        :section topic-name
                                        :was-archived false
                                        :body-placeholder (:body-placeholder custom-topic-data)
                                        :links [(assoc link :href (clojure.string/replace (:href link) "custom-{4-char-UUID}" topic-name))]
                                        :placeholder true}]
                    (submit-fn topic-name new-topic-data))} "Add"]]))

(rum/defcs category < rum/static
  [s cat company-data update-active-topics-cb all-sections is-archived]
  (let [archived-topics (:archived company-data)
        all-sections (into {} (if is-archived
                                (for [a archived-topics]
                                  [(keyword (:section a)) a])
                                (for [s (get-all-sections)]
                                  [(keyword (:section s)) s])))]
    [:div.category
      [:span.block.mb1.all-caps.category-title
        (str (:name cat) (when (:icon cat) " "))]
      (for [sec (:sections cat)
            :when (or is-archived
                      (and (not (utils/in? (:sections company-data) sec))
                           (not (utils/in? archived-topics sec))))
            :let [section-data (get all-sections (keyword sec))
                  section-kw (keyword (:section section-data))]]
        [:div.mb1.btn-reset.yellow-line-hover-child
          {:key (str "section-" sec)
           :on-click #(update-active-topics-cb (:section section-data) {:title (:title section-data)
                                                                        :section (:section section-data)
                                                                        :placeholder (not is-archived)
                                                                        :body-placeholder (:body-placeholder section-data)
                                                                        :links (:links section-data)
                                                                        :was-archived is-archived})}
          [:span.child.topic-title
            (:title section-data)
            (when (#{:finances :growth} section-kw)
              " ")
            (when (#{:finances :growth} section-kw)
              [:i.fa.fa-line-chart])]])]))

(defn is-active-or-archived [section active-sections archived-sections]
  (and (not (utils/in? active-sections section))
       (not (utils/in? archived-sections section))))

(rum/defcs add-topic < rum/reactive
                       (drv/drv :company-data)
                       (rum/local "" ::tt-key)
                       {:did-mount (fn [s]
                                    (let [rum-comp (:rum/react-component s)
                                          add-topic-node (js/ReactDOM.findDOMNode rum-comp)
                                          $add-topic (js/$ add-topic-node)
                                          add-topic-offset (.offset $add-topic)
                                          add-topic-width (.width $add-topic)
                                          add-topic-height (.height $add-topic)
                                          coords [(.-left add-topic-offset)
                                                  (+ (.-top add-topic-offset) 60)]
                                          company-data @(drv/get-ref s :company-data)
                                          tt-key (str "first-add-topic-" (:slug company-data))
                                          place {:place "left-bottom"}]
                                      (utils/after 500
                                       #(when (and (= (count (:sections company-data)) 0)
                                                   (= (count (:archived company-data)) 0))
                                        (t/tooltip coords {:config {:place "left-bottom"}
                                                           :id tt-key
                                                           :persistent true
                                                           :desktop "Select a topic you want everyone to know about, or you can name your own."})
                                        (t/show tt-key)))
                                      (assoc s ::tt-key tt-key)))
                       :will-unmount (fn [s]
                                       (t/hide (::tt-key s))
                                      s)}
  [s update-active-topics-cb]
  (let [company-data (drv/react s :company-data)
        all-sections (into {} (for [s (get-all-sections)]
                                [(keyword (:section s)) s]))
        categories (get-categories)
        sections (:sections company-data)
        archived (vec (map :section (:archived company-data)))
        archived-category {:name "Archived" :order 2 :sections archived}]
      [:div.add-topic.group
        [:div.add-topic-title
          (if (pos? (count sections))
            "Choose a topic to get started"
            "Add another topic")]
        [:hr]
        (when (pos? (count sections))
          [:span.close-add-topic
            {:on-click #(dis/dispatch! [:show-add-topic false])}
            (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 16 :stroke 8 :accent-color "rgba(78, 90, 107, 1.0)"})])
        [:div.mxn2.clearfix
          ;; column 1
          (for [column (keys categories)
                :let [all-categories (get categories column)
                      filter-categories (filter (fn [c]
                                                  (pos? (count (vec (filter
                                                                     #(is-active-or-archived % sections archived)
                                                                     (:sections c))))))
                                         all-categories)]
                :when (pos? (count filter-categories))]
            [:div.col.px2.col-4
              {:key (str "add-topic-col-" (name column))}
              (for [cat (get categories column)
                    :let [filtered-sections (vec (filter #(is-active-or-archived % sections archived) (:sections cat)))]
                    :when (pos? (count filtered-sections))]
                (rum/with-key (category (assoc cat :sections filtered-sections) company-data update-active-topics-cb all-sections false)
                 (str "col-" (:name cat))))])]
        [:div.mxn2.clearfix
          (when (pos? (count archived))
            [:div.col.px2.col-12
              (rum/with-key (category archived-category company-data update-active-topics-cb all-sections true)
               (str "col-Archived"))])]
        (custom-topic-input (get all-sections (keyword "custom-{4-char-UUID}")) #(update-active-topics-cb %1 %2))]))