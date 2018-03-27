(ns oc.web.actions.section
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.router :as router]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.ws-interaction-client :as ws-ic]
            [oc.web.lib.ws-change-client :as ws-cc]
            [oc.web.actions.org :as oa]
            [oc.web.api :as api]))

(defn is-currently-shown? [section]
  (= (router/current-board-slug)
     (:slug section)))

(defn watch-single-section [section]
  ;; only watch the currently visible board.
  (ws-ic/board-unwatch (fn [rep]
    (timbre/debug rep "Watching on socket " (:uuid section))
        (ws-ic/board-watch (:uuid section)))))

(defn load-other-sections
  [sections]
  (doseq [section sections
          :when (not (is-currently-shown? section))]
    (api/get-board (utils/link-for (:links section) ["item" "self"] "GET")
      (fn [status body success]
        (let [section-data (json->cljs body)]
              ;; is-loaded is meant for the currently in view board components
              (dispatcher/dispatch!
               [:section (assoc section-data :is-loaded false)]))))))

(defn section-seen
  [uuid]
  ;; Let the change service know we saw the board
  (ws-cc/container-seen uuid)
  (dispatcher/dispatch! [:section-seen uuid]))

(defn section-get-finish
  [section]
  (let [is-currently-shown (is-currently-shown? section)]
    (when is-currently-shown
      (when (and (router/current-activity-id)
                 (not (contains? (:entries section) (router/current-activity-id))))
        (router/nav! (oc-urls/board (router/current-org-slug) (:slug section))))
      ;; Tell the container service that we are seeing this board,
      ;; and update change-data to reflect that we are seeing this board
      (when-let [section-uuid (:uuid section)]
        (utils/after 10 #(section-seen section-uuid)))
      ;; only watch the currently visible board.
      (when (jwt/jwt) ; only for logged in users
        (watch-single-section section)))

    (dispatcher/dispatch! [:section (assoc section :is-loaded is-currently-shown)])))

(defn section-change
  [section-uuid change-at]
  (timbre/debug "Section change:" section-uuid "at:" change-at)
  (utils/after 1000 (fn []
    (let [current-section-data (dispatcher/board-data)]
      (if (= section-uuid (:uuid current-section-data))
        ;; Reload the current board data
        (api/get-board (utils/link-for (:links current-section-data) "self")
                       (fn [status body success]
                         (when success (section-get-finish (json->cljs body)))))
        ;; Reload a secondary board data
        (let [sections (:boards (dispatcher/org-data))
              filtered-sections (filter #(= (:uuid %) section-uuid) sections)]
          (load-other-sections filtered-sections))))))
  ;; Update change-data state that the board has a change
  (dispatcher/dispatch! [:section-change section-uuid change-at]))

(defn section-get
  [link]
  (api/get-board link
    (fn [status body success]
      (when success (section-get-finish (json->cljs body))))))

(defn section-nav-away [uuid]
  (dispatcher/dispatch! [:section-nav-away uuid]))

(defn section-delete [section-slug]
  (api/delete-board section-slug (fn [status success body]
    (if success
      (router/nav! (oc-urls/org (router/current-org-slug)))
      (.reload (.-location js/window))))))

(defn section-save [section-data]
  (timbre/debug section-data)
  (if (empty? (:links section-data))
    (api/create-board section-data
      (fn [{:keys [success status body]}]
        (let [section-data (when success (json->cljs body))]
          (if (= status 409)
            ;; Board name exists
            (dispatcher/dispatch!
             [:input
              [:section-editing :section-name-error]
              "Section name already exists or isn't allowed"])
            (do
              (utils/after 100 #(router/nav! (oc-urls/board (router/current-org-slug) (:slug section-data))))
              (ws-cc/container-watch [(:uuid section-data)])
              (dispatcher/dispatch! [:section-edit-save/finish section-data]))))))
    (api/patch-board section-data (fn [success body status]
      (if (= status 409)
        ;; Board name exists
        (dispatcher/dispatch!
         [:input
          [:section-editing :section-name-error]
          "Board name already exists or isn't allowed"])
        (do
          (oa/get-org)
          (dispatcher/dispatch! [:section-edit-save/finish (json->cljs body)])))))))

(defn private-section-user-add
  [user user-type]
  (dispatcher/dispatch! [:private-section-user-add user user-type]))

(defn private-section-user-remove
  [user]
  (dispatcher/dispatch! [:private-section-user-remove user]))

(defn private-section-kick-out-self
  [user]
  (when (= (:user-id user) (jwt/user-id))
    (api/remove-user-from-private-board user (fn [status success body]
      ;; Redirect to the first available board
      (let [org-data (dispatcher/org-data)
            all-boards (:boards org-data)
            current-board-slug (router/current-board-slug)
            except-this-boards (remove #(#{current-board-slug "drafts"} (:slug %)) all-boards)
            redirect-url (if-let [next-board (first except-this-boards)]
                           (oc-urls/board (:slug next-board))
                           (oc-urls/org (router/current-org-slug)))]
        (oa/get-org)
        (utils/after 0 #(router/nav! redirect-url))
        (dispatcher/dispatch! [:private-section-kick-out-self/finish success]))))))

(defn wc-subscribe
  [subscriber]
  (subscriber :container/change
    (fn [data]
      (let [change-data (:data data)
            container-id (:container-id change-data)]
        ;; not an org data change
        (when (not= container-id (:uuid (dispatcher/org-data)))
          (section-change container-id (:change-at change-data)))))))
