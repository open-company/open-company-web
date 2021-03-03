(ns oc.web.utils.poll
  (:require [defun.core :refer (defun)]
            [oops.core :refer (oget oset!)]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.utils.sentry :as sentry]
            [oc.web.utils.user :as user-utils]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

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

(defn poll-reply [user-data body & [ts]]
  {:created-at (or ts (created-at))
   :body (or body "")
   :author (user-utils/author-for-user user-data)
   :reply-id (new-reply-id)
   :votes []})

(defn- poll-default-replies [user-data]
  (let [ts (.getTime (utils/js-date))
        reps (map
              #(poll-reply user-data "" (.toISOString (js/Date. (+ ts %))))
              (range min-poll-replies))]
    (zipmap (map (comp keyword :reply-id) reps) reps)))

(defn poll-data [user-data poll-id]
  {:question ""
   :poll-uuid poll-id
   :can-add-reply false ;; To enable just use: (boolean ls/poll-can-add-reply)
   :created-at (created-at)
   :updated-at (created-at)
   :author (user-utils/author-for-user user-data)
   :replies (poll-default-replies user-data)})

(defn clean-poll-reply [poll-reply-data]
  (dissoc poll-reply-data :links))

(defn clean-poll [poll-data]
  (-> poll-data
   (dissoc :links :preview)
   (update :replies (fn [replies]
                      (zipmap
                       (mapv (comp keyword :reply-id) (vals replies))
                       (mapv clean-poll-reply (vals replies)))))))

(defn clean-polls
  "Clean not needed keys from poll maps."
  [activity-data]
  (if (seq (:polls activity-data))
    (update activity-data :polls (fn [polls]
                                   (zipmap
                                    (mapv (comp keyword :poll-uuid) (vals polls))
                                    (mapv clean-poll (vals polls)))))
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
                                               :org-slug (dis/current-org-slug)
                                               :container-selector container-selector
                                               :win-url (.. js/window -location -href)}
   "Failed creating portal for poll"))

;; Replies

(defun sorted-replies
  ([poll-data :guard #(and (map? %) (contains? % :replies))]
   (sorted-replies (-> poll-data :replies vals)))

  ([replies-map :guard map?]
   (sorted-replies (vals replies-map)))

  ([replies-coll :guard coll?]
   (sort-by #(.getTime (utils/js-date (:created-at %))) replies-coll)))

(defn poll-key-parts [poll-key]
  (let [[org-slug _posts activity-uuid _polls poll-uuid] (vec poll-key)]
    {:org-slug org-slug
     :activity-uuid activity-uuid
     :poll-uuid poll-uuid}))