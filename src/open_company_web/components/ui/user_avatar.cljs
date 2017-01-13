(ns open-company-web.components.ui.user-avatar
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.lib.responsive :as responsive]))

(rum/defc user-avatar < rum/static
                        rum/reactive
  [{:keys [classes click-cb]}]
  (let [has-avatar (not (clojure.string/blank? (get-in (rum/react dis/app-state) [:jwt :avatar])))
        not-mobile? (not (responsive/is-mobile-size?))]
    [:button.user-avatar-button.group
      {:type "button"
       :class (str classes (when-not has-avatar " no-image"))
       :id "dropdown-toggle-menu"
       :data-toggle (when not-mobile? "dropdown")
       :click-cb (when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (if-not has-avatar
        [:div.user-avatar-name
          (let [avatar-name (clojure.string/upper-case (str (first (get-in (rum/react dis/app-state) [:jwt :first-name]))
                                                            (first (get-in (rum/react dis/app-state) [:jwt :last-name]))))]
            [:span.user-avatar-name-span avatar-name])]
        [:img.user-avatar-img
          {:src (jwt/get-key :avatar)
           :title (get-in (rum/react dis/app-state) [:jwt :real-name])}])
      [:img {:src "/img/vert-ellipsis.svg" :width 5 :height 24}]]))