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
                     #(when-not (utils/event-inside? % (sel1 :div.dropdown-list-container))
                        (let [on-blur (:on-blur (first (:rum/args s)))]
                          (when (fn? on-blur)
                            (on-blur))))))
                   s)
     :will-unmount (fn [s]
                     (events/unlistenByKey @(::window-click-listener s))
                    s)}
  [{:keys [items value on-change on-blur selected-icon unselected-icon]}]
  [:div.dropdown-list-container
    [:div.triangle]
    [:div.dropdown-list-content
      [:ul.dropdown-list
        (for [item items]
          [:li.dropdown-list-item
            {:key (str "dropdown-list-item-" (if (= (:label item) :divider-line) "divider" (:value item)))
             :on-click #(when (and (:value item) (fn? on-change)) (on-change item))
             :class (utils/class-set {:select (and (:value item) (= value (:value item)))
                                      :divider-line (= (:label item) :divider-line)})}
            (when (string? (:label item))
              (if (and selected-icon (= (:value item) value))
                [:img.dropdown-list-item-icon {:src selected-icon}]
                (when unselected-icon
                  [:img.dropdown-list-item-icon {:src unselected-icon}])))
            (when (string? (:label item))
              (:label item))])]]])