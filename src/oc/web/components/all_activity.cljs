(ns oc.web.components.all-activity
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.components.entry-card :refer (entry-card)]))

(rum/defcs all-activity < rum/static
                          rum/reactive
                          (drv/drv :all-activity)
  [s]
  (let [all-activity-data (drv/react s :all-activity)]
    (js/console.log "all-activity/render data" all-activity-data)
    [:div.all-activity.group
      [:div.all-activity-cards
        (for [e (:entries all-activity-data)]
          (rum/with-key (entry-card e (not (empty? (:headline e))) (not (empty? (:body e))) true) (str "all-activity-entry-" (:uuid e))))]
      [:div.all-activity-nav
        [:div.nav-year.selected
          "2017"]
        (for [m (reverse ["January" "February" "March" "April" "May" "June"])]
          [:div.nav-month
            {:class (when (= m "June") "selected")
             :key (str "nav-month-" m)}
            m])
        [:div.nav-year
          {:class ""}
          "2016"]]]))
