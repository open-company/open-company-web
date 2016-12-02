(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.topic-edit :refer (topic-edit)]
            [open-company-web.components.topic-attribution :refer (topic-attribution)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.topic-read-more :refer (topic-read-more)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.events.EventType :as EventType]
            [goog.events :as events]
            [goog.style :as gstyle]
            [cljsjs.react.dom]))

(defcomponent topic-image-header [{:keys [image-header image-size]} owner options]
  (render [_]
    (dom/img {:src image-header
              :class "topic-header-img"})))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div #js {:className (str "topic-headline-inner group" (when (:placeholder data) " italic"))
                  :dangerouslySetInnerHTML (utils/emojify (:headline data))})))

(defn start-foce-click [owner]
  (let [section-kw (keyword (om/get-props owner :section))
        company-data (dis/company-data)
        section-data (get company-data section-kw)]
    (dis/dispatch! [:start-foce section-kw section-data])))

(defn pencil-click [owner e]
  (start-foce-click owner))

(defcomponent topic-internal [{:keys [topic-data
                                      section
                                      currency
                                      card-width
                                      columns-num
                                      prev-rev
                                      next-rev
                                      read-only-company
                                      is-stakeholder-update
                                      is-mobile?
                                      is-dashboard?
                                      foce-active
                                      topic-click] :as data} owner options]

  (render [_]
    (let [section-kw          (keyword section)
          chart-opts          {:chart-size {:width 230}
                               :hide-nav true}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          image-header        (:image-url topic-data)
          image-header-size   {:width (:image-width topic-data)
                               :height (:image-height topic-data)}
          topic-body          (if (:placeholder topic-data) (:body-placeholder topic-data) (:body topic-data))
          company-data        (dis/company-data)]
      (dom/div #js {:className "topic-internal group"
                    :key (str "topic-internal-" (name section))
                    :onClick #(when (and (responsive/is-mobile-size?)
                                         (not foce-active)
                                         (not is-stakeholder-update))
                                (.preventDefault %)
                                (topic-click))
                    :ref "topic-internal"}

        ;; Topic image for dashboard
        (when (and image-header
                   (or (not is-mobile?)
                       (not is-dashboard?)))
          (dom/div {:class "card-header card-image"}
            (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options})))

        ;; Topic title
        (dom/div {:class "group"}
          (dom/div {:class "topic-title"} (:title topic-data))
          (when (and (not is-stakeholder-update)
                     (or (not is-mobile?)
                         (not is-dashboard?))
                     (responsive/can-edit?)
                     (not (:read-only topic-data))
                     (not read-only-company)
                     (not foce-active))
            (dom/button {:class (str "topic-pencil-button btn-reset")
                         :on-click #(pencil-click owner %)}
              (dom/i {:class "fa fa-pencil"
                      :title "Edit"
                      :data-toggle "tooltip"
                      :data-container "body"
                      :data-placement "top"}))))

        ;; Topic data
        (when (and (or (not is-dashboard?)
                       (not is-mobile?))
                   is-growth-finances?
                   (utils/data-topic-has-data section topic-data))
          (dom/div {:class ""}
            (cond
              (= section "growth")
              (om/build topic-growth {:section-data topic-data
                                      :section section
                                      :card-width card-width
                                      :columns-num columns-num
                                      :currency currency} {:opts chart-opts})
              (= section "finances")
              (om/build topic-finances {:section-data (utils/fix-finances topic-data)
                                        :section section
                                        :currency currency} {:opts chart-opts}))))

        ;; Topic headline
        (when (and (or (not is-mobile?) (not is-dashboard?))
                   (not (clojure.string/blank? (:headline topic-data))))
          (om/build topic-headline topic-data))

        ;; Attribution for topic
        (when (and is-mobile? is-dashboard?)
          (dom/div {:class "mobile-date"}
            (utils/time-since (:updated-at topic-data))))
        
        ;; Topic body
        (when (and (or (not is-dashboard?)
                       (not is-mobile?))
                   (not (clojure.string/blank? topic-body)))
          (dom/div #js {:className (str "topic-body" (when (:placeholder topic-data) " italic"))
                        :ref "topic-body"
                        :dangerouslySetInnerHTML (utils/emojify topic-body)}))

        ; if it's SU preview or SU show only read-more
        (when (or (not is-dashboard?)
                  (not is-mobile?))
          (dom/div {:style {:margin-top "20px"}}
            (when-not is-stakeholder-update
              (om/build topic-attribution data {:opts options}))))))))

(defn animate-revision-navigation [owner]
  (let [cur-topic (om/get-ref owner "cur-topic")
        tr-topic (om/get-ref owner "tr-topic")
        current-state (om/get-state owner)
        anim-duration utils/oc-animation-duration
        appear-animation (Fade. tr-topic 0 1 anim-duration)
        cur-size (js/getComputedStyle cur-topic)
        tr-size (js/getComputedStyle tr-topic)
        topic (om/get-ref owner "topic-anim")
        topic-size (js/getComputedStyle topic)]
    ; resize the light box
    (.play (Resize. topic
                    #js [(js/parseFloat (.-width topic-size)) (js/parseFloat (.-height cur-size))]
                    #js [(js/parseFloat (.-width topic-size)) (js/parseFloat (.-height tr-size))]
                    anim-duration))
    ; make the current topic disappear
    (.play (Fade. cur-topic 1 0 anim-duration))
    ; appear the new topic
    (doto appear-animation
      (events/listen
        AnimationEventType/FINISH
        (fn []
          (om/set-state! owner (merge current-state
                                      {:as-of (:transition-as-of current-state)
                                       :transition-as-of nil}))
          (utils/after 100 #(utils/remove-tooltips))))
      (.play))))

(defcomponent topic [{:keys [active-topics
                             section-data
                             section
                             currency
                             column
                             card-width
                             archived-topics
                             is-stakeholder-update
                             topic-flex-num] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :actual-as-of (:updated-at section-data)
     :window-width (responsive/ww)
     :transition-as-of nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      ; initialize bootstrap tooltips
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :window-width (responsive/ww)))))

  (will-update [_ next-props _]
    (let [new-as-of (:updated-at (:section-data next-props))
          current-as-of (om/get-state owner :as-of)
          old-as-of (:updated-at section-data)]
      (when (and (not= old-as-of new-as-of)
                 (not= current-as-of new-as-of))
        (om/set-state! owner :as-of new-as-of)
        (om/set-state! owner :actual-as-of new-as-of))))

  (did-update [_ prev-props _]
    (when (om/get-state owner :transition-as-of)
      (animate-revision-navigation owner)))

  (render-state [_ {:keys [editing as-of actual-as-of transition-as-of window-width] :as state}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          slug (keyword (router/current-company-slug))
          all-revisions (dis/revisions slug)
          revisions-list (section-kw all-revisions)
          topic-data (utils/select-section-data section-data section-kw as-of)
          rev-cb (fn [_ rev] (om/set-state! owner :transition-as-of (:updated-at rev)))
          foce-active (not (nil? (dis/foce-section-key)))
          is-foce (= (dis/foce-section-key) section-kw)
          is-dashboard? (utils/in? (:route @router/path) "dashboard")
          is-mobile? (responsive/is-mobile-size?)
          with-order (if (contains? data :topic-flex-num) {:order topic-flex-num} {})
          topic-style (clj->js (if (or (utils/in? (:route @router/path) "su-snapshot-preview")
                                       (utils/in? (:route @router/path) "su-snapshot")
                                       (utils/in? (:route @router/path) "su-list")
                                       (responsive/is-mobile-size?))
                                 with-order
                                 (merge with-order {:width (if (responsive/window-exceeds-breakpoint) (str card-width "px") "auto")})))]
      ;; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ;; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className (utils/class-set {:topic true
                                                 :group true
                                                 :mobile-dashboard-topic (and is-mobile? is-dashboard?)
                                                 :topic-edit is-foce
                                                 :no-foce (and foce-active (not is-foce))})
                    :style topic-style
                    :ref "topic"
                    :data-section (name section)
                    :key (str "topic-" (name section))
                    :id (str "topic-" (name section))}
        (dom/div #js {:className "topic-anim group"
                      :key (str "topic-anim-" as-of "-" transition-as-of)
                      :ref "topic-anim"}
          (dom/div #js {:className "topic-as-of group"
                        :key (str "cur-" as-of)
                        :style #js {:opacity 1 :width "100%"}}
            (dom/div #js {:className "topic-cur-as-of"
                          :ref "cur-topic"
                          :style #js {:opacity 1}}
              (if is-foce
                (om/build topic-edit {:section section
                                      :topic-data topic-data
                                      :is-stakeholder-update (:is-stakeholder-update data)
                                      :currency currency
                                      :card-width card-width
                                      :foce-data-editing? (:foce-data-editing? data)
                                      :read-only-company (:read-only-company data)
                                      :foce-key (:foce-key data)
                                      :foce-data (:foce-data data)
                                      :prev-rev prev-rev
                                      :next-rev next-rev
                                      :show-first-edit-tip (:show-first-edit-tip data)}
                                     {:opts (merge options {:rev-click rev-cb})
                                      :key (str "topic-foce-" section)})
                (om/build topic-internal {:section section
                                          :topic-data topic-data
                                          :is-stakeholder-update (:is-stakeholder-update data)
                                          :currency currency
                                          :card-width card-width
                                          :read-only-company (:read-only-company data)
                                          :topic-click (:topic-click options)
                                          :is-foce is-foce
                                          :foce-active foce-active
                                          :is-mobile? is-mobile?
                                          :is-dashboard? is-dashboard?
                                          :prev-rev prev-rev
                                          :next-rev next-rev}
                                         {:opts (merge options {:rev-click rev-cb})
                                          :key (str "topic-" section)})))
            (when transition-as-of
              (dom/div #js {:className "topic-tr-as-of group"
                            :ref "tr-topic"
                            :key (str "tr-" transition-as-of "-expanded")
                            :style #js {:opacity 1}}
                (let [tr-topic-data (utils/select-section-data section-data section-kw transition-as-of)
                      tr-prev-rev (utils/revision-prev revisions transition-as-of)
                      tr-next-rev (utils/revision-next revisions transition-as-of)]
                  (om/build topic-internal {:section section
                                            :topic-data tr-topic-data
                                            :currency currency
                                            :read-only-company (:read-only-company data)
                                            :is-mobile? is-mobile?
                                            :is-dashboard? is-dashboard?
                                            :prev-rev tr-prev-rev
                                            :next-rev tr-next-rev}
                                           {:opts (merge options {:rev-click rev-cb})}))))))))))