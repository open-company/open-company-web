(ns oc.web.components.ui.nux-tooltip
  (:require [rum.core :as rum]
            [oops.core :refer (oset!)]
            [dommy.core :as dommy]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.utils :as utils]))

(defn- check-scroll-lock [state new-tip-data]
  (when (and @(::scroll-locked? state)
             (not (:lock-scroll new-tip-data)))
    (utils/after 100 #(dom-utils/unlock-page-scroll)))
  (when (and (not @(::scroll-locked? state))
             (:lock-scroll new-tip-data))
    (utils/after 100 #(dom-utils/lock-page-scroll))))

(defn- scroll-to-tooltip [state data el]
  (case (:scroll data)
    :top
    (oset! js/document "scrollingElement.scrollTop" (utils/page-scroll-top))
    :element
    (.scrollIntoView el #js {:block "center"})
    nil)
  (check-scroll-lock state data))

(defn- remove-old-tooltip-handle [state key]
  (when (not= key @(::last-key state))
    (when-let [old-el (dommy/sel1 @(::last-sel state))]
      (when (dommy/has-class? old-el :nux-tooltip-handle)
        (dommy/remove-class! old-el :nux-tooltip-handle)))))

(defn- next-tooltip [state el data]
  (let [key (-> state :rum/args first :key)
        new-key? (not= @(::last-key state) key)
        _ (when new-key?
            (scroll-to-tooltip state data el))
        rect (dom-utils/bounding-rect el)
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
               :y (+ (:y rect) (/ (:height rect) 2))}
              :left-top
              {:x (:x rect)
               :y (+ (:y rect) (/ (:height rect) 2))}
              :top-right
              {:x (- (:x rect) (:width rect))
               :y (:y rect)})]
    (dommy/add-class! el :nux-tooltip-handle)
    (when-not (= @(::pos state) pos)
      (reset! (::pos state) pos))
    (when new-key?
      (remove-old-tooltip-handle state key)
      (reset! (::last-key state) key)
      (reset! (::last-sel state) (:sel data)))))

(defn- init-tooltip [state data]
  (when @(::pos state)
    (reset! (::pos state) nil))
  (when (fn? (:show-el-cb data))
    ((:show-el-cb data))
    (utils/after 1500 #(reset! (::rand state) (rand 10000)))))

(defn- check-data
  ([state] (check-data state false))
  ([state init?]
   (let [args (-> state :rum/args first)
         data (:data args)
         el (when (:sel data) (dommy/sel1 (:sel data)))
         el-rect (when el (dom-utils/bounding-rect el))
         empty-rect? (and (map? el-rect)
                          (zero? (:width el-rect))
                          (zero? (:height el-rect)))]
     (cond init?
           (init-tooltip state data)
           (or (not (:sel data)) (not el) empty-rect?)
           (when (fn? (:dismiss-cb args))
             ((:dismiss-cb args) false))
           (and el (not empty-rect?))
           (next-tooltip state el data)))))

(rum/defcs nux-tooltip < rum/static
  (rum/local nil ::pos)
  (rum/local nil ::last-key)
  (rum/local nil ::last-sel)
  (rum/local nil ::rand)
  ui-mixins/no-scroll-mixin
  {:will-mount (fn [state]
                 (check-data state true)
                 state)
   :did-update (fn [state]
                 (check-data state)
                 state)}
  [state
   {nux-type                                :nux-type
    dismiss-cb                              :dismiss-cb
    next-cb                                 :next-cb
    prev-cb                                 :prev-cb
    key                                     :key
    {:keys [title description next-title back-title
            steps arrow-position position sel key
            post-dismiss-cb post-next-cb post-prev-cb
            scroll-lock]
     :as data}                              :data}]
  (let [{left :x top :y} @(::pos state)]
    (when (and data left top)
      [:div.nux-tooltip-container.foc-click-stop.exp-click-stop.foc-collapsed-click-stop
       {:on-mouse-down dom-utils/event-stop!
        :on-click dom-utils/event-stop!
        :on-mouse-up dom-utils/event-stop!
        :on-scroll (when scroll-lock dom-utils/event-stop!)}
        [:div.nux-tooltip
          {:class (utils/class-set {position true})
            :style (clj->js {:left (str left "px")
                             :top (str top "px")})
            :key (str "nux-tooltip-" key)}
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
                             (utils/after 100 #(dommy/remove-class! el :nux-tooltip-handle)))
                           (dismiss-cb e)
                           (when (fn? post-dismiss-cb)
                             (post-dismiss-cb)))}]]
            [:div.nux-tooltip-description
            description]
            [:div.nux-tooltip-footer
             [:div.nux-tooltip-back-bt-container
              (when back-title
                [:button.mlb-reset.nux-tooltip-back-bt
                  {:on-click (fn [e]
                              (utils/event-stop e)
                              (when-let [el (dommy/sel1 sel)]
                                (utils/after 100 #(dommy/remove-class! el :nux-tooltip-handle)))
                              (prev-cb)
                              (when (fn? post-prev-cb)
                                (post-prev-cb)))}
                  back-title])]
             [:div.nux-tooltip-steps
              steps]
             [:div.nux-tooltip-next-bt-container
              (when next-title
                [:button.mlb-reset.nux-tooltip-next-bt
                 {:on-click (fn [e]
                             (utils/event-stop e)
                             (when-let [el (dommy/sel1 sel)]
                               (utils/after 100 #(dommy/remove-class! el :nux-tooltip-handle)))
                             (next-cb true)
                             (when (fn? post-next-cb)
                               (post-next-cb true)))}
                 next-title])]]]]])))

(rum/defcs nux-tooltips-manager <
  rum/reactive
  (drv/drv :nux)
  [s]
  (let [{key :key nux-type :nux-type} (drv/react s :nux)]
    (when key
      (nux-tooltip {:key key
                    :nux-type nux-type
                    :data (nux-actions/get-tooltip-data key nux-type)
                    :dismiss-cb #(nux-actions/dismiss-nux %)
                    :next-cb #(nux-actions/next-step)
                    :prev-cb #(nux-actions/prev-step)}))))