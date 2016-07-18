(ns open-company-web.components.topic-list-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.topic :refer (topic)]
            [cljs-dynamic-resources.core :as cdr]
            [goog.style :refer (setStyle)]))

(defcomponent item [data owner options]
  (render [_]
    (let [topic (:id data)
          transition? (:transition data)
          topic-data (:item-data data)
          active-topics (:active-topics data)
          active (utils/in? active-topics topic)
          topic-name (or (:name topic-data) (utils/camel-case-str topic))
          topic-icon (:icon topic-data)
          topic-title (or (:title topic-data) topic-name)
          topic-description (or (:description topic-data) "")]
      (dom/div {:class (utils/class-set {:topic-edit true
                                         :group true
                                         :topic-sortable true
                                         (str "topic-" topic) true
                                         :active active})
                :key (str "topic-edit-" topic-name)}
        (dom/div {:class "topic-edit-internal group"}
          (dom/div {:class "right"
                    :on-click (:checkbox-click-cb data)
                    :style {:margin "13px 10px 0 0"}}
             (i/icon :check-square-09 {:accent-color (if active (occ/get-color-by-kw :blue) "transparent")}))
          (dom/div {:class (utils/class-set {:topic-edit-handle-placeholder (not active)
                                             :topic-edit-handle active
                                             :group true})})
          (when-not (clojure.string/blank? topic-icon)
            (i/icon topic-icon))
          (dom/div {:class "topic-edit-labels"}
            (dom/h3 {:class "topic-title oc-header"} topic-title)
            (dom/label {:class "topic-description"} topic-description)))))))

(defn setup-sortable [owner options]
  (when (and (om/get-state owner :did-mount)
             (om/get-state owner :sortable-loaded))
    (when-let [ul-node (om/get-ref owner "topic-list-sortable")]
      (.create js/Sortable ul-node #js {:handle ".topic-edit.active"
                                        :onStart #(dommy/add-class! ul-node :dragging)
                                        :onEnd (fn [_]
                                                 (dommy/remove-class! ul-node :dragging)
                                                 (let [li-active-elements (sel ul-node [:li.topic-active])
                                                       active-items (vec (map #(aget (.-dataset %) "itemname") li-active-elements))]
                                                   (om/set-state! owner :active-topics active-items)
                                                   ((:did-change-active-topics options) active-items)))}))))

(defn topic-on-click [item-name owner did-change-active-topics e]
  (.stopPropagation e)
  (when-not (om/get-state owner :transition-topic)
    (om/set-state! owner :transition-topic item-name)))

(defn transition-topic-finish [owner did-change-active-topics]
  (let [current-state (om/get-state owner)
        item-name (:transition-topic current-state)]
    (when item-name
      (let [active-topics (om/get-state owner :active-topics)
            inactive-topics (om/get-state owner :inactive-topics)
            is-active (utils/in? active-topics item-name)
            new-active-topics (if is-active
                                (utils/vec-dissoc active-topics item-name)
                                (conj active-topics item-name))
            new-inactive-topics (if is-active
                                  (concat [item-name] inactive-topics)
                                  (utils/vec-dissoc inactive-topics item-name))]
    (om/set-state! owner (merge current-state {:active-topics (vec new-active-topics)
                                               :inactive-topics (vec new-inactive-topics)
                                               :transition-topic nil}))
    (did-change-active-topics new-active-topics)))))

(defn get-state [{:keys [active-topics-list all-topics]} old-state]
  (let [topics-list (map name (keys all-topics))
        inactive-topics (map name (reduce utils/vec-dissoc topics-list active-topics-list))
        inactive-equal? (= (set inactive-topics) (set (:inactive-topics old-state)))]
    {:active-topics active-topics-list
     :inactive-topics (if inactive-equal? (:inactive-topics old-state) inactive-topics)
     :sortable-loaded (or (:sortable-loaded old-state) false)
     :did-mount (or (:did-mount old-state) false)}))

(defcomponent topic-list-edit [{:keys [all-topics active-topics-list] :as data} owner options]

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
    (setup-sortable owner options)
    (when (om/get-state owner :transition-topic)
      (js/setTimeout #(transition-topic-finish owner (:did-change-active-topics options)) 250)))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner (get-state next-props (om/get-state owner))))
    (setup-sortable owner options))

  (render-state [_ {:keys [inactive-topics active-topics transition-topic]}]
    (let [slug (keyword (router/current-company-slug))]
      (if (empty? (slug @caches/new-sections))
        (dom/h2 {} "Loading sections...")
        (dom/div {:class "topic-list-edit group no-select"
                  :style #js {:display (if (:active data) "inline" "none")}}
          (dom/div {}
            (dom/ul #js {:className "topic-list-sortable"
                         :ref "topic-list-sortable"
                         :key (apply str active-topics)}
              (for [item-name active-topics]
                (when (not= transition-topic item-name)
                  (dom/li #js {:data-itemname item-name
                               :className (utils/class-set {:topic-list-edit-li true
                                                            :topic-active true
                                                            :tt-collapse (= item-name transition-topic)})
                               :key (str "active-" item-name)}
                    (om/build item {:id item-name
                                    :item-data (get all-topics (keyword item-name))
                                    :checkbox-click-cb #(topic-on-click item-name owner (:did-change-active-topics options) %)
                                    :active-topics active-topics}))))
              (when (and transition-topic
                         (not (utils/in? active-topics transition-topic)))
                (dom/li #js {:data-itemname transition-topic
                             :key (str "active-" transition-topic)
                             :className "topic-list-edit-li topic-active tt-expand"}
                  (om/build item {:id transition-topic
                                  :transition true
                                  :item-data (get all-topics (keyword transition-topic))
                                  :checkbox-click-cb #(topic-on-click transition-topic owner (:did-change-active-topics options) %)
                                  :active-topics (conj active-topics transition-topic)}))))
          (when (seq inactive-topics)
            (dom/div {:class "topic-list-separator"}))
          (dom/div {}
            (dom/ul #js {:className "topic-list-inactive"
                         :key (apply str inactive-topics)}
              (when (and transition-topic
                         (not (utils/in? inactive-topics transition-topic)))
                (dom/li #js {:data-itemname transition-topic
                             :key (str "inactive-" transition-topic)
                             :className "topic-list-edit-li topic-inactive tt-expand"}
                  (om/build item {:id transition-topic
                                  :transition true
                                  :item-data (get all-topics (keyword transition-topic))
                                  :checkbox-click-cb #(topic-on-click transition-topic owner (:did-change-active-topics options) %)
                                  :active-topics (utils/vec-dissoc active-topics transition-topic)})))
              (for [item-name inactive-topics]
                (when (not= transition-topic item-name)
                  (dom/li #js {:data-itemname item-name
                               :key (str "inactive-" item-name)
                               :className (utils/class-set {:topic-list-edit-li true
                                                            :topic-inactive true
                                                            :tt-collapse (= item-name transition-topic)})}
                    (om/build item {:id item-name
                                    :item-data (get all-topics (keyword item-name))
                                    :checkbox-click-cb #(topic-on-click item-name owner (:did-change-active-topics options) %)
                                    :active-topics active-topics}))))))))))))