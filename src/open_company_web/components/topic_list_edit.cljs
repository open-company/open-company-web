(ns open-company-web.components.topic-list-edit
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.manage-topic :refer (manage-topic)]
            [open-company-web.components.ui.sortable.sortable-list :refer (sortable-list)]))

(defcomponent item [data owner options]
  (render [_]
    (let [section (keyword (:id data))
          section-data (:item-data data)
          active-sections (:active-sections data)
          active (utils/in? active-sections (name section))
          section-name (or (:name section-data) (utils/camel-case-str (name section)))
          section-title (or (:title section-data) section-name)
          section-description (or (:description section-data) "")]
      (dom/div {:class (utils/class-set {:topic-edit true
                                         :group true
                                         :topic-sortable true
                                         (str "topic-" section-name) true
                                         :active active})
                :data-sectionname section-name
                :on-click #(if active
                             ((:remove-section options) e section-name)
                             ((:add-section options) e section-name))
                :key (str "topic-edit-" section-name)}
        (dom/div {:class "topic-edit-internal group"}
          (dom/div {:class "topic-edit-labels"}
            (dom/h3 {:class "topic-title oc-header"} section-title)
            (dom/label {:class "topic-description"} section-description))
          (dom/img {:class "check" :src (str "/img/check_" (if active "checked" "empty") ".png?" ls/deploy-key)}))))))

(defn get-sections-data [category-sections]
  (apply merge
         (map (fn [section-data]
                (let [section-name (:name section-data)]
                  (hash-map (keyword section-name) section-data)))
              category-sections)))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    {:active-topics (get-in (:company-data data) [:sections (keyword (:active-category data))])})

  (did-mount [_]
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (let [new-topics (om/get-state owner :active-topics)]
            ((:save-sections-cb options) new-topics))))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:cancel-editing-cb options)))))))

  (render [_]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
      (let [active-category (:active-category options)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
            sections-list (vec (map #(:name %) category-sections))
            active-sections (om/get-state owner :active-topics)
            company-data (:company-data data)]
        (dom/div {:class "topic-list-edit fix-top-margin-scrolling group no-select"}
          (om/build sortable-list
                    {:sort sections-list
                     :items (get-sections-data category-sections)
                     :item item
                     :to-item {
                       :active-sections active-sections}}
                    {:opts (merge options {:height 68
                                           :add-section (fn [e section]
                                                          (om/set-state! owner :active-topics (concat active-sections [(name section)])))
                                           :remove-section (fn [e section]
                                                             (om/set-state! owner :active-topics (utils/vec-dissoc active-sections (name section))))})}))))))