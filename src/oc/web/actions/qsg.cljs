(ns oc.web.actions.qsg
  (:require [oc.web.dispatcher :as dis]))


;; Main fns
(defn reset-qsg []
  (dis/dispatch! [:qsg-profile-photo :reset]))

;; Profile photo

(defn start-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo :profile-photo-1]))

(defn next-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo]))

(defn finish-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo :profile-photo-done]))

;; Company logo

(defn start-company-logo-trail []
  (dis/dispatch! [:qsg-company-logo :company-logo-1]))

(defn next-company-logo-trail []
  (dis/dispatch! [:qsg-company-logo]))

(defn finish-company-logo-trail []
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