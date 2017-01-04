(ns open-company-web.components.topics-columns
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [cuerdas.core :as s]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-view :refer (topic-view)]
            [open-company-web.components.add-topic :refer (add-topic)]
            [open-company-web.components.bw-topics-list :refer (bw-topics-list)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def topic-margins 20)
(def mobile-topic-margins 3)

(defn get-initial-layout [columns-num]
  (cond
    ; 2 columns empty layout
    (= columns-num 2)
    {:1 [] :2 []}
    ; 3 columns empty layout
    (= columns-num 3)
    {:1 [] :2 [] :3 []}))

(defn calc-layout
  "Calculate the best layout given the list of topics and the number of columns to layout to"
  [owner data]
  (cond
    ; avoid to crash tests
    (utils/is-test-env?)
    (om/get-props owner :topics)
    ; just layout the sections in :sections order
    ; in 2 columns
    (= (om/get-props owner :columns-num) 2)
    (let [sections (:sections (:company-data data))
          layout (loop [idx 0
                        layout {:1 [] :2 []}]
                   (if (= idx (count sections))
                     layout
                     (let [topic (get sections idx)]
                       (recur (inc idx)
                              (if (even? idx)
                                 (assoc layout :1 (conj (:1 layout) topic))
                                 (assoc layout :2 (conj (:2 layout) topic)))))))]
      layout)
    ; just layout the sections in :sections order
    ; in 3 columns
    (= (om/get-props owner :columns-num) 3)
    (let [sections (:sections (:company-data data))
          layout (loop [idx 0
                        layout {:1 [] :2 [] :3 []}]
                   (if (= idx (count sections))
                     layout
                     (let [topic (get sections idx)]
                       (recur (inc idx)
                              (cond
                                 (= (mod idx 3) 0)
                                 (assoc layout :1 (conj (:1 layout) topic))
                                 (= (mod idx 3) 1)
                                 (assoc layout :2 (conj (:2 layout) topic))
                                 (= (mod idx 3) 2)
                                 (assoc layout :3 (conj (:3 layout) topic)))))))]
      layout)))

(defn render-topic [owner options section-name & [column]]
  (when section-name
    (let [props                 (om/get-props owner)
          company-data          (:company-data props)
          topics-data           (:topics-data props)
          topic-click           (or (:topic-click options) identity)
          is-dashboard          (:is-dashboard props)]
      (let [sd (->> section-name keyword (get topics-data))
            topic-row-style (if (or (utils/in? (:route @router/path) "su-snapshot-preview")
                                    (utils/in? (:route @router/path) "su-list"))
                              #js {}
                              #js {:width (if is-dashboard
                                            (if (responsive/window-exceeds-breakpoint)
                                              (str (:card-width props) "px")
                                              "auto")
                                            (if (responsive/is-mobile-size?)
                                              "auto"
                                              (str (:card-width props) "px")))})]
        (when-not (and (:read-only company-data) (:placeholder sd))
          (dom/div #js {:className "topic-row"
                        :data-topic (name section-name)
                        :style topic-row-style
                        :ref section-name
                        :key (str "topic-row-" (name section-name))}
            (om/build topic {:loading (:loading company-data)
                             :section section-name
                             :is-stakeholder-update (:is-stakeholder-update props)
                             :section-data sd
                             :card-width (:card-width props)
                             :columns-num (:columns-num props)
                             :foce-data-editing? (:foce-data-editing? props)
                             :read-only-company (:read-only company-data)
                             :currency (:currency company-data)
                             :foce-key (:foce-key props)
                             :foce-data (:foce-data props)
                             :dashboard-selected-topics (:dashboard-selected-topics props)
                             :dashboard-sharing (:dashboard-sharing props)
                             :is-dashboard is-dashboard
                             :show-editing (and is-dashboard
                                                (not (:read-only company-data)))
                             :column column}
                             {:opts {:section-name section-name
                                     :topic-click (partial topic-click section-name)}})))))))

(defn- update-active-topics [owner new-topic topic-data]
  (let [company-data (om/get-props owner :company-data)
        new-topic-kw (keyword new-topic)
        fixed-topic-data (merge topic-data {:section new-topic
                                            :new (not (:was-archived topic-data))
                                            :loading (:was-archived topic-data)})
        new-topics (conj (:sections company-data) new-topic)]
    (when (:was-archived topic-data)
      (api/patch-sections new-topics))
    (dis/dispatch! [:add-topic new-topic-kw fixed-topic-data])
    ; delay switch to topic view to make sure the FoCE data are in when loading the view
    (router/nav! (oc-urls/company-section (:slug company-data) new-topic))))

(defcomponent topics-columns [{:keys [columns-num
                                      content-loaded
                                      total-width
                                      card-width
                                      topics
                                      company-data
                                      topics-data
                                      is-dashboard
                                      is-stakeholder-update
                                      show-add-topic] :as data} owner options]

  (init-state [_]
    {:topics-layout nil
     :filtered-topics (if (or (:read-only company-data)
                              (responsive/is-mobile-size?))
                        (utils/filter-placeholder-sections topics topics-data)
                        topics)})

  (did-mount [_]
    (when (> columns-num 1)
      (om/set-state! owner :topics-layout (calc-layout owner data)))
    (js/console.log "topics-columns/did-mount" (sel1 [:div.topics-column-container]))
    (events/listen (sel1 [:div.topics-column-container]) EventType/CLICK
     (fn [e]
      (when (and (not (responsive/is-mobile-size?))
                 (not (nil? (:selected-topic-view data)))
                 (not (:dashboard-sharing data))
                 (nil? (:foce-key data))
                 (not (utils/event-inside? e (sel1 [:div.topic-view-container])))
                 (not (utils/event-inside? e (sel1 [:div.left-topics-list])))
                 (not (utils/event-inside? e (sel1 [:div.add-topic]))))
        (router/nav! (oc-urls/company (router/current-company-slug)))))))

  (will-receive-props [_ next-props]
    (om/set-state! owner :filtered-topics (if (or (:read-only (:company-data next-props))
                                                  (responsive/is-mobile-size?))
                                            (utils/filter-placeholder-sections (:topics next-props) (:topics-data next-props))
                                            (:topics next-props)))
    (when (and (> (:columns-num next-props) 1)
               (or (not= (:topics next-props) (:topics data))
                   (not= (:columns-num next-props) (:columns-num data))))
      (om/set-state! owner :topics-layout (calc-layout owner next-props))))

  (render-state [_ {:keys [topics-layout filtered-topics]}]
    (let [selected-topic-view   (:selected-topic-view data)
          partial-render-topic  (partial render-topic owner options)
          columns-container-key (apply str filtered-topics)
          topics-column-conatiner-style (if is-dashboard
                                          (if (responsive/window-exceeds-breakpoint)
                                            #js {:width total-width}
                                            #js {:margin "0px 9px"
                                                 :width "auto"})
                                          (if (responsive/is-mobile-size?)
                                            #js {:margin "0px 9px"
                                                 :width "auto"}
                                            #js {:width total-width}))
          topic-view-width (responsive/topic-view-width card-width columns-num)]
      ;; Topic list
      (dom/div {:class (utils/class-set {:topics-columns true
                                         :overflow-visible true
                                         :group true
                                         :content-loaded content-loaded})}
        (cond
          ;; render 2 or 3 column layout
          (> columns-num 1)
          (dom/div {:class (utils/class-set {:topics-column-container true
                                             :group true
                                             :tot-col-3 (and is-dashboard
                                                             (= columns-num 3))
                                             :tot-col-2 (and is-dashboard
                                                             (= columns-num 2))})
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (when (:dashboard-sharing data)
              (dom/div {:class "dashboard-sharing-select-all"}
                (dom/span "Click topics to include or")
                (dom/button {:class "btn-reset btn-link"
                             :on-click #(dis/dispatch! [:dashboard-select-all])} "select all")))
            (when-not (responsive/is-tablet-or-mobile?)
              (om/build bw-topics-list data))
            (cond
              (and is-dashboard
                   (not (responsive/is-mobile-size?))
                   (not selected-topic-view)
                   show-add-topic)
              (dom/div {:class "add-topic-container"
                        :style {:margin-right (str (- topic-view-width 660) "px")}}
                (add-topic (partial update-active-topics owner)))
              (and is-dashboard
                   (not (responsive/is-mobile-size?))
                   selected-topic-view)
              (om/build topic-view {:card-width card-width
                                    :columns-num columns-num
                                    :company-data company-data
                                    :foce-key (:foce-key data)
                                    :foce-data (:foce-data data)
                                    :foce-data-editing? (:foce-data-editing? data)
                                    :selected-topic-view selected-topic-view})
              ; for each column key contained in best layout
              :else
              (dom/div {:class "right" :style {:width (str (- (int total-width) responsive/left-topics-list-width) "px")}}
                (for [kw (if (= columns-num 3) [:1 :2 :3] [:1 :2])]
                  (let [column (get topics-layout kw)]
                    (dom/div {:class (str "topics-column col-" (name kw))
                              :style #js {:width (str (+ card-width (if (responsive/is-mobile-size?) mobile-topic-margins topic-margins)) "px")}}
                      ; render the topics
                      (when (pos? (count column))
                        (for [idx (range (count column))
                              :let [section-kw (get column idx)
                                    section-name (name section-kw)]]
                          (partial-render-topic section-name (name kw))))))))))
          ;; 1 column or default
          :else
          (dom/div {:class "topics-column-container columns-1 group"
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (dom/div {:class "topics-column"}
              (for [section filtered-topics]
                (partial-render-topic (name section) 1)))))))))