(ns open-company-web.lib.devcards
  (:require-macros [devcards.core :as dc :refer (defcard)])
  (:require [devcards.core :as dc]
            [open-company-web.components.simple-section :refer (simple-section)]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.data.company :as company-data]
            [open-company-web.router :as router]))

(router/set-route! ["companies" "buffer"] {:slug "buffer"})

(def fixed-company-data (utils/fix-sections company-data/company))

(def challenges-data (:challenges fixed-company-data))

(def finances-data (:finances fixed-company-data))

(def growth-data (:growth fixed-company-data))

(defcard text-rw-section
  "A read/write text section component"
  (dc/om-root simple-section {})
  {
    :section :challenges
    :section-data challenges-data
    :read-only false
  }
  {
    :inspect-data false
    :history true
  })

(defcard text-ro-section
  "A read-only text section component"
  (dc/om-root simple-section {})
  {
    :section :challenges
    :section-data challenges-data
    :read-only true
  }
  {
    :inspect-data false
    :history true
  })

(defcard finances-rw
  "A read/write finances component"
  (dc/om-root finances {})
  {
    :section :finances
    :section-data finances-data
    :read-only false
    :editable-click-callback #()
  }
  {
    :inspect-data true
    :history true
  })

(defcard finances-ro
  "A read/write finances component"
  (dc/om-root finances {})
  {
    :section :finances
    :section-data finances-data
    :read-only true
    :editable-click-callback #()
  }
  {
    :inspect-data true
    :history true
  })

(defcard growth-rw
  "A read/write growth component"
  (dc/om-root growth {})
  {
    :section :growth
    :section-data growth-data
    :read-only false
    :editable-click-callback #()
  }
  {
    :inspect-data true
    :history true
  })

(defcard growth-ro
  "A read/write growth component"
  (dc/om-root growth {})
  {
    :section :growth
    :section-data growth-data
    :read-only true
    :editable-click-callback #()
  }
  {
    :inspect-data true
    :history true
  })