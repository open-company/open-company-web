(ns oc.web.stores.pin
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as activity-utils]))

(def pins-sort-pivot-ms (* 1000 60 60 24 ls/pins-sort-pivot-days))

(defn toggle-pin
  [db org-slug entry-data pin-container-uuid container-key board?]
  (let [entry-key (dispatcher/activity-key org-slug (:uuid entry-data))
        container-data (get-in db container-key)
        pin-container-kw (keyword pin-container-uuid)
        on? (map? (get-in db (concat entry-key [:pins pin-container-kw])))
        current-user-data (get-in db dispatcher/current-user-key)
        org-data (dispatcher/org-data db org-slug)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        existing-pins (get-in db (conj entry-key :pins) {})
        pinned-at (when-not on?
                    (utils/as-of-now))
        updated-pins (if on?
                      (dissoc existing-pins pin-container-kw)
                      (assoc existing-pins pin-container-kw {:author (user-utils/author-for-user current-user-data)
                                                             :pinned-at pinned-at}))
        new-sort-value (let [t (.getTime (utils/js-date pinned-at))]
                         (if on?
                           (- t pins-sort-pivot-ms)
                           (+ t pins-sort-pivot-ms)))
        update-sort-item-fn (fn [entry]
                              (if (= (:uuid entry) (:uuid entry-data))
                                (-> entry
                                    (assoc :sort-value new-sort-value)
                                    (assoc :pinned-at pinned-at))
                                entry))
        update-posts-list-fn (fn [posts-list]
                               (->> posts-list
                                    (mapv update-sort-item-fn)
                                    (sort-by :sort-value)
                                    (reverse)))
        update-container-fn #(if board?
                               (activity-utils/update-board % (:slug container-data) org-data change-data active-users)
                               (activity-utils/update-container % (:container-slug container-data) org-data change-data active-users))]
    (as-> db tdb
      ;; Update full entry map value
      (update-in tdb entry-key merge {:sort-value new-sort-value
                                      :pins updated-pins})
      ;; Update the relative sort-value to make sure the sort works
      (update-in tdb (conj container-key :posts-list) update-posts-list-fn)
      ;; Resort the posts and re-create the feed headers etc
      (update-container-fn tdb))))

(defmethod dispatcher/action :toggle-home-pin!
  [db [_ org-slug entry-data pin-container-uuid]]
  (let [following-key (dispatcher/container-key org-slug :following dispatcher/recently-posted-sort)]
    (toggle-pin db org-slug entry-data pin-container-uuid following-key false)))

(defmethod dispatcher/action :toggle-board-pin!
  [db [_ org-slug entry-data pin-container-uuid]]
  (let [board-key (dispatcher/board-data-key org-slug pin-container-uuid dispatcher/recently-posted-sort)]
    (toggle-pin db org-slug entry-data pin-container-uuid board-key true)))