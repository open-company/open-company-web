(ns oc.web.components.ui.user-avatar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.components.ui.icon :as i]
            [oc.web.lib.responsive :as responsive]))

(rum/defc avatar-with-initials < rum/static
  [user-data]
  [:div.user-avatar-name
    (when (or (:first-name user-data)
              (:last-name user-data))
      (let [first-name-initial (or (first (:first-name user-data)) "")
            last-name-initial (or (first (:last-name user-data)) "")
            avatar-name (clojure.string/upper-case (str first-name-initial
                                                        last-name-initial))]
        [:span.user-avatar-name-span avatar-name]))])

(rum/defc user-avatar-image < rum/static
  [user-data]
  (if-not (clojure.string/blank? (:avatar-url user-data))
    [:img.user-avatar-img
      {:src (:avatar-url user-data)
       :title (str (:first-name user-data) " " (:last-name user-data))}]
    (avatar-with-initials user-data)))

(rum/defcs user-avatar < rum/static
                         rum/reactive
                         (drv/drv :current-user-data)
  [s {:keys [classes click-cb]}]
  (let [not-mobile? (not (responsive/is-mobile-size?))]
    [:button.user-avatar-button.group
      {:type "button"
       :class (str classes (when (clojure.string/blank? (:avatar-url (drv/react s :current-user-data))) " no-image"))
       :id "dropdown-toggle-menu"
       :data-toggle (when not-mobile? "dropdown")
       :on-click (when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (user-avatar-image (drv/react s :current-user-data))
      [:img {:src (str ls/cdn-url "/img/vert-ellipsis.svg") :width 5 :height 24}]]))