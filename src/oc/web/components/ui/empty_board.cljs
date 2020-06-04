(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :org-data)
                         (drv/drv :current-user-data)
                         (drv/drv :contributions-user-data)
                         section-mixins/container-nav-in
  [s]
  (let [is-all? (= (router/current-board-slug) "all-posts")
        is-saved? (= (router/current-board-slug) "bookmarks")
        is-home? (= (router/current-board-slug) "following")
        is-unfollowing? (= (router/current-board-slug) "unfollowing")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-contributions? (seq (router/current-contributions-id))
        org-data (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        contrib-user-data (drv/react s :contributions-user-data)
        current-user-contrib? (and is-contributions?
                                   (= (:user-id contrib-user-data) (:user-id current-user-data)))]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration-container
          {:class (when is-contributions? "contrib")}
          [:div.empty-board-illustration
            {:class (utils/class-set {:contributions is-contributions?
                                      :all-posts is-all?
                                      :drafts is-drafts-board?
                                      :saved is-saved?
                                      :home is-home?
                                      :explore is-unfollowing?})}
            (when is-contributions?
              (user-avatar-image contrib-user-data))]]
        [:div.empty-board-title
          (cond
           current-user-contrib? "You haven't published any posts yet"
           is-contributions? (str (:short-name contrib-user-data) " hasn't posted anything yet")
           is-all? (str "This is a feed of what's happening at " (:name org-data) ".")
           is-drafts-board? "Nothing in drafts"
           is-saved? "You don't have any saved update"
           is-home?
           [:div.empty-follow
             "Home is where you'll find updates from the people and topics you're following."
             ; [:button.mlb-reset.follow-picker-bt
             ;   {:on-click #(nav-actions/show-follow-picker)}
             ;   "Curate your Home feed"]
               ]
           is-unfollowing?
           [:div.empty-follow
             "Explore is where you'll find updates that you decided to not follow."
             ; [:button.mlb-reset.follow-picker-bt
             ;   {:on-click #(nav-actions/show-follow-picker)}
             ;   "Curate your feed"]
               ]
           :else "This team has no updates")]]]))
