(ns oc.web.stores.qsg
  (:require [clojure.string :as s]
            [cljs-flux.dispatcher :as flux]
            [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

;; Reducer used to watch for user dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (when-not (some #{action-type} [:update :input])
                      (timbre/debug "Dispatching QSG reducer:" action-type))
                    action-type))

(def qsg-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

;; Utils

(def admin-keys [:verify-email-done :profile-photo-done :company-logo-done
                 :invited? :add-post? :add-reminder? :add-section?])

(defn- progress-percentage [qsg-data]
  (let [all-keys admin-keys
                  ;; :see-digest-sample?]
        done-values (vals (select-keys qsg-data all-keys))
        truty-values (filterv boolean done-values)]
    (* (/ (count truty-values) (count all-keys)) 100)))

(defmethod dispatcher/action :show-qsg-view
  [db [_ persist?]]
  (-> db
    (assoc-in [:qsg :visible] true)
    (assoc-in [:qsg :show-guide?] persist?)
    (assoc-in [:qsg :guide-dismissed?] (if persist? false (:guide-dismissed? (:qsg db))))
    (assoc-in [:qsg :should-show-qsg?] false)
    (assoc-in [:qsg :overall-progress] (progress-percentage (:qsg db)))))

(defmethod dispatcher/action :dismiss-qsg-view
  [db [_]]
  (-> db
    (assoc-in [:qsg :visible] false)
    (assoc-in [:qsg :guide-dismissed?] true)))

(defmethod dispatcher/action :qsg-reset
  [db [_]]
  (update-in db [:qsg] dissoc :step))

;; Profile photo

(defn- profile-photo-next-step [cur-step]
  (case cur-step
    nil?
    :profile-photo-1

    :profile-photo-1
    :profile-photo-2

    :profile-photo-2
    :profile-photo-3

    :profile-photo-3
    nil
    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-profile-photo
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (profile-photo-next-step cur-step))
        next-db (if (= force-step :profile-photo-done)
                  (assoc-in db [:qsg :profile-photo-done] true)
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Company logo

(defn- company-logo-next-step [cur-step]
  (case cur-step
    nil?
    :company-logo-1

    :company-logo-1
    :company-logo-2

    :company-logo-2
    :company-logo-3

    :company-logo-3
    nil
    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-company-logo
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (company-logo-next-step cur-step))
        next-db (if (= force-step :company-logo-done)
                  (assoc-in db [:qsg :company-logo-done] true)
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Invite team

(defn- invite-team-next-step [cur-step]
  (case cur-step
    nil?
    :invite-team-1

    :invite-team-1
    :invite-team-2

    :invite-team-2
    :invite-team-3

    :invite-team-3
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-invite-team
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (invite-team-next-step cur-step))
        next-db (if (= force-step :invited?)
                  (assoc-in db [:qsg :invited?] true)
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Create post

(defn- create-post-next-step [cur-step]
  (case cur-step
    nil?
    :create-post-1

    :create-post-1
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-create-post
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (create-post-next-step cur-step))
        next-db (if (= force-step :add-post?)
                  (assoc-in db [:qsg :add-post?] true)
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Create reminder

(defn- create-reminder-next-step [cur-step]
  (case cur-step
    nil?
    :create-reminder-1

    :create-reminder-1
    :create-reminder-2

    :create-reminder-2
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-create-reminder
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (create-reminder-next-step cur-step))
        next-db (if (= force-step :add-reminder?)
                  (assoc-in db [:qsg :add-reminder?] true)
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Add section

(defn- add-section-next-step [cur-step]
  (case cur-step
    nil?
    :add-section-1

    :add-section-1
    nil
    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-add-section
  [db [_ force-step]]
  (let [qsg-data (:qsg db)
        cur-step (:step qsg-data)
        next-step (or force-step
                      (add-section-next-step cur-step))
        next-db (if (= force-step :add-section?)
                  (-> db
                    (assoc-in [:qsg :add-section?] true)
                    (assoc-in [:qsg :show-section-settings-tooltip] (not (:add-section? qsg-data))))
                  db)
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Digest sample

(defmethod dispatcher/action :qsg-digest-sample-done
  [db [_]]
  (let [qsg-data (:qsg db)
        next-qsg (assoc qsg-data :see-digest-sample? true)
        with-percentage (assoc next-qsg :overall-progress (progress-percentage next-qsg))]
    (assoc-in db [:qsg] with-percentage)))

;; Reducer

;; QSG store specific reducers
(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

(defn- qsg-data-from-user
  "Read the qsg data from user map, do not override the local visibility value of QSG."
  [current-qsg user-data]
  (let [qsg-checklist (:qsg-checklist user-data)
        qsg-visible (if (contains? current-qsg :visible)
                      (:visible current-qsg)
                      (and (:show-guide? qsg-checklist)
                           (not (:guide-dismissed? qsg-checklist))))]
    (assoc qsg-checklist :visible qsg-visible)))

(defmethod reducer :user-data
  [db [_ user-data]]
  (let [old-qsg-data (:qsg db)
        qsg-data (qsg-data-from-user old-qsg-data user-data)
        updated-qsg-data (merge qsg-data old-qsg-data
                          {:verify-email-done (= (:status user-data) "active")
                           :profile-photo-done (and (not (s/blank? (:avatar-url user-data)))
                                                    (s/starts-with? (:avatar-url user-data) "http"))})

        overall-progress (progress-percentage updated-qsg-data)
        has-slack-user? (pos? (count (:slack-users user-data)))
        slack-dismissed? (boolean (or (:slack-dismissed? updated-qsg-data)
                                      (and has-slack-user?
                                           (jwt/team-has-bot? (:team-id (dispatcher/org-data))))))
        resend-verification-email-link (utils/link-for (:links user-data) "resend-verification" "POST")]
    (-> db
      (update-in [:qsg] merge updated-qsg-data)
      (assoc-in [:qsg :slack-dismissed?] slack-dismissed?)
      (assoc-in [:qsg :can-resend-verification?] resend-verification-email-link)
      (assoc-in [:qsg :has-slack-user?] has-slack-user?)
      (assoc-in [:qsg :overall-progress] overall-progress))))

(defmethod reducer :org-loaded
  [db [_ org-data]]
  (let [company-logo-done? (not (s/blank? (:logo-url org-data)))
        delete-samples-link (utils/link-for (:links org-data) "delete-samples" "DELETE")
        next-db (update-in db [:qsg] merge {:company-logo-done company-logo-done?})]
    (assoc-in next-db [:qsg :overall-progress] (progress-percentage (:qsg next-db)))))
