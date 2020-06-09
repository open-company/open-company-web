(ns oc.web.components.ui.dropdown-list
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.mixins.ui :as ui-mixins]))

(rum/defc dropdown-list
  "Component to create a dropdown list. Accept a map with these keys:
   :items - the list of items map to render, see below for the format to use
   :value - current value, add the class selected to the corresponing value li
   :on-change - fn callback for changes
   :on-blur - fn called on click out of the dropdown cb
   :selected-icon - full url of an icon to show besides the selected item, ignored if empty
   :unselected-icon - full url of an icon to show besides the unselected item, ignored if empty
  Elements should be passed in a vector with this format:
  {:value \"the value\"
   :label \"The label to show, optional\"
   :color \"optional\"
   :user-map \"optional: to user the user avatars\"
   :disabled \"optional: not usable\"
   :tooltip  \"optional: tooltip to show on the row\"}.
  Elements with this format will be transfomed into a divider line:
  {:value nil :label :divider-line}."
  < rum/static
    ;; Mixins
    (ui-mixins/on-window-click-mixin (fn [s e]
     (when-not (utils/event-inside? e (rum/ref-node s :dropdown-list))
       (let [on-blur (:on-blur (first (:rum/args s)))]
         (when (fn? on-blur)
           (on-blur))))))
    ui-mixins/refresh-tooltips-mixin
  [{:keys [items value on-change on-blur selected-icon unselected-icon placeholder]}]
  (let [fixed-items (map #(if-not (contains? % :label)
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
               :on-click #(if (:disabled item)
                            (utils/event-stop %)
                            (when (and (:value item) (fn? on-change))
                              (on-change item %)))
               :style (when (seq (:color item)) {:color (:color item)})
               :title (:tooltip item)
               :data-toggle (when (:tooltip item)
                              "tooltip")
               :data-placement "top"
               :data-container "body"
               :class (utils/class-set {:select (and (:value item) (= value (:value item)))
                                        :divider-line (= (:label item) :divider-line)
                                        :user-avatar-icons (:user-map item)
                                        :disabled (:disabled item)
                                        (str (:class item)) (seq (:class item))})}
              (when (and (string? (:label item))
                         (or selected-icon
                            unselected-icon))
                (if (and selected-icon (= (:value item) value))
                  [:img.dropdown-list-item-icon {:src selected-icon}]
                  (when unselected-icon
                    [:img.dropdown-list-item-icon {:src unselected-icon}])))
              (when (and (string? (:label item))
                         (:user-map item)
                         (:avatar-url (:user-map item)))
                (user-avatar-image (:user-map item)))
              (when (string? (:label item))
                [:span.dropdown-list-item-label
                  (:label item)])])]
        (when placeholder
          placeholder)]]))