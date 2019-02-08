(ns oc.web.stores.qsg
  (:require [oc.web.dispatcher :as dispatcher]))

;; Utils

(defn- progress-percentage [qsg-data]
  (let [done-values (vals (select-keys qsg-data [:verify-email-done :profile-photo-done :company-logo-done
                                                 :invite-team-done :create-post-done :create-reminder-done
                                                 :add-section-done :configure-section-done]))
        truty-values (filterv #(boolean %) done-values)]
    (* (/ (count truty-values) 8) 100)))

(defmethod dispatcher/action :show-qsg-view
  [db [_]]
  (-> db
    (assoc-in [:qsg :visible] true)
    (assoc-in [:qsg :overall-progress] (progress-percentage (:qsg db)))))

;; Verify email

(defmethod dispatcher/action :qsg-user-data
  [db [_ user-data]]
  (if (= (:status user-data) "active")
    (let [next-db (assoc-in db [:qsg :verify-email-done] true)]
      (assoc-in next-db [:qsg :overall-progress] (progress-percentage (:qsg next-db))))
    db))

;; Profile photo

(defn- profile-photo-next-step [cur-step]
  (case cur-step
    nil?
    :profile-photo-1

    :profile-photo-1
    :profile-photo-2

    :profile-photo-2
    :profile-photo-3

    (:profile-photo-3 :reset)
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
                  (update-in db [:qsg] dissoc :profile-photo-done))
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

    (:company-logo-3 :reset)
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
                  (update-in db [:qsg] dissoc :company-logo-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Invite team

(defn- invite-team-next-step [cur-step]
  (case cur-step
    nil?
    :invite-team-1

    (:invite-team-1 :reset)
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-invite-team
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (invite-team-next-step cur-step))
        next-db (if (= force-step :invite-team-done)
                  (assoc-in db [:qsg :invite-team-done] true)
                  (update-in db [:qsg] dissoc :invite-team-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Create post

(defn- create-post-next-step [cur-step]
  (case cur-step
    nil?
    :create-post-1

    (:create-post-1 :reset)
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-create-post
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (create-post-next-step cur-step))
        next-db (if (= force-step :create-post-done)
                  (assoc-in db [:qsg :create-post-done] true)
                  (update-in db [:qsg] dissoc :create-post-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Create reminder

(defn- create-reminder-next-step [cur-step]
  (case cur-step
    nil?
    :create-reminder-1

    (:create-reminder-1 :reset)
    nil

    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-create-reminder
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (create-reminder-next-step cur-step))
        next-db (if (= force-step :create-reminder-done)
                  (assoc-in db [:qsg :create-reminder-done] true)
                  (update-in db [:qsg] dissoc :create-reminder-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))