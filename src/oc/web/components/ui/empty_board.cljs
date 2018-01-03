(ns oc.web.components.ui.empty-board
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(def mobile-image-size
 {:width 250
  :height 211
  :ratio (/ 250 211)})

(rum/defcs empty-board < rum/reactive
                         (drv/drv :board-data)
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
              {:on-click #(dis/dispatch! [:entry-edit {:board-slug (:slug board-data) :board-name (:name board-data)}])}
              "Add one?"])])
      [:img.empty-board-image
        {:src (utils/cdn (if mobile?
                           "/img/ML/mobile_empty_board.png"
                           "/img/ML/empty_board.svg"))
         :srcSet (when mobile?
                    (str (utils/cdn "/img/ML/mobile_empty_board@2x.png") "  2x"))
         :style {:width (str (if mobile? (- ww 24 24) 416) "px")
                 :height (str (if mobile? (* (- ww 24 24) (:ratio mobile-image-size)) 424) "px")
                 :max-width (str (if mobile? (:width mobile-image-size) 416) "px")
                 :max-height (str (if mobile? (:height mobile-image-size) 424) "px")}}]
      (when mobile?
        (let [empty-board-copy (str "Shoot, looks like there aren’t any posts in " (:name board-data) " yet....")]
          [:div.empty-board-footer
            {:dangerouslySetInnerHTML (utils/emojify empty-board-copy)}]))
      (when false ;; mobile?
        [:button.mlb-reset.empty-board-create-first-post
          [:div.empty-board-first-post-container
            [:div.empty-board-first-post-pencil]
            "Create the first post"]])]))