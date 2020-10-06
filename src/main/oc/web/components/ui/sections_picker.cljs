(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oops.core :refer (oget)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as du]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]))

(def self-board-name "All")

(defn- self-board [user-data]
  {:name ""
   :slug utils/default-section-slug
   :publisher-board true
   :access "team"
   :authors [(:user-id user-data)]})

(def relative-max-allowed-height (/ 1 2))
(def min-allowed-height 200)

(def distance-from-edge 32)

(def default-popup-direction :down)

(defn calc-max-height [s]
  (let [popup-direction (or (-> s :rum/args first :direction))
        parent-node (oget (rum/dom-node s) "parentElement")
        win-height (du/window-height)
        max-allowed-height (* win-height relative-max-allowed-height)
        elem-rect (du/bounding-rect parent-node)
        container-max-height (if (= popup-direction :up)
                               (- (:bottom elem-rect) distance-from-edge)
                               (- (du/viewport-height) (:top elem-rect) (:height elem-rect) distance-from-edge))]
    (reset! (::container-max-height s) (min max-allowed-height (max container-max-height min-allowed-height)))))

(rum/defcs sections-picker < ;; Mixins
                             rum/reactive
                             ;; Derivatives
                             (drv/drv :org-data)
                             (drv/drv :editable-boards)
                             ;; Locals
                             (rum/local nil ::container-max-height)
                             ;; Local mixins
                             {:did-mount (fn [s]
                               (calc-max-height s)
                               s)}
  [s {:keys [active-slug on-change moving? current-user-data direction] :or {direction default-popup-direction}}]
  (let [org-data (drv/react s :org-data)
        editable-boards (vals (drv/react s :editable-boards))
        author? (not= (:role current-user-data) :viewer)
        user-publisher-board (some #(when (and (:publisher-board %)
                                               (= (-> % :author :user-id) (:user-id current-user-data)))
                                      %)
                              editable-boards)
        filtered-boards (filter (comp not :publisher-board) editable-boards)
        sorted-boards (sort-by :name filtered-boards)
        fixed-publisher-board (or user-publisher-board (self-board current-user-data))
        all-sections (if (and author? ls/publisher-board-enabled?)
                       (cons fixed-publisher-board sorted-boards)
                       sorted-boards)
        container-style (if @(::container-max-height s)
                          {:max-height (str @(::container-max-height s) "px")}
                          {:opacity 0})
        scroller-style  (if @(::container-max-height s)
                          {:max-height (str (- @(::container-max-height s) 55) "px")}
                          {:opacity 0})
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