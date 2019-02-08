(ns oc.web.actions.qsg
  (:require [oc.web.dispatcher :as dis]))

(defn start-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo :profile-photo-1]))

(defn next-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo]))

(defn finish-profile-photo-trail []
  (dis/dispatch! [:qsg-profile-photo :profile-photo-done]))

(defn reset-qsg []
  (dis/dispatch! [:qsg-profile-photo :reset]))