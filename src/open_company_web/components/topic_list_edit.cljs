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
            [open-company-web.components.ui.sortable-list :refer (sortable-list)]))

(defcomponent item [data owner options]
  (render [_]
    (let [section (keyword (:id data))
          section-data (:item-data data)
          active-topics (:active-topics data)
          active (utils/in? active-topics (name section))
          section-name (or (:name section-data) (utils/camel-case-str (name section)))
          section-title (or (:title section-data) section-name)
          section-description (or (:description section-data) "")]
      (dom/div {:class (utils/class-set {:topic-edit true
                                         :group true
                                         :topic-sortable true
                                         (str "topic-" section-name) true
                                         :active active})
                :data-sectionname section-name
                :on-click #((:item-click options) section-name)
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

(defn ordered-sections [active-sections all-sections]
  (loop [ret active-sections
         secs all-sections]
    (if (zero? (count secs))
      ret
      (let [new-el (first secs)
            new-ret (if (utils/in? ret new-el)
                      ret
                      (conj ret new-el))]
       (recur new-ret (next secs))))))

(defn sorted-active-topics [sorted-sections active-topics]
  (loop [ret []
         sections sorted-sections]
    (let [section (first sections)
          next-ret (if (utils/in? active-topics section)
                     (conj ret section)
                     ret)]
      (if (zero? (count sections))
        next-ret
        (recur next-ret (next sections))))))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (let [active-category (:active-category options)
          active-topics (get-in (:company-data data) [:sections (keyword active-category)])
          all-sections (:new-sections options)
          category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
          sections-list (vec (map :name category-sections))
          cleaned-sections (ordered-sections active-topics sections-list)]
      {:active-topics active-topics
       :sorted-sections cleaned-sections}))

  (will-mount [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch)))

  (will-unmount [_]
    (utils/remove-channel "save-bt-navbar")
    (utils/remove-channel "cancel-bt-navbar"))

  (did-mount [_]
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (let [sorted-sections (om/get-state owner :sorted-sections)
                active-topics (om/get-state owner :active-topics)]
            ((:save-sections-cb options) (sorted-active-topics sorted-sections active-topics)))))))
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
            active-topics (om/get-state owner :active-topics)]
        (dom/div {:class "topic-list-edit fix-top-margin-scrolling group no-select"}
          (om/build sortable-list
                    {:sort (om/get-state owner :sorted-sections)
                     :items (get-sections-data category-sections)
                     :item item
                     :to-item {
                       :active-topics active-topics}}
                    {:opts
                     (merge options {:height 68
                                     :did-change-sort
                                     (fn [items]
                                       (om/set-state! owner :sorted-sections items))
                                     :item-click
                                     (fn [section]
                                       (if (utils/in? active-topics section)
                                         (om/set-state! owner :active-topics (utils/vec-dissoc active-topics (name section)))
                                         (om/set-state! owner :active-topics (concat active-topics [(name section)]))))})}))))))