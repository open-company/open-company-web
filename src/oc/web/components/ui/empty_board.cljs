(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(def mobile-image-size
 {:width 250
  :height 211
  :ratio (/ 250 211)})

(rum/defcs empty-board < rum/reactive
                         (drv/drv :org-data)
                         (drv/drv :board-data)
                         (drv/drv :editable-boards)
                         section-mixins/container-nav-in
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        editable-boards (drv/react s :editable-boards)
        is-all-posts? (= (router/current-board-slug) "all-posts")
        is-follow-ups? (= (router/current-board-slug) "follow-ups")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-author? (utils/is-admin-or-author? org-data board-data)]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration
          {:class (utils/class-set {:drafts is-drafts-board?
                                    :follow-ups is-follow-ups?})}]
        [:div.empty-board-title
          (cond
           ; is-all-posts? "Stay up to date"
           is-drafts-board? "Nothing in drafts"
           is-follow-ups? "You’re all caught up!"
           :else (if-not (:read-only board-data) "Create a post to get started" "There's nothing to see here"))]
        (when (and (not is-drafts-board?)
                   (not is-follow-ups?))
          [:div.empty-board-subtitle
            (cond
             is-all-posts? "All posts is a stream of what’s new in Carrot."
             :else (when is-author? (str "Looks like there aren’t any posts in " (:name board-data) ".")))])]]))