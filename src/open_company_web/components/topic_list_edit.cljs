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
            [cljs-dynamic-resources.core :as cdr]))

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
          (dom/div {:class "topic-edit-labels"}
            (dom/h3 {:class "topic-title oc-header"} section-title)
            (dom/label {:class "topic-description"} section-description))
          (dom/img {:class "check" :src check-img}))))))

(defn get-sections-data [category-sections]
  (apply merge
         (map (fn [section-data]
                (let [section-name (:section-name section-data)]
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

(defn setup-sortable [owner]
  (when (and (om/get-state owner :did-mount) (om/get-state owner :sortable-loaded))
    (let [ul-node (sel1 :ul.topic-list-sortable)]
      (.create js/Sortable ul-node (clj->js {:onSort (fn [_]
                                                         (let [li-elements (sel [:ul.topic-list-sortable :li])
                                                               items (map #(.-itemname (.-dataset %)) li-elements)]
                                                           (om/set-state! owner :sorted-sections items)))})))))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (cdr/add-script! "/lib/Sortable.js/Sortable.js"
                     (fn []
                       (om/set-state! owner :sortable-loaded true)
                       (setup-sortable owner)))
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (let [active-category (:active-category options)
          active-topics (get-in (:company-data data) [:sections (keyword active-category)])
          all-sections (:new-sections options)
          category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
          sections-list (vec (map :section-name category-sections))
          cleaned-sections (ordered-sections active-topics sections-list)]
      {:active-topics active-topics
       :sorted-sections cleaned-sections
       :sortable-loaded false
       :did-mount false}))

  (will-mount [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch)))

  (will-unmount [_]
    (utils/remove-channel "save-bt-navbar")
    (utils/remove-channel "cancel-bt-navbar"))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner)
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

  (render-state [_ {:keys [sorted-sections active-topics]}]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
      (let [active-category (:active-category options)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
            items (get-sections-data category-sections)]
        (dom/div {:class "topic-list-edit fix-top-margin-scrolling group no-select"}
          (apply dom/ul
                 ; props
                 {:class "topic-list-sortable"
                  :key "topic-list-edit"}
                 ; childrens
                 (map (fn [item-name]
                        (dom/li {:data-itemname item-name
                                 :key (str item-name (rand 5))
                                 :on-click (fn []
                                             (let [new-active-topics (if (utils/in? active-topics item-name)
                                                                       (utils/vec-dissoc active-topics item-name)
                                                                       (concat active-topics [item-name]))]
                                               (om/set-state! owner :active-topics new-active-topics)))}
                          (om/build item {:id item-name
                                          :item-data (get items (keyword item-name))
                                          :active-topics active-topics}))) sorted-sections)))))))