(ns oc.web.actions.qsg
  (:require [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.org :as org-actions]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.activity :as activity-actions]))

;; Server dialog

(def qsg-checklist-allowed-props [:show-guide?
                                  :invited?
                                  :add-post?
                                  :add-reminder?
                                  :add-section?
                                  :section-dialog-seen?
                                  :slack-dismissed?
                                  :start-fresh-dismissed?
                                  :guide-dismissed?])

(defn update-qsg-checklist []
  (let [qsg-data (:qsg @dis/app-state)
        user-data (dis/current-user-data)
        user-profile-link (utils/link-for (:links user-data) "partial-update" "PATCH")
        cleaned-qsg-checklist (select-keys qsg-data qsg-checklist-allowed-props)]
    (when (and user-profile-link
               (not (empty? cleaned-qsg-checklist)))
      (api/patch-user user-profile-link {:qsg-checklist cleaned-qsg-checklist}
        (fn [status body success]
         (if (= status 422)
           (dis/dispatch! [:user-profile-update/failed])
           (when success
             (dis/dispatch! [:user-data (json->cljs body)]))))))))

(defn delete-samples []
  (let [org-data (dis/org-data)
        delete-samples-link (utils/link-for (:links org-data) "delete-samples")]
    (when delete-samples-link
      (activity-actions/delete-samples))
    (dis/dispatch! [:input [:qsg :sample-content?] false])))

;; QSG tooltip

(defn show-qsg-tooltip []
  (dis/dispatch! [:input [:qsg :show-qsg-tooltip?] true]))

(defn dismiss-qsg-tooltip []
  (dis/dispatch! [:input [:qsg :show-qsg-tooltip?] false]))

;; QSG view actions

(defn reset-qsg []
  (dis/dispatch! [:qsg-reset]))

(defn turn-on-show-guide []
  (dis/dispatch! [:show-qsg-view true])
  (update-qsg-checklist))

(defn show-qsg-view []
  (dis/dispatch! [:show-qsg-view]))

(defn dismiss-qsg-view []
  (dis/dispatch! [:dismiss-qsg-view])
  (update-qsg-checklist)
  (show-qsg-tooltip))

(defn dismiss-slack []
  (dis/dispatch! [:input [:qsg :slack-dismissed?] true])
  (update-qsg-checklist))

(defn dismiss-start-fresh []
  (dis/dispatch! [:input [:qsg :start-fresh-dismissed?] true])
  (update-qsg-checklist))

(defn slack-click []
  (let [org-data (dis/org-data)
        team-data (dis/team-data (:team-id org-data))
        cur-user-data (dis/current-user-data)]
    (if (and (:has-slack-org team-data)
             (not (jwt/team-has-bot? (:team-id org-data))))
      (org-actions/bot-auth team-data cur-user-data (router/get-token))
      (team-actions/slack-team-add cur-user-data (router/get-token)))))

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
  (dis/dispatch! [:qsg-invite-team :invited?])
  (update-qsg-checklist))

;; Create post

(defn start-create-post-trail []
  (dis/dispatch! [:qsg-create-post :create-post-1]))

(defn finish-create-post-trail []
  (dis/dispatch! [:qsg-create-post :add-post?])
  (update-qsg-checklist))

;; Create reminder

(defn start-create-reminder-trail []
  (dis/dispatch! [:qsg-create-reminder :create-reminder-1]))

(defn finish-create-reminder-trail []
  (dis/dispatch! [:qsg-create-reminder :add-reminder?])
  (update-qsg-checklist))

;; Add section

(defn start-add-section-trail []
  (dis/dispatch! [:qsg-add-section :add-section-1]))

(defn finish-add-section-trail []
  (dis/dispatch! [:qsg-add-section :add-section?])
  (update-qsg-checklist))

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
  (dis/dispatch! [:qsg-configure-section :section-dialog-seen?])
  (update-qsg-checklist))