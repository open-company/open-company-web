(ns oc.web.stores.pin
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as user-utils]
            [oc.web.local-settings :as ls]))

(defn toggle-pin
  [db org-slug entry-data pin-container-uuid container-key]
  (let [entry-pins-key (dispatcher/pins-key org-slug (:uuid entry-data))
        on? (contains? (get-in db entry-pins-key) pin-container-uuid)
        current-user-data (get-in db dispatcher/current-user-key)
        container-posts-list-key (conj container-key :posts-list)
        pins-sort-pivot-ms (* 1000 60 60 24 ls/pins-sort-pivot-days)]
    (as-> db tdb
      (if on?
        (update-in tdb entry-pins-key dissoc pin-container-uuid)
        (assoc-in tdb (conj entry-pins-key pin-container-uuid) {:pinned-at (utils/as-of-now)
                                                                :author (user-utils/author-for-user current-user-data)}))
      ;; Update the following container by moving the pinned post
      (update-in tdb
                 container-posts-list-key
                 (fn [posts-list]
                   (sort-by :sort-value
                            (mapv (fn [entry]
                                    (if (= (:uuid entry) (:uuid entry-data))
                                      (update entry
                                              :sort-value
                                              #(if on?
                                                 (- % pins-sort-pivot-ms)
                                                 (+ % pins-sort-pivot-ms)))
                                      entry))
                                  posts-list)))))))

(defmethod dispatcher/action :toggle-home-pin!
  [db [_ org-slug entry-data pin-container-uuid]]
  (let [following-key (dispatcher/container-key org-slug :following dispatcher/recently-posted-sort)]
    (toggle-pin db org-slug entry-data pin-container-uuid following-key)))

(defmethod dispatcher/action :toggle-board-pin!
  [db [_ org-slug entry-data pin-container-uuid]]
  (let [board-key (dispatcher/board-data-key org-slug pin-container-uuid dispatcher/recently-posted-sort)]
    (toggle-pin db org-slug entry-data pin-container-uuid board-key)))