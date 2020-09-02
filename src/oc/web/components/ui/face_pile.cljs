(ns oc.web.components.ui.face-pile
  (:require [rum.core :as rum]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(def max-face-pile 3)

(rum/defc face-pile < rum/static
  [{:keys [faces width max-faces] :or {width 18 max-faces max-face-pile}}]
  (let [is-mobile? (responsive/is-mobile-size?)
        faces-to-render (take max-faces faces)
        face-pile-count (count faces-to-render)
        width (if is-mobile? (- width 2) width)
        margin (/ width 9)
        total-width (+ width (* margin 2))
        left-margin (if is-mobile? (/ width 2) (/ width 1.8))
        total-width (when (pos? face-pile-count)
                      (+ left-margin (* (- total-width left-margin) face-pile-count)))]
    [:div.face-pile.group
     {:style {:width (str total-width "px")}
      :class (when (> face-pile-count 1) "show-border")}
     (for [face faces-to-render]
       [:div.face-pile-face
        {:key (str "face-pile-" (or (:user-id face) (rand 10)))}
        (user-avatar-image face)])]))