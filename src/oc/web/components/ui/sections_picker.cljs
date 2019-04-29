(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.section-editor :refer (section-editor)]))

(defn calc-max-height [s]
  (let [win-height (or (.-clientHeight (.-documentElement js/document))
                       (.-innerHeight js/window))
        dom-node (rum/dom-node s)
        scroll-top (.-scrollTop (.-scrollingElement js/document))
        body-rect (.getBoundingClientRect (.-body js/document))
        elem-rect (.getBoundingClientRect dom-node)
        offset-top (- (.-top elem-rect) (+ (.-top body-rect) scroll-top))]
    (reset! (::container-max-height s) (- win-height offset-top 8))))

(rum/defcs sections-picker < ;; Mixins
                             rum/reactive
                             (on-window-click-mixin (fn [s e]
                              (when-not (utils/event-inside? e (rum/dom-node s))
                                (dis/dispatch! [:input [:show-sections-picker] false]))))
                             ;; Derivatives
                             (drv/drv :editable-boards)
                             (drv/drv :section-editing)
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
  (let [section-editing (drv/react s :section-editing)
        fixed-section-editing (when-not (string/blank? (:name section-editing))
                                section-editing)
        all-sections (filterv #(not= (:slug fixed-section-editing) (:slug %)) (vals (drv/react s :editable-boards)))
        fixed-all-sections (if fixed-section-editing
                             (vec (concat [fixed-section-editing] all-sections))
                             all-sections)
        sorted-all-sections (sort-by :name fixed-all-sections)
        container-style (if @(::container-max-height s)
                          {:max-height (str @(::container-max-height s) "px")}
                          {})
        scroller-style  (if @(::container-max-height s)
                          {:max-height (str (- @(::container-max-height s) 55) "px")}
                          {})
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.sections-picker.group
      {:style container-style}
      [:div.sections-picker-header
        [:div.sections-picker-header-left
          (if moving?
            "Move to"
            "Post to")]
        [:div.sections-picker-header-right
          [:button.mlb-reset.add-new-section-bt
            {:on-click #(nav-actions/show-section-add-with-callback on-change)
             :title "Create a new section"
             :aria-label "Create a new section"
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement "top"
             :data-container "body"
             :data-trigger "hover"
             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
      [:div.sections-picker-content
        {:style scroller-style}
        [:div.sections-picker-group
          (when (pos? (count sorted-all-sections))
            (for [b sorted-all-sections
                  :let [active (= (:slug b) active-slug)]]
              [:div.sections-picker-section.group
                {:key (str "sections-picker-" (:uuid b))
                 :class (when active "active")
                 :on-click #(when (fn? on-change)
                              (on-change b))}
                [:div.sections-picker-section-name
                  [:span.name (:name b)]
                  [:span.count (str " (" (or (:entry-count b) 0) ")")]]
                [:div.sections-picker-circle]]))]]]))