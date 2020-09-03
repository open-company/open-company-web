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
        inner-width (if is-mobile? (- width 2) width)
        margin (js/Math.ceil (/ inner-width 9))
        outer-width (+ inner-width (* margin 2))
        left-margin (js/Math.ceil (if is-mobile? (/ inner-width 2) (/ inner-width 1.8)))
        total-width (when (pos? face-pile-count)
                      (+ (* (- outer-width left-margin) face-pile-count) left-margin))]
    [:div.face-pile.group
     {:style {:width (str total-width "px")}
      :class (when (> face-pile-count 1) "show-border")}
     (for [face faces-to-render]
       [:div.face-pile-face
        {:key (str "face-pile-" (or (:user-id face) (rand 10)))}
        (user-avatar-image face)])]))