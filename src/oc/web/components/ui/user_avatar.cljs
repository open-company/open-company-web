(ns oc.web.components.ui.user-avatar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.stores.user :as store]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.images :as img]))

(rum/defcs user-avatar-image < rum/static
                               (rum/local false ::use-default)
                               ui-mixins/refresh-tooltips-mixin
  [s user-data tooltip?]
  (let [use-default @(::use-default s)
        default-avatar (store/user-icon (:user-id user-data))
        user-avatar-url (if (or use-default (empty? (:avatar-url user-data)))
                         (utils/cdn default-avatar)
                         (-> user-data :avatar-url (img/optimize-image-url 72)))]
    [:div.user-avatar-img-container
      {:data-user-id (:user-id user-data)
       :data-intercom-target "User avatar dropdown"
       :class utils/hide-class}
      [:div.user-avatar-img-helper]
      [:img.user-avatar-img
        {:src user-avatar-url
         :on-error #(reset! (::use-default s) true)
         :data-toggle (if tooltip? "tooltip" "")
         :data-placement "top"
         :data-container "body"
         :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
         :title (if tooltip? (:name user-data) "")}]]))

(rum/defcs user-avatar < rum/static
                         rum/reactive
                         (drv/drv :current-user-data)
  [s {:keys [click-cb]}]
  (let [not-mobile? (not (responsive/is-tablet-or-mobile?))]
    [:button.mlb-reset.user-avatar-button.group
      {:type "button"
       :on-click #(when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (user-avatar-image (drv/react s :current-user-data) not-mobile?)]))
