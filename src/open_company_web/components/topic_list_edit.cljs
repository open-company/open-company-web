(ns open-company-web.components.topic-list-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.ui.manage-topics :refer (manage-topics)]
            [cljs-dynamic-resources.core :as cdr]
            [goog.style :refer (setStyle)]))

(defcomponent item [data owner options]
  (render [_]
    (let [section (:id data)
          section-data (:item-data data)
          active-topics (:active-topics data)
          active (utils/in? active-topics section)
          section-name (or (:name section-data) (utils/camel-case-str section))
          section-title (or (:title section-data) section-name)
          section-description (or (:description section-data) "")
          check-img (str "/img/check_" (if active "checked" "empty") ".png?" ls/deploy-key)]
      (dom/div {:class (utils/class-set {:topic-edit true
                                         :group true
                                         :topic-sortable true
                                         (str "topic-" section) true
                                         :active active})
                :key (str "topic-edit-" section-name)}
        (dom/div {:class "topic-edit-internal group"}
          (dom/img {:class "check" :src check-img})
          (dom/div {:class "topic-edit-handle group"})
          (dom/div {:class "topic-edit-labels"}
            (dom/h3 {:class "topic-title oc-header"} section-title)
            (dom/label {:class "topic-description"} section-description)))))))

(defn get-sections-data [category-sections]
  (apply merge
         (map (fn [section-data]
                (let [section-name (:section-name section-data)]
                  (hash-map (keyword section-name) section-data)))
              category-sections)))

(defn setup-sortable [owner options]
  (when (and (om/get-state owner :did-mount) (om/get-state owner :sortable-loaded))
    (let [ul-node (om/get-ref owner "topic-list-sortable")]
      (.create js/Sortable ul-node (clj->js #js {:handle ".topic-edit-handle"
                                                 :onSort (fn [_]
                                                           (let [li-elements (sel ul-node [:li])
                                                                 items (vec (map #(.-itemname (.-dataset %)) li-elements))]
                                                             (println "onSort" li-elements)
                                                             (println "onSort" (map #(.-dataset %) li-elements))
                                                             (om/set-state! owner :active-topics items)
                                                             ((:did-change-sort options) items)))})))))

(defn topic-on-click [item-name owner did-change-sort]
  (let [active-topics (om/get-state owner :active-topics)
        unactive-topics (om/get-state owner :unactive-topics)
        is-active (utils/in? active-topics item-name)
        new-active-topics (if is-active
                            (utils/vec-dissoc active-topics item-name)
                            (concat active-topics [item-name]))
        new-unactive-topics (if is-active
                              (concat [item-name] unactive-topics)
                              (utils/vec-dissoc unactive-topics item-name))]
    (om/set-state! owner :active-topics (vec new-active-topics))
    (om/set-state! owner :unactive-topics (vec new-unactive-topics))
    (did-change-sort new-active-topics)))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (cdr/add-script! "/lib/Sortable.js/Sortable.js"
                     (fn []
                       (om/set-state! owner :sortable-loaded true)
                       (setup-sortable owner options)))
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (let [active-topics (:active-topics data)
          category (:category data)
          all-sections (:new-sections options)
          category-sections (:sections (first (filter #(= (:name %) category) (:categories all-sections))))
          sections-list (vec (map :section-name category-sections))
          unactive-topics (reduce utils/vec-dissoc sections-list active-topics)]
      {:initial-active-topics active-topics
       :active-topics active-topics
       :unactive-topics unactive-topics
       :sortable-loaded false
       :did-mount false}))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner options))

  (render-state [_ {:keys [unactive-topics active-topics]}]
    (.setTimeout js/window #(setup-sortable owner options) 100)
    (if (empty? @caches/new-sections)
      (dom/h2 {:style #js {:display (if (:active data) "inline" "none")}}
        "Loading sections...")
      (let [current-category (:category data)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) current-category) (:categories all-sections))))
            items (get-sections-data category-sections)]
        (dom/div {:class "topic-list-edit group no-select"
                  :style #js {:display (if (:active data) "inline" "none")}}
          (dom/div {}
            (dom/ul #js {:className "topic-list-sortable"
                         :ref "topic-list-sortable"
                         :key (apply str active-topics)}
              (for [item-name active-topics]
                (dom/li {:data-itemname item-name
                         :key (str "active-" item-name)
                         :on-click #(topic-on-click item-name owner (:did-change-sort options))}
                  (om/build item {:id item-name
                                  :item-data (get items (keyword item-name))
                                  :active-topics active-topics})))))
          (dom/div {}
            (dom/ul {:class "topic-list-unactive"
                     :key (apply str unactive-topics)}
              (for [item-name unactive-topics]
                (dom/li {:data-itemname item-name
                         :key (str "unactive-" item-name)
                         :on-click #(topic-on-click item-name owner (:did-change-sort options))}
                  (om/build item {:id item-name
                                  :item-data (get items (keyword item-name))
                                  :active-topics active-topics}))))))))))