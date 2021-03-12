(ns oc.web.utils.sentry
  (:require ["@sentry/browser" :as sentry-browser]
            [oc.web.actions.notifications :as notification-actions]
            [taoensso.timbre :as timbre]))


(defn- custom-error [error-name error-message]
  (let [err (js/Error. error-message)]
    (set! (.-name ^js err) error-name)
    err))

(defn capture-error!
  ([e]
   (timbre/info "Capture error:" e)
   (let [event-id (.captureException ^js sentry-browser e)]
     (notification-actions/sentry-event-id event-id)
     event-id))
  ([e error-info]
   (timbre/info "Capture error:" e "extra:" error-info)
   (let [event-id (.captureException ^js sentry-browser e #js {:extra error-info})]
     (notification-actions/sentry-event-id event-id)
     event-id)))

(defn capture-message! [msg & [log-level]]
  (timbre/info "Capture message:" msg)
  (let [event-id (.captureMessage ^js sentry-browser msg (or log-level "info"))]
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
      (.setExtra ^js scope (str prefix (when (seq prefix) "|") (name k)) (get ctx k)))))

(defn capture-message-with-extra-context! [ctx message]
  (timbre/info "Capture message:" message "with context:" ctx)
  (.withScope ^js sentry-browser (fn [scope]
                                   (set-extra-context! scope ctx)
                                   (capture-message! message))))

(defn capture-error-with-extra-context! [ctx error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message "with context:" ctx)
  (.withScope ^js sentry-browser (fn [scope]
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

(defn ^:export show-report-dialog
  ([] (.showReportDialog ^js sentry-browser #js {}))
  ([event-id]
   (.showReportDialog ^js sentry-browser #js {:eventId event-id})))