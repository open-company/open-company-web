(ns open-company-web.components.ui.add-topic-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defn is-child-of-popover [el]
  (loop [cur-el el]
    (if (= (.-id cur-el) "add-topic-popover")
      true
      (if (.-parentNode cur-el)
        (recur (.-parentNode cur-el))
        false))))

(defn dismiss-popover [options]
  (.replaceState js/history #js {} "Dashboard" (oc-urls/company))
  ((:dismiss-popover options)))

(defn on-click-out [owner options e]
  (when (not (is-child-of-popover (.-target e)))
    (dismiss-popover options)))

(defn on-click-in [owner options e]
  (.stopPropagation e))

(defn get-state [{:keys [active-topics-list all-topics]} old-state]
  (let [topics-list (map name (keys all-topics))
        unactive-topics (map name (reduce utils/vec-dissoc topics-list active-topics-list))
        unactive-equal? (= (set unactive-topics) (set (:unactive-topics old-state)))]
    {:active-topics active-topics-list
     :unactive-topics (if unactive-equal? (:unactive-topics old-state) unactive-topics)
     :highlighted-topic -1}))

(defn add-topic-click [owner options topic]
  (let [all-topics (om/get-props owner :all-topics)
        topic-data (->> topic keyword (get all-topics))
        category-name (:category topic-data)]
    ((:did-change-active-topics options) category-name topic)
    (dismiss-popover options)))

(def down-arrow-key-code 40)
(def up-arrow-key-code 38)
(def enter-key-code 13)
(def esc-key-code 27)

(defn kb-key-down [owner options e]
  (let [key-code        (.-keyCode e)
        unactive-topics (om/get-state owner :unactive-topics)]
    (when (or (= key-code down-arrow-key-code)
              (= key-code up-arrow-key-code)
              (= key-code enter-key-code)
              (= key-code esc-key-code))
      (.stopPropagation e)
      (.preventDefault e)
      (cond
        (= key-code enter-key-code)
        ; enter key: select topic
        (let [topic (get (vec unactive-topics) (om/get-state owner :highlighted-topic))]
          (add-topic-click owner options topic))
        (= key-code esc-key-code)
        (dismiss-popover options)
        :else
        ; arrow key: change focus and scroll the parent div
        (let [cur-highlighted  (om/get-state owner :highlighted-topic)
              idx-fn           (cond
                                 (= key-code down-arrow-key-code) inc
                                 (= key-code up-arrow-key-code) dec)
              min-idx          0
              max-idx          (dec (count unactive-topics))
              next-highlighted (min (max (idx-fn cur-highlighted) min-idx) max-idx)
              topics-to-add    (om/get-ref owner "topics-to-add")
              new-topic-div    (om/get-ref owner (str "potential-topic-" next-highlighted))]
          (set! (.-scrollTop topics-to-add) (- (.-offsetTop new-topic-div) (.-offsetTop topics-to-add)))
          (cond
            (= key-code down-arrow-key-code)
            (om/set-state! owner :highlighted-topic next-highlighted)
            (= key-code up-arrow-key-code)
            (om/set-state! owner :highlighted-topic next-highlighted)))))))

(defn history-nav [options]
  ((:dismiss-popover options)))

(defcomponent add-topic-popover [{:keys [all-topics active-topics-list column show-above] :as data} owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (get-state data nil))

  (will-mount [_]
    (.pushState js/history #js {} "Add topic" (str (oc-urls/company) "#add-topic")))

  (did-mount [_]
    (let [click-listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))
          kb-listener (events/listen (sel1 [:body]) EventType/KEYDOWN (partial kb-key-down owner options))
          nav-listener (events/listen @router/history HistoryEventType/NAVIGATE (partial history-nav options))]
      (om/set-state! owner :click-out-listener click-listener)
      (om/set-state! owner :kb-listener kb-listener)
      (om/set-state! owner :nav-listener nav-listener)))

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner (get-state next-props (om/get-state owner)))))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-out-listener))
    (events/unlistenByKey (om/get-state owner :kb-listener))
    (events/unlistenByKey (om/get-state owner :nav-listener)))

  (render-state [_ {:keys [active-topics unactive-topics highlighted-topic]}]
    (dom/div {:class (utils/class-set {:add-topic-popover true
                                       (str "column-" column) true
                                       :show-above show-above})
              :id "add-topic-popover"
              :on-click (partial on-click-in owner options)}
      (dom/div {:class "triangle"})
      (dom/div {:class "add-topic-popover-header"} "CHOOSE A TOPIC")
      (dom/div {:class "add-topic-popover-subheader"} "SUGGESTED TOPICS")
      (dom/div #js {:className "topics-to-add"
                    :ref "topics-to-add"}
        (for [idx (range (count unactive-topics))
              :let [topic (get (vec unactive-topics) idx)
                    topic-data (->> topic keyword (get all-topics))]]
          (dom/div #js {:className (str "potential-topic" (when (= highlighted-topic idx) " highlighted"))
                        :ref (str "potential-topic-" idx)
                        :onMouseOver #(om/set-state! owner :highlighted-topic idx)
                        :onClick #(add-topic-click owner options topic)}
            (:title topic-data)))))))