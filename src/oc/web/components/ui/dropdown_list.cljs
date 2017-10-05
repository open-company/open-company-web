(ns oc.web.components.ui.dropdown-list
  (:require [rum.core :as rum]
            [goog.events :as events]
            [oc.web.lib.utils :as utils]
            [dommy.core :refer-macros (sel1)]
            [goog.events.EventType :as EventType]))

(rum/defc dropdown-list < rum/static
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
            {:key (str "dropdown-list-item-" (:value item))
             :on-click #(when (fn? did-select-cb) (did-select-cb item))
             :class (when (= selected-item (:value item)) "select")}
            (:label item)])]]])