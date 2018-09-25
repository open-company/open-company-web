(ns oc.web.actions.nav-sidebar
  (:require [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.components.org-settings :as org-settings]))

(defn close-navigation-sidebar []
  (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))

(defn nav-to-url! [e url]
  (when (and e
             (.-preventDefault e))
    (.preventDefault e))
  (dis/dispatch! [:reset-ap-initial-at (router/current-org-slug)])
  (let [current-path (str (.. js/window -location -pathname) (.. js/window -location -search))]
    (if (= current-path url)
      (do
        (routing-actions/routing @router/path)
        (user-actions/initial-loading true))
      (router/nav! url)))
  (close-navigation-sidebar))

(defn mobile-nav-sidebar []
  (dis/dispatch! [:update [:mobile-navigation-sidebar] not]))


(defn show-section-add []
  (dis/dispatch! [:input [:show-section-add-cb]
   (fn [sec-data note]
     (if sec-data
       (section-actions/section-save sec-data note #(dis/dispatch! [:input [:show-section-add] false]))
       (dis/dispatch! [:input [:show-section-add] false])))])
  (dis/dispatch! [:input [:show-section-add] true])
  (close-navigation-sidebar))

(defn show-invite []
  (org-settings/show-modal :invite)
  (close-navigation-sidebar))