(ns oc.web.components.topics-columns
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1 sel)]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.utils :as utils]
            [oc.web.components.topic :refer (topic)]
            [oc.web.components.topic-view :refer (topic-view)]
            ; [open-company-web.components.add-topic :refer (add-topic)]
            [oc.web.components.bw-topics-list :refer (bw-topics-list)]))

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
    (:topics data)
    ; just layout the topics in :topics order
    ; in 2 columns
    (= (:columns-num data) 2)
    (let [board-data (:board-data data)
          all-topics (:topics board-data)
          topics (into [] (map name (filter #(contains? board-data %) (map keyword all-topics))))
          layout (loop [idx 0
                        layout {:1 [] :2 []}]
                   (if (= idx (count topics))
                     layout
                     (let [topic (get topics idx)]
                       (recur (inc idx)
                              (if (even? idx)
                                 (assoc layout :1 (conj (:1 layout) topic))
                                 (assoc layout :2 (conj (:2 layout) topic)))))))]
      layout)
    ; just layout the topics in :topics order
    ; in 3 columns
    (= (:columns-num data) 3)
    (let [board-data (:board-data data)
          all-topics (:topics board-data)
          topics (into [] (map name (filter #(contains? board-data %) (map keyword all-topics))))
          layout (loop [idx 0
                        layout {:1 [] :2 [] :3 []}]
                   (if (= idx (count topics))
                     layout
                     (let [topic (get topics idx)]
                       (recur (inc idx)
                              (cond
                                 (= (mod idx 3) 0)
                                 (assoc layout :1 (conj (:1 layout) topic))
                                 (= (mod idx 3) 1)
                                 (assoc layout :2 (conj (:2 layout) topic))
                                 (= (mod idx 3) 2)
                                 (assoc layout :3 (conj (:3 layout) topic)))))))]
      layout)))

(defn render-topic [owner options topic-name column]
  (let [props       (om/get-props owner)
        board-data  (:board-data props)
        topics-data (:topics-data props)]
    (when (and topic-name (contains? topics-data (keyword topic-name)))
      (let [topic-click           (or (:topic-click options) identity)
            is-dashboard          (:is-dashboard props)
            sd (->> topic-name keyword (get topics-data))
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
        (when-not (and (:read-only board-data) (:placeholder sd))
          (dom/div #js {:className "topic-row"
                        :data-topic (name topic-name)
                        :style topic-row-style
                        :ref topic-name
                        :key (str "topic-row-" (name topic-name))}
            (om/build topic {:loading (:loading board-data)
                             :topic topic-name
                             :is-stakeholder-update (:is-stakeholder-update props)
                             :topic-data sd
                             :card-width (:card-width props)
                             :columns-num (:columns-num props)
                             :foce-data-editing? (:foce-data-editing? props)
                             :read-only-board (:read-only board-data)
                             :currency (:currency board-data)
                             :foce-key (:foce-key props)
                             :foce-data (:foce-data props)
                             :dashboard-selected-topics (:dashboard-selected-topics props)
                             :dashboard-sharing (:dashboard-sharing props)
                             :is-dashboard is-dashboard
                             :show-editing (and is-dashboard
                                                (not (:read-only board-data)))
                             :column column
                             :show-top-menu (:show-top-menu props)}
                             {:opts {:topic-name topic-name
                                     :topic-click (partial topic-click topic-name)}})))))))

(defn- update-active-topics [owner new-topic topic-data]
  (let [board-data (om/get-props owner :board-data)
        new-topic-kw (keyword new-topic)
        fixed-topic-data (merge topic-data {:topic new-topic
                                            :new (not (:was-archived topic-data))
                                            :loading (:was-archived topic-data)})
        new-topics (conj (:topics board-data) new-topic)]
    (when (:was-archived topic-data)
      (api/patch-topics new-topics))
    (dis/dispatch! [:add-topic new-topic-kw fixed-topic-data])
    ; delay switch to topic view to make sure the FoCE data are in when loading the view
    (when (:was-archived topic-data)
      (router/nav! (oc-urls/topic (router/current-org-slug) (:slug board-data) new-topic)))))

(defcomponent topics-columns [{:keys [columns-num
                                      content-loaded
                                      total-width
                                      card-width
                                      topics
                                      board-data
                                      topics-data
                                      is-dashboard
                                      is-stakeholder-update
                                      show-add-topic] :as data} owner options]

  (init-state [_]
    {:topics-layout nil
     :filtered-topics (if (or (:read-only board-data)
                              (responsive/is-mobile-size?))
                        (utils/filter-placeholder-topics topics topics-data)
                        topics)})

  (did-mount [_]
    (when (> columns-num 1)
      (om/set-state! owner :topics-layout (calc-layout owner data))))

  (will-receive-props [_ next-props]
    (om/set-state! owner :filtered-topics (if (or (:read-only (:board-data next-props))
                                                  (responsive/is-mobile-size?))
                                            (utils/filter-placeholder-topics (:topics next-props) (:topics-data next-props))
                                            (:topics next-props)))
    (when (and (> (:columns-num next-props) 1)
               (or (not= (:topics next-props) (:topics data))
                   (not= (:columns-num next-props) (:columns-num data))
                   (not= (:card-width next-props) (:card-width data))))
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
              ; (and is-dashboard
              ;      (not (responsive/is-mobile-size?))
              ;      (not selected-topic-view)
              ;      show-add-topic)
              ; (dom/div {:class "add-topic-container"
              ;           :style {:margin-right (str (- topic-view-width 660) "px")}}
              ;   (add-topic (partial update-active-topics owner)))
              (and is-dashboard
                   (not (responsive/is-mobile-size?))
                   selected-topic-view)
              (om/build topic-view {:card-width card-width
                                    :columns-num columns-num
                                    :board-data board-data
                                    :foce-key (:foce-key data)
                                    :foce-data (:foce-data data)
                                    :foce-data-editing? (:foce-data-editing? data)
                                    :new-topics (:new-topics data)
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
                              :let [topic-kw (get column idx)
                                    topic-name (name topic-kw)]]
                          (partial-render-topic topic-name (name kw))))))))))
          ;; 1 column or default
          :else
          (dom/div {:class "topics-column-container columns-1 group"
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (dom/div {:class "topics-column"}
              (for [topic filtered-topics]
                (partial-render-topic (name topic) 1)))))))))