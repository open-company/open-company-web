(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(def distance-from-bottom 80)

(defn- self-board [user-data]
  {:name "Post as yourself"
   :slug (:user-id user-data)
   :publisher-board true
   :access "team"
   :authors [(:user-id user-data)]})

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
                             ;; Locals
                             (rum/local nil ::container-max-height)
                             ;; Local mixins
                             {:did-mount (fn [s]
                               (calc-max-height s)
                               s)
                              :did-remount (fn [_ s]
                               (calc-max-height s)
                               s)}
  [s {:keys [active-slug on-change moving? current-user-data]}]
  (js/console.log "DBG sections-picker" on-change current-user-data)
  (let [editable-boards (vals (drv/react s :editable-boards))
        post-as-self-board (some #(when (= (:slug %) (:user-id current-user-data)) %) editable-boards)
        filtered-boards (filter #(not= (:slug %) (:user-id current-user-data)) editable-boards)
        sorted-boards (sort-by :name filtered-boards)
        all-sections (concat [(or post-as-self-board (self-board current-user-data))] sorted-boards)
        container-style (if @(::container-max-height s)
                          {:max-height (str @(::container-max-height s) "px")}
                          {})
        scroller-style  (if @(::container-max-height s)
                          {:max-height (str (- @(::container-max-height s) 55) "px")}
                          {})
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.sections-picker
      {:style container-style}
      [:div.sections-picker-content
        {:style scroller-style}
        (when (pos? (count all-sections))
          (for [b all-sections
                :let [active (= (:slug b) active-slug)]]
            [:div.sections-picker-section
              {:key (str "sections-picker-" (:uuid b))
               :class (utils/class-set {:active active
                                        :has-access-icon (#{"public" "private"} (:access b))
                                        :bottom-border (:publisher-board b)})
               :on-click #(when (fn? on-change)
                            (on-change b))}
              [:div.sections-picker-section-name
                (:name b)]
              (case (:access b)
                "private" [:div.private-icon]
                "public" [:div.public-icon]
                nil)]))]]))