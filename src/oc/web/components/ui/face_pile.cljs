(ns oc.web.components.ui.face-pile
  (:require [rum.core :as rum]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defc face-pile < rum/static
  [{:keys [users-data face-size face-space] :or {face-size 22 face-space 10}}]
  (when (seq users-data)
    (let [width (+ face-size (* (- face-size face-space) (dec (count users-data))))]
      [:div.face-pile.group
        {:style {:width (str width "px")}}
        (for [user-data users-data]
          [:div.face
            (user-avatar-image user-data)])])))