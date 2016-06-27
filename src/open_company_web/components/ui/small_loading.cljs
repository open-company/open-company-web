(ns open-company-web.components.ui.small-loading
  (:require [rum.core :as rum]))

(rum/defc small-loading < rum/static
  ([]
   (rum-small-loading {:style {:padding "2px"} :class "inline mr1"}))
  ([attributes]
   [:div attributes
    [:img {:style {:height "15px" :width "15px"} :src "/img/small_loading.gif"}]]))