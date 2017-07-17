(ns oc.web.components.ui.small-loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc small-loading < rum/static
  ([]
   (small-loading {:style {:padding "2px"} :class "inline mr1"}))
  ([attributes]
   [:div attributes
    [:img {:style {:height "15px" :width "15px"} :src (utils/cdn "/img/small_loading.gif")}]]))
