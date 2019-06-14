(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(rum/defcs lazy-stream < rum/static
                         (rum/local false ::mounted)
                         (rum/local false ::scrolled)
                         {:did-mount (fn [s]
                           (utils/after 180 #(reset! (::mounted s) true))
                           s)
                          :after-render (fn [s]
                           (when (and (not @(::scrolled s))
                                      @(::mounted s))
                             (reset! (::scrolled s) true)
                             (utils/scroll-to-y (:scroll-y @router/path) 0))
                           s)}
  [s stream-comp]
  [:div.lazy-stream
    (if @(::mounted s)
      (stream-comp)
      [:div.lazy-stream-interstitial])])