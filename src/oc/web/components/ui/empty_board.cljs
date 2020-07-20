(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
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
                         (drv/drv :board-slug)
                         (drv/drv :contributions-id)
                         section-mixins/container-nav-in
  [s]
  (let [current-board-kw (keyword (drv/react s :board-slug))
        current-contributions-id (drv/react s :contributions-id)
        is-all? (= current-board-kw :all-posts)
        is-replies? (= current-board-kw :replies)
        is-bookmarks? (= current-board-kw :bookmarks)
        is-following? (= current-board-kw :following)
        is-unfollowing? (= current-board-kw :unfollowing)
        is-drafts-board? (= current-board-kw (keyword utils/default-drafts-board-slug))
        is-contributions? (seq current-contributions-id)
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
                                      :replies is-replies?
                                      :drafts is-drafts-board?
                                      :bookmarks is-bookmarks?
                                      :following is-following?
                                      :unfollowing is-unfollowing?})}
            (when is-contributions?
              (user-avatar-image contrib-user-data))]]
        [:div.empty-board-title
          (cond
           current-user-contrib? "You haven't published any posts yet"
           is-replies? 
           [:div.empty-replies
             "When people reply to your updates and comments or mention your name, you’ll see it here."]
           is-contributions? (str (:short-name contrib-user-data) " hasn't posted anything yet")
           is-all? (str "This is a feed of what's happening at " (:name org-data) ".")
           is-drafts-board? "Nothing in drafts"
           is-bookmarks? "No updates are bookmarked"
           is-following?
           [:div.empty-follow
             "Home is where you'll find updates from the people and topics you're following."
             ; [:button.mlb-reset.follow-picker-bt
             ;   {:on-click #(nav-actions/show-follow-picker)}
             ;   "Curate your Home feed"]
               ]
           is-unfollowing?
           [:div.empty-follow
             "Here is where you'll find updates that you decided to not follow."
             ; [:button.mlb-reset.follow-picker-bt
             ;   {:on-click #(nav-actions/show-follow-picker)}
             ;   "Curate your feed"]
               ]
           :else "This topic has no updates")]]]))
