(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defcs lazy-stream < rum/static
                         (rum/local false ::mounted)
                         {:did-mount (fn [s]
                           (utils/after 180 #(reset! (::mounted s) true))
                           s)}
  [s stream-comp]
  [:div.lazy-stream
    (if @(::mounted s)
      (stream-comp)
      [:div.lazy-stream-interstitial])])