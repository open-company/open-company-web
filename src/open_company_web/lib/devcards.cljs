(ns open-company-web.lib.devcards
  (:require-macros [devcards.core :as dc :refer (defcard)])
  (:require [devcards.core :as dc]
            [open-company-web.components.simple-section :refer (simple-section)]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [open-company-web.components.section-selector :refer (section-selector)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.data.company :as company-data]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]))

(router/set-route! ["companies" "buffer"] {:slug "buffer"})

(def fixed-company-data (utils/fix-sections company-data/company))

(def values-data (:values fixed-company-data))

(def finances-data (:finances fixed-company-data))

(def growth-data (:growth fixed-company-data))

(reset! caches/revisions company-data/revisions)

(defcard text-section
  "A text section component"
  (dc/om-root section-selector {})
  {
    :section :values
    :section-data values-data
  }
  {
    :inspect-data true
    :history true
  })

(defcard finances
  "A finances component"
  (dc/om-root section-selector {})
  {
    :section :finances
    :section-data finances-data
    :currency (:currency fixed-company-data)
  }
  {
    :inspect-data true
    :history true
  })

(defcard growth
  "A growth component"
  (dc/om-root section-selector {})
  {
    :section :growth
    :section-data growth-data
  }
  {
    :inspect-data true
    :history true
  })