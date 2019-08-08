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
        is-must-see? (= (router/current-board-slug) "must-see")
        is-follow-ups? (= (router/current-board-slug) "follow-ups")
        is-drafts-board? (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-author? (utils/is-admin-or-author? org-data board-data)]
    [:div.empty-board.group
      [:div.empty-board-grey-box
        [:div.empty-board-illustration
          {:class (utils/class-set {:ap-section (and (not is-must-see?) (not is-drafts-board?))
                                    :must-see is-must-see?
                                    :drafts is-drafts-board?
                                    :follow-ups is-follow-ups?})}]
        [:div.empty-board-title
          (cond
           ; is-all-posts? "Stay up to date"
           is-must-see? "Highlight what's important"
           is-drafts-board? "Jot down your ideas and notes"
           is-follow-ups? "You’re all caught up!"
           :else (if (not (:read-only board-data)) "Create a post to get started" "There's nothing to see here"))]
        [:div.empty-board-subtitle
          (cond
           is-all-posts? "All posts is a stream of what’s new in Carrot."
           is-must-see? "When someone marks a post as “must see” everyone will see it here."
           is-follow-ups? "Posts that require action or that you'd like to read later will show up here."
           is-drafts-board? "Keep a private draft until you're ready to share it with your team."
           :else (when is-author? (str "Looks like there aren’t any posts in " (:name board-data) ".")))]
        (when (and (not is-follow-ups?)
                   (or (and (or is-all-posts? is-must-see? is-drafts-board?)
                       (pos? (count editable-boards)))
                        (not (:read-only board-data))))
          [:button.mlb-reset.create-new-post-bt
            {:on-click #(activity-actions/activity-edit)}
            "Create a new post"])]]))