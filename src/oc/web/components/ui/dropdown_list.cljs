(ns oc.web.components.ui.dropdown-list
  (:require [rum.core :as rum]
            [goog.events :as events]
            [oc.web.lib.utils :as utils]
            [dommy.core :refer-macros (sel1)]
            [goog.events.EventType :as EventType]))

(rum/defc dropdown-list
  "Component to create a dropdown list. Accept a map with these keys:
   :items - the list of items map to render, see below for the format to use
   :value - current value, add the class selected to the corresponing value li
   :on-change - fn callback for changes
   :on-blur - fn called on click out of the dropdown cb
   :selected-icon - full url of an icon to show besides the selected item, ignored if empty
   :unselected-icon - full url of an icon to show besides the unselected item, ignored if empty
  Elements should be passed in a vector with this format:
  {:value \"the value\" :label \"The label to show\" :color \"optional\"}.
  Elements with this format will be transfomed into a divider line:
  {:value nil :label :divider-line}."
  < rum/static
    (rum/local nil ::window-click-listener)
    {:will-mount (fn [s]
                   (reset! (::window-click-listener s)
                    (events/listen (.getElementById js/document "app") EventType/CLICK
                     #(when-not (utils/event-inside? % (sel1 :div.dropdown-list-container))
                        (let [on-blur (:on-blur (first (:rum/args s)))]
                          (when (fn? on-blur)
                            (on-blur))))))
                   s)
     :will-unmount (fn [s]
                     (events/unlistenByKey @(::window-click-listener s))
                    s)}
  [{:keys [items value on-change on-blur selected-icon unselected-icon placeholder]}]
  [:div.dropdown-list-container
    [:div.triangle]
    [:div.dropdown-list-content
      [:ul.dropdown-list
        (for [item items]
          [:li.dropdown-list-item
            {:key (str "dropdown-list-item-" (if (= (:label item) :divider-line) "divider" (:value item)))
             :on-click #(when (and (:value item) (fn? on-change)) (on-change item))
             :style (when (seq (:color item)) {:color (:color item)})
             :class (utils/class-set {:select (and (:value item) (= value (:value item)))
                                      :divider-line (= (:label item) :divider-line)
                                      (str (:class item)) (seq (:class item))})}
            (when (string? (:label item))
              (if (and selected-icon (= (:value item) value))
                [:img.dropdown-list-item-icon {:src selected-icon}]
                (when unselected-icon
                  [:img.dropdown-list-item-icon {:src unselected-icon}])))
            (when (string? (:label item))
              (:label item))])]
      (when placeholder
        placeholder)]])