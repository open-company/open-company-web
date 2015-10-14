(ns test.test-navigation
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.page]
            [test.open-company-web.components.page-not-found]
            [test.open-company-web.components.list-companies]
            [test.open-company-web.components.navbar]
            [test.open-company-web.components.sidebar]
            [test.open-company-web.components.section-selector]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests 
  'test.open-company-web.components.page
  'test.open-company-web.components.page-not-found
  'test.open-company-web.components.list-companies
  'test.open-company-web.components.navbar
  'test.open-company-web.components.sidebar
  'test.open-company-web.components.section-selector)