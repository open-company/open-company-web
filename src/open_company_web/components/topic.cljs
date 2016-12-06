(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic-edit :refer (topic-edit)]
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

(defn start-foce-click [owner]
  (let [section-kw (keyword (om/get-props owner :section))
        section-data (om/get-props owner :topic-data)]
    (dis/dispatch! [:start-foce section-kw section-data])))

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
                                      topic-click
                                      show-editing
                                      column] :as data} owner options]

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
          company-data        (dis/company-data)
          fixed-column        (js/parseInt column)]
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
                       (not is-dashboard))
                   (not is-mobile?)
                   is-dashboard)
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
                         :data-placement (if (= fixed-column 1)
                                            "right"
                                            (if (or (and (= columns-num 2)
                                                         (= fixed-column 2))
                                                    (= fixed-column 3))
                                              "left"
                                              "top"))
                         :data-container "body"
                         :key (str "tt-attrib-" (:name (:author topic-data)) (:updated-at topic-data))
                         :title (str "by " (:name (:author topic-data)) " on " (utils/date-string (utils/js-date (:updated-at topic-data)) [:year]))}
                " · " (utils/time-since (:updated-at topic-data) [:short-month]))))
          (when (and show-editing
                     (not is-stakeholder-update)
                     (or (not is-mobile?)
                         (not is-dashboard))
                     (responsive/can-edit?)
                     (not (:read-only topic-data))
                     (not read-only-company)
                     (not foce-active))
            (dom/button {:class (str "topic-pencil-button btn-reset")
                         :on-click #(start-foce-click owner)}
              (dom/i {:class "fa fa-pencil"
                      :title "Edit"
                      :data-toggle "tooltip"
                      :data-container "body"
                      :data-placement "top"}))))

        ;; Topic image for dashboard
        (when (and image-header
                   (or is-stakeholder-update
                       is-topic-view))
          (dom/div {:class "card-header card-image"}
            (om/build topic-image-header {:image-header image-header :image-size image-header-size} {:opts options})))

        ;; Topic headline
        (when (and is-topic-view
                   (not (clojure.string/blank? (:headline topic-data))))
          (om/build topic-headline topic-data))

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
                                        :card-width card-width
                                        :columns-num columns-num
                                        :currency currency} {:opts chart-opts}))))

        ;; Topic headline
        (when (and (or (not is-mobile?) (not is-dashboard))
                   (not is-topic-view)
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
                             columns-num
                             archived-topics
                             is-stakeholder-update
                             is-dashboard
                             is-topic-view
                             foce-key
                             foce-data
                             foce-data-editing?
                             show-editing
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
          foce-active (not (nil? foce-key))
          is-current-foce (and (= foce-key section-kw) (= (:updated-at foce-data) (:updated-at section-data)))
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
                                                 :no-tablet (not (responsive/is-tablet-or-mobile?))
                                                 :topic-edit is-current-foce
                                                 :dashboard-topic is-dashboard
                                                 :no-foce (and foce-active (not is-current-foce))})
                    :onClick (fn []
                                (when (and (not (responsive/is-mobile-size?))
                                           is-dashboard)
                                  (router/nav! (oc-urls/company-section (router/current-company-slug) section))))
                    :style topic-style
                    :ref "topic"
                    :data-section (name section)
                    :key (str "topic-" (when is-current-foce "foce-") (name section) "-" (:updated-at section-data))
                    :id (str "topic-" (name section) "-" (:updated-at section-data))}
        (if is-current-foce
          (om/build topic-edit {:section section
                                :topic-data section-data
                                :is-stakeholder-update is-stakeholder-update
                                :currency currency
                                :card-width card-width
                                :foce-data-editing? foce-data-editing?
                                :read-only-company (:read-only-company data)
                                :foce-key foce-key
                                :foce-data foce-data}
                               {:opts options
                                :key (str "topic-foce-" section "-" (:updated-at section-data))})
          (om/build topic-internal {:section section
                                    :topic-data section-data
                                    :is-stakeholder-update (:is-stakeholder-update data)
                                    :currency currency
                                    :card-width card-width
                                    :columns-num columns-num
                                    :read-only-company (:read-only-company data)
                                    :topic-click (:topic-click options)
                                    :foce-active foce-active
                                    :is-mobile? is-mobile?
                                    :is-dashboard is-dashboard
                                    :is-topic-view is-topic-view
                                    :show-editing show-editing
                                    :column column}
                                   {:opts options
                                    :key (str "topic-" section)}))))))