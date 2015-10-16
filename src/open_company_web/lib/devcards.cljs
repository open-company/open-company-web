(ns open-company-web.lib.devcards
  (:require [devcards.core :as dc]
            [open-company-web.components.editable-title :refer [editable-title]])
  (:require-macros [devcards.core :as dc :refer [defcard]]))

(defcard dc-editable-title
  "Editable title component"
  (dc/om-root editable-title {})
  {:section-data {:title "This is a title"}
   :read-only false
   :section :finances
   :save-channel "test"}
  {:inspect-data true :history true})