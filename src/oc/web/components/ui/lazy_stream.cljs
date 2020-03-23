(ns oc.web.components.ui.lazy-stream
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs lazy-stream < rum/static
                         rum/reactive
                         (drv/drv :board-data)
                         (drv/drv :contributor-data)
                         (drv/drv :container-data)
                         (drv/drv :activity-data)
                         (drv/drv :foc-layout)
                         {:did-mount (fn [s]
                           (utils/scroll-to-y (:scroll-y @router/path) 0)
                           s)}
  [s stream-comp]
  (let [board-data (drv/react s :board-data)
        contributor-data (drv/react s :contributor-data)
        container-data (drv/react s :container-data)
        activity-data (drv/react s :activity-data)
        foc-layout (drv/react s :foc-layout)
        is-container? (dis/is-container? (router/current-board-slug))
        is-contributor? (seq (router/current-contributor-id))
        is-expanded-post? (seq (router/current-activity-id))
        container-loaded? (and ;; Another container
                               (not is-expanded-post?)
                               is-container?
                               ;; But no container data yet
                              (not container-data))
        board-loaded? (and ;; board specified
                           (not is-expanded-post?)
                           (not is-container?)
                           (not is-contributor?)
                           ;; But no board data yet
                           (not board-data))
        contributor-loaded? (and ;; Contrib specified
                                 (not is-expanded-post?)
                                 is-contributor?
                                 ;; But no contributor data data yet
                                 (not contributor-data))
        post-loaded? (and ;; Post specified
                          is-expanded-post?
                          ;; but not post data yet
                          (not activity-data))
        loading? (or container-loaded?
                     board-loaded?
                     contributor-loaded?
                     post-loaded?)]
    [:div.lazy-stream
      (if-not loading?
        (stream-comp)
        [:div.lazy-stream-interstitial
          {:class (when (= foc-layout dis/other-foc-layout) "collapsed")
           :style {:height (str (+ (:scroll-y @router/path)
                                   (or (.. js/document -documentElement -clientHeight)
                                       (.-innerHeight js/window)))
                             "px")}}])]))
