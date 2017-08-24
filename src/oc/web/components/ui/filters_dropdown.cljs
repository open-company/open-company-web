(ns oc.web.components.ui.filters-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]))

(defn compare-topic-names [topics topic-slug-1 topic-slug-2]
  (let [topic-name-1 (some #(when (= (:slug %) topic-slug-1) (:name %)) topics)
        topic-name-2 (some #(when (= (:slug %) topic-slug-2) (:name %)) topics)]
    (compare topic-name-1 topic-name-2)))

(rum/defcs filters-dropdown < rum/reactive
                              (drv/drv :board-filters)
                              (drv/drv :board-data)
                              (rum/local false ::show-filters-dropdown)
                              (rum/local false ::remove-bt-hovering)
  [s]
  (let [board-data (drv/react s :board-data)
        board-filters (drv/react s :board-filters)
        topic-groups (group-by :topic-slug (:entries board-data))]
    [:div.filters-dropdown-name.group
      (when-not (string? board-filters)
        [:button.mlb-reset.filters-dropdown-button.choice
            {:type "button"
             :class (when (or (nil? board-filters) (= board-filters :latest)) "select")
             :on-click (fn []
                         (cook/set-cookie! (router/last-board-filter-cookie (router/current-org-slug) (router/current-board-slug)) (name :latest) (* 60 60 24 30) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
                         (router/nav! (oc-urls/board)))}
            "Latest"])
      (when-not (string? board-filters)
        [:button.mlb-reset.filters-dropdown-button.filters-dropdown-by-topic.choice
            {:type "button"
             :class (when (= board-filters :by-topic) "select")
             :on-click (fn []
                         (cook/set-cookie! (router/last-board-filter-cookie (router/current-org-slug) (router/current-board-slug)) (name :by-topic) (* 60 60 24 30) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
                         (router/nav! (oc-urls/board-sort-by-topic)))}
            "By Topic"])
      (when (string? board-filters)
        [:button.mlb-reset.board-remove-filter
          {:on-click #(let [org-slug (router/current-org-slug)
                            board-slug (router/current-board-slug)
                            last-filter (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug)))]
                        (if (= last-filter :by-topic)
                          (router/nav! (oc-urls/board-sort-by-topic))
                          (router/nav! (oc-urls/board))))
           :on-mouse-enter #(reset! (::remove-bt-hovering s) true)
           :on-mouse-leave #(reset! (::remove-bt-hovering s) false)}
          [:img {:src (utils/cdn (str "/img/ML/board_remove_filter" (when @(::remove-bt-hovering s) "_white") ".png")) :width 12 :height 12}]])
      (when (string? board-filters)
        [:button.mlb-reset.filters-dropdown-button.choice
          {:class (when (or (= board-filters :by-topic) (string? board-filters)) "select")
           :on-click #(when (pos? (count topic-groups))
                        (reset! (::show-filters-dropdown s) (not @(::show-filters-dropdown s))))}
          (or (:name (utils/get-topic (:topics board-data) board-filters)) (s/capital board-filters))])
      [:div.filters-dropdown-container
        [:button.mlb-reset.filters-dropdown-caret.dropdown-toggle.choice
          {:class (when (or (= board-filters :by-topic) (string? board-filters)) "select")
           :on-click #(when (pos? (count topic-groups))
                        (reset! (::show-filters-dropdown s) (not @(::show-filters-dropdown s))))}
          (when (pos? (count topic-groups)) [:i.fa.fa-caret-down])]
        (let [sorted-topics (sort #(compare-topic-names (:topics board-data) %1 %2) (remove #(empty? %) (keys topic-groups)))
              selected-topics (filter #(utils/in? sorted-topics (:slug %)) (:topics board-data))
              topics (vec (map #(clojure.set/rename-keys % {:name :label :slug :value}) selected-topics))]
          (when @(::show-filters-dropdown s)
            (dropdown-list topics board-filters
             (fn [t]
               (reset! (::show-filters-dropdown s) false)
               (router/nav! (oc-urls/board-filter-by-topic (or (:value t) "uncategorized"))))
             (fn []
               (reset! (::show-filters-dropdown s) false)))))]]))