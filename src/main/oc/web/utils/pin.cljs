(ns oc.web.utils.pin
  (:require [oc.lib.hateoas :refer (link-for)]))

(defn- author-user-id [author]
  (if (string? author)
    author
    (:user-id author)))

(defn- authors-set [{authors :authors}]
  (set (map author-user-id authors)))

(defn can-home-pin? [entry-data current-user-id org-data]
  (if (contains? entry-data :can-home-pin?)
    entry-data
    (let [home-pin-link (link-for (:links entry-data) "home-pin")
          org-authors-set (authors-set org-data)]
      (assoc entry-data :can-home-pin? (and home-pin-link
                                            (org-authors-set current-user-id))))))

(defn can-board-pin? [entry-data current-user-id org-data board-data]
  (if (contains? entry-data :can-board-pin?)
    entry-data
    (let [board-pin-link (link-for (:links entry-data) "board-pin")
          private-board? (= (:access board-data) "private")
          org-authors-set (authors-set org-data)
          board-authors-set (authors-set board-data)]
      (assoc entry-data :can-board-pin? (and board-pin-link
                                             (or (and (not private-board?)
                                                      (org-authors-set current-user-id))
                                                 (and private-board?
                                                      (board-authors-set current-user-id))))))))
