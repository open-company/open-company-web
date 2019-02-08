(ns oc.web.actions.qsg
  (:require [oc.web.dispatcher :as dis]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))


;; Main fns

(defn reset-qsg []
  (dis/dispatch! [:qsg-reset]))

(defn show-qsg-view []
  (dis/dispatch! [:show-qsg-view]))

(defn dismiss-qsg-view []
  (dis/dispatch! [:dismiss-qsg-view]))

;; Profile photo

(defn start-profile-photo-trail []
  (js/console.log "DBG start-profile-photo-trail")
  (dis/dispatch! [:qsg-profile-photo :profile-photo-1]))

(defn next-profile-photo-trail []
  (js/console.log "DBG next-profile-photo-trail")
  (dis/dispatch! [:qsg-profile-photo]))

(defn finish-profile-photo-trail []
  (js/console.log "DBG finish-profile-photo-trail")
  (dis/dispatch! [:qsg-profile-photo :profile-photo-done]))

;; Company logo

(defn start-company-logo-trail []
  (js/console.log "DBG start-company-logo-trail done?" (:company-logo-done @dis/app-state))
  (dis/dispatch! [:qsg-company-logo :company-logo-1]))

(defn next-company-logo-trail []
  (js/console.log "DBG next-company-logo-trail")
  (dis/dispatch! [:qsg-company-logo]))

(defn finish-company-logo-trail []
  (js/console.log "DBG finish-company-logo-trail")
  (dis/dispatch! [:qsg-company-logo :company-logo-done]))

;; Invite team

(defn start-invite-team-trail []
  (dis/dispatch! [:qsg-invite-team :invite-team-1]))

(defn finish-invite-team-trail []
  (dis/dispatch! [:qsg-invite-team :invite-team-done]))

;; Create post

(defn start-create-post-trail []
  (dis/dispatch! [:qsg-create-post :create-post-1]))

(defn finish-create-post-trail []
  (dis/dispatch! [:qsg-create-post :create-post-done]))

;; Create reminder

(defn start-create-reminder-trail []
  (dis/dispatch! [:qsg-create-post :create-reminder-1]))

(defn finish-create-reminder-trail []
  (dis/dispatch! [:qsg-create-reminder :create-reminder-done]))

;; Add section

(defn start-add-section-trail []
  (dis/dispatch! [:qsg-add-section :add-section-1]))

(defn finish-add-section-trail []
  (dis/dispatch! [:qsg-add-section :add-section-done]))

;; Configure section

(defn start-configure-section-trail []
  (let [current-board (keyword (router/current-board-slug))
        step (if (or (= current-board :all-posts)
                     (= current-board :must-see)
                     (= current-board (keyword utils/default-drafts-board-slug)))
               :configure-section-1
               :configure-section-2)]
    (dis/dispatch! [:qsg-configure-section step])))

(defn next-configure-section-trail []
  (dis/dispatch! [:qsg-configure-section]))

(defn finish-configure-section-trail []
  (dis/dispatch! [:qsg-configure-section :configure-section-done]))