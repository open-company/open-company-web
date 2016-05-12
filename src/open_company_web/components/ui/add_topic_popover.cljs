(ns open-company-web.components.ui.add-topic-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]))

(defn is-child-of-popover [el]
  (loop [cur-el el]
    (if (= (.-id cur-el) "add-topic-popover")
      true
      (if (.-parentNode cur-el)
        (recur (.-parentNode cur-el))
        false))))

(defn on-click-out [owner options e]
  (when (not (is-child-of-popover (.-target e)))
    ((:dismiss-popover options))))

(defn on-click-in [owner options e]
  (.stopPropagation e))

(defn get-state [{:keys [active-topics-list all-topics]} old-state]
  (let [topics-list (map name (keys all-topics))
        unactive-topics (map name (reduce utils/vec-dissoc topics-list active-topics-list))
        unactive-equal? (= (set unactive-topics) (set (:unactive-topics old-state)))]
    {:active-topics active-topics-list
     :unactive-topics (if unactive-equal? (:unactive-topics old-state) unactive-topics)}))

(defn add-topic-click [owner options topic]
  (let [active-topics (om/get-state owner :active-topics)
        new-active-topics (concat active-topics [topic])]
    ((:did-change-active-topics options) new-active-topics)
    ((:dismiss-popover options))))

(defcomponent add-topic-popover [{:keys [all-topics active-topics-list] :as data} owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (get-state data nil))

  (did-mount [_]
    (let [listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))]
      (om/set-state! owner :click-out-listener listener)))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner (get-state next-props (om/get-state owner)))))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-out-listener)))

  (render-state [_ {:keys [active-topics unactive-topics]}]
    (dom/div {:class "add-topic-popover"
              :id "add-topic-popover"
              :on-click (partial on-click-in owner options)}
      (dom/div {:class "triangle"})
      (dom/div {:class "add-topic-popover-header"} "CHOOSE A TOPIC")
      (dom/div {:class "add-topic-popover-subheader"} "SUGGESTED TOPICS")
      (dom/div {:class "topics-to-add"}
        (for [topic unactive-topics
              :let [topic-data (->> topic keyword (get all-topics))]]
          (dom/div {:class "potential-topic"
                    :on-click #(add-topic-click owner options topic)}
            (:title topic-data)))))))