(ns oc.web.utils.user
  (:require [defun.core :refer (defun)]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.oauth :as oauth]
            [oc.web.lib.utils :as utils]))

(def publisher-board-slug-prefix "publisher-board-")

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(def user-name-max-lenth 64)

;; Associated Slack user check

(defn user-has-slack-with-bot?
  "Check if the current user has an associated Slack user under a team that has the bot."
  [current-user-data bots-data team-roster]
  (let [slack-orgs-with-bot (map :slack-org-id bots-data)
        slack-users (:slack-users (first (filter #(= (:user-id %) (:user-id current-user-data)) (:users team-roster))))]
    (some #(contains? slack-users (keyword %)) slack-orgs-with-bot)))

(defn user-has-push-token?
  [current-user-data push-token]
  (let [current-push-tokens (set (:expo-push-tokens current-user-data))]
    (current-push-tokens push-token)))

(defn auth-link-with-state [original-url {:keys [user-id team-id redirect redirect-origin] :as state}]
  (let [parsed-url       (js/URL. original-url)
        old-state-string (.. parsed-url -searchParams (get "state"))
        decoded-state    (oauth/decode-state-string old-state-string)
        combined-state   (merge decoded-state state)
        new-state-string (oauth/encode-state-string combined-state)]
    (.. parsed-url -searchParams (set "state" new-state-string))
    (str parsed-url)))

(defn- localized-time [tz]
  (try
   (.toLocaleTimeString (js/Date.)
    (.. js/window -navigator -language)
    #js {:hour "2-digit"
         :minute "2-digit"
         :format "hour:minute"
         :timeZone tz})
   (catch :default e
    nil)))

(defn time-with-timezone [timezone]
  (when-let [localized-time (localized-time timezone)]
    (utils/time-without-leading-zeros localized-time)))

(defn timezone-location-string [user-data & [local-time-string?]]
  (let [twt (time-with-timezone (:timezone user-data))]
    (str
     (when (seq twt)
       (str
        twt
        (when local-time-string?
          " local time")))
     (if (seq (:location user-data))
       (if (seq twt)
         (str " (" (:location user-data) ")")
         (:location user-data))
       (when (seq (:timezone user-data))
         (if (seq twt)
           (str " (" (:timezone user-data) ")")
           (str (:timezone user-data))))))))

(defun active?
  ([user :guard map?] (active? (:status user)))

  ([_user-status :guard not] false)

  ([user-status :guard string?] (#{"active" "unverified"} user-status)))

(defn filter-active-users [users-list]
  (filter active? users-list))