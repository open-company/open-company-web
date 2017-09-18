(ns oc.web.components.ui.dropdown-list
  (:require [rum.core :as rum]
            [goog.events :as events]
            [oc.web.lib.utils :as utils]
            [dommy.core :refer-macros (sel1)]
            [goog.events.EventType :as EventType]))

(rum/defc dropdown-list
  "Component to create a dropdown list. Elements should be passed in a vector with this format:
  {:value \"the value\" :label \"The label to show\"}.
  Elements with this format will be transfomed into a divider line:
  {:value nil :label :divider-line}."
  < rum/static
    (rum/local nil ::window-click-listener)
    (rum/local nil ::window-click-listener)
    {:will-mount (fn [s]
                   (reset! (::window-click-listener s)
                    (events/listen js/window EventType/CLICK
                     #(when (not (utils/event-inside? % (sel1 :div.dropdown-list-container)))
                        (let [on-click-out (nth (:rum/args s) 3)]
                          (when (fn? on-click-out)
                            (on-click-out))))))
                   s)
     :will-unmount (fn [s]
                     (events/unlistenByKey @(::window-click-listener s))
                    s)}
  [items selected-item did-select-cb did-click-out]
  [:div.dropdown-list-container
    [:div.triangle]
    [:div.dropdown-list-content
      [:ul.dropdown-list
        (for [item items]
          [:li.dropdown-list-item
            {:key (str "dropdown-list-item-" (if (= (:label item) :divider-line) "divider" (:value item)))
             :on-click #(when (and (:value item) (fn? did-select-cb)) (did-select-cb item))
             :class (utils/class-set {:select (and (:value item) (= selected-item (:value item)))
                                      :divider-line (= (:label item) :divider-line)})}
            (when (string? (:label item))
              (:label item))])]]])