(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [goog.events.EventType :as EventType]
            [goog.events :as events]
            [cljsjs.react.dom]))

(defcomponent topic-image-header [{:keys [image-header image-size]} owner options]
  (render [_]
    (dom/img {:src image-header
              :class "topic-header-img"})))

(defcomponent topic-headline [data owner]
  (render [_]
    (dom/div #js {:className (str "topic-headline-inner group" (when (:placeholder data) " italic"))
                  :dangerouslySetInnerHTML (utils/emojify (:headline data))})))

(defcomponent topic-internal [{:keys [topic-data
                                      section
                                      currency
                                      card-width
                                      columns-num
                                      read-only-company
                                      is-stakeholder-update
                                      is-mobile?
                                      is-dashboard
                                      is-topic-view
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
                       (not is-dashboard)))
          (dom/div {:class "card-header card-image"}
            (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options})))

        ;; Topic title
        (dom/div {:class "group"}
          (dom/div {:class "topic-title"}
            (:title topic-data)
            (when (or is-topic-view
                      (and (not is-mobile?)
                           is-dashboard))
              (dom/span {:data-toggle "tooltip"
                         :data-placement "right"
                         :data-container "body"
                         :key (str "tt-attrib-" (:name (:author topic-data)) (:updated-at topic-data))
                         :title (str "by " (:name (:author topic-data)) " on " (utils/date-string (utils/js-date (:updated-at topic-data)) [:year]))}
                " · " (utils/time-since (:updated-at topic-data))))))

        ;; Topic data
        (when (and (or (not is-dashboard)
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
        (when (and (or (not is-mobile?) (not is-dashboard))
                   (not (clojure.string/blank? (:headline topic-data))))
          (om/build topic-headline topic-data))

        ;; Attribution for topic
        (when (and is-mobile? is-dashboard)
          (dom/div {:class "mobile-date"}
            (utils/time-since (:updated-at topic-data))))
        
        ;; Topic body
        (when (and (or (not is-dashboard)
                       (not is-mobile?))
                   (not (clojure.string/blank? topic-body)))
          (dom/div #js {:className (str "topic-body" (when (:placeholder topic-data) " italic"))
                        :ref "topic-body"
                        :dangerouslySetInnerHTML (utils/emojify topic-body)}))))))

(defcomponent topic [{:keys [active-topics
                             section-data
                             section
                             currency
                             column
                             card-width
                             archived-topics
                             is-stakeholder-update
                             is-dashboard
                             is-topic-view
                             topic-flex-num] :as data} owner options]

  (init-state [_]
    {:window-width (responsive/ww)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      ; initialize bootstrap tooltips
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :window-width (responsive/ww)))))

  (did-update [_ _ _]
    (when-not (utils/is-test-env?)
      ; initialize bootstrap tooltips
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {:keys [editing as-of window-width] :as state}]
    (let [section-kw (keyword section)
          slug (keyword (router/current-company-slug))
          foce-active (not (nil? (dis/foce-section-key)))
          is-foce (= (dis/foce-section-key) section-kw)
          is-mobile? (responsive/is-mobile-size?)
          with-order (if (contains? data :topic-flex-num) {:order topic-flex-num} {})
          topic-style (clj->js (if (or (utils/in? (:route @router/path) "su-snapshot-preview")
                                       (utils/in? (:route @router/path) "su-snapshot")
                                       (utils/in? (:route @router/path) "su-list")
                                       (responsive/is-mobile-size?))
                                 with-order
                                 (merge with-order {:width (if (responsive/window-exceeds-breakpoint) (str card-width "px") "auto")})))]
      (dom/div #js {:className (utils/class-set {:topic true
                                                 :group true
                                                 :mobile-dashboard-topic (and is-mobile? is-dashboard)
                                                 :topic-edit is-foce
                                                 :dashboard-topic is-dashboard
                                                 :no-foce (and foce-active (not is-foce))})
                    :onClick (fn []
                                (when (and (not (responsive/is-mobile-size?))
                                           is-dashboard)
                                  (dis/dispatch! [:select-topic-view section])))
                    :style topic-style
                    :ref "topic"
                    :data-section (name section)
                    :key (str "topic-" (name section))
                    :id (str "topic-" (name section))}
        (om/build topic-internal {:section section
                                  :topic-data section-data
                                  :is-stakeholder-update (:is-stakeholder-update data)
                                  :currency currency
                                  :card-width card-width
                                  :read-only-company (:read-only-company data)
                                  :topic-click (:topic-click options)
                                  :is-foce is-foce
                                  :foce-active foce-active
                                  :is-mobile? is-mobile?
                                  :is-dashboard is-dashboard
                                  :is-topic-view is-topic-view}
                                 {:opts options
                                  :key (str "topic-" section)})))))