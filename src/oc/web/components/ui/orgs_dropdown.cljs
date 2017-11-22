(ns oc.web.components.ui.orgs-dropdown
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]))

(rum/defc org-dropdown-item < rum/static
  [current-slug org]
  [:li
    {:class (when (= (:slug org) current-slug) "active")
     :on-click #(do (utils/event-stop %) (router/nav! (oc-urls/org (:slug org))))}
    (org-avatar org false :always)])

(rum/defcs orgs-dropdown < rum/static
                           rum/reactive
                           (drv/drv :orgs)
                           (drv/drv :org-data)
  [s]
  (let [orgs (drv/react s :orgs)
        org-data (drv/react s :org-data)
        current-org-slug (:slug org-data)
        should-show-dropdown? (> (count orgs) 1)]
    [:div.orgs-dropdown
      {:class (utils/class-set {:dropdown should-show-dropdown?
                                :org-has-logo (seq (:logo-url org-data))})}
      [:button.orgs-dropdown-btn
        {:id "orgs-dropdown"
         :class (when should-show-dropdown? "dropdown-toggle")
         :data-toggle (when should-show-dropdown? "dropdown")
         :aria-haspopup true
         :aria-expanded false
         :on-click (fn [e] (utils/event-stop e))}
        (org-avatar org-data (not should-show-dropdown?))]
      (when should-show-dropdown?
        [:ul.dropdown-menu
          {:aria-labelledby "orgs-dropdown"}
          (for [org orgs]
            (rum/with-key
             (org-dropdown-item current-org-slug org)
             (str "org-dropdown-" (:slug org))))])]))