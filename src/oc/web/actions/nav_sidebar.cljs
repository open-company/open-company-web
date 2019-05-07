(ns oc.web.actions.nav-sidebar
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.section :as section-actions]))

;; Panels
;; :menu
;; :org
;; :integrations
;; :team
;; :invite
;; :billing
;; :profile
;; :notifications
;; :reminders
;; :reminder-{uuid}/:reminder-new
;; :section-add
;; :section-edit

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
  (qsg-actions/turn-on-show-guide)
  (close-navigation-sidebar))

(defn mobile-nav-sidebar []
  (dis/dispatch! [:update [:mobile-navigation-sidebar] not]))

;; Push panel

(defn- push-panel [panel]
  (dis/dispatch! [:update [:panel-stack] #(vec (conj (or % []) panel))]))

(defn- pop-panel []
  (let [panel-stack (:panel-stack @dis/app-state)]
    (when (pos? (count panel-stack))
      (dis/dispatch! [:update [:panel-stack] pop]))
    (peek panel-stack)))

;; Section settings

(defn show-section-editor []
  (push-panel :section-edit))

(defn hide-section-editor []
  (pop-panel))

(defn show-section-add []
  (dis/dispatch! [:input [:show-section-add-cb]
   (fn [sec-data note]
     (if sec-data
       (section-actions/section-save sec-data note #(dis/dispatch! [:input [:show-section-add] false]))
       (pop-panel)))])
  (push-panel :section-add)
  (close-navigation-sidebar))

(defn show-section-add-with-callback [callback]
  (dis/dispatch! [:input [:show-section-add-cb]
   (fn [sec-data note]
     (callback sec-data note)
     (dis/dispatch! [:input [:show-section-add-cb] nil])
     (pop-panel))])
  (push-panel :section-add))

(defn hide-section-add []
  (pop-panel))

;; Reminders

(defn show-reminders []
  (push-panel :reminders)
  (close-navigation-sidebar))

(defn show-new-reminder []
  (push-panel :reminder-new)
  (close-navigation-sidebar))

(defn edit-reminder [reminder-uuid]
  (push-panel (keyword (str "reminder-" reminder-uuid)))
  (close-navigation-sidebar))

(defn close-reminders []
  (pop-panel))

;; Menu

(defn menu-toggle []
  (let [panel-stack (:panel-stack @dis/app-state)]
    (if (= (peek panel-stack) :menu)
      (pop-panel)
      (push-panel :menu))))

(defn menu-close []
  (pop-panel))

;; Show panels

(defn show-org-settings [panel]
  (if panel
    (push-panel panel)
    (pop-panel))
  (close-navigation-sidebar))

(defn show-user-settings [panel]
  (if panel
    (push-panel panel)
    (pop-panel))
  (close-navigation-sidebar))