(ns oc.web.components.ui.filters-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]))

(defn board-filters-label [board-filters]
  (case board-filters
    :latest "Latest Updates"
    :by-topic "Latest by Topic"
    (s/capital board-filters)))

(rum/defcs filters-dropdown < rum/reactive
                              (drv/drv :board-filters)
                              (drv/drv :board-data)
  [s]
  (let [board-data (drv/react s :board-data)
        board-filters (drv/react s :board-filters)]
    [:div.filters-dropdown-name
      (when (string? board-filters)
        [:button.mlb-reset.board-remove-filter
          {:on-click #(let [org-slug (router/current-org-slug)
                            board-slug (router/current-board-slug)
                            last-filter (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug)))]
                        (if (= last-filter :by-topic)
                          (router/nav! (oc-urls/board-sort-by-topic))
                          (router/nav! (oc-urls/board-sort-latest))))}])
      (board-filters-label board-filters)
      [:div.filters-dropdown-container
        [:button.mlb-reset.filters-dropdown-caret.dropdown-toggle
          {:type "button"
           :id "filters-dropdown-btn"
           :data-toggle "dropdown"
           :aria-haspopup true
           :aria-expanded false}
          [:i.fa.fa-caret-down]]
        [:div.filters-dropdown-list.dropdown-menu
          {:aria-labelledby "filters-dropdown-btn"}
          [:div.triangle]
          [:div.filters-dropdown-list-content
            [:ul
              [:li.category
                {:on-click #(router/nav! (oc-urls/board-sort-latest))
                 :class (if (= board-filters :latest) "select" "")}
                "Latest Updates"]
              [:li.category
                {:on-click #(router/nav! (oc-urls/board-sort-by-topic))
                 :class (if (= board-filters :by-topic) "select" "")}
                "Latest by Topic"]
              [:li.divider]
              (for [t (:topics board-data)]
                [:li
                  {:on-click #(router/nav! (oc-urls/board-filter-by-topic t))
                   :class (if (= board-filters t) "select" "")
                   :key (str "board-filters-topic-" t)}
                  (s/capital t)])]]]]]))