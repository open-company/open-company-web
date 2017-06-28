(ns oc.web.components.ui.filters-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

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
                {:on-click #(dis/dispatch! [:board-filters-set :latest])
                 :class (if (= board-filters :latest) "select" "")}
                "Latest Updates"]
              [:li.category
                {:on-click #(dis/dispatch! [:board-filters-set :by-topic])
                 :class (if (= board-filters :by-topic) "select" "")}
                "Latest by Topic"]
              [:li.divider]
              (for [t (:topics board-data)]
                [:li
                  {:on-click #(dis/dispatch! [:board-filters-set t])
                   :class (if (= board-filters t) "select" "")
                   :key (str "board-filters-topic-" t)}
                  (s/capital t)])]]]]]))