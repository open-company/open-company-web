(ns oc.web.components.entry-card
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :refer (get-color-by-kw)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]
            [goog.object :as gobj]))

(defn cut-body [entry-body]
  (let [cleaned-body (utils/strip-img-tags entry-body)
        dom (.html (js/$ (str "<div>" (gobj/get (utils/emojify cleaned-body) "__html") "</div>")))]
    (.truncate js/$ dom (clj->js {:length 90 :words true :ellipsis "... <span class=\"read-full-update\">Read Full Update</span>"}))))

(rum/defc entry-card-empty
  [read-only?]
  [:div.entry-card.empty-state.group
    [:div.empty-state-content
      [:img {:src (utils/cdn "/img/ML/entry_empty_state.svg")}]
      [:div.entry-card-title
        "This topic’s a little sparse. "
        (when-not read-only?
          [:button.mlb-reset
            {:on-click #(dis/dispatch! [:entry-edit {}])}
            "Add an update?"])]]])

(defn delete-clicked [e entry-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message "Delete this entry?"
                    :first-button-title "No"
                    :first-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :second-button-title "Yes"
                    :second-button-cb #(do
                                        (dis/dispatch! [:entry-delete entry-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs entry-card < rum/static
                        (rum/local false ::hovering-card)
                        (rum/local false ::showing-dropdown)
                        {:after-render (fn [s]
                                         (.click (js/$ "div.entry-card div.entry-card-body a") #(.preventDefault %))
                                         s)
                         :did-mount (fn [s]
                                      (let [entry-data (first (:rum/args s))]
                                        (.on (js/$ (str "div.entry-card-" (:uuid entry-data)))
                                         "show.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) true)))
                                        (.on (js/$ (str "div.entry-card-" (:uuid entry-data)))
                                         "hidden.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) false))))
                                      s)}
  [s entry-data show-topic? has-headline has-body]
  [:div.entry-card
    {:class (str "entry-card-" (:uuid entry-data))
     :on-click #(dis/dispatch! [:entry-modal-fade-in (:uuid entry-data)])
     :on-mouse-over #(reset! (::hovering-card s) true)
     :on-mouse-leave #(reset! (::hovering-card s) false)}
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
          (let [topic-name (or (:topic-name entry-data) (s/upper (:topic-slug entry-data)))]
            [:div.topic-tag
              {:on-click #(router/nav! (oc-urls/board-filter-by-topic (:topic-slug entry-data)))}
              topic-name]))]]
    [:div.entry-card-content.group
      [:div.entry-card-headline
        {:dangerouslySetInnerHTML (utils/emojify (:headline entry-data))
         :class (when has-headline "has-headline")}]
      [:div.entry-card-body
        {:dangerouslySetInnerHTML #js {"__html" (cut-body (:body entry-data))}
         :class (when has-body "has-body")}]]
    [:div.entry-card-footer.group
      (interactions-summary entry-data)
      [:div.more-button.dropdown
        [:button.mlb-reset.more-ellipsis.dropdown-toggle
          {:type "button"
           :class (utils/class-set {:hidden (and (not @(::hovering-card s)) (not @(::showing-dropdown s)))})
           :id (str "entry-card-more-" (router/current-board-slug) "-" (:uuid entry-data))
           :on-click #(utils/event-stop %)
           :title "More"
           :data-toggle "dropdown"
           :aria-haspopup true
           :aria-expanded false}]
        [:div.dropdown-menu
          {:aria-labelledby (str "entry-card-more-" (router/current-board-slug) "-" (:uuid entry-data))}
          [:div.triangle]
          [:ul.entry-card-more-menu
            [:li
              {:on-click (fn [e]
                           (utils/event-stop e)
                           (dis/dispatch! [:entry-edit entry-data]))}
              "Edit"]
            [:li
              {:on-click #(delete-clicked % entry-data)}
              "Delete"]]]]]])