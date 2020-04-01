(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.face-pile :refer (face-pile)]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(rum/defcs empty-board < rum/reactive
                         (drv/drv :current-user-data)
                         (drv/drv :board-data)
                         (drv/drv :active-users)
                         section-mixins/container-nav-in
  [s]
  (let [is-inbox? (= (router/current-board-slug) "inbox")
        is-all-posts? (= (router/current-board-slug) "all-posts")
        is-bookmarks? (= (router/current-board-slug) "bookmarks")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)
        board-data (drv/react s :board-data)
        is-direct-board? (and (not is-inbox?)
                              (not is-all-posts?)
                              (not is-bookmarks?)
                              (not is-drafts-board?)
                              (:direct board-data))
        current-user-data (drv/react s :current-user-data)
        active-users (drv/react s :active-users)
        direct-users (when is-direct-board?
                       (map #(get active-users %)
                        (remove nil?
                         (map #(let [user-id (if (map? %) (:user-id %) %)] (map? %)
                                 (when (not= user-id (:user-id current-user-data))
                                   user-id))
                          (:authors board-data)))))]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration-container
          {:class (utils/class-set {:direct is-direct-board?})}
          [:div.empty-board-illustration
            {:class (utils/class-set {:inbox is-inbox?
                                      :all-posts is-all-posts?
                                      :drafts is-drafts-board?
                                      :bookmarks is-bookmarks?
                                      :direct-board is-direct-board?})}
            (when is-direct-board?
              (face-pile {:users-data direct-users :face-size 56 :face-space 25}))]]
        [:div.empty-board-title
          (cond
           is-inbox? "You’re all caught up!"
           is-all-posts? "Recent is a stream of what’s new in Carrot"
           is-drafts-board? "Nothing in drafts"
           is-bookmarks? "You don't have any bookmarks"
           is-direct-board? "There are no discussions yet"
           :else "This section is empty")]]]))
