(ns oc.web.components.ui.user-avatar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.icon :as i]
            [oc.web.lib.responsive :as responsive]))

(def default-user-image "/img/ML/happy_face_red.svg")
(def other-user-images
 ["/img/ML/happy_face_green.svg"
  "/img/ML/happy_face_blue.svg"
  "/img/ML/happy_face_purple.svg"
  "/img/ML/happy_face_yellow.svg"])

(defn random-user-image []
  (first (shuffle (vec (conj other-user-images default-user-image)))))

(def default-avatar-url (random-user-image))

(defn- user-icon [user-id]
  (if (= user-id (jwt/get-key :user-id))
    ;; If the user id is the same of the current JWT use the red icon
    default-user-image
    ;; if not get a random icon from the rest of the images vector
    (first other-user-images)))

(rum/defcs user-avatar-image < rum/static
                               (rum/local false ::use-default)
  [s user-data tooltip?]
  (let [use-default @(::use-default s)
        default-avatar (user-icon (:user-id user-data))
        user-avatar-url (if (or use-default (empty? (:avatar-url user-data)))
                         (utils/cdn default-avatar)
                         (:avatar-url user-data))]
    [:div.user-avatar-img-container
      [:div.user-avatar-img-helper]
      [:img.user-avatar-img
        {:src user-avatar-url
         :on-error #(reset! (::use-default s) true)
         :data-toggle (if tooltip? "tooltip" "")
         :data-placement "top"
         :data-container "body"
         :title (if tooltip? (:name user-data) "")}]]))

(rum/defcs user-avatar < rum/static
                         rum/reactive
                         (drv/drv :current-user-data)
  [s {:keys [classes click-cb disable-menu]}]
  (let [not-mobile? (not (responsive/is-tablet-or-mobile?))]
    [:button.user-avatar-button.group
      {:type "button"
       :class (str classes)
       :id "dropdown-toggle-menu"
       :data-toggle (when (or not-mobile? (not disable-menu)) "dropdown")
       :on-click (when (fn? click-cb) (click-cb))
       :aria-haspopup true
       :aria-expanded false}
      (user-avatar-image (drv/react s :current-user-data) not-mobile?)]))