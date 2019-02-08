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