(ns oc.web.actions.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions :as actions]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.user-cache :as uc]
            [oc.web.lib.responsive :as responsive]))

(defn load-cached-item
  [entry-data edit-key & [completed-cb]]
  (let [cache-key (actions/get-entry-cache-key (:uuid entry-data))]
    (uc/get-item cache-key
     (fn [item err]
       (if (and (not err)
                (map? item)
                (= (:updated-at entry-data) (:updated-at item)))
         (let [entry-to-save (merge item (select-keys entry-data [:links :board-slug :board-name]))]
           (dis/dispatch! [:input [edit-key] entry-to-save]))
         (do
           ;; If we got an item remove it since it won't be used
           ;; since we have an updated version of it already
           (when item
             (actions/remove-cached-item (:uuid entry-data)))
           (dis/dispatch! [:input [edit-key] entry-data])))
       (when (fn? completed-cb)
         (completed-cb))))))

(defn activity-modal-fade-in
  [activity-data & [editing item-load-cb]]
  (let [org (router/current-org-slug)
        board (:board-slug activity-data)
        activity-uuid (:uuid activity-data)
        to-url (oc-urls/entry board activity-uuid)]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org board activity-uuid "activity"]
     {:org org
      :board board
      :activity activity-uuid
      :query-params (dissoc (:query-params @router/path) :ap-initial-at)
      :from-all-posts (= (router/current-board-slug) "all-posts")}))
  (when editing
    (utils/after 100 #(load-cached-item activity-data :modal-editing-data item-load-cb)))
  (dis/dispatch! [:activity-modal-fade-in activity-data editing]))

(defn activity-modal-fade-out
  [board-slug]
  (let [from-all-posts (:from-all-posts @router/path)
        to-board (if from-all-posts "all-posts" board-slug)
        org (router/current-org-slug)
        to-url (if from-all-posts
                (oc-urls/all-posts org)
                (oc-urls/board org board-slug))]
    (.pushState (.-history js/window) #js {} "" to-url)
    (router/set-route! [org to-board (if from-all-posts "all-posts" "dashboard")]
     {:org org
      :board to-board
      :activity nil
      :query-params (:query-params @router/path)
      :from-all-posts false}))
  (dis/dispatch! [:activity-modal-fade-out board-slug]))

(defn entry-edit
  [initial-entry-data]
  (load-cached-item initial-entry-data :entry-editing))

(defn activity-edit
  [activity-data]
  (if (or (responsive/is-tablet-or-mobile?)
          (not= (:status activity-data) "published"))
    (load-cached-item activity-data :entry-editing)
    (activity-modal-fade-in activity-data true (fn [] (dis/dispatch! [:modal-editing-activate])))))

(defn entry-edit-dismiss
  []
  ;; If the user was looking at the modal, dismiss it too
  (when (router/current-activity-id)
    (utils/after 1 #(let [from-all-posts (or
                                          (:from-all-posts @router/path)
                                          (= (router/current-board-slug) "all-posts"))]
                      (router/nav!
                        (if from-all-posts ; AP
                          (oc-urls/all-posts (router/current-org-slug))
                          (oc-urls/board (router/current-org-slug) (router/current-board-slug)))))))
  ;; Add :entry-edit-dissmissing for 1 second to avoid reopening the activity modal after edit is dismissed.
  (utils/after 1000 #(dis/dispatch! [:input [:entry-edit-dissmissing] false]))
  (dis/dispatch! [:entry-edit/dismiss]))

(defn activity-modal-edit
  [activity-data activate]
  (if activate
    (do
      (load-cached-item activity-data :modal-editing-data)
      (dis/dispatch! [:modal-editing-activate]))
    (dis/dispatch! [:modal-editing-deactivate])))

(defn entry-save-on-exit
  [edit-key activity-data entry-body]
  (let [entry-map (assoc activity-data :body entry-body)
        cache-key (actions/get-entry-cache-key (:uuid activity-data))]
    (uc/set-item cache-key entry-map
     (fn [err]
       (when-not err
         (dis/dispatch! [:entry-toggle-save-on-exit false]))))))

(defn entry-toggle-save-on-exit
  [enable?]
  (dis/dispatch! [:entry-toggle-save-on-exit enable?]))

(defn entry-modal-save [activity-data board-slug]
  (api/update-entry activity-data board-slug :modal-editing-data)
  (dis/dispatch! [:entry-modal-save]))

(defn nux-next-step [next-step]
  (dis/dispatch! [:nux-next-step next-step]))

(defn show-add-post-tooltip []
  (cook/set-cookie! (router/show-add-post-tooltip-cookie (jwt/user-id)) true (* 60 60 24 365))
  (dis/dispatch! [:input [:show-add-post-tooltip] true]))

(defn hide-add-post-tooltip []
  (cook/remove-cookie! (router/show-add-post-tooltip-cookie (jwt/user-id)))
  (dis/dispatch! [:input [:show-add-post-tooltip] false]))

(defn should-show-add-post-tooltip
  "Check if we need to show the add post tooltip."
  []
  (let [org-data (dis/org-data)]
    (and ;; the cookie is set
         (cook/get-cookie (router/show-add-post-tooltip-cookie (jwt/user-id)))
         ;; has only one board
         (= (count (:boards org-data)) 1)
         ;; and the board
         (let [board-data (dis/board-data)
               first-post (first (vals (:fixed-items board-data)))
               first-post-author (when first-post
                                   (if (map? (:author first-post))
                                      (:author first-post)
                                      (first (:author first-post))))]
           ;; or
           (or (and ;; has only one post
                    (= (count (:fixed-items board-data)) 1)
                    first-post
                    ;; from CarrotHQ
                    (= (:user-id first-post-author) "0000-0000-0000"))
                ;; has no posts
               (zero? (count (:fixed-items board-data))))))))

(defn nux-end []
  (if true ; (should-show-add-post-tooltip)
    (show-add-post-tooltip)
    (hide-add-post-tooltip))
  (cook/remove-cookie! (router/show-nux-cookie (jwt/user-id)))
  (dis/dispatch! [:nux-end]))