(ns oc.web.utils.board
  (:require [defun.core :refer (defun)]))

(def default-board-slug "--default-section-slug")
(def default-board-access "team")

(def default-board
  {:name ""
   :access default-board-access
   :slack-mirror []
   :slug default-board-slug})

(def board-name-exists-error "Team name already exists or isn't allowed")

(def default-drafts-board-name "Drafts")

(def default-drafts-board-slug "drafts")

(def default-draft-status "draft")

(def default-drafts-board
  {:uuid "0000-0000-0000"
   :created-at "2000-01-01T00:00:00.000Z"
   :updated-at "2000-01-01T00:00:00.000Z"
   :slug default-drafts-board-slug
   :name default-drafts-board-name
   :entries []
   :access "private"
   :read-only true})

(defun format-mirror-channel
  ([slack-org :guard map? channel] (format-mirror-channel (:slack-org-id slack-org) channel))
  ([slack-org-id :guard string? channel]
   {:channel-id (:id channel)
    :channel-name (:name channel)
    :slack-org-id slack-org-id
    :needs-join (:needs-join channel)
    :type (or (:channel-type channel) "channel")}))

(defun prefix-for-channel
  ([channel :guard map?]
   (prefix-for-channel (or (:channel-type channel) (:type channel))))
  ([channel-type :guard string?]
   (if (= channel-type "user") "@" "#")))


(def ^{:private true} chid #(or (:id %) (:channel-id %)))

(def ^{:private true} orgid #(or (:slack-org-id %) %))

(defn compare-channel
  ([slack-org channel]
   #(and (= (orgid slack-org) (orgid %))
         (= (chid channel) (chid %))))

  ([slack-org channel channel-b]
   (and (= (orgid slack-org) (orgid channel-b))
        (= (chid channel) (chid channel-b)))))

(defn contains-channel?
  ([channels-list]
   (if (seq channels-list)
     (fn [slack-org channel-b]
       (some (compare-channel slack-org channel-b) channels-list))
     #(not true)))

  ([channels-list slack-org channel]
   (let [comp-ch (compare-channel slack-org channel)]
     (some comp-ch channels-list))))
