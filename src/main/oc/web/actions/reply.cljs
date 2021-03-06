(ns oc.web.actions.reply
  (:require [oc.web.dispatcher :as dis]
            [oc.web.utils.activity :as au]))

;; Reply actions

(defn reply-expand [entry-data reply-data]
  (dis/dispatch! [:reply-expand (dis/current-org-slug) entry-data reply-data]))

(defn reply-mark-seen [entry-data reply-data]
  (dis/dispatch! [:replies-mark-seen (dis/current-org-slug) entry-data reply-data]))

(defn replies-mark-seen [entry-data]
  (dis/dispatch! [:replies-entry-mark-seen (dis/current-org-slug) entry-data]))

(defn reply-unwrap-body [entry-data reply-data]
  (dis/dispatch! [:reply-unwrap-body (dis/current-org-slug) entry-data reply-data]))

(defn replies-expand [entry-data]
  (dis/dispatch! [:replies-expand (dis/current-org-slug) entry-data]))

(defn replies-add [entry-data new-comment-data]
  (let [org-data (dis/org-data)
        replies-data (dis/container-data @dis/app-state (:slug org-data) :replies dis/recent-activity-sort)
        parsed-comment-data (au/parse-comment org-data entry-data new-comment-data (:last-seen-at replies-data))
        parsed-reply-data (merge parsed-comment-data {:unwrapped-body true :collapsed false :unseen false})]
    (dis/dispatch! [:replies-add (:slug org-data) entry-data parsed-reply-data])))