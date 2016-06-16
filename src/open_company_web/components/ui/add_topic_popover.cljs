(ns open-company-web.components.ui.add-topic-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [cljsjs.react.dom]))

(defn find-dom-node [r]
  (.findDOMNode js/ReactDOM r))

(defn remove-listeners [owner]
  (events/unlistenByKey (om/get-state owner :click-out-listener))
  (om/set-state! owner :click-out-listener nil)
  (events/unlistenByKey (om/get-state owner :kb-listener))
  (om/set-state! owner :kb-listener nil)
  (events/unlistenByKey (om/get-state owner :nav-listener))
  (om/set-state! owner :nav-listener nil)
  (events/unlistenByKey (om/get-state owner :scroll-listener))
  (om/set-state! owner :scroll-listener nil))

(defn is-child-of-popover [el]
  (loop [cur-el el]
    (if (= (.-id cur-el) "add-topic-popover")
      true
      (if (.-parentNode cur-el)
        (recur (.-parentNode cur-el))
        false))))

(defn dismiss-popover [owner options]
  (remove-listeners owner)
  (.replaceState js/history #js {} "Dashboard" (oc-urls/company))
  ((:dismiss-popover options)))

(defn on-click-out [owner options e]
  (when-not (is-child-of-popover (.-target e))
    (dismiss-popover owner options)))

(defn on-click-in [owner options e]
  (.stopPropagation e))

(defn get-state [{:keys [active-topics-list all-topics archived-topics] :as data} old-state]
  (let [topics-list (map name (keys all-topics))
        sorted-archived-topics (sort #(compare (:title %1) (:title %2)) archived-topics)
        archived-topics-list (vec (map :section sorted-archived-topics))
        reduce-dissoc (partial reduce utils/vec-dissoc)
        unactive-topics (map name (-> topics-list
                                      (reduce-dissoc active-topics-list)
                                      (reduce-dissoc archived-topics-list)))
        unactive-equal? (= (set unactive-topics) (set (:unactive-topics old-state)))]
    {:active-topics active-topics-list
     :archived-topics-list archived-topics-list
     :unactive-topics (if unactive-equal? (:unactive-topics old-state) unactive-topics)
     :highlighted-topic nil
     :adding-custom-topic false
     :custom-topic-title ""}))

(defn add-topic-click [owner options topic]
  (let [all-topics (om/get-props owner :all-topics)
        topic-data (->> topic keyword (get all-topics))
        category-name (or (:category topic-data) "progress")]
    (dismiss-popover owner options)
    ((:did-change-active-topics options) category-name topic)))

(def down-arrow-key-code 40)
(def up-arrow-key-code 38)
(def enter-key-code 13)
(def esc-key-code 27)

(defn kb-key-down [owner options e]
  (when-not (om/get-state owner :adding-custom-topic)
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
          (when-let [topic (om/get-state owner :highlighted-topic)]
            (add-topic-click owner options topic))
          (= key-code esc-key-code)
          (dismiss-popover owner options)
          :else
          ; arrow key: change focus and scroll the parent div
          (let [all-topics            (vec (concat (vec (om/get-state owner :unactive-topics)) (vec (om/get-state owner :archived-topics-list))))
                cur-highlighted-topic (om/get-state owner :highlighted-topic)
                cur-highlighted       (.indexOf (to-array all-topics) cur-highlighted-topic)
                idx-fn                (cond
                                        (= key-code down-arrow-key-code) inc
                                        (= key-code up-arrow-key-code) dec)
                min-idx               0
                max-idx               (dec (count all-topics))
                next-highlighted-idx  (min (max (idx-fn cur-highlighted) min-idx) max-idx)
                next-highlighted      (get all-topics next-highlighted-idx)
                new-topic-div         (om/get-ref owner (str "potential-topic-" next-highlighted))
                topics-to-add        (om/get-ref owner "add-topic-popover-scroll")]
            (set! (.-scrollTop topics-to-add) (- (.-offsetTop new-topic-div) (.-offsetTop topics-to-add) 40))
            (cond
              (= key-code down-arrow-key-code)
              (om/set-state! owner :highlighted-topic next-highlighted)
              (= key-code up-arrow-key-code)
              (om/set-state! owner :highlighted-topic next-highlighted))))))))

(defn history-nav [options]
  ((:dismiss-popover options)))

(defn add-custom-topic [owner e]
  (utils/event-stop e)
  (om/set-state! owner :adding-custom-topic true))

(defn custom-topic-key-down [owner options e]
  (.stopPropagation e)
  (let [key-code (.-keyCode e)]
    (cond
      (and (= key-code enter-key-code)
           (pos? (count (om/get-state owner :custom-topic-title))))
      ; ENTER: add custom topic
      (let [topic-name (str "custom-" (utils/my-uuid))
            topic-title (om/get-state owner :custom-topic-title)
            new-topic-data {:title (str topic-title) :headline "" :body ""}]
        (dismiss-popover owner options)
        ((:did-change-active-topics options) :progress topic-name new-topic-data))
      (= key-code esc-key-code)
      ; ESC: exit adding topic
      (om/set-state! owner :adding-custom-topic false))))

(def popover-max-height 312)

(defn fix-position [owner]
  (when-let [add-topic-popover (find-dom-node (om/get-ref owner "add-topic-popover"))]
    (let [show-above (om/get-props owner :show-above)
          triangle (find-dom-node (om/get-ref owner "triangle"))
          popover-height (.-clientHeight add-topic-popover)
          popover-top (if-not show-above "29px" (str (+ (* (- popover-max-height 55) -1) (- popover-max-height popover-height)) "px"))
          triangle-top (if-not show-above "8px" (str (- popover-height 20) "px"))]
      (setStyle add-topic-popover #js {:top popover-top})
      (setStyle triangle #js {:top triangle-top}))))

(defn scrolled [owner]
  (let [add-topic-popover-scroll (om/get-ref owner "add-topic-popover-scroll")]
    (println "scroll:" (.-scrollTop add-topic-popover-scroll))))

(defn add-listeners [owner options]
  (when-not (om/get-state owner :click-out-listener)
    (let [click-listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))]
      (om/set-state! owner :click-out-listener click-listener)))
  (when-not (om/get-state owner :kb-listener)
    (let [kb-listener (events/listen (sel1 [:body]) EventType/KEYDOWN (partial kb-key-down owner options))]
      (om/set-state! owner :kb-listener kb-listener)))
  (when-not (om/get-state owner :nav-listener)
    (let [nav-listener (events/listen @router/history HistoryEventType/NAVIGATE (partial history-nav options))]
      (om/set-state! owner :nav-listener nav-listener)))
  (when-not (om/get-state owner :scroll-listener)
    (let [add-topic-popover-scroll (om/get-ref owner "add-topic-popover-scroll")
          scroll-listener (events/listen add-topic-popover-scroll EventType/SCROLL #(scrolled owner))]
      (om/set-state! owner :scroll-listener scroll-listener))))

(defcomponent add-topic-popover [{:keys [all-topics active-topics-list column show-above archived-topics] :as data} owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    (get-state data nil))

  (will-mount [_]
    (.pushState js/history #js {} "Add topic" (str (oc-urls/company) "#add-topic")))

  (did-mount [_]
    (add-listeners owner options)
    (fix-position owner))

  (will-receive-props [_ next-props]
    (add-listeners owner options)
    (when-not (= next-props data)
      (om/set-state! owner (get-state next-props (om/get-state owner)))))

  (will-unmount [_]
    (remove-listeners owner))

  (did-update [_ _ prev-state]
    (add-listeners owner options)
    (fix-position owner)
    (when (and (om/get-state owner :adding-custom-topic)
               (not (:adding-custom-topic prev-state)))
      (.focus (find-dom-node (om/get-ref owner "add-custom-topic-input")))))

  (render-state [_ {:keys [active-topics unactive-topics highlighted-topic archived-topics-list adding-custom-topic custom-topic-title]}]
    (let [has-archived-topics (pos? (count archived-topics-list))
          has-unactive-topics (pos? (count unactive-topics))]
      (dom/div #js {:className (utils/class-set {:add-topic-popover true
                                                 (str "column-" column) true
                                                 :group true
                                                 :show-above show-above})
                    :id "add-topic-popover"
                    :ref "add-topic-popover"
                    :on-click (partial on-click-in owner options)}
        (dom/div #js {:className "triangle"
                      :ref "triangle"})
        (dom/div {:class "add-topic-popover-header"} "CHOOSE A TOPIC")
        (when has-unactive-topics
          (dom/div {:class "add-topic-popover-subheader"} "SUGGESTED TOPICS"))
        (when (and (not has-unactive-topics)
                   has-archived-topics)
          (dom/div {:class "add-topic-popover-subheader"} "ARCHIVED TOPICS"))
        (dom/div #js {:className "add-topic-popover-scroll group"
                      :ref "add-topic-popover-scroll"}
          (when has-unactive-topics
            (dom/div {:class "topics-to-add first-content"}
              (for [idx (range (count unactive-topics))
                    :let [topic (get (vec unactive-topics) idx)
                          topic-data (->> topic keyword (get all-topics))]]
                (dom/div #js {:className (str "potential-topic" (when (= highlighted-topic topic) " highlighted"))
                              :ref (str "potential-topic-" topic)
                              :data-topic topic
                              :onMouseOver #(om/set-state! owner :highlighted-topic topic)
                              :onClick #(add-topic-click owner options topic)}

                  (:title topic-data)))))
          (when (and has-unactive-topics
                     has-archived-topics)
            (dom/div {:class (str "add-topic-popover-subheader " (if has-unactive-topics "second-header" "first-header"))} "ARCHIVED TOPICS"))
          (when has-archived-topics
            (dom/div {:class "topics-to-add"}
              (for [idx (range (count archived-topics-list))
                    :let [topic (get (vec archived-topics-list) idx)
                          topic-data (->> topic keyword (get all-topics))
                          topic-title (:title (first (filter #(= (:section %) topic) archived-topics)))]]
                (dom/div #js {:className (str "potential-topic" (when (= highlighted-topic topic) " highlighted"))
                              :ref (str "potential-topic-" topic)
                              :data-topic topic
                              :onMouseOver #(om/set-state! owner :highlighted-topic topic)
                              :onClick #(add-topic-click owner options topic)}
                  topic-title)))))
        (dom/div {:class "add-custom-topic-container"
                  :on-click #(when-not adding-custom-topic (add-custom-topic owner %))}
          (if-not adding-custom-topic
            (dom/label {} "+ Or Add New Topic")
            (dom/input #js {:type "text"
                            :className "add-custom-topic-input"
                            :ref "add-custom-topic-input"
                            :value custom-topic-title
                            :onChange #(om/set-state! owner :custom-topic-title (.-value (.-target %)))
                            :onKeyDown  (partial custom-topic-key-down owner options)
                            :placeholder "Topic title"})))))))