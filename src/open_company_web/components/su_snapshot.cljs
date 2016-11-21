(ns open-company-web.components.su-snapshot
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]
            [cljsjs.hammer]))

(defn close-overlay-cb [owner]
  (om/set-state! owner :transitioning false)
  (om/set-state! owner :selected-topic nil)
  (om/set-state! owner :selected-metric nil)
  (.pushState js/history nil "Stakeholder update" (oc-urls/stakeholder-update (router/current-company-slug) (router/current-stakeholder-update-date) (router/current-stakeholder-update-slug))))

(defn topic-click [owner topic selected-metric]
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric)
  (.pushState js/history nil (name topic) (oc-urls/stakeholder-update-section  (router/current-company-slug) (router/current-stakeholder-update-date) (router/current-stakeholder-update-slug) topic)))

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
    (.pushState js/history nil (name new-topic) (oc-urls/stakeholder-update-section (router/current-company-slug) (router/current-stakeholder-update-date) (router/current-stakeholder-update-slug) new-topic))
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
     :columns-num (responsive/columns-num)
     :prior-list (:list (router/query-params))})

  (did-mount [_]
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num)))
    (when (and (not (utils/is-test-env?))
               (responsive/user-agent-mobile?))
      (let [kb-listener (events/listen js/window EventType/KEYDOWN (partial kb-listener owner))]
        (om/set-state! owner :kb-listener kb-listener))))

  (will-unmount [_]
    (when (and (not (utils/is-test-env?))
               (responsive/user-agent-mobile?))
      (events/unlistenByKey (om/get-state owner :kb-listener))))

  (did-update [_ _ _]
    (if (om/get-state owner :tr-selected-topic)
      (animate-selected-topic-transition owner)
      (dommy/remove-class! (sel1 [:body]) :no-scroll)))

  (render-state [_ {:keys [selected-topic tr-selected-topic selected-metric transitioning columns-num prior-list]}]

    (let [company-data (dis/company-data data)
          su-data      (dis/stakeholder-update-data)
          mobile?      (responsive/is-mobile-size?)
          fixed-card-width (responsive/calc-update-width (responsive/columns-num))
          title        (if (clojure.string/blank? (:title su-data))
                          (str (:name company-data) " Update")
                          (:title su-data))]
      (dom/div {:class (utils/class-set {:su-snapshot true
                                         :main-scroll true
                                         :has-navbar (not prior-list)})}
        (dom/div {:class "page"}
          (when-not prior-list
            ;; Navbar
            (om/build navbar {:card-width (responsive/calc-card-width)
                              :columns-num columns-num
                              :company-data company-data
                              :foce-key nil
                              :show-share-su-button false
                              :mobile-menu-open false
                              :su-navbar true
                              :hide-right-menu true}))

          ; closing X
          (when (and prior-list mobile?)
            (back-to-dashboard-btn))

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
                                :style #js {:opacity 1 :backgroundColor "rgba(78, 90, 107, 0.5)"}}
                    (om/build fullscreen-topic {:section selected-topic
                                                :section-data (->> selected-topic keyword (get su-data))
                                                :selected-metric selected-metric
                                                :read-only true
                                                :card-width fixed-card-width
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
                                                :card-width fixed-card-width
                                                :currency (:currency company-data)
                                                :hide-history-navigation true
                                                :animate false}
                                               {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                                       :topic-navigation #(om/set-state! owner :topic-navigation %)}})))))
              (dom/div {:class "su-sp-company-header"}
                (dom/div {:class "su-snapshot-title"} title))
              (om/build topics-columns {:columns-num 1
                                        :card-width (- fixed-card-width 10)
                                        :total-width (- fixed-card-width 10)
                                        :content-loaded (not (:loading data))
                                        :topics (:sections su-data)
                                        :topics-data su-data
                                        :company-data company-data
                                        :hide-add-topic true
                                        :is-stakeholder-update true}
                                       {:opts {:topic-click (partial topic-click owner)}})))
          (when company-data
            (dom/div {:class "su-sp-footer"} "Updates by "
              (dom/a {:href "https://opencompany.com"} "OpenCompany"))))))))