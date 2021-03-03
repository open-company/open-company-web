(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(def last-container-slug (atom nil))

(rum/defcs lazy-stream < rum/static
                         rum/reactive
                         (rum/local true ::delay)
                         (drv/drv :container-data)
                         (drv/drv :activity-data)
                         (drv/drv :board-slug)
                         (drv/drv :foc-layout)
                         {:will-mount (fn [s]
                           (let [container-slug (dis/current-board-slug)]
                             (when-not (= container-slug @last-container-slug)
                               (reset! (::delay s) true)
                               (reset! last-container-slug container-slug)
                               (utils/after 10 #(reset! (::delay s) false)))
                             s))
                          :did-mount (fn [s]
                           (utils/scroll-to-y (dis/route-param :scroll-y) 0)
                           s)}
  [s stream-comp]
  (let [container-data (drv/react s :container-data)
        current-board-slug (drv/react s :board-slug)
        foc-layout (drv/react s :foc-layout)
        data-ready? (map? container-data)
        delay? @(::delay s)
        collapsed-foc? (or (= foc-layout dis/other-foc-layout)
                           (= current-board-slug "replies"))]
    [:div.lazy-stream
      (cond (not data-ready?)
            [:div.lazy-stream-interstitial
             {:class (when collapsed-foc? "collapsed")
              :style {:height (str (+ (dis/route-param :scroll-y)
                                      (or (.. js/document -documentElement -clientHeight)
                                          (.-innerHeight js/window)))
                                   "px")}}]
            delay?
            [:div]
            :else
            (stream-comp))]))
