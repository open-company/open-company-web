(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs lazy-stream < rum/static
                         rum/reactive
                         (rum/local false ::delayed)
                         (drv/drv :container-data)
                         (drv/drv :activity-data)
                         (drv/drv :foc-layout)
                         {:did-mount (fn [s]
                           (utils/scroll-to-y (dis/route-param :scroll-y) 0)
                           (utils/after 100 #(reset! (::delayed s) true))
                           s)}
  [s stream-comp]
  (let [container-data (drv/react s :container-data)
        activity-data (drv/react s :activity-data)
        foc-layout (drv/react s :foc-layout)
        ready? (and @(::delayed s)
                    (map? container-data))]
    [:div.lazy-stream
      (if ready?
        (stream-comp)
        [:div.lazy-stream-interstitial
          {:class (when (= foc-layout dis/other-foc-layout) "collapsed")
           :style {:height (str (+ (dis/route-param :scroll-y)
                                   (or (.. js/document -documentElement -clientHeight)
                                       (.-innerHeight js/window)))
                             "px")}}])]))
