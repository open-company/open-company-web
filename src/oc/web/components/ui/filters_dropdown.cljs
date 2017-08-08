(ns oc.web.components.ui.filters-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]))

(defn compare-topic-names [topics topic-slug-1 topic-slug-2]
  (let [topic-name-1 (some #(when (= (:slug %) topic-slug-1) (:name %)) topics)
        topic-name-2 (some #(when (= (:slug %) topic-slug-2) (:name %)) topics)]
    (compare topic-name-1 topic-name-2)))

(rum/defcs filters-dropdown < rum/reactive
                              (drv/drv :board-filters)
                              (drv/drv :board-data)
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
                          (router/nav! (oc-urls/board))))}])
      (when (string? board-filters)
        [:button.mlb-reset.filters-dropdown-button.choice
          {:type "button"
           :id "filters-dropdown-btn"
           :class (when (or (= board-filters :by-topic) (string? board-filters)) "select")
           :data-toggle (if (pos? (count topic-groups)) "dropdown" "")}
          (or (:name (utils/get-topic (:topics board-data) board-filters)) (s/capital board-filters))])
      [:div.filters-dropdown-container
        [:button.mlb-reset.filters-dropdown-caret.dropdown-toggle.choice
          {:type "button"
           :id "filters-dropdown-btn"
           :class (when (or (= board-filters :by-topic) (string? board-filters)) "select")
           :data-toggle (if (pos? (count topic-groups)) "dropdown" "")
           :aria-haspopup true
           :aria-expanded false}
          (when (pos? (count topic-groups)) [:i.fa.fa-caret-down])]
        [:div.filters-dropdown-list.dropdown-menu
          {:aria-labelledby "filters-dropdown-btn"}
          [:div.triangle]
          [:div.filters-dropdown-list-content
            [:ul
              (for [topic-slug (sort #(compare-topic-names (:topics board-data) %1 %2) (remove #(empty? %) (keys topic-groups)))
                    :let [t (some #(when (= (:slug %) topic-slug) %) (:topics board-data))]]
                [:li.choice
                  {:on-click #(router/nav! (oc-urls/board-filter-by-topic (or (:slug t) "uncategorized")))
                   :class (if (or (= board-filters (:slug t))
                                  (and (= board-filters "uncategorized")
                                       (nil? t))) "select" "")
                   :key (str "board-filters-topic-" (or (:slug t) "uncategorized"))}
                  (if t
                    (s/capital (:name t))
                    "Uncategorized")])]]]]]))