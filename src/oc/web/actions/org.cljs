(ns oc.web.actions.org
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.actions.comment :as ca]
            [oc.web.actions.reaction :as ra]
            [oc.web.actions.activity :as aa]
            [oc.web.actions.activity :as sa]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.ws-change-client :as ws-cc]
            [oc.web.lib.ws-interaction-client :as ws-ic]))

;; Org get

(defn org-loaded [org-data ap-initial-at saved?]
  ;; Save the last visited org
  (when (and org-data
             (= (router/current-org-slug) (:slug org-data)))
    (cook/set-cookie! (router/last-org-cookie) (:slug org-data) (* 60 60 24 6)))
  ;; Check the loaded org
  (let [ap-initial-at (:ap-initial-at @dis/app-state)
        boards (:boards org-data)]
    (cond
      ;; If it's all posts page, loads all posts for the current org
      (or (= (router/current-board-slug) "all-posts")
          ap-initial-at)
      (if (utils/link-for (:links org-data) "activity")
        ;; Load all posts only if not coming from a digest url
        ;; in that case do not load since we already have the results we need
        (aa/all-posts-get org-data ap-initial-at)
        (router/redirect-404!))
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        ; Load the board data since there is a link to the board in the org data
        (when-let [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
          (sa/section-get board-link))
        ; The board wasn't found, showing a 404 page
        (if (= (router/current-board-slug) utils/default-drafts-board-slug)
          (utils/after 100 #(dis/dispatch! [:board utils/default-drafts-board]))
          (router/nav! (oc-urls/org (router/current-org-slug)))))
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "org-settings-invite"))
           (not (utils/in? (:route @router/path) "org-settings-team"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "email-verification"))
           (not (utils/in? (:route @router/path) "sign-up"))
           (not (utils/in? (:route @router/path) "email-wall"))
           (not (utils/in? (:route @router/path) "confirm-invitation"))
           (not (utils/in? (:route @router/path) "secure-activity")))

      (when (>= (count boards) 1)
        ;; Redirect to the first board if at least one is present
        (let [board-to (utils/get-default-board org-data)]
          (utils/after 10
            #(router/nav!
               (if board-to
                 (oc-urls/board (:slug org-data) (:slug board-to))
                 (oc-urls/all-posts (:slug org-data)))))))))
  ;; Change service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "changes")]
      (ws-cc/reconnect ws-link (jwt/get-key :user-id) (:slug org-data) (conj (map :uuid (:boards org-data)) (:uuid org-data)))
      (ws-cc/subscribe :container/change #(dis/dispatch! [:container/change (:data %)]))
      (ws-cc/subscribe :container/status #(dis/dispatch! [:container/status (:data %)]))))

  ;; Interaction service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "interactions")]
      (ws-ic/reconnect ws-link (jwt/get-key :user-id))
      (ra/subscribe ws-ic/subscribe)
      (ca/subscribe ws-ic/subscribe)))
  (dis/dispatch! [:org-loaded org-data saved?]))

(defn get-org-cb [{:keys [status body success]}]
  (let [org-data (json->cljs body)]
    (org-loaded org-data nil false)))

(defn get-org [& [org-data]]
  (let [fixed-org-data (or org-data (dis/org-data))]
    (api/get-org fixed-org-data get-org-cb)))

;; Org redirect

(defn org-redirect [org-data]
  ;; Show NUX for first ever user when the dashboard is loaded
  (cook/set-cookie!
   (router/show-nux-cookie (jwt/user-id))
   (:first-ever-user router/nux-cookie-values)
   (* 60 60 24 7))
  (when org-data
    (let [org-slug (:slug org-data)]
      (utils/after 100 #(router/redirect! (oc-urls/all-posts org-slug))))))

;; Org create

(defn team-patch-cb [org-data {:keys [success body status]}]
  (when success
    (org-redirect org-data)))

(defn org-create-cb [{:keys [success status body]}]
  (when-let [org-data (when success (json->cljs body))]
    (org-loaded org-data nil false)
    (let [team-data (dis/team-data (:team-id org-data))]
      (if (and (empty? (:name team-data))
               (utils/link-for (:links team-data) "partial-update"))
        ; if the current team has no name and
        ; the user has write permission on it
        ; use the org name
        ; for it and patch it back
        (api/patch-team (:team-id org-data) {:name (:name org-data)} org-data (partial team-patch-cb org-data))
        ; if not redirect the user to the slug)
        (org-redirect org-data)))))

(defn org-create [org-data]
  (when-not (empty? (:name org-data))
    (api/create-org (:name org-data) (:logo-url org-data) (:logo-width org-data) (:logo-height org-data) org-create-cb)))

;; Org edit

(defn org-edit-setup [org-data]
  (dis/dispatch! [:org-edit-setup org-data]))

(defn org-edit-save-cb [{:keys [success body status]}]
  (org-loaded (json->cljs body) nil true))

(defn org-edit-save [org-data]
  (api/patch-org org-data org-edit-save-cb))