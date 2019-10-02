(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(rum/defcs lazy-stream < rum/static
                         rum/reactive
                         (drv/drv :board-data)
                         (drv/drv :container-data)
                         (drv/drv :activity-data)
                         {:did-mount (fn [s]
                           (utils/scroll-to-y (:scroll-y @router/path) 0)
                           s)}
  [s stream-comp]
  (let [board-data (drv/react s :board-data)
        container-data (drv/react s :container-data)
        activity-data (drv/react s :activity-data)
        loading? (or ;; Board specified
                     (and (not= (router/current-board-slug) "all-posts")
                          (not= (router/current-board-slug) "follow-ups")
                          ;; But no board data yet
                          (not board-data))
                     ;; Another container
                     (and (or (= (router/current-board-slug) "all-posts")
                              (= (router/current-board-slug) "follow-ups"))
                          ;; But no all-posts data yet
                         (not container-data))
                     ;; Activity loaded
                     (and (router/current-activity-id)
                          (not activity-data)))]
    [:div.lazy-stream
      (if-not loading?
        (stream-comp)
        [:div.lazy-stream-interstitial
          {:style {:height (str (+ (:scroll-y @router/path)
                                   (or (.. js/document -documentElement -clientHeight)
                                       (.-innerHeight js/window)))
                             "px")}}])]))
