(ns oc.web.components.ui.small-loading
  (:require [rum.core :as rum]))

(rum/defc small-loading < rum/static
  ([]
   (small-loading {:style {:padding "2px"} :class "inline mr1"}))
  ([attributes]
   [:div attributes
    [:img {:style {:height "15px" :width "15px"} :src "https://d1wc0stj82keig.cloudfront.net/img/small_loading.gif"}]]))
