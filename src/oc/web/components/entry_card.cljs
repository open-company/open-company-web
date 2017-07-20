(ns oc.web.components.entry-card
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :refer (get-color-by-kw)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]))

(defn cut-body [entry-body]
  (.truncate js/$ entry-body (clj->js {:length 86 :words true :ellipsis "... <span class=\"full-update\">Read Full Update</span>"})))

(rum/defc entry-card-empty
  [read-only?]
  [:div.entry-card.empty-state.group
    [:div.entry-card-title
      "This topic’s a little sparse. "
      (when-not read-only?
        [:button.mlb-reset
          {:on-click #(dis/dispatch! [:entry-edit {}])}
          "Add an update?"])]])

(rum/defcs entry-card < rum/static
  [s entry-data show-topic?]
  [:div.entry-card
    {:on-click #(dis/dispatch! [:entry-modal-fade-in (:uuid entry-data)])}
    ; Card header
    [:div.entry-card-head.group
      ; Card author
      [:div.entry-card-head-author
        (user-avatar-image (first (:author entry-data)))
        [:div.name (:name (first (:author entry-data)))]
        [:div.time-since
          [:time
            {:date-time (:updated-at entry-data)
             :data-toggle "tooltip"
             :data-placement "top"
             :data-container "body"
             :title (let [js-date (utils/js-date (:updated-at entry-data))] (str (.toDateString js-date) " at " (.toLocaleTimeString js-date)))}
            (utils/time-since (:updated-at entry-data))]]]
      ; Card labels
      [:div.entry-card-head-right
        (when (and (:topic-slug entry-data) show-topic?)
          (let [topic-name (s/upper (or (:topic-name entry-data) (:topic-slug entry-data)))
                topic-color (utils/rgb-with-opacity (:topic-color entry-data) "1")
                topic-border (utils/rgb-with-opacity (:topic-color entry-data) "0.4")]
            [:div.new
              {:style {:color topic-color
                       :border (str "1px solid " topic-border)}}
              topic-name]))]]
    [:div.entry-card-content.group
      [:div.entry-card-headline
        (:headline entry-data)]
      [:div.entry-card-body
        {:dangerouslySetInnerHTML #js {:__html (cut-body (:body entry-data))}}]]
    [:div.entry-card-footer.group
      (interactions-summary entry-data)
      [:div.more-button.dropdown
        [:button.mlb-reset.more-ellipsis.dropdown-toggle
          {:type "button"
           :id (str "more-ellipsis-toggle-" (router/current-board-slug) "-" (:uuid entry-data))
           :on-click #(utils/event-stop %)
           :title "More"
           :data-toggle "dropdown"
           :aria-haspopup true
           :aria-expanded false
           ; :data-placement "top"
           ; :data-container "body"
           }]
        [:div.dropdown-menu
          [:div.triangle]
          [:ul.entry-card-more-menu
            [:li
              {:on-click (fn [e]
                           (utils/event-stop e)
                           (dis/dispatch! [:entry-edit entry-data]))}
              "Edit"]
            [:li
              {:on-click (fn [e]
                           (utils/event-stop e)
                           (dis/dispatch! [:entry-delete entry-data]))}
              "Delete"]]]]]])