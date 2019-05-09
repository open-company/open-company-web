(ns oc.web.components.ui.activity-move
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(defn move-post [s]
  ;; move the post
  (let [opts (first (:rum/args s))
        activity-data (:activity-data opts)
        new-board @(::selected-board s)
        on-change (:on-change opts)
        dismiss-cb (:dismiss-cb opts)]
    (activity-actions/activity-move activity-data new-board)
    (reset! (::show-boards-list s) false)
    (reset! (::selected-board s) nil)
    (when (fn? on-change)
      (on-change))
    (when (fn? dismiss-cb)
      (dismiss-cb))))

(rum/defcs activity-move < (rum/local nil ::show-boards-list)
                           (rum/local nil ::selected-board)
                           (on-window-click-mixin (fn [s e]
                            (let [opts (first (:rum/args s))
                                  dismiss-cb (:dismiss-cb opts)]
                              (when (and (not (utils/event-inside? e (rum/dom-node s)))
                                         (fn? dismiss-cb))
                                (dismiss-cb)))))
  [s {:keys [boards-list activity-data dismiss-cb]}]
  (let [sorted-boards-list (sort-by :name boards-list)]
    [:div.activity-move
      {:on-click #(do
                    (utils/event-stop %)
                    (when @(::show-boards-list s)
                      (reset! (::show-boards-list s) false)))}
      [:div.move-post-inner
        [:div.move-post-title
          "Move"]
        [:div.select-new-board
          {:on-click #(do (utils/event-stop %) (reset! (::show-boards-list s) (not @(::show-boards-list s))))
           :class (when (nil? @(::selected-board s)) "placeholder")}
          (or (:name @(::selected-board s)) "Select a new board...")]
        (when @(::show-boards-list s)
          [:div.boards-list
            (for [board sorted-boards-list]
              [:div.board-item
                {:key (str "activity-move-" (:uuid activity-data) "-board-list-" (:slug board))
                 :class (when (= (:board-slug activity-data) (:slug board)) "disabled")
                 :on-click #(when (not= (:board-slug activity-data) (:slug board))
                              (reset! (::selected-board s) board)
                              (reset! (::show-boards-list s) false))}
                (:name board)])])
        [:button.mlb-reset.mlb-default
          {:on-click #(do (utils/event-stop %) (move-post s))
           :disabled (not @(::selected-board s))}
          "Move post"]]]))