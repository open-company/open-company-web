(ns open-company-web.lib.devcards
  (:require [devcards.core :as dc]
            [open-company-web.components.simple-section :refer [simple-section]]
            [open-company-web.components.finances.finances :refer [finances]]
            [open-company-web.components.growth.growth :refer [growth]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.data.finances :as company-data]
            [open-company-web.router :as router])
  (:require-macros [devcards.core :as dc :refer [defcard]]))

(router/set-route! ["companies" "buffer"] {:slug "buffer"})

(defn challenges-data []
  (:challenges company-data/finances))

(defn finances-data []
  (:finances company-data/finances))

(defcard dc-simple-section
  "Simple section component"
  (dc/om-root simple-section {})
  {:section :challenges
   :section-data (challenges-data)
   :read-only false}
  {:inspect-data true :history true})

(defcard dc-finances
  "Finances component"
  (dc/om-root finances {})
  {:section :finances
   :section-data (finances-data)
   :read-only false
   :editable-click-callback #()}
  {:inspect-data true :history true})

(def growth-section {
  :section :growth
  :title "Key metrics"
  :metrics [{
    :slug "test-metric"
    :name "Test metric"
    :interval "monthly"
    :unit "USD"
    :target "high"
  }]
  :data [{
    :period "10-2015"
    :slug "test-metric"
    :value 12345
  } {
    :period "11-2015"
    :slug "test-metric"
    :value 13456
  }]
  :author {
    :user-id "123456"
    :image "http://www.emoticonswallpapers.com/avatar/cartoons/Wiley-Coyote-Dazed.jpg"
    :name "Wile E. Coyote"
  }
  :updated-at "2015-09-14T20:49:19Z"
  :notes {
    :body "Notes body"
    :author {
      :user-id "123456"
      :image "http://www.emoticonswallpapers.com/avatar/cartoons/Wiley-Coyote-Dazed.jpg"
      :name "Wile E. Coyote"
    }
    :updated-at "2015-09-14T20:49:19Z"
  }
})

(defcard dc-growth
  "Growth component"
  (dc/om-root growth {})
  {:section :growth
   :section-data growth-section
   :read-only false
   :editable-click-callback #()}
  {:inspect-data true :history true})