(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(def self-board-name "<None>")

(defn- self-board [user-data]
  {:name ""
   :slug utils/default-section-slug
   :publisher-board true
   :access "team"
   :authors [(:user-id user-data)]})

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
                             (drv/drv :org-data)
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
  (let [org-data (drv/react s :org-data)
        editable-boards (vals (drv/react s :editable-boards))
        author? (not= (utils/get-user-type current-user-data org-data) :viewer)
        user-publisher-board (some #(when (and (:publisher-board %)
                                               (= (-> % :author :user-id) (:user-id current-user-data)))
                                      %)
                              editable-boards)
        filtered-boards (filter (comp not :publisher-board) editable-boards)
        sorted-boards (sort-by :name filtered-boards)
        fixed-publisher-board (or user-publisher-board (self-board current-user-data))
        all-sections (if author?
                       (cons fixed-publisher-board sorted-boards)
                       sorted-boards)
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
                :let [active (= (:slug b) active-slug)
                      self-board? (:publisher-board b)
                      fixed-board (if (and author? (not self-board?) active)
                                    ;; When user selects again the currently selected board,
                                    ;; unselect it and go back to publisher board if he has/can have one
                                    fixed-publisher-board
                                    (update b :name #(if self-board?
                                                       self-board-name
                                                       %)))]]
            [:div.sections-picker-section
              {:key (str "sections-picker-" (:uuid b))
               :class (utils/class-set {:active active
                                        :has-access-icon (#{"public" "private"} (:access b))
                                        :publisher-board (:publisher-board b)})
               :on-click #(when (fn? on-change)
                            (on-change fixed-board))}
              [:div.sections-picker-section-name
                (if self-board?
                  self-board-name
                  (:name b))]
              (case (:access b)
                "private" [:div.private-icon]
                "public" [:div.public-icon]
                nil)]))]]))