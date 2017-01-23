(ns open-company-web.components.ui.user-avatar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.lib.responsive :as responsive]))

(rum/defcs avatar-with-initials < rum/static
                                 rum/reactive
                                 (drv/drv :jwt)
  [s]
  [:div.user-avatar-name
    (when (or (:first-name (drv/react s :jwt))
              (:last-name (drv/react s :jwt)))
      (let [first-name-initial (or (first (:first-name (drv/react s :jwt))) "")
            last-name-initial (or (first (:last-name (drv/react s :jwt))) "")
            avatar-name (clojure.string/upper-case (str first-name-initial
                                                        last-name-initial))]
        [:span.user-avatar-name-span avatar-name]))])

(rum/defcs user-avatar-image < rum/static
                               rum/reactive
                               (drv/drv :jwt)
  [s]
  (if-let [has-avatar (not (clojure.string/blank? (:avatar (drv/react s :jwt))))]
    [:img.user-avatar-img
      {:src (:avatar (drv/react s :jwt))
       :title (:real-name (drv/react s :jwt))}]
    (avatar-with-initials)))

(rum/defcs user-avatar < rum/static
                         rum/reactive
                         (drv/drv :jwt)
  [s {:keys [classes click-cb]}]
  (let [not-mobile? (not (responsive/is-mobile-size?))]
    [:button.user-avatar-button.group
      {:type "button"
       :class (str classes (when (clojure.string/blank? (:avatar (drv/react s :jwt))) " no-image"))
       :id "dropdown-toggle-menu"
       :data-toggle (when not-mobile? "dropdown")
       :on-click (when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (user-avatar-image)
      [:img {:src "/img/vert-ellipsis.svg" :width 5 :height 24}]]))