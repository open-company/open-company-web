(ns oc.web.stores.qsg
  (:require [oc.web.dispatcher :as dispatcher]
            [clojure.string :as s]))

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

(defmethod dispatcher/action :dismiss-qsg-view
  [db [_]]
  (assoc-in db [:qsg :visible] false))

(defmethod dispatcher/action :qsg-reset
  [db [_]]
  (update-in db [:qsg] dissoc :step))

;; Verify email

(defmethod dispatcher/action :qsg-user-data
  [db [_ user-data]]
  (let [done-vals (remove false? {:verify-email-done (= (:status user-data) "active")
                                  :profile-photo-done (and (not (s/blank? (:avatar-url user-data)))
                                                           (s/starts-with? (:avatar-url user-data) "http"))})
        next-db (update-in db [:qsg] merge done-vals)]
    (assoc-in next-db [:qsg :overall-progress] (progress-percentage (:qsg next-db)))))

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
                  (update-in db [:qsg] dissoc :profile-photo-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (js/console.log "DBG stores/:qsg-profile-photo cur" (:qsg db) "next" (:qsg next-qsg))
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Company logo

(defmethod dispatcher/action :qsg-org-data
  [db [_ org-data]]
  (if (not (s/blank? (:logo-url org-data)))
    (assoc-in db [:qsg :company-logo-done] true)
    db))

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
                  (update-in db [:qsg] dissoc :company-logo-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (js/console.log "DBG stores/:qsg-company-logo cur" (:qsg db) "next" (:qsg next-qsg))
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Invite team

(defn- invite-team-next-step [cur-step]
  (case cur-step
    nil?
    :invite-team-1

    :invite-team-1
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

    :create-post-1
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

    :create-reminder-1
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
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (add-section-next-step cur-step))
        next-db (if (= force-step :add-section-done)
                  (assoc-in db [:qsg :add-section-done] true)
                  (update-in db [:qsg] dissoc :add-section-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))

;; Configure section

(defn- configure-section-next-step [cur-step]
  (case cur-step
    nil?
    :configure-section-1

    :configure-section-1
    :configure-section-2

    (:configure-section-2 :reset)
    nil
    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-configure-section
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (configure-section-next-step cur-step))
        next-db (if (= force-step :configure-section-done)
                  (assoc-in db [:qsg :configure-section-done] true)
                  (update-in db [:qsg] dissoc :configure-section-done))
        next-qsg (assoc-in next-db [:qsg :step] next-step)]
    (assoc-in next-qsg [:qsg :overall-progress] (progress-percentage (:qsg next-qsg)))))