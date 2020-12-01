(ns oc.web.components.ui.user-type-dropdown
  "Show a list of user-types and events on it."
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.user :as user-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defc user-type-dropdown < rum/static
                               ui-mixins/refresh-tooltips-mixin
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
       (when-not hide-admin
         [:li
          {:on-click #(when (fn? on-change)
                        (on-change :admin))
           :class (when (= user-type :admin) "selected")}
          "Admin"])
        [:li
          {:on-click #(when (fn? on-change)
                        (on-change :author))
           :class (when (= user-type :author) "selected")}
          "Contributor"]
        [:li
         {:on-click #(if premium?
                       (when (fn? on-change)
                         (on-change :viewer))
                       (nav-actions/toggle-premium-picker!))
          :data-toggle (when-not premium? "tooltip")
          :data-placement "top"
          :data-container "body"
          :title "Premium accounts can assign view-only access to teammates. Click for details."
          :class (utils/class-set {:selected (= user-type :viewer)
                                   :premium-lock (not premium?)})}
         "Viewer"]
        (when (fn? on-remove)
          [:li.remove-li
            {:on-click #(on-remove)}
            "Remove User"])]]))