(ns oc.web.components.ui.orgs-dropdown
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]))

(rum/defc org-dropdown-item < rum/static
  [current-slug org is-mobile?]
  [:li
    {:class (when (= (:slug org) current-slug) "active")
     :on-click (fn [e]
                 (.stopPropagation e)
                 (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
                 (dis/dispatch! [:input [:orgs-dropdown-visible] false]))}
    [:a
      {:href (oc-urls/default-landing (:slug org))}
      (org-avatar org false :always)]])

(rum/defcs orgs-dropdown < rum/static
                           rum/reactive
                           (drv/drv :orgs)
                           (drv/drv :org-data)
                           (drv/drv :orgs-dropdown-visible)
                           (on-window-click-mixin (fn [s e]
                            (when (and @(drv/get-ref s :orgs-dropdown-visible)
                                        (not (utils/event-inside? e (rum/dom-node s))))
                              (dis/dispatch! [:input [:orgs-dropdown-visible] false]))))
  [s]
  (let [orgs (drv/react s :orgs)
        org-data (drv/react s :org-data)
        current-org-slug (:slug org-data)
        should-show-dropdown? (> (count orgs) 1)
        orgs-dropdown-visible (drv/react s :orgs-dropdown-visible)
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.orgs-dropdown
      {:class (utils/class-set {:dropdown should-show-dropdown?
                                :org-has-logo (seq (:logo-url org-data))})}
      [:button.orgs-dropdown-btn
        {:id "orgs-dropdown"
         :class (utils/class-set {:dropdown-toggle should-show-dropdown?
                                  :show-dropdown-caret orgs-dropdown-visible})
         :on-click (fn [e]
                     (utils/event-stop e)
                     (when should-show-dropdown?
                       (dis/dispatch! [:input [:orgs-dropdown-visible] (not orgs-dropdown-visible)])))}
        (org-avatar org-data (not should-show-dropdown?) :always)]
      (when orgs-dropdown-visible
        [:div.orgs-dropdown-container
          [:div.triangle]
          [:ul.orgs-dropdown-menu
            (for [org orgs]
              (rum/with-key
               (org-dropdown-item current-org-slug org is-mobile?)
               (str "org-dropdown-" (:slug org))))]])]))