(ns oc.web.actions.web-app-update
  (:require [oc.web.api :as api]
            [oc.web.actions.notifications :as notification-actions]))

(defonce web-app-update-cycle (atom false))
(defonce web-app-update-timeout (atom nil))
(defonce should-check-for-updates (atom true))

(def initial-web-app-update-wait (* 1000 10)) ;; 10 seconds
(def default-web-app-update-interval (* 1000 60 5)) ;; 5 minutes
(def default-web-app-update-extended-interval (* 1000 60 60 24)) ;; 24 hours

(declare real-web-app-update-check)

(defn- set-web-app-update-timeout!
  "Set a timeout to check for updates."
  [interval]
  (when @web-app-update-timeout
    (.clearTimeout js/window @web-app-update-timeout))
  (reset! web-app-update-timeout
   (.setTimeout js/window real-web-app-update-check interval)))

(defn- web-app-update-notification-dismissed
  "App update notification was dismissed, let's set a new timeout with a longer wait time."
  []
  ;; Clear the old interval
  (when @web-app-update-timeout
    (.clearTimeout js/window @web-app-update-timeout))
  ;; Set a new interval
  (set-web-app-update-timeout! default-web-app-update-extended-interval))

(def update-verbage
  (if js/window.isDesktop
    "Update"
    "Refresh page"))

(defn- real-web-app-update-check
  "Check for app updates, show the notification if necessary, set a new timeout else."
  []
  (reset! web-app-update-timeout nil)
  (api/web-app-version-check
    (fn [{:keys [success body status]}]
      (if (= status 404)
        (notification-actions/show-notification {:title "New version of Carrot available!"
                                                 :web-app-update true
                                                 :id :web-app-update-error
                                                 :dismiss-bt true
                                                 :dismiss-x true
                                                 :secondary-bt-title update-verbage
                                                 :secondary-bt-style :green
                                                 :secondary-bt-class :update-app-bt
                                                 :secondary-bt-cb #(js/window.location.reload)
                                                 :expire 0
                                                 :dismiss web-app-update-notification-dismissed})
        (set-web-app-update-timeout! default-web-app-update-interval)))))

(defn start-web-app-update-check!
  "Start the app update cycle, make sure it's started only once."
  []
  (when (compare-and-set! web-app-update-cycle false true)
    ;; Do the first check after 10 seconds from the app initialization
    (set-web-app-update-timeout! initial-web-app-update-wait))) 