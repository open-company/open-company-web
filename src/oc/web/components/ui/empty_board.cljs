(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :current-user-data)
                         (drv/drv :contributor-user-data)
                         section-mixins/container-nav-in
  [s]
  (let [is-inbox? (= (router/current-board-slug) "inbox")
        is-all-posts? (= (router/current-board-slug) "all-posts")
        is-bookmarks? (= (router/current-board-slug) "bookmarks")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-contributor? (seq (router/current-contributor-id))
        current-user-data (drv/react s :current-user-data)
        contrib-user-data (drv/react s :contributor-user-data)
        current-user-contrib? (and is-contributor?
                                   (= (:user-id contrib-user-data) (:user-id current-user-data)))]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration-container
          {:class (when is-contributor? "contrib")}
          [:div.empty-board-illustration
            {:class (utils/class-set {:contributor is-contributor?
                                      :inbox is-inbox?
                                      :all-posts is-all-posts?
                                      :drafts is-drafts-board?
                                      :bookmarks is-bookmarks?})}
            (when is-contributor?
              (user-avatar-image contrib-user-data))]]
        [:div.empty-board-title
          (cond
           current-user-contrib? "You haven't published any posts yet"
           is-contributor? (str (:short-name contrib-user-data) " hasn't posted anything yet")
           is-inbox? "You’re all caught up!"
           is-all-posts? "Recent is a stream of what’s new in Carrot"
           is-drafts-board? "Nothing in drafts"
           is-bookmarks? "You don't have any bookmarks"
           :else "This section is empty")]]]))
