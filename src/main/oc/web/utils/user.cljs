(ns oc.web.utils.user
  (:require [defun.core :refer (defun)]
            [cuerdas.core :as string]
            [oc.lib.oauth :as oauth]
            [oc.lib.user :as user-lib]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.lib.utils :as utils]))

(def default-avatar "/img/ML/happy_face_red.svg")
(def other-default-avatars
 ["/img/ML/happy_face_green.svg"
  "/img/ML/happy_face_blue.svg"
  "/img/ML/happy_face_purple.svg"
  "/img/ML/happy_face_yellow.svg"])

(defn default-avatar? [image-url]
  (let [images-set (conj (set other-default-avatars) default-avatar)]
    (images-set image-url)))

(defn random-avatar []
  (first (shuffle (vec (conj other-default-avatars default-avatar)))))

(def publisher-board-slug-prefix "publisher-board-")

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(def user-name-max-lenth 64)

;; Associated Slack user check

(defn user-has-slack-with-bot?
  "Check if the current user has an associated Slack user under a team that has the bot."
  [current-user-data bots-data team-roster]
  (let [slack-orgs-with-bot (map :slack-org-id bots-data)
        slack-users (:slack-users (first (filter #(= (:user-id %) (:user-id current-user-data)) (:users team-roster))))]
    (some #(contains? slack-users (keyword %)) slack-orgs-with-bot)))

(defn user-has-push-token?
  [current-user-data push-token]
  (let [current-push-tokens (set (:expo-push-tokens current-user-data))]
    (current-push-tokens push-token)))

(defn auth-link-with-state [original-url {:keys [user-id team-id redirect redirect-origin] :as state}]
  (let [parsed-url       (js/URL. original-url)
        old-state-string (.. parsed-url -searchParams (get "state"))
        decoded-state    (oauth/decode-state-string old-state-string)
        combined-state   (merge decoded-state state)
        new-state-string (oauth/encode-state-string combined-state)]
    (.. parsed-url -searchParams (set "state" new-state-string))
    (str parsed-url)))

(defn- localized-time
  ([tz] (localized-time tz {}))
  ([tz {:keys [suffix] :as opts}]
  (try
   (str
    (.toLocaleTimeString (js/Date.)
     (.. js/window -navigator -language)
     #js {:hour "2-digit"
          :minute "2-digit"
          :format "hour:minute"
          :timeZone tz})
     (when (seq suffix)
       (str " " (string/trim suffix))))
   (catch :default e
    nil))))

(defn readable-tz [tz]
  (string/collapse-whitespace (string/replace tz #"(_|-)" " ")))

(defn time-with-timezone
  ([timezone] (time-with-timezone timezone {}))
  ([timezone opts]
  (when-let [lt (localized-time timezone opts)]
    (utils/time-without-leading-zeros lt))))

(defn timezone-location-string [user-data & [local-time-string?]]
  (let [twt (time-with-timezone (:timezone user-data) {:suffix (when local-time-string? "local time")})
        tz (readable-tz (:timezone user-data))]
    (str twt
         (if (seq (:location user-data))
           (if (seq twt)
             (str " (" (:location user-data) ")")
             (:location user-data))
           (when (seq tz)
             (if (seq twt)
               (str " (" tz ")")
               tz))))))

(defn location-timezone-string [user-data & [local-time-string?]]
  (let [twt (time-with-timezone (:timezone user-data) {:suffix (when local-time-string? "local time")})
        tz (readable-tz (:timezone user-data))]
    (str
     (if (seq (:location user-data))
       (if (seq twt)
         (str (:location user-data) " (" twt ")")
         (:location user-data))
       (when (seq tz)
         (if (seq twt)
           (str twt " (" tz ")")
           tz))))))

(defun active?
  ([user :guard map?] (active? (:status user)))

  ([_user-status :guard not] false)

  ([user-status :guard string?] (#{"active" "unverified"} user-status)))

(defn filter-active-users [users-list]
  (filter active? users-list))

; (defn user-role [org-data user-data]
;   (let [is-admin? (j/is-admin? (:team-id org-data))
;         is-author? (utils/link-for (:links org-data) "create")]
;     (cond
;       is-admin? :admin
;       is-author? :author
;       :else :viewer)))

(defn user-role-string [role-kw]
  (case role-kw
   :admin
   "admin"
   :author
   "contributor"
   "viewer"))

(defun get-author
  "Get the author data from the org list of authors"
  ([user-id _x :guard empty?]
   nil)
  ([user-id authors :guard #(every? map? %)]
   (recur user-id (map :user-id authors)))
  ([user-id author-uuids :guard #(every? string? %)]
   (when (and (seq author-uuids)
              (seq user-id))
     ((set author-uuids) user-id))))

(defun get-user-type
  "Calculate the user type, return admin if it's an admin,
  check if it's in the authors list if not admin
  return viewer else."
  ([user-data org-data]
  (cond
    ;; if :admin is present and it's true
    (true? (:admin? user-data))
    :admin
    ;; if :admin is present and it's a list of teams
    ;; and it contains the actual org team
    ((set (:admin user-data)) (:team-id org-data))
    :admin
    ;; if the user is in the list of authors of the org data
    ;; or if a board is passed and the authors list contains the user-id
    (get-author (:user-id user-data) (:authors org-data))
    :author
    ;; viewer in all other cases
    :else
    :viewer))
  ([user-data org-data board-data :guard map?]
   (if (= (:access board-data) "private")
     (or (get-author (:user-id user-data) (:authors board-data))
         :viewer)
     (get-user-type user-data org-data))))

;; Follow/unfollow related actions
(defn load-follow-list []
  (ws-cc/follow-list))

(defn load-followers-count []
  (ws-cc/followers-count))

(defn load-follow-data []
  (load-follow-list)
  (load-followers-count))

(defn author-for-user [user-data]
  {:name (user-lib/name-for user-data)
   :avatar-url (:avatar-url user-data)
   :user-id (:user-id user-data)})