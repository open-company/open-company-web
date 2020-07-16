(ns oc.web.utils.drafts)

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