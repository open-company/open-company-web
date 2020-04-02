(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(def distance-from-bottom 80)

(defn calc-max-height [s]
  (let [win-height (or (.-clientHeight (.-documentElement js/document))
                       (.-innerHeight js/window))
        dom-node (rum/dom-node s)
        scroll-top (.-scrollTop (.-scrollingElement js/document))
        body-rect (.getBoundingClientRect (.-body js/document))
        elem-rect (.getBoundingClientRect dom-node)
        offset-top (- (.-top elem-rect) (+ (.-top body-rect) scroll-top))]
    (reset! (::container-max-height s) (max 239 (- win-height offset-top 8 distance-from-bottom)))))

(rum/defcs sections-picker < ;; Mixins
                             rum/reactive
                             (on-window-click-mixin (fn [s e]
                              (when-not (utils/event-inside? e (rum/dom-node s))
                                (dis/dispatch! [:input [:show-sections-picker] false]))))
                             ;; Derivatives
                             (drv/drv :editable-boards)
                             (drv/drv :current-user-data)
                             ;; Locals
                             (rum/local nil ::container-max-height)
                             ;; Local mixins
                             {:did-mount (fn [s]
                               (calc-max-height s)
                               s)
                              :did-remount (fn [_ s]
                               (calc-max-height s)
                               s)}
  [s active-slug on-change moving?]
  (let [all-sections (vals (drv/react s :editable-boards))
        sorted-all-sections (sort-by :name all-sections)
        container-style (if @(::container-max-height s)
                          {:max-height (str @(::container-max-height s) "px")}
                          {})
        scroller-style  (if @(::container-max-height s)
                          {:max-height (str (- @(::container-max-height s) 55) "px")}
                          {})
        is-mobile? (responsive/is-tablet-or-mobile?)
        current-user-data (drv/react s :current-user-data)
        sorted-sections (filter (comp not :direct) sorted-all-sections)
        direct-sorted-sections (filter :direct sorted-all-sections)]
    [:div.sections-picker
      {:style container-style}
      [:div.sections-picker-content
        {:style scroller-style}
        (when (seq sorted-sections)
          (for [b sorted-sections
                :let [active (= (:slug b) active-slug)]]
            [:div.sections-picker-section
              {:key (str "sections-picker-" (:uuid b))
               :class (utils/class-set {:active active
                                        :has-access-icon (#{"public" "private"} (:access b))})
               :on-click #(when (fn? on-change)
                            (on-change b))}
              [:div.sections-picker-section-name
                (:name b)]
              (case (:access b)
                "private" [:div.private-icon]
                "public" [:div.public-icon]
                nil)]))
        (when (seq direct-sorted-sections)
          [:div.sections-picker-divider
            "Direct:"])
        (when (seq direct-sorted-sections)
          (for [b direct-sorted-sections
                :let [active (= (:slug b) active-slug)]]
            [:div.sections-picker-section.has-access-icon.group
              {:key (str "sections-picker-" (:uuid b))
               :class (utils/class-set {:active active})
               :on-click #(when (fn? on-change)
                            (on-change b))}
              [:div.sections-picker-section-name
                (:name b)]
              (cond
                (= (count (:direct-users b)) 1)
                (user-avatar-image (first (:direct-users b)))
                :else
                [:div.multi-user-direct-count
                  (count (:direct-users b))])]))]]))
