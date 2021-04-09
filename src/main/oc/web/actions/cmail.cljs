(ns oc.web.actions.cmail
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [clojure.set :as clj-set]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.lib.user-cache :as uc]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.notifications :as notification-actions]
            [cuerdas.core :as string]))

;; Cached items

(defn get-entry-cache-key
  [entry-uuid]
  (str (or
        entry-uuid
        (dis/current-org-slug))
   "-entry-edit"))

(defn remove-cached-item
  [item-uuid]
  (uc/remove-item (get-entry-cache-key item-uuid)))

(defn load-cached-item
  [entry-data edit-key & [completed-cb]]
  (let [cache-key (get-entry-cache-key (:uuid entry-data))]
    (uc/get-item cache-key
     (fn [item err]
       (if (and (not err)
                (map? item)
                (= (:updated-at entry-data) (:updated-at item)))
         (let [entry-to-save (merge item (select-keys entry-data [:links :board-slug :board-name :board-uuid :board-access :publisher-board]))]
           (dis/dispatch! [:input [edit-key] entry-to-save]))
         (do
           ;; If we got an item remove it since it won't be used
           ;; since we have an updated version of it already
           (when item
             (remove-cached-item (:uuid entry-data)))
           (dis/dispatch! [:input [edit-key] entry-data])))
       (when (fn? completed-cb)
         (completed-cb))))))

;; Last used and default board for editing

(defn- ->entry-board [board-data]
  (-> board-data
      (clj-set/rename-keys {:name :board-name
                            :access :board-access
                            :uuid :board-uuid
                            :slug :board-slug})
      (select-keys [:board-name :board-access :board-uuid :board-slug :publisher-board])))

(defn get-default-board
  ([] (get-default-board (vals (dis/editable-boards-data))))
  ([editable-boards]
   (let [editable-boards (if (seq editable-boards)
                           editable-boards
                           (vals (dis/editable-boards-data)))
         last-used-slug (au/last-used-section)
         last-used-board (when last-used-slug
                           (some #(when (and (not (:draft %)) (= (:slug %) last-used-slug)) %) editable-boards))
         board-data (or last-used-board (first editable-boards))]
     (when board-data
       (->entry-board board-data)))))

(defn get-board-for-edit
  ([] (get-board-for-edit nil (vals (dis/editable-boards-data))))
  ([board-slug] (get-board-for-edit board-slug (dis/editable-boards-data)))
  ([board-slug editable-boards-list]
   (let [editable-boards (if (seq editable-boards-list)
                           editable-boards-list
                           (dis/editable-boards-data))
         sorted-editable-boards (sort-by :name editable-boards)
         required-board (when board-slug
                          (some #(when (= (:slug %) board-slug) %) sorted-editable-boards))
         current-board-slug (dis/current-board-slug)
         current-board (when current-board-slug
                         (some #(when (= (:slug %) (dis/current-board-slug)) %) sorted-editable-boards))]
     (->entry-board (or required-board current-board (get-default-board sorted-editable-boards))))))

;; Entry

(defn get-entry-finished [org-slug entry-uuid {:keys [status success body]} & [secure-uuid]]
  ;; Redirect to the real post page in case user has access to it
  (let [activity-data (when success (json->cljs body))
        published? (au/is-published? activity-data)
        not-found-status? (<= 400 status 499)
        error-status? (<= 500 status 599)
        success-status? (<= 200 status 299)
        current-secure-post? (and (seq (dis/current-secure-activity-id))
                                  (= (dis/current-secure-activity-id) secure-uuid))
        current-post? (and (seq (dis/current-activity-id))
                           (= (dis/current-activity-id) entry-uuid))
        no-jwt-auth? (string/empty-or-nil? (jwt/jwt))
        no-auth? (and no-jwt-auth?
                      (string/empty-or-nil? (jwt/id-token)))]
    (when (and current-secure-post?
               published?
               (not no-jwt-auth?)
               (:member? (dis/org-data)))
      (router/redirect! (oc-urls/entry org-slug (:board-slug activity-data) (:uuid activity-data))))
    ;; Show the login wall if trying to access a non-public post
    (when (and not-found-status?
               no-auth?
               current-post?)
      (dis/dispatch! [:show-login-wall]))
    ;; Show a not found screen if the user is logged out and is trying to access a secure url. No login wall!
    (when (and not-found-status?
               no-auth?
               current-secure-post?)
      (router/redirect-404!))
    ;; User trying to open a non published post?
    (when (and (or current-secure-post?
                   current-post?)
               success-status?
               (not published?))
      (router/redirect-404!))
    (when (and current-post?
               error-status?)
      (notification-actions/show-notification utils/entry-get-error))
    (cond
      not-found-status?
      (dis/dispatch! [:activity-get/not-found org-slug entry-uuid secure-uuid])
      (not success)
      (dis/dispatch! [:activity-get/failed org-slug entry-uuid secure-uuid])
      :else
      (dis/dispatch! [:activity-get/finish org-slug (when success (json->cljs body)) secure-uuid]))))

(defn get-entry-with-uuid
  [board-slug entry-uuid & [loaded-cb]]
  (timbre/infof "Loading entry %s of board %s" entry-uuid board-slug)
  (let [org-slug (dis/current-org-slug)
        entry-data (dis/activity-data entry-uuid)
        fixed-board-slug (or board-slug
                             (:board-slug entry-data)
                             (:board-uuid entry-data))]
    (if fixed-board-slug
      (do
        (dis/dispatch! [:activity-get {:org-slug org-slug :board-slug fixed-board-slug :activity-uuid entry-uuid}])
        (api/get-entry-with-uuid org-slug fixed-board-slug entry-uuid
                                 (fn [{:keys [status success] :as resp}]
                                   (get-entry-finished org-slug entry-uuid resp)
                                   (when (fn? loaded-cb)
                                     (utils/after 100 #(loaded-cb success status))))))
      (timbre/warnf "Can't load entry %s, missing board slug: %s" entry-uuid fixed-board-slug))))

;; Cmail

(defn edit-open-cookie []
  (str "edit-open-" (jwt/user-id) "-" (:slug (dis/org-data))))

(defn- cmail-fullscreen-cookie []
  (str "cmail-fullscreen-" (jwt/user-id)))

(defn- cmail-fullscreen-save [fullscreen?]
  (cook/set-cookie! (cmail-fullscreen-cookie) fullscreen? (* 60 60 24 30)))

(defn- cmail-distraction-free-cookie []
  (str "cmail-distraction-free-" (jwt/user-id)))

(defn- cmail-distraction-free-save [on?]
  (cook/set-cookie! (cmail-distraction-free-cookie) on? (* 60 60 24 30)))

(defn- save-edit-open-cookie [entry-data]
  (cook/set-cookie! (edit-open-cookie) (or (str (:board-slug entry-data) "/" (:uuid entry-data)) true) (* 60 60 24 365)))

(defn cmail-show [initial-entry-data & [cmail-state]]
  (let [fixed-cmail-state (dissoc cmail-state :auto)]
    (when-not (:collapsed cmail-state)
      (cook/remove-cookie! (cmail-fullscreen-cookie)))
    (when (and (not (:auto cmail-state))
               (not (:collapsed cmail-state)))
      (save-edit-open-cookie initial-entry-data))
    (load-cached-item initial-entry-data (first dis/cmail-data-key)
     #(dis/dispatch! [:cmail-state/update fixed-cmail-state]))))

(defn cmail-expand [initial-entry-data]
  (cook/remove-cookie! (cmail-fullscreen-cookie))
  (save-edit-open-cookie initial-entry-data)
  (load-cached-item initial-entry-data (first dis/cmail-data-key)
   #(dis/dispatch! [:cmail-expand])))

(defn cmail-collapse []
  (dis/dispatch! [:cmail-collapse]))

(defn cmail-reset []
  (dis/dispatch! [:cmail-reset]))

(defn maybe-reset-cmail []
  (utils/after 100 #(when (:collapsed (dis/cmail-state)) (cmail-reset))))

(defn cmail-hide []
  (cook/remove-cookie! (edit-open-cookie))
  (let [cmail-state (dis/cmail-state)]
    (when (and (:fullscreen cmail-state)
               (not (:collapsed cmail-state)))
      (dom-utils/unlock-page-scroll)))
  (cmail-reset))

(defn cmail-fullscreen []
  (let [saved-distractoin-free-state (cook/get-cookie (cmail-distraction-free-cookie))]
    (cmail-fullscreen-save true)
    (utils/scroll-to-y 0 0)
    (dom-utils/lock-page-scroll)
    (dis/dispatch! [:update dis/cmail-state-key #(merge % {:fullscreen true :collapsed false :distraction-free? (= saved-distractoin-free-state "true")})])))

(defn cmail-toggle-fullscreen []
  (let [next-fullscreen-value (not (:fullscreen (dis/cmail-state)))]
    (cmail-fullscreen-save next-fullscreen-value)
    (when next-fullscreen-value
      (utils/scroll-to-y 0 0))
    (if next-fullscreen-value
      (dom-utils/lock-page-scroll)
      (dom-utils/unlock-page-scroll))
    (dis/dispatch! [:update dis/cmail-state-key #(merge % {:fullscreen next-fullscreen-value})])))

(defn cmail-toggle-distraction-free []
  (let [next-value (not (:distraction-free? (dis/cmail-state)))]
    (cmail-distraction-free-save next-value)
    (dis/dispatch! [:update dis/cmail-state-key #(merge % {:distraction-free? next-value})])))

(defonce cmail-reopen-only-one (atom false))

(defn cmail-reopen? []
  (when (compare-and-set! cmail-reopen-only-one false true)
      ;; Make sure the new param is alone and not with an access param that means
      ;; it was adding a slack team or bot
      (utils/after 100
       ;; If cmail is already open let's not reopen it
       #(when (or (not (dis/cmail-state))
                  (:collapsed (dis/cmail-state)))
          (let [cmail-state {:auto true
                             ;; reopen fullscreen on desktop, mobile doesn't use it
                             :fullscreen (not (responsive/is-mobile-size?))
                             :collapsed false
                             :key (utils/activity-uuid)}]
            (if (and (contains? (dis/query-params) :new)
                     (not (contains? (dis/query-params) :access)))
              ;; We have the new GET parameter, let's open a new post with the specified headline if any
              (let [new-data (get-board-for-edit (dis/query-param :new))
                    with-headline (if (dis/query-param :headline)
                                   (assoc new-data :headline (dis/query-param :headline))
                                   new-data)]
                (when new-data
                  (cmail-show with-headline cmail-state)))
              ;; We have the edit paramter or the edit cookie saved
              (when-let [edit-activity-param (or (dis/query-param :edit) (cook/get-cookie (edit-open-cookie)))]
                (if (= edit-activity-param "true")
                  ;; If it's simply true open a new post with the data saved in the local DB
                  (cmail-show {} cmail-state)
                  ;; If it's composed by board-slug/activity-uuid
                  (let [[board-slug activity-uuid] (string/split edit-activity-param #"/")
                        edit-activity-data (dis/activity-data activity-uuid)]
                    (if edit-activity-data
                      ;; Open the activity in edit if it's already present in the app-state
                      (cmail-show edit-activity-data cmail-state)
                      ;; Load it from the server if it's not
                      (when (and board-slug activity-uuid)
                        (get-entry-with-uuid board-slug activity-uuid
                         (fn [success status]
                           (when success
                             (cmail-show (dis/activity-data activity-uuid) cmail-state)))))))))))))))

(defn cmail-data-update
  ([cmail-data] (cmail-data-update cmail-data false))
  ([cmail-data no-changes-flag?]
   (dis/dispatch! [:cmail-data/update (if no-changes-flag? cmail-data (assoc cmail-data :has-changes true))])))

(defn cmail-data-replace
  ([cmail-data] (cmail-data-replace cmail-data true))
  ([cmail-data update-changes-flag?]
   (dis/dispatch! [:cmail-data/replace (if update-changes-flag? (assoc cmail-data :has-changes true) cmail-data)])))

(defn cmail-data-changed []
  (dis/dispatch! [:cmail-data/update {:has-changes true}]))

(defn cmail-data-remove-has-changes []
  (dis/dispatch! [:cmail-data/remove-has-changes]))

(defn toggle-cmail-label [label]
  (dis/dispatch! [:cmail-toggle-label label]))

(defn add-cmail-label [label]
  (dis/dispatch! [:cmail-add-label label]))

(defn remove-cmail-label [label]
  (dis/dispatch! [:cmail-remove-label label]))

(defn toggle-cmail-floating-labels-view
  ([] (toggle-cmail-floating-labels-view nil))
  ([v] (dis/dispatch! [:toggle-cmail-floating-labels-view v])))

(defn toggle-cmail-inline-labels-view
  ([] (toggle-cmail-inline-labels-view nil))
  ([v] (dis/dispatch! [:toggle-cmail-inline-labels-view v])))

(defn toggle-cmail-labels-views
  ([] (toggle-cmail-labels-views nil))
  ([v] (dis/dispatch! [:toggle-cmail-labels-views v])))

(defn cmail-label-remove-last-label []
  (dis/dispatch! [:cmail-label-remove-last-label]))