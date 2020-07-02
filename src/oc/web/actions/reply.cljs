(ns oc.web.actions.reply
  (:require [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.activity :as au]))

;; Reply actions

(defn reply-expand [entry-data reply-data]
  (dis/dispatch! [:reply-expand (router/current-org-slug) entry-data reply-data]))

(defn reply-mark-seen [entry-data reply-data]
  (dis/dispatch! [:replies-mark-seen (router/current-org-slug) entry-data reply-data]))

(defn replies-mark-seen [entry-data]
  (dis/dispatch! [:replies-entry-mark-seen (router/current-org-slug) entry-data]))

(defn reply-unwrap-body [entry-data reply-data]
  (dis/dispatch! [:reply-unwrap-body (router/current-org-slug) entry-data reply-data]))

(defn replies-expand [entry-data]
  (dis/dispatch! [:replies-expand (router/current-org-slug) entry-data]))

(defn replies-add [entry-data new-comment-data]
  (let [org-data (dis/org-data)
        replies-data (dis/container-data @dis/app-state (:slug org-data) :replies dis/recent-activity-sort)
        parsed-comment-data (au/parse-comment org-data entry-data new-comment-data (:last-seen-at replies-data))
        parsed-reply-data (merge parsed-comment-data {:unwrapped-body true :expanded true :unseen false})]
    (dis/dispatch! [:replies-add (:slug org-data) entry-data parsed-reply-data])))