(ns oc.web.components.ui.user-type-picker
  ""
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]            
            [oc.web.components.ui.role-explainer-popover :refer (role-explainer-popover)]
            [oc.web.components.ui.popover :as popover :refer (add-popover-with-rum-component
                                                              hide-popover)]))

(rum/defc user-type-dropdown < rum/static
  [user-id user-type click-cb & [hide-admin?]]
  (let [user-dropdown-id (str "dropdown-" user-id)]
    [:div.dropdown
      [:button.btn-reset.user-type-btn.dropdown-toggle
        {:id user-dropdown-id
         :data-toggle "dropdown"
         :aria-haspopup true
         :aria-expanded false}
        (case user-type
          :admin
          [:i.fa.fa-gear]
          :author
          [:i.fa.fa-pencil]
          [:i.fa.fa-user])]
      [:ul.dropdown-menu.user-type-dropdown-menu
        {:aria-labelledby user-dropdown-id}
        [:li
          {:class (when (= user-type :viewer) "active")
           :on-click #(click-cb :viewer)}
          [:i.fa.fa-user] " View"]
        [:li
          {:class (when (= user-type :author) "active")
           :on-click #(click-cb :author)}
          [:i.fa.fa-pencil] " Edit"]
        (when-not hide-admin?
          [:li
            {:class (when (= user-type :admin) "active")
             :on-click #(click-cb :admin)}
            [:i.fa.fa-gear] " Admin"])]]))

(defn show-role-explainer-popover [e & [hide-admin]]
  (.stopPropagation e)
  (add-popover-with-rum-component role-explainer-popover {;:hide-popover-cb #(hide-popover nil "role-explainer-popover")
                                                           :hide-admin hide-admin
                                                           :width 422
                                                           :height (if hide-admin 170 230)
                                                           :hide-on-click-out true
                                                           :z-index-popover 0
                                                           :container-id "role-explainer-popover"}))

(rum/defcs user-type-picker < (rum/local nil ::last-user-type)
  [s user-type enabled? did-select-cb show-admins?]
  [:div.user-type-picker
    {:on-mouse-leave #(did-select-cb @(::last-user-type s))}
    [:button.user-type-picker-btn.btn-reset.viewer
      {:class (str "" (when-not enabled? "disabled") (when (= user-type :viewer) " active"))
       :on-mouse-enter #(did-select-cb :viewer)
       :on-click #(do (reset! (::last-user-type s) :viewer) (did-select-cb :viewer))}
      (when (= user-type :viewer)
        [:span.user-type-disc.viewer
          {:on-click #(show-role-explainer-popover %)}
          "VIEW " [:i.fa.fa-question-circle]])
      [:i.fa.fa-user]]
    [:button.user-type-picker-btn.btn-reset.author
      {:class (str "" (when-not enabled? "disabled") (when (= user-type :author) " active"))
       :on-mouse-enter #(did-select-cb :author)
       :on-click #(do (reset! (::last-user-type s) :author) (did-select-cb :author))}
      (when (= user-type :author)
        [:span.user-type-disc.author
          {:on-click #(show-role-explainer-popover %)}
          "EDIT " [:i.fa.fa-question-circle]])
      [:i.fa.fa-pencil]]
    (when show-admins?
      [:button.user-type-picker-btn.btn-reset.admin
        {:class (str "" (when-not enabled? "disabled") (when (= user-type :admin) " active"))
         :on-mouse-enter #(did-select-cb :admin)
         :on-click #(do (reset! (::last-user-type s) :admin) (did-select-cb :admin))}
        (when (= user-type :admin)
          [:span.user-type-disc.admin
            {:on-click #(show-role-explainer-popover %)}
            "ADMIN " [:i.fa.fa-question-circle]])
      [:i.fa.fa-gear]])])