(ns open-company-web.components.topic-list-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.icon :as i]
            [open-company-web.components.topic :refer (topic)]
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
          section-description (or (:description section-data) "")]
      (dom/div {:class (utils/class-set {:topic-edit true
                                         :group true
                                         :topic-sortable true
                                         (str "topic-" section) true
                                         :active active})
                :key (str "topic-edit-" section-name)}
        (dom/div {:class "topic-edit-internal group"}
          (dom/div {:class "right"
                    :style {:margin "13px 10px 0 0"}}
             (i/icon :check-square-09 {:accent-color (when-not active "transparent")}))
          (dom/div {:class (utils/class-set {:topic-edit-handle-placeholder (not active)
                                             :topic-edit-handle active
                                             :group true})})
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
  (when (and (om/get-state owner :did-mount)
             (om/get-state owner :sortable-loaded))
    (when-let [ul-node (om/get-ref owner "topic-list-sortable")]
      (.create js/Sortable ul-node #js {:handle ".topic-edit-handle"
                                        :onStart #(dommy/add-class! ul-node :dragging)
                                        :onEnd (fn [_]
                                                 (dommy/remove-class! ul-node :dragging)
                                                 (let [li-active-elements (sel ul-node [:li.topic-active])
                                                       active-items (vec (map #(aget (.-dataset %) "itemname") li-active-elements))]
                                                   (om/set-state! owner :active-topics active-items)
                                                   ((:did-change-active-topics options) active-items)))}))))

(defn topic-on-click [item-name owner did-change-active-topics]
  (let [active-topics (om/get-state owner :active-topics)
        unactive-topics (om/get-state owner :unactive-topics)
        is-active (utils/in? active-topics item-name)
        new-active-topics (if is-active
                            (utils/vec-dissoc active-topics item-name)
                            (concat active-topics [item-name]))
        new-unactive-topics (if is-active
                              (concat unactive-topics [item-name])
                              (utils/vec-dissoc unactive-topics item-name))]
    (om/set-state! owner :active-topics (vec new-active-topics))
    (om/set-state! owner :unactive-topics (vec new-unactive-topics))
    (did-change-active-topics new-active-topics)))

(defn get-state [data old-state]
  (let [active-topics (:active-topics data)
        category (name (:category data))
        all-sections (:new-sections data)
        category-sections (:sections (first (filter #(= (:name %) category) (:categories all-sections))))
        sections-list (vec (map :section-name category-sections))
        unactive-topics (reduce utils/vec-dissoc sections-list active-topics)]
    {:initial-active-topics active-topics
     :active-topics active-topics
     :unactive-topics unactive-topics
     :sortable-loaded (or (:sortable-loaded old-state) false)
     :did-mount (or (:did-mount old-state) false)}))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (cdr/add-script! "/lib/Sortable.js/Sortable.js"
                     (fn []
                       (om/set-state! owner :sortable-loaded true)
                       (setup-sortable owner options)))
    ; load the new sections if needed
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (get-state data nil))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner options))

  (did-update [_ _ _]
    ; make sure the new-sections has been loaded and the data are available
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (setup-sortable owner options))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner (get-state next-props (om/get-state owner))))
    (setup-sortable owner options))

  (render-state [_ {:keys [unactive-topics active-topics]}]
    (let [slug (keyword (:slug @router/path))]
      (if (empty? (slug @caches/new-sections))
        (dom/h2 {:style #js {:display (if (:active data) "inline" "none")}} "Loading sections...")
        (let [current-category (name (:category data))
              all-sections (:new-sections data)
              category-sections (:sections (first (filter #(= (:name %) current-category) (:categories all-sections))))
              items (get-sections-data category-sections)]
          (dom/div {:class "topic-list-edit group no-select"
                    :style #js {:display (if (:active data) "inline" "none")}}
            (dom/div {}
              (dom/ul #js {:className "topic-list-sortable"
                           :ref "topic-list-sortable"
                           :key (apply str active-topics)}
                (for [item-name active-topics]
                  (dom/li #js {:data-itemname item-name
                               :className "topic-list-edit-li topic-active"
                               :key (str "active-" item-name)
                               :onClick #(topic-on-click item-name owner (:did-change-active-topics options))}
                    (om/build item {:id item-name
                                    :item-data (get items (keyword item-name))
                                    :active-topics active-topics})))))
            (dom/div {:class "topic-list-separator"})
            (dom/div {}
              (dom/ul #js {:className "topic-list-unactive"
                           :key (apply str active-topics)}
                (for [item-name unactive-topics]
                  (dom/li #js {:data-itemname item-name
                               :key (str "unactive-" item-name)
                               :className "topic-list-edit-li topic-unactive"
                               :onClick #(topic-on-click item-name owner (:did-change-active-topics options))}
                    (om/build item {:id item-name
                                    :item-data (get items (keyword item-name))
                                    :active-topics active-topics})))))))))))