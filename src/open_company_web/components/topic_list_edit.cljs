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
            [cljs-dynamic-resources.core :as cdr]
            [shodan.console :as console]
            [shodan.inspection :refer (inspect)]))

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

(defn setup-sortable [owner options]
  (when (and (om/get-state owner :did-mount) (om/get-state owner :sortable-loaded))
    (let [ul-node (sel1 :ul.topic-list-sortable)]
      (.create js/Sortable ul-node (clj->js {:onStart (fn [e]
                                                        (let [item (.-itemname (.-dataset (.-item e)))
                                                              active-topics (om/get-state owner :active-topics)]
                                                          (when-not (utils/in? active-topics item)
                                                            (om/set-state! owner :active-topics (concat active-topics [item]))
                                                            ((:save-bt-active-cb options) true))))
                                             :onSort (fn [_]
                                                         (let [li-elements (sel [:ul.topic-list-sortable :li])
                                                               items (map #(.-itemname (.-dataset %)) li-elements)]
                                                           (om/set-state! owner :active-topics items)
                                                           ((:save-bt-active-cb options) true)))})))))

(defn topic-on-click [item-name owner save-bt-active-cb]
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
    (save-bt-active-cb (not= active-topics (om/get-state owner :initial-active-topics)))))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (cdr/add-script! "/lib/Sortable.js/Sortable.js"
                     (fn []
                       (om/set-state! owner :sortable-loaded true)
                       (setup-sortable owner options)))
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
          unactive-topics (reduce utils/vec-dissoc sections-list active-topics)]
      {:initial-active-topics active-topics
       :active-topics active-topics
       :unactive-topics unactive-topics
       :sortable-loaded false
       :did-mount false}))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner options)
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (let [active-topics (om/get-state owner :active-topics)]
            ((:save-sections-cb options) active-topics))))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:cancel-editing-cb options)))))))

  (render-state [_ {:keys [unactive-topics active-topics]}]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
      (let [active-category (:active-category options)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
            items (get-sections-data category-sections)]
        (dom/div {:class "topic-list-edit group no-select"}
          (dom/div {}
            (apply dom/ul
                   ; props
                   {:class "topic-list-sortable"
                    :key "topic-list-edit"}
                   ; childrens
                   (map (fn [item-name]
                          (dom/li {:data-itemname item-name
                                   :key item-name
                                   :on-click #(topic-on-click item-name owner (:save-bt-active-cb options))}
                            (om/build item {:id item-name
                                            :item-data (get items (keyword item-name))
                                            :active-topics active-topics}))) active-topics)))
          (dom/div {}
            (apply dom/ul
                   {:class "topic-list-unactive"
                    :key "topic-list-unactive"}
                   (map (fn [item-name]
                          (dom/li {:data-itemname item-name
                                   :key item-name
                                   :on-click #(topic-on-click item-name owner (:save-bt-active-cb options))}
                            (om/build item {:id item-name
                                            :item-data (get items (keyword item-name))
                                            :active-topics active-topics}))) unactive-topics))))))))