(ns open-company-web.lib.devcards
  (:require [devcards.core :as dc]
            [open-company-web.components.simple-section :refer [simple-section]]
            [open-company-web.components.finances.finances :refer [finances]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.data.finances :as company-data]
            [open-company-web.router :as router])
  (:require-macros [devcards.core :as dc :refer [defcard]]))

(router/set-route! ["companies" "buffer"] {:slug "buffer"})

(defn challenges-data []
  (let [data (dissoc company-data/finances :finances)
        data (assoc data :sections ["challenges"])]
    data))

(defn finances-data []
  (let [data (dissoc company-data/finances :challenges)
        data (assoc data :sections ["finances"])]
    data))

(defcard dc-simple-section
  "Simple section component"
  (dc/om-root simple-section {})
  {:section :challenges
   :read-only false
   :company-data (challenges-data)}
  {:inspect-data true :history true})

(defcard dc-finances
  "Finances component"
  (dc/om-root finances {})
  {:section :finances
   :company-data (finances-data)
   :editable-click-callback #()}
  {:inspect-data true :history true})