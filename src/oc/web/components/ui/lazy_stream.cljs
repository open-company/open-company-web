(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(defn- get-interstitials []
  (vec (repeat 5
      [:div.lazy-stream-interstitial
        [:div.lazy-stream-header.animate]
        [:div.lazy-stream-headline.animate]
        [:div.lazy-stream-body.animate]
        [:div.lazy-stream-body-2.animate]
        [:div.lazy-stream-footer.animate]])))

(rum/defcs lazy-stream < rum/static
                         (rum/local false ::mounted)
                         {:did-mount (fn [s]
                           (utils/after 180 #(reset! (::mounted s) true))
                           s)}
  [s stream-comp]
  [:div.lazy-stream
    ; (if @(::mounted s)
    ;   (stream-comp)
    ;   [:div.lazy-stream-interstitial])
    (assoc (get-interstitials) 0 :div.lazy-stream-inner)])