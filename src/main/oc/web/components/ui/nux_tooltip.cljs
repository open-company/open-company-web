(ns oc.web.components.ui.nux-tooltip
  (:require-macros [if-let.core :refer (if-let*)])
  (:require [rum.core :as rum]
            [dommy.core :as dommy]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.utils :as utils]))

(rum/defcs nux-tooltip < ;;rum/static
  (rum/local nil ::pos)
  {:will-update (fn [state]
                 (if-let* [data (-> state :rum/args first :data)
                           el (dommy/sel1 (:sel data))]
                   (let [rect (dom-utils/bounding-rect el)
                         pos (case (:position data)
                               :top
                               {:x (:x rect)
                                :y (+ (:y rect) (:height rect))}
                               :right
                               {:x (+ (:x rect) (:width rect))
                                :y (+ (:y rect) (/ (:height rect) 2))}
                               :bottom
                               {:x (+ (:x rect) (/ (:width rect) 2))
                                :y (+ (:y rect) (:height rect))}
                               :left
                               {:x (:x rect)
                                :y (+ (:y rect) (/ (:height rect) 2))}
                               :bottom-left
                               {:x (- (dom-utils/window-width) 24)
                                :y (+ (:y rect) (:height rect))}
                               :right-top
                               {:x (+ (:x rect) (:width rect))
                                :y (+ (:y rect) (/ (:height rect) 2))})]
                     (dommy/add-class! el :nux-tooltip-handle)
                     (when-not (= @(::pos state) pos)
                       (reset! (::pos state) pos)))
                   (when @(::pos state)
                     (reset! (::pos state) nil)))
                 state)
   :did-mount (fn [state]
                (utils/after 900
                 #(let [initial-cb (-> state :rum/args first :data :initial-cb)]    
                    (when (and (rum/dom-node state)
                              (fn? initial-cb))
                      (initial-cb))))
                state)}
  [state
   {user-type                               :user-type
    dismiss-cb                              :dismiss-cb
    next-cb                                 :next-cb
    step                                    :step
    {:keys [title description next-title
            steps arrow-position position
            post-dismiss-cb sel]
     :as data}                              :data}]
  (let [{left :x top :y} @(::pos state)]
    (when (and left top
              step)
      [:div.nux-tooltip-container
       {:on-mouse-down utils/event-stop}
        [:div.nux-tooltip
          {:class (utils/class-set {position true})
            :style (clj->js {:left (str left "px")
                             :top (str top "px")})
            :key (str "nux-tooltip-" (name step))}
          (when arrow-position
            [:div.triangle
              {:class (utils/class-set {arrow-position true})}])
          [:div.nux-tooltip-inner
            [:div.nux-tooltip-header
            [:div.nux-tooltip-title
              title]
            [:button.mlb-reset.nux-tooltip-dismiss-bt
              {:on-click (fn [e]
                           (utils/event-stop e)
                           (when-let [el (dommy/sel1 sel)]
                             (dommy/remove-class! el :nux-tooltip-handle))
                           (dismiss-cb e)
                           (when (fn? post-dismiss-cb)
                             (post-dismiss-cb)))}]]
            [:div.nux-tooltip-description
            description]
            [:div.nux-tooltip-footer
            [:div.nux-tooltip-steps
              steps]
            [:button.mlb-reset.nux-tooltip-next-bt
              {:on-click (fn [e]
                          (utils/event-stop e)
                          (next-cb e)
                          (when-let [el (dommy/sel1 sel)]
                            (dommy/remove-class! el :nux-tooltip-handle))
                          (when (fn? post-dismiss-cb)
                            (post-dismiss-cb)))}
              next-title]]]]])))

(rum/defcs nux-tooltips-manager <
  rum/reactive
  (drv/drv :nux)
  [s]
  (let [{step :step user-type :user-type} (drv/react s :nux)]
    (when step
      (nux-tooltip {:step step
                    :user-type user-type
                    :data (nux-actions/get-tooltip-data step user-type)
                    :dismiss-cb #(nux-actions/dismiss-nux)
                    :next-cb #(nux-actions/next-step)}))))