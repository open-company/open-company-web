(ns open-company-web.components.su-snapshot
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.footer :refer (footer)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.company-header :refer (company-header)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]
            [cljsjs.hammer]))

(defn close-overlay-cb [owner]
  (om/set-state! owner :transitioning false)
  (om/set-state! owner :selected-topic nil)
  (om/set-state! owner :selected-metric nil)
  (.pushState js/history nil "Stakeholder update" (oc-urls/stakeholder-update (router/current-company-slug) (router/current-stakeholder-update-slug))))

(defn topic-click [owner topic selected-metric]
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric)
  (.pushState js/history nil (name topic) (oc-urls/stakeholder-update-section  (router/current-company-slug) (router/current-stakeholder-update-slug) topic)))

(defn switch-topic [owner is-left?]
  (when (and (om/get-state owner :topic-navigation)
             (om/get-state owner :selected-topic)
             (nil? (om/get-state owner :tr-selected-topic)))
    (let [selected-topic (om/get-state owner :selected-topic)
          topics         (:sections (dis/stakeholder-update-data))
          current-idx    (.indexOf (to-array topics) selected-topic)]
      (if is-left?
        ;prev
        (let [prev-idx (mod (dec current-idx) (count topics))
              prev-topic (get (vec topics) prev-idx)]
          (om/set-state! owner :tr-selected-topic prev-topic))
        ;next
        (let [next-idx (mod (inc current-idx) (count topics))
              next-topic (get (vec topics) next-idx)]
          (om/set-state! owner :tr-selected-topic next-topic))))))

(defn kb-listener [owner e]
  (let [key-code (.-keyCode e)]
    (when (= key-code 39)
      ;next
      (switch-topic owner false))
    (when (= key-code 37)
      (switch-topic owner true))))

(defn animation-finished [owner]
  (let [cur-state (om/get-state owner)
        new-topic (:tr-selected-topic cur-state)]
    (.pushState js/history nil (name new-topic) (oc-urls/stakeholder-update-section (router/current-company-slug) (router/current-stakeholder-update-slug) new-topic))
    (om/set-state! owner (merge cur-state {:selected-topic (:tr-selected-topic cur-state)
                                           :transitioning true
                                           :tr-selected-topic nil}))))

(defn animate-selected-topic-transition [owner]
  (let [selected-topic (om/get-ref owner "selected-topic")
        tr-selected-topic (om/get-ref owner "tr-selected-topic")
        fade-anim (new Fade selected-topic 1 0 utils/oc-animation-duration)
        cur-state (om/get-state owner)]
    (.play (new Fade tr-selected-topic 0 1 utils/oc-animation-duration))
    (doto fade-anim
      (.listen AnimationEventType/FINISH #(animation-finished owner))
      (.play))))

(defcomponent su-snapshot [data owner options]

  (init-state [_]
    {:selected-topic (router/current-section)
     :selected-metric nil
     :topic-navigation true
     :transitioning false
     :columns-num (responsive/columns-num)})

  (did-mount [_]
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num)))
    (when-not (utils/is-test-env?)
      (let [kb-listener (events/listen js/window EventType/KEYDOWN (partial kb-listener owner))
            swipe-listener (js/Hammer (sel1 [:div#app]))];(.-body js/document))]
        (om/set-state! owner :kb-listener kb-listener)
        (om/set-state! owner :swipe-listener swipe-listener)
        (.on swipe-listener "swipeleft" (fn [e] (switch-topic owner true)))
        (.on swipe-listener "swiperight" (fn [e] (switch-topic owner false))))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      (events/unlistenByKey (om/get-state owner :kb-listener))
      (let [swipe-listener (om/get-state owner :swipe-listener)]
        (.off swipe-listener "swipeleft")
        (.off swipe-listener "swiperight"))))

  (did-update [_ _ _]
    (when (om/get-state owner :tr-selected-topic)
      (animate-selected-topic-transition owner)))

  (render-state [_ {:keys [selected-topic tr-selected-topic selected-metric transitioning columns-num]}]
    (let [company-data (dis/company-data data)
          su-data      (dis/stakeholder-update-data)
          card-width   (responsive/calc-card-width 1)
          ww           (.-clientWidth (sel1 js/document :body))
          total-width  (if (> ww 413) (str (min ww (+ card-width 100)) "px") "auto")
          su-subtitle  (str "- " (utils/date-string (js/Date.) true))]
      (dom/div {:class "su-snapshot main-scroll"}
        (dom/div {:class "page"}
          ;; SU Snapshot
          (when company-data
            (dom/div {:class "su-sp-content"}
              ;; Fullscreen topic
              (when selected-topic
                (dom/div {:class "selected-topic-container"
                          :style #js {:opacity (if selected-topic 1 0)}}
                  (dom/div #js {:className "selected-topic"
                                :key (str "transition-" selected-topic)
                                :ref "selected-topic"
                                :style #js {:opacity 1 :backgroundColor "rgba(255, 255, 255, 0.98)"}}
                    (om/build fullscreen-topic {:section selected-topic
                                                :section-data (->> selected-topic keyword (get su-data))
                                                :change-url false
                                                :selected-metric selected-metric
                                                :read-only true
                                                :card-width card-width
                                                :currency (:currency company-data)
                                                :hide-history-navigation true
                                                :animate (not transitioning)}
                                               {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                                       :topic-navigation #(om/set-state! owner :topic-navigation %)}}))
                  ;; Fullscreen topic for transition
                  (when tr-selected-topic
                    (dom/div #js {:className "tr-selected-topic"
                                  :key (str "transition-" tr-selected-topic)
                                  :ref "tr-selected-topic"
                                  :style #js {:opacity (if tr-selected-topic 0 1)}}
                    (om/build fullscreen-topic {:section tr-selected-topic
                                                :section-data (->> tr-selected-topic keyword (get su-data))
                                                :selected-metric selected-metric
                                                :read-only (:read-only company-data)
                                                :card-width card-width
                                                :currency (:currency company-data)
                                                :hide-history-navigation true
                                                :animate false}
                                               {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                                       :topic-navigation #(om/set-state! owner :topic-navigation %)}})))))
              (dom/div {:class "su-sp-company-header"}
                (dom/img {:class "company-logo" :src (:logo company-data)})
                (dom/span {:class "company-name"} (:name company-data)))
              (when (:title su-data)
                (dom/div {:class "su-snapshot-title"} (:title su-data)))
              (dom/div {:class "su-snapshot-subtitle"} su-subtitle)
              (om/build topics-columns {:columns-num 1
                                        :card-width card-width
                                        :total-width total-width
                                        :content-loaded (not (:loading data))
                                        :topics (:sections su-data)
                                        :topics-data su-data
                                        :company-data company-data
                                        :hide-add-topic true}
                                       {:opts {:topic-click (partial topic-click owner)}})))
          ;;Footer
          (when company-data
            (om/build footer {:su-preview true
                              :card-width card-width
                              :columns-num 1})))))))