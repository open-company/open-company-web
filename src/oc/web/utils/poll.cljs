(ns oc.web.utils.poll
  (:require [oops.core :refer (oget oset!)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.sentry :as sentry]))

(defonce ^:export poll-selector-prefix "oc-poll-portal-")

(defonce min-poll-replies 2)
(defonce max-question-length 128)
(defonce max-reply-length 64)

(defn created-at []
  (utils/as-of-now))

(defn new-poll-id []
  (utils/activity-uuid))

(defn new-reply-id []
  (utils/activity-uuid))

(defn- author-for-user [user-data]
  {:name (user-lib/name-for user-data)
   :avatar-url (:avatar-url user-data)
   :user-id (:user-id user-data)})

(defn poll-reply [user-data body]
  {:body (or body "")
   :author (author-for-user user-data)
   :votes-count 0
   :reply-id (new-reply-id)
   :votes []})

(defn poll-data [user-data poll-id]
  {:question ""
   :poll-uuid poll-id
   :can-add-reply (boolean ls/poll-can-add-reply)
   :created-at (created-at)
   :updated-at (created-at)
   :author (author-for-user user-data)
   :total-votes-count 0
   :replies (mapv #(poll-reply user-data "") (range min-poll-replies))})

(defn clean-polls
  "Clean not needed keys from poll maps."
  [activity-data]
  (if (contains? activity-data :polls)
    (assoc activity-data :polls
     (mapv (fn [poll]
      (-> poll
       (dissoc :links :preview)
       (update :replies
        (fn [replies]
         (mapv #(dissoc % :links) replies)))))
      (:polls activity-data)))
    activity-data))

;; Dom manipulation

(defn get-poll-portal-element [poll-uuid]
  (sel1 (str "." poll-selector-prefix poll-uuid)))

(defn set-poll-element-question! [poll-uuid question-string]
  (when-let [portal-el (get-poll-portal-element poll-uuid)]
    (oset! portal-el "dataset.!question" question-string)))

;; Poll helpers

(defn report-unmounted-poll
  [{:keys [activity-data poll-data container-selector] :as _props}]
  (sentry/capture-message-with-extra-context! {:poll-uuid (:poll-uuid poll-data)
                                               :activity-uuid (:uuid activity-data)
                                               :revision-id (:revision-id activity-data)
                                               :poll-updated-at (:updated-at poll-data)
                                               :org-slug (router/current-org-slug)
                                               :container-selector container-selector
                                               :win-url (.. js/window -location -href)}
   "Failed creating portal for poll"))