(ns oc.web.actions.nav-sidebar
  (:require [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [taoensso.timbre :as timbre]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.components.org-settings :as org-settings]))

(defn set-posts-filter [posts-filter]
  (dis/dispatch! [:posts-filter posts-filter]))

(defn close-navigation-sidebar []
  (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))

(defn nav-to-url! [e url]
  (when (and e
             (.-preventDefault e))
    (.preventDefault e))
  (set-posts-filter (nth (clojure.string/split url "/") 2))
  (let [current-path (.. js/window -location -pathname)]
    (if (= current-path url)
      (do
        (routing-actions/routing @router/path)
        (user-actions/initial-loading true))
      (router/nav! url)))
  (close-navigation-sidebar))


(defn mobile-nav-sidebar [mobile-navigation-sidebar]
  (dis/dispatch! [:input [:mobile-navigation-sidebar] (not mobile-navigation-sidebar)]))


(defn show-section-add []
  (dis/dispatch! [:input [:show-section-add] true])
  (close-navigation-sidebar))

(defn show-invite []
  (org-settings/show-modal :invite)
  (close-navigation-sidebar))