(ns oc.web.components.ui.orgs-dropdown
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]))

(rum/defc org-dropdown-item < rum/static
  [current-slug org]
  [:li
    {:class (when (= (:slug org) current-slug) "active")}
    (org-avatar org true true)])

(rum/defcs orgs-dropdown < rum/static
                           rum/reactive
                           (drv/drv :org-slug)
                           (drv/drv :orgs)
  [s]
  (let [orgs (drv/react s :orgs)
        current-org-slug (drv/react s :org-slug)
        should-show-dropdown? (> (count orgs) 1)]
    [:div.orgs-dropdown
      {:class (when should-show-dropdown? "dropdown")}
      [:button.orgs-dropdown-btn
        {:id "orgs-dropdown"
         :class (when should-show-dropdown? "dropdown-toggle")
         :data-toggle (when should-show-dropdown? "dropdown")
         :aria-haspopup true
         :aria-expanded false
         :on-click (fn [e] (utils/event-stop e))}
        (org-avatar (some #(when (= (:slug %) current-org-slug) %) orgs) (not should-show-dropdown?))]
      (when should-show-dropdown?
        [:ul.dropdown-menu
          {:aria-labelledby "orgs-dropdown"}
          (for [org orgs]
            (rum/with-key
             (org-dropdown-item current-org-slug org)
             (str "org-dropdown-" (:slug org))))])]))