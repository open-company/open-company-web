(ns oc.web.stores.reply
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.comment :as cu]))

;; Reply actions

(defn remove-collapse-items-inner [entry-uuid entries]
  (mapv (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data
            (fn [replies-data]
              (filterv #(not= (:resource-type %) :collapsed-comments) replies-data)))
           entry-data))
   entries))

(defn- remove-collapse-items [db replies-key entry-uuid]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial remove-collapse-items-inner entry-uuid))
   (update-in (conj replies-key :posts-list) (partial remove-collapse-items-inner entry-uuid))))

(defn- update-field [item kv fv]
  (if (fn? fv)
    (update item kv fv)
    (assoc item kv fv)))

(defn update-reply-inner [entry-uuid reply-uuid-or-set kv fv entries]
  (mapv (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data
            (fn [replies-data]
              (mapv (fn [reply-data]
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
  (mapv (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update entry-data :replies-data (fn [replies-data]
                                              (let [with-seen (map #(assoc % :unseen false) replies-data)]
                                                (if (:expanded-replies entry-data)
                                                  ;; If the user expanded the comments just add the new comment at the end of the list
                                                  (vec (conj with-seen parsed-reply-data))
                                                  ;; Otherwise re-apply the collapse logic to the new list
                                                  (cu/collapse-comments
                                                   (filterv #(= (:resource-type %) :comment)
                                                    (vec (conj with-seen parsed-reply-data))))))))
           entry-data))
   entries))

(defn- add-reply [db replies-key entry-uuid parsed-reply-data]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial add-reply-inner entry-uuid parsed-reply-data))
   (update-in (conj replies-key :posts-list) (partial add-reply-inner entry-uuid parsed-reply-data))))

(defn- update-entry-inner [entry-uuid kv fv entries]
  (mapv (fn [entry-data]
         (if (= (:uuid entry-data) entry-uuid)
           (update-field entry-data kv fv)
           entry-data))
   entries))

(defn- update-entry [db replies-key entry-uuid kv fv]
  (-> db
   (update-in (conj replies-key :items-to-render) (partial update-entry-inner entry-uuid kv fv))
   (update-in (conj replies-key :posts-list) (partial update-entry-inner entry-uuid kv fv))))

;; Store actions

(defmethod dispatcher/action :reply-expand
  [db [_ org-slug entry-data reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (update-reply db replies-key (:uuid entry-data) (:uuid reply-data) :collapsed false)))

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
  [db [_ org-slug entry-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (-> db
     (update-entry replies-key (:uuid entry-data) :expanded-replies true)
     (update-reply replies-key (:uuid entry-data) :all :collapsed false)
     (remove-collapse-items replies-key (:uuid entry-data)))))

(defmethod dispatcher/action :replies-add
  [db [_ org-slug entry-data parsed-reply-data]]
  (let [replies-key (dispatcher/container-key org-slug :replies dispatcher/recent-activity-sort)]
    (-> db
     ;; Add the new comment as last comment
     (add-reply replies-key (:uuid entry-data) parsed-reply-data)
     ;; Mark entry seen
     (update-entry replies-key (:uuid entry-data) :unseen false)
     (update-entry replies-key (:uuid entry-data) :unseen-comments false))))

