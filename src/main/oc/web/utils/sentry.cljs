(ns oc.web.utils.sentry
  (:require ["@sentry/browser" :as sentry-browser]
            [oc.web.local-settings :as ls]
            [oops.core :refer (ocall oset!)]
            [oc.web.actions.notifications :as notification-actions]
            [taoensso.timbre :as timbre]))

(defn- fix-event
  ([] (fix-event {}))
  ([event]
   (cond (or (string? event)
             (instance? js/Error event))
         event
         (map? event)
         (clj->js (assoc event :dist ls/sentry-deploy))
         (instance? js/Object event)
         (oset! event "!dist" ls/sentry-deploy)
         :else
         event)))

(defn- custom-error [error-name error-message]
  (let [err (js/Error. error-message)]
    (oset! err "name" error-name)
    err))

(defn capture-error!
  ([e]
   (timbre/info "Capture error:" e)
   (let [event-id (ocall sentry-browser "captureException" e (fix-event))]
     (notification-actions/sentry-event-id event-id)
     event-id))
  ([e error-info]
   (timbre/info "Capture error:" e "extra:" error-info)
   (let [event-id (ocall sentry-browser "captureException" e (fix-event {:extra error-info}))]
     (notification-actions/sentry-event-id event-id)
     event-id)))

(defn capture-message! [msg & [log-level]]
  (timbre/info "Capture message:" msg)
  (let [event-id (ocall sentry-browser "captureMessage" msg (or log-level "info") (fix-event))]
    (notification-actions/sentry-event-id event-id)
    event-id))

(defn ^:export test-sentry []
  (js/setTimeout #(capture-message! "Message from clojure") 1000)
  (try
    (js/errorThrowingCode.)
    (catch :default e
      (capture-error! e))))

(defn set-extra-context! [scope ctx & [prefix]]
  (doseq [k (keys ctx)]
    (if (map? (get ctx k))
      (set-extra-context! scope (get ctx k) (str prefix (when (seq prefix) "|") (name k)))
      (ocall scope "setExtra" (str prefix (when (seq prefix) "|") (name k)) (get ctx k)))))

(defn capture-message-with-extra-context! [ctx message]
  (timbre/info "Capture message:" message "with context:" ctx)
  (ocall sentry-browser "withScope" (fn [scope]
                                      (set-extra-context! scope ctx)
                                      (capture-message! message))))

(defn capture-error-with-extra-context! [ctx error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message "with context:" ctx)
  (ocall sentry-browser "withScope" (fn [scope]
                                      (set-extra-context! scope ctx)
                                      (try
                                        (throw (custom-error (or error-message error-name) (if error-message error-name "Error")))
                                        (catch :default e
                                          (capture-error! e))))))

(defn ^:export capture-error-with-message! [error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message)
  (try
    (throw (custom-error (or error-message error-name) (if error-message error-name "Error")))
    (catch :default e
      (capture-error! e))))

(defn- sentry-event-id
  ([] (sentry-event-id "EMPTY MESSAEG" "info"))
  ([msg] (sentry-event-id msg "info"))
  ([msg log-level] (ocall sentry-browser "captureMessage" msg (or log-level "info") (fix-event))))

(defn ^:export show-report-dialog
  ([] (show-report-dialog (sentry-event-id)))
  ([event-id]
   (ocall sentry-browser "showReportDialog" #js {:eventId event-id})))

(defn ^:export test-error-dialog []
   (notification-actions/show-notification {:title "This is a generic error, click the feedback button below!"
                                            :description "Probably just a temporary issue. Please refresh if this persists."
                                            :server-error true
                                            :id :generic-network-error
                                            :feedback-bt true
                                            :expire 15
                                            :dismiss true})
   (capture-error-with-message! "Test error with user feedback!"))
