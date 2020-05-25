(ns oc.web.actions.comment
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.utils.comment :as comment-utils]
            [oc.web.stores.comment :as comment-store]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notification-actions]))

(defn add-comment-focus [activity-uuid]
  (dis/dispatch! [:add-comment-focus activity-uuid]))

(defn add-comment-blur []
  (dis/dispatch! [:add-comment-focus nil]))

(defn edit-comment [activity-uuid comment-data]
  (dis/dispatch! [:add-comment-change (router/current-org-slug) activity-uuid (:reply-parent comment-data) (:uuid comment-data) (:body comment-data)]))

(defn stop-comment-edit [activity-uuid comment-data]
  (dis/dispatch! [:add-comment-change (router/current-org-slug) activity-uuid (:reply-parent comment-data) (:uuid comment-data) nil]))

(defn add-comment-change [activity-data parent-comment-uuid comment-uuid comment-body]
  ;; Save the comment change in the app state to remember it
  (dis/dispatch! [:add-comment-change (router/current-org-slug) (:uuid activity-data) parent-comment-uuid comment-uuid comment-body]))

(defn add-comment-reset [activity-uuid parent-comment-uuid comment-uuid]
  (dis/dispatch! [:add-comment-reset (router/current-org-slug) activity-uuid parent-comment-uuid comment-uuid]))

(defn add-comment [activity-data comment-body parent-comment-uuid save-done-cb]
  (add-comment-blur)
  (let [org-slug (router/current-org-slug)
        comments-key (dis/activity-comments-key org-slug (:uuid activity-data))
        comments-data (get-in @dis/app-state comments-key)
        add-comment-link (utils/link-for (:links activity-data) "create" "POST")
        current-user-id (jwt/user-id)
        is-publisher? (= (-> activity-data :publisher :user-id) current-user-id)
        new-comment-uuid (utils/activity-uuid)
        user-data (if (jwt/jwt)
                    (jwt/get-contents)
                    (jwt/get-id-token-contents))
        new-comment-map {:body comment-body
                         :created-at (utils/as-of-now)
                         :parent-uuid parent-comment-uuid
                         :resource-uuid (:uuid activity-data)
                         :uuid new-comment-uuid
                         :author {:name (:name user-data)
                                  :avatar-url (:avatar-url user-data)
                                  :user-id (:user-id user-data)}}
        first-comment-from-user? (when-not is-publisher?
                                   (not (seq (filter #(= (-> % :author :user-id) current-user-id) comments-data))))
        should-show-follow-notification? (and first-comment-from-user?
                                              (utils/link-for (:links activity-data) "follow"))]
    ;; Reset the add comment field
    (dis/dispatch! [:add-comment-reset (router/current-org-slug) (:uuid activity-data) parent-comment-uuid nil])
    ;; Add the comment to the app-state to show it immediately
    (dis/dispatch! [:comment-add org-slug activity-data new-comment-map parent-comment-uuid comments-key new-comment-uuid])
    ;; Send WRT read on comment add
    (activity-actions/send-item-read (:uuid activity-data))
    (api/add-comment add-comment-link comment-body new-comment-uuid parent-comment-uuid
      ;; Once the comment api request is finished refresh all the comments, no matter
      ;; if it worked or not
      (fn [{:keys [status success body]}]
        (save-done-cb success)
        ;; If the user is not the publisher of the post and is leaving his first comment on it
        ;; let's inform them that they are now following the post
        (when success
          (do
            (dis/dispatch! [:comment-add/replace activity-data (json->cljs body) comments-key new-comment-uuid])
            (swap! router/path assoc :refresh true))
          (when should-show-follow-notification?
            (notification-actions/show-notification {:title "You are now following this post."
                                                     :dismiss true
                                                     :expire 3
                                                     :id :first-comment-follow-post})))
        ; (utils/after 100 (fn [](let [comments-link (utils/link-for (:links activity-data) "comments")]
        ;   (api/get-comments comments-link #(comment-utils/get-comments-finished comments-key activity-data %)))))
        (let [comments-link (utils/link-for (:links activity-data) "comments")]
          (api/get-comments comments-link #(comment-utils/get-comments-finished comments-key activity-data %)))
        ;; In case save didn't go well let's re-set the comment body in the add comment field
        (when-not success
          ;; Remove the newly added comment if still in the list
          (dis/dispatch! [:comment-add/failed activity-data new-comment-map comments-key])
          ;; Move the comment back in the body field
          (dis/dispatch! [:add-comment-change (router/current-org-slug) (:uuid activity-data) parent-comment-uuid nil comment-body true]))))
    new-comment-map))

(defn get-comments [activity-data]
  (comment-utils/get-comments activity-data))

(defn get-comments-if-needed [activity-data comments-data]
  (comment-utils/get-comments-if-needed activity-data comments-data))

(defn delete-comment [activity-data comment-data]
  ;; Send WRT read on comment delete
  (activity-actions/send-item-read (:uuid activity-data))
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:uuid activity-data))
        delte-comment-link (utils/link-for (:links comment-data) "delete")]
    (dis/dispatch! [:comment-delete
                    (:uuid activity-data)
                    comment-data
                    comments-key])
    (api/delete-comment delte-comment-link
      (fn [{:keys [status success body]}]
        (let [comments-link (utils/link-for (:links activity-data) "comments")]
          (api/get-comments comments-link
           #(comment-utils/get-comments-finished comments-key activity-data %)))))))

(defn comment-reaction-toggle [activity-data comment-data reaction-data reacting?]
  (activity-actions/send-item-read (:uuid activity-data))
  (let [comments-key (dis/activity-comments-key (router/current-org-slug)
                      (:uuid activity-data))
        link-method (if reacting? "PUT" "DELETE")
        reaction-link (utils/link-for (:links reaction-data) "react" link-method)]
    (dis/dispatch! [:comment-reaction-toggle
                    comments-key
                    activity-data
                    comment-data
                    reaction-data
                    reacting?])
    (api/toggle-reaction reaction-link
      (fn [{:keys [status success body]}]
        (get-comments activity-data)))))

(defn react-from-picker [activity-data comment-data emoji]
  (let [react-link (utils/link-for (:links comment-data) "react" "POST")
        comments-key (dis/activity-comments-key (router/current-org-slug) (:uuid activity-data))]
    (dis/dispatch! [:comment-react-from-picker comments-key (:uuid activity-data) (:uuid comment-data) emoji])
    (api/react-from-picker react-link emoji
      (fn [{:keys [status succes body]}]
        (get-comments activity-data)))))

(defn- inc-time [t]
  (.getTime (js/Date. (inc (.getTime (js/Date. t))))))

(defn save-comment [activity-data comment-data new-body save-done-cb]
  ;; Send WRT on comment update
  (activity-actions/send-item-read (:uuid activity-data))
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:uuid activity-data))
        patch-comment-link (utils/link-for (:links comment-data) "partial-update")
        updated-comment-map (merge comment-data {:body new-body
                                                 :updated-at (inc-time (:updated-at comment-data))})]
    ;; Add the new comment to the list of comments
    (dis/dispatch! [:comment-save (router/current-org-slug) comments-key updated-comment-map])
    ;; Reset the add comment field
    (dis/dispatch! [:add-comment-reset (router/current-org-slug) (:uuid activity-data) (:reply-parent comment-data) (:uuid comment-data)])
    (api/patch-comment patch-comment-link new-body
      (fn [{:keys [success]}]
        (save-done-cb success)
        (let [comments-link (utils/link-for (:links activity-data) "comments")]
          (api/get-comments comments-link #(comment-utils/get-comments-finished comments-key activity-data %)))
        (when-not success
          ;; Remove the newly added comment if still in the list
          (dis/dispatch! [:comment-save/failed activity-data comment-data comments-key])
          ;; Move the comment back in the body field
          (dis/dispatch! [:add-comment-change (router/current-org-slug) (:uuid activity-data) (:parent-uuid comment-data) (:uuid comment-data) new-body true]))))
    updated-comment-map))

(defn ws-comment-update
  [interaction-data]
  (let [comments-key (dis/activity-comments-key
                      (router/current-org-slug)
                      (:resource-uuid interaction-data))]
    (dis/dispatch! [:ws-interaction/comment-update
                    comments-key
                    interaction-data])))

(defn ws-comment-delete [comment-data]
  (dis/dispatch! [:ws-interaction/comment-delete (router/current-org-slug) comment-data]))

(defn ws-comment-add [interaction-data]
  (let [org-slug   (router/current-org-slug)
        activity-uuid (:resource-uuid interaction-data)
        entry-data (dis/activity-data org-slug activity-uuid)
        new-comment-uuid (:uuid (:interaction interaction-data))
        comments-data (get-in @dis/app-state (dis/activity-comments-key org-slug activity-uuid))
        comment-exists? (seq (filterv #(= (:uuid %) new-comment-uuid) comments-data))]
    (when (and entry-data
               (not comment-exists?))
      ;; Refresh the entry data to get the new links to interact with
      (activity-actions/get-entry entry-data)
      (dis/dispatch! [:ws-interaction/comment-add
                      org-slug
                      entry-data
                      interaction-data]))))

(defn subscribe []
  (ws-ic/subscribe :interaction-comment/add
                   #(utils/after 0 (fn [] (ws-comment-add (:data %)))))
  (ws-ic/subscribe :interaction-comment/update
                   #(ws-comment-update (:data %)))
  (ws-ic/subscribe :interaction-comment/delete
                   #(ws-comment-delete (:data %))))
