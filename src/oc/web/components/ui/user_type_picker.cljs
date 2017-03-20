(ns oc.web.components.ui.user-type-picker
  ""
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]            
            [oc.web.components.ui.team-disclaimer-popover :refer (team-disclaimer-popover)]
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
          [:i.fa.fa-user] " Viewer"]
        [:li
          {:class (when (= user-type :author) "active")
           :on-click #(click-cb :author)}
          [:i.fa.fa-pencil] " Author"]
        (when-not hide-admin?
          [:li
            {:class (when (= user-type :admin) "active")
             :on-click #(click-cb :admin)}
            [:i.fa.fa-gear] " Admin"])]]))

(defn show-team-disclaimer-popover [e & [hide-admin]]
  (.stopPropagation e)
  (add-popover-with-rum-component team-disclaimer-popover {;:hide-popover-cb #(hide-popover nil "team-disclaimer-popover")
                                                           :hide-admin hide-admin
                                                           :width 422
                                                           :height (if hide-admin 170 230)
                                                           :hide-on-click-out true
                                                           :z-index-popover 0
                                                           :container-id "team-disclaimer-popover"}))

(rum/defcs user-type-picker < (rum/local nil ::last-user-type)
  [s user-type enabled? did-select-cb show-admins?]
  [:div.user-type-picker
    {:on-mouse-out #(let [el (or (.-toElement %) (.-relatedTarget %))]
                      ; mouseOut event is triggerend also when the mouse enter a child so we need to
                      ; check that it's not entering a child of this
                      (when (not (utils/is-parent? (sel1 [:div.user-type-picker]) el))
                        ; reset the user-type to what was before the user enter this div with the mouse
                        (did-select-cb @(::last-user-type s))))}
    [:button.user-type-picker-btn.btn-reset.viewer
      {:class (str "" (when-not enabled? "disabled") (when (= user-type :viewer) " active"))
       :on-mouse-over #(did-select-cb :viewer)
       :on-click #(do (reset! (::last-user-type s) :viewer) (did-select-cb :viewer))}
      (when (= user-type :viewer)
        [:span.user-type-disc.viewer
          {:on-click #(show-team-disclaimer-popover %)}
          "VIEWER " [:i.fa.fa-question-circle]])
      [:i.fa.fa-user]]
    [:button.user-type-picker-btn.btn-reset.author
      {:class (str "" (when-not enabled? "disabled") (when (= user-type :author) " active"))
       :on-mouse-over #(did-select-cb :author)
       :on-click #(do (reset! (::last-user-type s) :author) (did-select-cb :author))}
      (when (= user-type :author)
        [:span.user-type-disc.author
          {:on-click #(show-team-disclaimer-popover %)}
          "AUTHOR " [:i.fa.fa-question-circle]])
      [:i.fa.fa-pencil]]
    (when show-admins?
      [:button.user-type-picker-btn.btn-reset.admin
        {:class (str "" (when-not enabled? "disabled") (when (= user-type :admin) " active"))
         :on-mouse-over #(did-select-cb :admin)
         :on-click #(do (reset! (::last-user-type s) :admin) (did-select-cb :admin))}
        (when (= user-type :admin)
          [:span.user-type-disc.admin
            {:on-click #(show-team-disclaimer-popover %)}
            "ADMIN " [:i.fa.fa-question-circle]])
      [:i.fa.fa-gear]])])