(ns oc.web.stores.contributions
  (:require [cuerdas.core :as str]
            [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(defmethod dispatcher/action :contributions-get/finish
  [db [_ org-slug author-uuid contrib-data]]
  (let [org-data (dispatcher/org-data db org-slug)
        fixed-contrib-data (au/fix-contributions (:collection contrib-data) (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db))
        contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid)
        posts-key (dispatcher/posts-data-key org-slug)
        merged-items (merge (get-in db posts-key)
                            (:fixed-items fixed-contrib-data))]
    (-> db
     (update-in posts-key merge (:fixed-items fixed-contrib-data))
     (assoc-in contrib-data-key (dissoc fixed-contrib-data :fixed-items)))))

(defmethod dispatcher/action :contributions-more
  [db [_ org-slug author-uuid]]
  (let [contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid)
        contrib-data (get-in db contrib-data-key)
        next-contrib-data (assoc contrib-data :loading-more true)]
    (assoc-in db contrib-data-key next-contrib-data)))

(defmethod dispatcher/action :contributions-more/finish
  [db [_ org-slug author-uuid direction next-contrib-data]]
  (if next-contrib-data
    (let [contrib-data-key (dispatcher/contributions-data-key org-slug author-uuid)
          contrib-data (get-in db contrib-data-key)
          posts-data-key (dispatcher/posts-data-key org-slug)
          old-posts (get-in db posts-data-key)
          prepare-contrib-data (merge next-contrib-data {:posts-list (:posts-list contrib-data)
                                                         :old-links (:links contrib-data)})
          org-data (dispatcher/org-data db org-slug)
          fixed-contrib-data (au/fix-contributions prepare-contrib-data (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db) direction)
          new-items-map (merge old-posts (:fixed-items fixed-contrib-data))
          new-contrib-data (-> fixed-contrib-data
                            (assoc :direction direction)
                            (dissoc :loading-more))]
      (-> db
        (assoc-in contrib-data-key new-contrib-data)
        (assoc-in posts-data-key new-items-map)))
    db))