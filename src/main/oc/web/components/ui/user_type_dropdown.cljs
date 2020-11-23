(ns oc.web.components.ui.user-type-dropdown
  "Show a list of user-types and events on it."
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(def user-type-premium-descriptions
  [:div.user-type-premium-lock-description
     {:on-click #(nav-actions/toggle-premium-picker!)}
     [:strong "*"]
     [:span "You can invite viewers but all users are authors except for Premium plans."]])

(rum/defc user-type-dropdown < rum/static
  [{:keys [user-id user-type on-change hide-admin on-remove disabled? premium?]}]
  (let [user-dropdown-id (str "dropdown-" user-id)]
    [:div.dropdown
      [:button.btn-reset.user-type-btn.dropdown-toggle.oc-input
        {:id user-dropdown-id
         :data-toggle "dropdown"
         :aria-haspopup true
         :aria-expanded false
         :disabled disabled?}
        (string/capital (user-utils/user-role-string user-type))]
      [:ul.dropdown-menu.user-type-dropdown-menu
        {:aria-labelledby user-dropdown-id}
        [:li
         {:on-click #(when (fn? on-change)
                       (on-change :viewer))
          :class (utils/class-set {:selected (= user-type :viewer)
                                   :premium-lock (not premium?)})}
         "Viewer"
         (when-not premium?
           "*")]
        [:li
          {:on-click #(when (fn? on-change)
                        (on-change :author))
           :class (when (= user-type :author) "selected")}
          "Contributor"]
        (when-not hide-admin
          [:li
            {:on-click #(when (fn? on-change)
                        (on-change :admin))
             :class (when (= user-type :admin) "selected")}
            "Admin"])
        (when (fn? on-remove)
          [:li.remove-li
            {:on-click #(on-remove)}
            "Remove User"])]]))