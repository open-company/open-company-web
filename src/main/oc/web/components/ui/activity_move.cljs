(ns oc.web.components.ui.activity-move
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.mixins.ui :refer (on-click-out)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]))

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
                           (on-click-out (fn [s e]
                            (let [opts (first (:rum/args s))
                                  dismiss-cb (:dismiss-cb opts)]
                              (when (fn? dismiss-cb)
                                (dismiss-cb)))))
  [s {:keys [current-user-data activity-data dismiss-cb]}]
  [:div.activity-move
    {:on-click #(do
                  (utils/event-stop %)
                  (when @(::show-boards-list s)
                    (reset! (::show-boards-list s) false)))}
    [:div.move-post-inner
      [:div.move-post-title
        "Move"]
      [:div.select-new-board.oc-input
        {:on-click #(do (utils/event-stop %) (reset! (::show-boards-list s) (not @(::show-boards-list s))))
         :class (utils/class-set {:placeholder (nil? @(::selected-board s))
                                  :active @(::show-boards-list s)})}
        (or (:name @(::selected-board s)) "Move to...")]
      (when @(::show-boards-list s)
        [:div.boards-list
          (sections-picker {:active-slug (:board-slug activity-data)
                            :on-change (fn [board-data note dismiss-action]
                                         (when (not= (:board-slug activity-data) (:slug board-data))
                                           (reset! (::selected-board s) board-data)
                                           (reset! (::show-boards-list s) false)))
                            :current-user-data current-user-data})])
      [:button.mlb-reset.mlb-default
        {:on-click #(do (utils/event-stop %) (move-post s))
         :disabled (not @(::selected-board s))}
        "Move post"]]])