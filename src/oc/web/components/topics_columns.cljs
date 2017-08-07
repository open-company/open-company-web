(ns oc.web.components.topics-columns
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.utils :as utils]
            [oc.web.components.boards-list :refer (boards-list)]
            [oc.web.components.ui.filters-dropdown :refer (filters-dropdown)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.entry-card :refer (entry-card)]
            [oc.web.components.entries-layout :refer (entries-layout)]))

(defn- update-active-topics [owner new-topic topic-data]
  (let [board-data (om/get-props owner :board-data)
        new-topic-kw (keyword new-topic)
        fixed-topic-data (merge topic-data {:topic new-topic
                                            :new (not (:was-archived topic-data))
                                            :loading (:was-archived topic-data)})
        new-topics (conj (:topics board-data) new-topic)]
    (dis/dispatch! [:topic-add new-topic-kw fixed-topic-data])
    ; delay switch to topic view to make sure the FoCE data are in when loading the view
    (when (:was-archived topic-data)
      (router/nav! (oc-urls/entry (router/current-org-slug) (:slug board-data) new-topic)))))

(defcomponent topics-columns [{:keys [columns-num
                                      content-loaded
                                      total-width
                                      board-data
                                      is-dashboard
                                      is-stakeholder-update
                                      board-filters] :as data} owner options]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render [_]
    (let [current-entry-uuid (router/current-entry-uuid)
          is-mobile-size? (responsive/is-mobile-size?)
          columns-container-key (if current-entry-uuid
                                  (str "topics-columns-selected-topic-" current-entry-uuid)
                                  (apply str (:topics board-data)))
          topics-column-conatiner-style (if is-dashboard
                                          (if (responsive/window-exceeds-breakpoint)
                                            #js {:width total-width}
                                            #js {:margin "0px 9px"
                                                 :width "auto"})
                                          (if is-mobile-size?
                                            #js {:margin "0px 9px"
                                                 :width "auto"}
                                            #js {:width total-width}))
          total-width-int (js/parseInt total-width 10)
          empty-board? (zero? (count (:entries board-data)))]
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
            (when-not (responsive/is-tablet-or-mobile?)
              (om/build boards-list data))
            (dom/div {:class "board-container right"
                      :style {:width (str (- total-width-int responsive/left-boards-list-width responsive/topic-list-right-margin (* 2 responsive/topic-list-x-padding)) "px")}}
              ;; Board name row: board name, settings button and say something button
              (dom/div {:class "group"}
                ;; Board name and settings button
                (dom/div {:class "board-name"}
                  (if (router/current-board-slug)
                    (:name board-data)
                    "All Activity")
                  ;; Settings button
                  (when (and (router/current-board-slug)
                             (not (:read-only board-data)))
                    (dom/button {:class "mlb-reset board-settings-bt"
                                 :data-toggle "tooltip"
                                 :data-placement "top"
                                 :data-container "body"
                                 :title "Board settings"
                                 :on-click #(dis/dispatch! [:board-edit board-data])})))
                ;; Say something button
                (when (and (router/current-board-slug)
                           (not (:read-only (dis/org-data)))
                           (not (:foce-key data))
                           (not (responsive/is-tablet-or-mobile?)))
                  (dom/button {:class "mlb-reset mlb-default add-to-board-btn"
                               :on-click #(dis/dispatch! [:entry-edit {}])}
                    (dom/div {:class "add-to-board-pencil"})
                    "New Update"))
                ;; Board filters dropdown
                (when (and (not is-mobile-size?)
                           (not empty-board?))
                  (filters-dropdown)))
              ;; Board content: empty board, add topic, topic view or topic cards
              (cond
                (and is-dashboard
                     (not is-mobile-size?)
                     (not current-entry-uuid)
                     empty-board?)
                (empty-board)
                ; for each column key contained in best layout
                :else
                (entries-layout board-data board-filters))))
          ;; 1 column or default
          :else
          (dom/div {:class "topics-column-container columns-1 group"
                    :style topics-column-conatiner-style
                    :key columns-container-key}
            (dom/div {:class "topics-column"}
              (for [topic (:topics board-data)
                    :let [topic-data (get board-data (keyword topic))]
                    :when (not (:placeholder topic-data))]
                (entry-card topic-data)))))))))
