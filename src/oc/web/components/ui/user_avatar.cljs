(ns oc.web.components.ui.user-avatar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.icon :as i]
            [oc.web.lib.responsive :as responsive]))

(def default-user-image "/img/ML/user_avatar_red.png")

(rum/defcs user-avatar-image < rum/static
                               (rum/local false ::use-default)
  [s user-data]
  (let [use-default @(::use-default s)
        user-avatar-url (if (or use-default (empty? (:avatar-url user-data))) default-user-image (:avatar-url user-data))]
    [:img.user-avatar-img
      {:src user-avatar-url
       :on-error #(reset! true (::use-default s))
       :title (str (:first-name user-data) " " (:last-name user-data))}]))

(rum/defcs user-avatar < rum/static
                         rum/reactive
                         (drv/drv :current-user-data)
  [s {:keys [classes click-cb]}]
  (let [not-mobile? (not (responsive/is-mobile-size?))]
    [:button.user-avatar-button.group
      {:type "button"
       :class (str classes)
       :id "dropdown-toggle-menu"
       :data-toggle (when not-mobile? "dropdown")
       :on-click (when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (user-avatar-image (drv/react s :current-user-data))]))