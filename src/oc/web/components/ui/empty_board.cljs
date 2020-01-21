(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(rum/defcs empty-board < rum/reactive
                         section-mixins/container-nav-in
  [s]
  (let [is-inbox? (= (router/current-board-slug) "inbox")
        is-all-posts? (= (router/current-board-slug) "all-posts")
        is-follow-ups? (= (router/current-board-slug) "follow-ups")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration-container
          [:div.empty-board-illustration
            {:class (utils/class-set {:inbox is-inbox?
                                      :all-posts is-all-posts?
                                      :drafts is-drafts-board?
                                      :follow-ups is-follow-ups?})}]]
        [:div.empty-board-title
          (cond
           is-all-posts? "All posts is a stream of what’s new in Carrot."
           is-drafts-board? "You have no drafts."
           (or is-inbox?
              is-follow-ups?) "You’re all caught up!"
           :else "This section is empty.")]]]))
