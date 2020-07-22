(ns oc.web.actions.cmail
  (:require [defun.core :refer (defun)]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.lib.user-cache :as uc]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.json :refer (json->cljs)]))

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
         (let [entry-to-save (merge item (select-keys entry-data [:links :board-slug :board-name :publisher-board]))]
           (dis/dispatch! [:input [edit-key] entry-to-save]))
         (do
           ;; If we got an item remove it since it won't be used
           ;; since we have an updated version of it already
           (when item
             (remove-cached-item (:uuid entry-data)))
           (dis/dispatch! [:input [edit-key] entry-data])))
       (when (fn? completed-cb)
         (completed-cb))))))

;; Last used and default section for editing

(defn get-default-section [& [editable-boards]]
  (let [editable-boards (or editable-boards (vals (dis/editable-boards-data (dis/current-org-slug))))
        cookie-value (au/last-used-section)
        board-from-cookie (when cookie-value
                            (some #(when (and (not (:draft %)) (= (:slug %) cookie-value)) %)
                             editable-boards))
        filtered-boards (filterv #(and (not (:draft %))
                                       ;; Pick publisher board only if they are enabled
                                       (or (and (:publisher-board %)
                                                ls/publisher-board-enabled?)
                                           (not (:publisher-board %))))
                         editable-boards)
        board-data (or board-from-cookie (first (sort-by :name filtered-boards)))]
    (when board-data
      {:board-name (:name board-data)
       :board-slug (:slug board-data)
       :publisher-board (:publisher-board board-data)})))

(defn get-board-for-edit [& [board-slug editable-boards]]
  (let [sorted-editable-boards (sort-by :name editable-boards)
        board-data (or
                    (some #(when (= (:slug %) board-slug) %) sorted-editable-boards)
                    (some #(when (= (:slug %) (dis/current-board-slug)) %) sorted-editable-boards)
                    (first sorted-editable-boards))]
    (if (or (not board-data)
            (= (:slug board-data) utils/default-drafts-board-slug)
            (:draft board-data)
            (not (utils/link-for (:links board-data) "create")))
      (get-default-section sorted-editable-boards)
      {:board-name (:name board-data)
       :board-slug (:slug board-data)
       :publisher-board (:publisher-board board-data)})))

;; Entry

(defn get-entry-with-uuid [board-slug entry-uuid & [loaded-cb]]
  (let [entry-data (dis/activity-data entry-uuid)]
    (when (and (not= entry-data :404)
               (or board-slug
                   (:board-slug entry-data)))
      (dis/dispatch! [:activity-get {:org-slug (dis/current-org-slug) :board-slug (or board-slug (:board-slug entry-data)) :activity-uuid entry-uuid}])
      (api/get-entry-with-uuid (dis/current-org-slug) board-slug entry-uuid
       (fn [{:keys [status success body]}]
        (cond
          (= status 404)
          (dis/dispatch! [:activity-get/not-found (dis/current-org-slug) entry-uuid nil])
          (not success)
          (dis/dispatch! [:activity-get/failed (dis/current-org-slug) entry-uuid nil])
          :else
          (dis/dispatch! [:activity-get/finish status (dis/current-org-slug) (when success (json->cljs body)) nil]))
        (when (fn? loaded-cb)
          (utils/after 100 #(loaded-cb success status))))))))

;; Cmail

(defn edit-open-cookie []
  (str "edit-open-" (jwt/user-id) "-" (:slug (dis/org-data))))

(defn- cmail-fullscreen-cookie []
  (str "cmail-fullscreen-" (jwt/user-id)))

(defn- cmail-fullscreen-save [fullscreen?]
  (cook/set-cookie! (cmail-fullscreen-cookie) fullscreen? (* 60 60 24 30)))

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
     #(dis/dispatch! [:input dis/cmail-state-key fixed-cmail-state]))))

(defn cmail-expand [initial-entry-data cmail-state]
  (cook/remove-cookie! (cmail-fullscreen-cookie))
  (save-edit-open-cookie initial-entry-data)
  (load-cached-item initial-entry-data (first dis/cmail-data-key)
   #(dis/dispatch! [:input dis/cmail-state-key (merge cmail-state {:collapsed false})])))

(defn cmail-hide []
  (cook/remove-cookie! (edit-open-cookie))
  (dis/dispatch! [:input dis/cmail-data-key (get-default-section)])
  (dis/dispatch! [:input dis/cmail-state-key {:collapsed true :key (utils/activity-uuid)}])
  (dom-utils/unlock-page-scroll))

(defn cmail-fullscreen []
  (let [current-state (dis/cmail-state)]
    (cmail-fullscreen-save true)
    (utils/scroll-to-y 0 0)
    (dom-utils/lock-page-scroll)
    (dis/dispatch! [:update dis/cmail-state-key #(merge % {:fullscreen true :collapsed false})])))

(defn cmail-toggle-fullscreen []
  (let [next-fullscreen-value (not (:fullscreen (dis/cmail-state)))]
    (cmail-fullscreen-save next-fullscreen-value)
    (when next-fullscreen-value
      (utils/scroll-to-y 0 0))
    (if next-fullscreen-value
      (dom-utils/lock-page-scroll)
      (dom-utils/unlock-page-scroll))
    (dis/dispatch! [:update dis/cmail-state-key #(merge % {:fullscreen next-fullscreen-value})])))

(defn cmail-toggle-must-see []
  (dis/dispatch! [:update dis/cmail-data-key #(merge % {:must-see (not (:must-see %))
                                                        :has-changes true})]))

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
                  (let [[board-slug activity-uuid] (clojure.string/split edit-activity-param #"/")
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