(ns oc.web.actions.nav-sidebar
  (:require [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [taoensso.timbre :as timbre]
            [oc.web.components.org-settings :as org-settings]))

(defn current-posts-filter
  "Find the filter based on the route"
  [posts-filter]
  (cond
   (= posts-filter "all-posts")
   (fn [p] (not= (:status p) "draft"))

   (= posts-filter "must-see")
   (fn [p] (and (= (:must-see p) true)
                (not= (:status p) "draft")))

   (= posts-filter "drafts")
   (fn [p] (= (:status p) "draft"))

   :default
   (fn [p] (and (= (:board-slug p) posts-filter)
                (not= (:status p) "draft")))))

(defn set-posts-filter [posts-filter]
  (timbre/debug posts-filter)
  (dis/dispatch! [:posts-filter (current-posts-filter posts-filter)]))

(defn close-navigation-sidebar []
  (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))

(defn nav-to-url! [e url]
  (when (and e
             (.-preventDefault e))
    (.preventDefault e))
  (set-posts-filter (second (clojure.string/split url "/")))
  (router/nav! url)
  (close-navigation-sidebar))


(defn mobile-nav-sidebar [mobile-navigation-sidebar]
  (dis/dispatch! [:input [:mobile-navigation-sidebar] (not mobile-navigation-sidebar)]))


(defn show-section-add []
  (dis/dispatch! [:input [:show-section-add] true])
  (close-navigation-sidebar))

(defn show-invite []
  (org-settings/show-modal :invite)
  (close-navigation-sidebar))