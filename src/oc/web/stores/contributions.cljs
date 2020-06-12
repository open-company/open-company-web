(ns oc.web.stores.contributions
  (:require [cuerdas.core :as str]
            [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.router :as router]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(defn- force-list-update-value
  ([current-value] (force-list-update-value current-value nil))
  ([current-value author-uuid]
   (if (or (nil? author-uuid)
           (= author-uuid (router/current-contributions-id)))
     (utils/activity-uuid)
     current-value)))

(defmethod dispatcher/action :contributions-get/finish
  [db [_ org-slug author-uuid sort-type contrib-data]]
  (let [org-data (dispatcher/org-data db org-slug)
        prepare-container-data (-> contrib-data :collection (assoc :container-slug :contributions))
        fixed-contrib-data (au/parse-contributions prepare-container-data (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db) (dispatcher/follow-publishers-list org-slug db) sort-type)
        contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        merged-items (merge (get-in db posts-key)
                            (:fixed-items fixed-contrib-data))]
    (-> db
     (update-in posts-key merge (:fixed-items fixed-contrib-data))
     (update-in dispatcher/force-list-update-key #(force-list-update-value % (:author-uuid contrib-data)))
     (assoc-in contrib-data-key (dissoc fixed-contrib-data :fixed-items)))))

(defmethod dispatcher/action :contributions-more
  [db [_ org-slug author-uuid sort-type]]
  (let [contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid sort-type)
        contrib-data (get-in db contrib-data-key)
        next-contrib-data (assoc contrib-data :loading-more true)]
    (assoc-in db contrib-data-key next-contrib-data)))

(defmethod dispatcher/action :contributions-more/finish
  [db [_ org-slug author-uuid sort-type direction next-contrib-data]]
  (if next-contrib-data
    (let [contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid sort-type)
          contrib-data (get-in db contrib-data-key)
          posts-data-key (dispatcher/posts-data-key org-slug)
          old-posts (get-in db posts-data-key)
          prepare-contrib-data (merge next-contrib-data {:posts-list (:posts-list contrib-data)
                                                         :old-links (:links contrib-data)
                                                         :container-slug :contributions})
          org-data (dispatcher/org-data db org-slug)
          fixed-contrib-data (au/parse-contributions prepare-contrib-data (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db) (dispatcher/follow-publishers-list org-slug db) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-contrib-data))
          new-contrib-data (-> fixed-contrib-data
                            (assoc :direction direction)
                            (dissoc :loading-more))]
      (-> db
        (assoc-in contrib-data-key new-contrib-data)
        (update-in dispatcher/force-list-update-key #(force-list-update-value % (:author-uuid contrib-data)))
        (assoc-in posts-data-key new-items-map)))
    db))