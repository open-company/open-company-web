(ns oc.web.stores.reply
  (:require [oc.web.dispatcher :as dispatcher]))

;; Reply actions

(defn remove-collapse-reply-inner [entry-uuid collapse-id entries]
  (map (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data
            (fn [replies-data]
              (filter #(not= (:collapse-id %) collapse-id) replies-data)))
           entry-data))
   entries))

(defn- remove-collapse-reply [db replies-key entry-uuid collapse-id]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial remove-collapse-reply-inner entry-uuid collapse-id))
   (update-in (conj replies-key :posts-list) (partial remove-collapse-reply-inner entry-uuid collapse-id))))

(defn- update-field [item kv fv]
  (if (fn? fv)
    (update item kv fv)
    (assoc item kv fv)))

(defn update-reply-inner [entry-uuid reply-uuid-or-set kv fv entries]
  (map (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data
            (fn [replies-data]
              (map (fn [reply-data]
                     (if (or (= reply-uuid-or-set :all)
                             (= reply-uuid-or-set (:uuid reply-data))
                             (and (set? reply-uuid-or-set)
                                  (reply-uuid-or-set (:uuid reply-data))))
                       (update-field reply-data kv fv)
                       reply-data))
               replies-data)))
           entry-data))
   entries))

(defn- update-reply [db replies-key entry-uuid reply-uuid kv fv]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial update-reply-inner entry-uuid reply-uuid kv fv))
   (update-in (conj replies-key :posts-list) (partial update-reply-inner entry-uuid reply-uuid kv fv))))

(defn- update-replies [db replies-key entry-uuid kv fv]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial update-reply-inner entry-uuid :all kv fv))
   (update-in (conj replies-key :posts-list) (partial update-reply-inner entry-uuid :all kv fv))))

(defn- add-reply-inner [entry-uuid parsed-reply-data entries]
  (map (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data conj parsed-reply-data)
           entry-data))
   entries))

(defn- add-reply [db replies-key entry-uuid parsed-reply-data]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial add-reply-inner entry-uuid parsed-reply-data))
   (update-in (conj replies-key :posts-list) (partial add-reply-inner entry-uuid parsed-reply-data))))

;; Store actions

(defmethod dispatcher/action :reply-expand
  [db [_ org-slug entry-data reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (update-reply db replies-key (:uuid entry-data) (:uuid reply-data) :expanded true)))

(defmethod dispatcher/action :reply-mark-seen
  [db [_ org-slug entry-data reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (update-reply db replies-key (:uuid entry-data) (:uuid reply-data) :unseen false)))

(defmethod dispatcher/action :reply-unwrap-body
  [db [_ org-slug entry-data reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (update-reply db replies-key (:uuid entry-data) (:uuid reply-data) :unwrapped-body true)))

(defmethod dispatcher/action :replies-entry-mark
  [db [_ org-slug entry-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (update-replies db replies-key (:uuid entry-data) :unseen false)))

(defmethod dispatcher/action :replies-expand
  [db [_ org-slug entry-data collapse-item]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (-> db
     (update-reply replies-key (:uuid entry-data) (set (:comment-uuids collapse-item)) :expanded true)
     (remove-collapse-reply replies-key (:uuid entry-data) (:collapse-id collapse-item)))))

(defmethod dispatcher/action :replies-add
  [db [_ org-slug entry-data parsed-reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (-> db
     (add-reply replies-key (:uuid entry-data) parsed-reply-data)
     (update-replies replies-key (:uuid entry-data) :unseen false))))

