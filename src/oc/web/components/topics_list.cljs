(ns oc.web.components.topics-list
  "
  Display either a dashboard listing of topics in 1-3 columns, or a selected topic full-screen.

  Handle topic selection, topic navigation, and share initiation.
  "
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.raven :as sentry]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            ; [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [oc.web.components.topics-columns :refer (topics-columns)]
            [oc.web.components.topics-mobile-layout :refer (topics-mobile-layout)]
            [goog.events :as events]
            [goog.object :as gobj]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade Slide)]
            [cljsjs.hammer]))

(def scrolled-to-top (atom false))

;; ===== Events =====

(defn- topic-click [owner topic selected-metric & [force-edit]]
  (let [org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)]
    (.pushState js/history nil (name topic) (oc-urls/topic org-slug board-slug (name topic))))
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric))

(defn- close-overlay-cb [owner]
  (.pushState js/history nil "Dashboard" (oc-urls/board (router/current-org-slug) (router/current-board-slug)))
  (om/set-state! owner (merge (om/get-state owner) {:transitioning false
                                                    :selected-topic nil
                                                    :selected-metric nil})))

(defn- switch-topic [owner is-left?]
  (when (and (om/get-state owner :topic-navigation)
             (om/get-state owner :selected-topic)
             (nil? (om/get-state owner :tr-selected-topic)))
    (let [selected-topic (om/get-state owner :selected-topic)
          active-topics (om/get-state owner :active-topics)
          topics-list (keys active-topics)
          current-idx (.indexOf (vec topics-list) selected-topic)]
      (om/set-state! owner :animation-direction is-left?)
      (if is-left?
        ;prev
        (let [prev-idx (mod (dec current-idx) (count topics-list))
              prev-topic (get (vec topics-list) prev-idx)]
          (om/set-state! owner :tr-selected-topic prev-topic))
        ;next
        (let [next-idx (mod (inc current-idx) (count topics-list))
              next-topic (get (vec topics-list) next-idx)]
          (om/set-state! owner :tr-selected-topic next-topic))))))

(defn- kb-listener [owner e]
  (let [key-code (.-keyCode e)]
    (when (= key-code 39)
      ;next
      (switch-topic owner false))
    (when (= key-code 37)
      (switch-topic owner true))))

;; ===== Animation =====

(defn- animation-finished [owner]
  (let [cur-state (om/get-state owner)]
    (.pushState js/history nil (name (:tr-selected-topic cur-state)) (oc-urls/topic (router/current-org-slug) (router/current-board-slug) (:tr-selected-topic cur-state)))
    (om/set-state! owner (merge cur-state {:selected-topic (:tr-selected-topic cur-state)
                                           :transitioning true
                                           :tr-selected-topic nil}))))

(defn- animate-selected-topic-transition [owner left?]
  (let [selected-topic (om/get-ref owner "selected-topic")
        tr-selected-topic (om/get-ref owner "tr-selected-topic")
        width (responsive/fullscreen-topic-width (om/get-state owner :card-width))
        fade-anim (new Slide selected-topic #js [0 0] #js [(if left? width (* width -1)) 0] utils/oc-animation-duration)
        cur-state (om/get-state owner)]
    (doto fade-anim
      (.listen AnimationEventType/FINISH #(animation-finished owner))
      (.play))
    (.play (new Fade selected-topic 1 0 utils/oc-animation-duration))
    (.play (new Slide tr-selected-topic #js [(if left? (* width -1) width) 0] #js [0 0] utils/oc-animation-duration))
    (.play (new Fade tr-selected-topic 0 1 utils/oc-animation-duration))))

;; ===== Topic List Component =====

(defn- get-state [owner data current-state]
  ; get internal component state
  (let [board-data (:board-data data)
        active-topics (apply merge (map #(hash-map (keyword %) (->> % keyword (get board-data))) (:topics board-data)))
        selected-topic (when current-state (:selected-topic current-state))]
    {; initial active topics to check with the updated active topics
     :initial-active-topics active-topics
     ; actual active topics possibly changed by the user
     :active-topics active-topics
     ; card with
     :card-width (:card-width data)
     ; remember if the /slug/new call was already started
     :new-topics-requested (or (:new-topics-requested current-state) false)
     ; selected topic for fullscreen
     :selected-topic selected-topic
     ; transitioning btw fullscreen topics, navigated with kb arrows or swipe on mobile
     :tr-selected-topic nil
     ; enamble/disable fullscreen topic navigation
     :topic-navigation (or (:topic-navigation current-state) true)
     ; transitioning btw fullscreen topics
     :transitioning false
     ; redirect the user to the updates preview page
     :redirect-to-preview (or (:redirect-to-preview current-state) false)}))

;; -------------------------------------------------

(def card-x-margins 20)
(def columns-layout-padding 20)

(defcomponent topics-list [data owner options]

  (init-state [_]
    (get-state owner data nil))

  (did-mount [_]
    ; scroll to top when the component is initially mounted to
    ; make sure the calculation for the fixed navbar are correct
    (when-not @scrolled-to-top
      (set! (.-scrollTop (.-body js/document)) 0)
      (reset! scrolled-to-top true))
    (when (not (utils/is-test-env?))
      (when-not (responsive/user-agent-mobile?)
        (let [kb-listener (events/listen js/window EventType/KEYDOWN (partial kb-listener owner))]
          (om/set-state! owner :kb-listener kb-listener)))))

  (will-unmount [_]
    (when (and (not (utils/is-test-env?))
               (not (responsive/user-agent-mobile?)))
      (events/unlistenByKey (om/get-state owner :kb-listener))))

  (will-receive-props [_ next-props]
    (when-let* [new-topic-foce (om/get-state owner :new-topic-foce)
                new-topic-data (-> next-props :board-data new-topic-foce)]
      (dispatcher/dispatch! [:start-foce new-topic-foce new-topic-data]))
    (when (om/get-state owner :redirect-to-preview)
      (utils/after 100 #(router/nav! (oc-urls/stakeholder-update-preview))))
    (when-not (= (:board-data next-props) (:board-data data))
      (om/set-state! owner (get-state owner next-props (om/get-state owner))))
    (let [board-data            (:board-data next-props)
          topics                  (vec (:topics board-data))
          no-placeholder-topics (utils/filter-placeholder-topics topics board-data)]
      (when (and (:force-edit-topic next-props) (contains? board-data (keyword (:force-edit-topic next-props))))
        (om/set-state! owner :selected-topic (dispatcher/force-edit-topic)))))

  (did-update [_ prev-props _]
    (when-not (utils/is-test-env?)
      (when (om/get-state owner :tr-selected-topic)
        (animate-selected-topic-transition owner (om/get-state owner :animation-direction)))))

  (render-state [_ {:keys [active-topics
                           selected-topic
                           selected-metric
                           tr-selected-topic
                           transitioning
                           redirect-to-preview
                           rerender]}]
    (let [board-slug    (router/current-board-slug)
          board-data    (:board-data data)
          board-topics  (filter #(contains? board-data %) (vec (map keyword (:topics board-data))))
          card-width    (:card-width data)
          columns-num   (:columns-num data)
          ww            (responsive/ww)
          total-width   (if (and (= columns-num 1)
                                (< ww responsive/c1-min-win-width))
                          "auto"
                          (str (responsive/total-layout-width-int card-width columns-num) "px"))
          can-edit-secs (utils/can-edit-topics? board-data)]
      (dom/div {:class (utils/class-set {:topic-list true
                                         :group true
                                         :editable can-edit-secs})
                :data-rerender rerender
                :key (str "topic-list-" rerender)}
        ; ;; Fullscreen topic
        ; (when selected-topic
        ;   (dom/div {:class "selected-topic-container"
        ;             :style #js {:opacity (if selected-topic 1 0)}}
        ;       (dom/div #js {:className "selected-topic"
        ;                     :key (str "transition-" selected-topic)
        ;                     :ref "selected-topic"
        ;                     :style #js {:opacity 1}}
        ;         (om/build fullscreen-topic {:topic selected-topic
        ;                                     :topic-data (->> selected-topic keyword (get board-data))
        ;                                     :selected-metric selected-metric
        ;                                     :read-only (:read-only board-data)
        ;                                     :card-width card-width
        ;                                     :currency (:currency board-data)
        ;                                     :animate (not transitioning)}
        ;                                    {:opts {:close-overlay-cb #(close-overlay-cb owner)
        ;                                            :topic-navigation #(om/set-state! owner :topic-navigation %)}}))
        ;     ;; Fullscreen topic for transition
        ;     (when tr-selected-topic
        ;       (dom/div #js {:className "tr-selected-topic"
        ;                     :key (str "transition-" tr-selected-topic)
        ;                     :ref "tr-selected-topic"
        ;                     :style #js {:opacity (if tr-selected-topic 0 1)}}
        ;       (om/build fullscreen-topic {:topic tr-selected-topic
        ;                                   :topic-data (->> tr-selected-topic keyword (get board-data))
        ;                                   :selected-metric selected-metric
        ;                                   :read-only (:read-only board-data)
        ;                                   :card-width card-width
        ;                                   :currency (:currency board-data)
        ;                                   :animate false}
        ;                                  {:opts {:close-overlay-cb #(close-overlay-cb owner)
        ;                                          :topic-navigation #(om/set-state! owner :topic-navigation %)}})))))
        ;; Topics list columns
        (let [comp-data {:columns-num columns-num
                         :card-width card-width
                         :selected-metric selected-metric
                         :total-width total-width
                         :content-loaded (:content-loaded data)
                         :loading (:loading data)
                         :topics board-topics
                         :foce-data-editing? (:foce-data-editing? data)
                         :new-topics (:new-topics data)
                         :board-data board-data
                         :entries-data (:entries-data data)
                         :topics-data board-data
                         :foce-key (:foce-key data)
                         :foce-data (:foce-data data)
                         :show-add-topic (:show-add-topic data)
                         :selected-topic-view (:selected-topic-view data)
                         :dashboard-selected-topics (:dashboard-selected-topics data)
                         :dashboard-sharing (:dashboard-sharing data)
                         :is-dashboard (:is-dashboard data)
                         :show-top-menu (:show-top-menu data)}
              comp-opts {:opts {:topic-click (partial topic-click owner)}}
              sub-component (if (responsive/is-mobile-size?) topics-mobile-layout topics-columns)]
          (om/build sub-component comp-data comp-opts))))))