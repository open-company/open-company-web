(ns oc.web.components.ui.boards-picker
  (:require [rum.core :as rum]
            [oops.core :refer (oget)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as du]
            [oc.web.lib.responsive :as responsive]
            [oc.web.local-settings :as ls]))

(def self-board-name "All")

(defn- self-board [user-data]
  {:name ""
   :slug utils/default-board-slug
   :publisher-board true
   :access "team"
   :authors [(:user-id user-data)]})

(def relative-max-allowed-height (/ 1 2))
(def min-allowed-height 200)

(def distance-from-edge 32)

(def default-popup-direction :down)

(defn calc-max-height [state]
  (let [popup-direction (or (-> state :rum/args first :direction))
        parent-node (oget (rum/dom-node state) "parentElement")
        win-height (du/window-height)
        max-allowed-height (* win-height relative-max-allowed-height)
        elem-rect (du/bounding-rect parent-node)
        container-max-height (if (= popup-direction :up)
                               (- (:bottom elem-rect) distance-from-edge)
                               (- (du/viewport-height) (:top elem-rect) (:height elem-rect) distance-from-edge))]
    (reset! (::container-max-height state) (min max-allowed-height (max container-max-height min-allowed-height)))))

(rum/defcs boards-picker < ;; Mixins
                             rum/reactive
                             ;; Derivatives
                             (drv/drv :editable-boards)
                             (drv/drv :private-boards)
                             (drv/drv :public-boards)
                             ;; Locals
                             (rum/local nil ::container-max-height)
                             ;; Local mixins
                             {:did-mount (fn [state]
                               (calc-max-height state)
                               state)}
  [state {:keys [active-slug on-change moving? current-user-data direction] :or {direction default-popup-direction}}]
  (let [editable-boards (vec (vals (drv/react state :editable-boards)))
        private-boards (vec (vals (drv/react state :private-boards)))
        public-boards (vec (vals (drv/react state :public-boards)))
        boards-list (vec (concat editable-boards private-boards public-boards))
        author? (not= (:role current-user-data) :viewer)
        user-publisher-board (some #(when (and (:publisher-board %)
                                               (= (-> % :author :user-id) (:user-id current-user-data)))
                                      %)
                              editable-boards)
        filtered-boards (filter (comp not :publisher-board) boards-list)
        sorted-boards (sort-by :name filtered-boards)
        fixed-publisher-board (or user-publisher-board (self-board current-user-data))
        all-boards (if (and author? ls/publisher-board-enabled?)
                     (vec (cons fixed-publisher-board sorted-boards))
                     (vec sorted-boards))
        container-max-height @(::container-max-height state)
        container-style (if container-max-height
                          {:max-height (str container-max-height "px")}
                          {:opacity 0})
        scroller-style  (if container-max-height
                          {:max-height (str (- container-max-height 55) "px")}
                          {:opacity 0})
        is-mobile? (responsive/is-mobile-size?)]
    [:div.boards-picker
      {:style container-style}
      [:div.boards-picker-content
        {:style scroller-style}
        (when (seq all-boards)
          (for [b all-boards
                :let [active (= (:slug b) active-slug)
                      self-board? (:publisher-board b)
                      fixed-board (if (and author? (not self-board?) active)
                                    ;; When user selects again the currently selected board,
                                    ;; unselect it and go back to publisher board if he has/can have one
                                    fixed-publisher-board
                                    (update b :name #(if self-board?
                                                       self-board-name
                                                       %)))]]
            [:div.boards-picker-board
              {:key (str "boards-picker-" (:uuid b))
               :class (utils/class-set {:active active
                                        :premium-lock (:premium-lock b)
                                        :has-access-icon (#{"public" "private"} (:access b))
                                        :publisher-board (:publisher-board b)})
               :data-toggle (when (and (not is-mobile?)
                                       (:premium-lock b))
                              "tooltip")
               :data-placement "top"
               :data-container "body"
               :title (str (:premium-lock b) " Click for details.")
               :on-click #(if (:premium-lock b)
                            (nav-actions/toggle-premium-picker! (:premium-lock b))
                            (when (fn? on-change)
                              (on-change fixed-board)))}
              [:div.boards-picker-board-name
                (if self-board?
                  self-board-name
                  (:name b))]
              (case (:access b)
                "private" [:div.private-icon]
                "public" [:div.public-icon]
                nil)]))]]))