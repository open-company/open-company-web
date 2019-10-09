(ns oc.web.components.ui.user-type-dropdown
  ""
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]))

(rum/defc user-type-dropdown < rum/static
  [{:keys [user-id user-type on-change hide-admin on-remove disabled?]}]
  (let [user-dropdown-id (str "dropdown-" user-id)]
    [:div.dropdown
      [:button.btn-reset.user-type-btn.dropdown-toggle.oc-input
        {:id user-dropdown-id
         :data-toggle "dropdown"
         :aria-haspopup true
         :aria-expanded false
         :disabled disabled?}
        (case user-type
          :admin
          "Admin"
          :author
          "Contributor"
          "Viewer")]
      [:ul.dropdown-menu.user-type-dropdown-menu
        {:aria-labelledby user-dropdown-id}
        [:li
          {:on-click #(when (fn? on-change)
                        (on-change :viewer))
           :class (when (= user-type :viewer) "selected")}
          "Viewer"]
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