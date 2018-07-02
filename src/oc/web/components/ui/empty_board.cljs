(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.activity :as activity-actions]))

(def mobile-image-size
 {:width 250
  :height 211
  :ratio (/ 250 211)})

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
                         section-mixins/container-nav-in
  [s]
  (let [board-data (drv/react s :board-data)
        mobile? (responsive/is-mobile-size?)
        ww (when mobile? (responsive/ww))]
    [:div.empty-board.group
      (when-not mobile?
        [:div.empty-board-headline
          (str "There aren’t any posts in " (:name board-data) " yet. ")
          (when-not (:read-only board-data)
            [:button.mlb-reset
              {:on-click #(activity-actions/entry-edit {:board-slug (:slug board-data)
                                                        :board-name (:name board-data)})}
              "Add one?"])])
      [:img.empty-board-image
        {:src (utils/cdn "/img/ML/empty_board.svg")
         :style {:width (str (if mobile? (- ww 24 24) 416) "px")
                 :height (str (if mobile? (* (- ww 24 24) (:ratio mobile-image-size)) 424) "px")
                 :max-width (str (if mobile? (:width mobile-image-size) 416) "px")
                 :max-height (str (if mobile? (:height mobile-image-size) 424) "px")}}]
      (when mobile?
        [:div.empty-board-footer
          "Add a team update, announcement, or story to get started."])
      (when false ;; mobile?
        [:button.mlb-reset.empty-board-create-first-post
          [:div.empty-board-first-post-container
            [:div.empty-board-first-post-pencil]
            "Create the first post"]])]))