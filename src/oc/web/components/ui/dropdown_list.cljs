(ns oc.web.components.ui.dropdown-list
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(rum/defc dropdown-list
  "Component to create a dropdown list. Accept a map with these keys:
   :items - the list of items map to render, see below for the format to use
   :value - current value, add the class selected to the corresponing value li
   :on-change - fn callback for changes
   :on-blur - fn called on click out of the dropdown cb
   :selected-icon - full url of an icon to show besides the selected item, ignored if empty
   :unselected-icon - full url of an icon to show besides the unselected item, ignored if empty
  Elements should be passed in a vector with this format:
  {:value \"the value\" :label \"The label to show, optional\" :color \"optional\"}.
  Elements with this format will be transfomed into a divider line:
  {:value nil :label :divider-line}."
  < rum/static
    (on-window-click-mixin (fn [s e]
     (when-not (utils/event-inside? e (rum/ref-node s :dropdown-list))
       (let [on-blur (:on-blur (first (:rum/args s)))]
         (when (fn? on-blur)
           (on-blur))))))
  [{:keys [items value on-change on-blur selected-icon unselected-icon placeholder]}]
  (let [fixed-items (map #(if (not (contains? % :label))
                            (assoc % :label (:value %))
                            %) items)]
    [:div.dropdown-list-container
      {:ref :dropdown-list}
      [:div.triangle]
      [:div.dropdown-list-content
        [:ul.dropdown-list
          (for [item fixed-items]
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
          placeholder)]]))