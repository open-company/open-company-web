(ns oc.web.actions.nux
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.org :as org-utils]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(defn get-nux-cookie
  "Read the cookie from the document only if the nux-cookie-value atom is nil.
  In all the other cases return the read value in the atom."
  []
  (let [cookie-name (router/nux-cookie (jwt/user-id))
        cookie-value (cook/get-cookie cookie-name)]
    (when cookie-value
      (json->cljs cookie-value))))

(defn set-nux-cookie
  "Create a map for the new user cookie and save it. Also update the value of
  the nux-cookie-value atom."
  [user-type value-map]
  (let [old-nux-cookie (get-nux-cookie)
        value-map (merge {:user-type user-type} old-nux-cookie value-map)
        json-map (cljs->json value-map)
        json-string (.stringify js/JSON json-map)]
    (cook/set-cookie!
     (router/nux-cookie (jwt/user-id))
      json-string
      (* 60 60 24 7))))

(defn new-user-registered [user-type]
  (cook/set-cookie! (router/first-ever-landing-cookie (jwt/user-id))
   true (* 60 60 24 7))
  (set-nux-cookie user-type
    {:show-add-post-tooltip true
     :show-post-added-tooltip false
     :show-edit-tooltip true}))

(defn nux-end
  "NUX completed for the current user, remove the cookie and update the nux-cookie-value."
  []
  (cook/remove-cookie! (router/nux-cookie (jwt/user-id))))

;; Value used to save an already shown tooltip, use a string since keyword
;; will be lost during json read/write
(def default-tooltip-done "done")

(defn- parse-nux-cookie-value [v]
  (if (= v default-tooltip-done)
    false
    v))

(defn mark-nux-step-done [nux-step-key]
  (when-let [nux-cookie (get-nux-cookie)]
    (set-nux-cookie (:user-type nux-cookie)
     {nux-step-key default-tooltip-done})))

(defn check-nux
  []
  (when-let* [nv (get-nux-cookie)
              org-data (dis/org-data)]
    (let [team-data (dis/team-data)
          can-edit? (utils/is-admin-or-author? org-data)
          is-admin? (jwt/is-admin? (:team-id org-data))
          add-post-tooltip (:show-add-post-tooltip nv)
          post-added-tooltip (:show-post-added-tooltip nv)
          fixed-post-added-tooltip (parse-nux-cookie-value post-added-tooltip)
          edit-tooltip (:show-edit-tooltip nv)
          user-type (:user-type nv)
          team-has-bot? (jwt/team-has-bot? (:team-id org-data))
          ;; Show add post tooltip if
          fixed-add-post-tooltip (and ;; it has not been done already
                                      (not= add-post-tooltip default-tooltip-done)
                                      ;; we are not showing the next tooltip (post added)
                                      (not (true? post-added-tooltip)))
          ;; Show the tooltip inside editing
          fixed-edit-tooltip (and ;; has not been done already
                                  (not= edit-tooltip default-tooltip-done)
                                  ;; user is not a viewer
                                  can-edit?)]
      ;; If we don't need to show the first tooltip but it's
      ;; not marked as done let's mark it to remember
      (when (and (not fixed-add-post-tooltip)
                 (true? post-added-tooltip))
        (mark-nux-step-done :show-add-post-tooltip))
      (when (and (not fixed-post-added-tooltip)
                 (not can-edit?))
        (mark-nux-step-done :show-post-added-tooltip))
      (when (and (not fixed-edit-tooltip)
                 (not can-edit?))
        (mark-nux-step-done :show-edit-tooltip))
      (when (and (not fixed-edit-tooltip)
                 (not can-edit?))
        (mark-nux-step-done :show-edit-tooltip))
      (dis/dispatch! [:input [:nux]
       {:show-add-post-tooltip (if fixed-add-post-tooltip
                                 (if (:author? org-data)
                                  true
                                  :is-second-user)
                                 false)
        :show-post-added-tooltip fixed-post-added-tooltip
        :show-edit-tooltip fixed-edit-tooltip
        :user-type user-type}])

      ;; Check if we need to remove the nux cookie
      (when (and (= (:show-add-post-tooltip nv) default-tooltip-done)
                 (= (:show-post-added-tooltip nv) default-tooltip-done)
                 (= (:show-edit-tooltip nv) default-tooltip-done))
        (nux-end)))))

(defn dismiss-add-post-tooltip []
  (mark-nux-step-done :show-add-post-tooltip)
  (check-nux))

(defn show-post-added-tooltip [post-uuid]
  (when-let [nux-cookie (get-nux-cookie)]
    (set-nux-cookie (:user-type nux-cookie)
     {:show-add-post-tooltip default-tooltip-done
      :show-post-added-tooltip (or (:show-post-added-tooltip nux-cookie) post-uuid)}))
  (check-nux))

(defn dismiss-post-added-tooltip []
  (mark-nux-step-done :show-post-added-tooltip)
  (check-nux))

(defn dismiss-edit-tooltip []
  (mark-nux-step-done :show-edit-tooltip)
  (check-nux))