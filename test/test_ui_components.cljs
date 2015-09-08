(ns test.test-ui-components
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.comment]
            [test.open-company-web.components.currency-picker]
            [test.open-company-web.components.link]
            [test.open-company-web.components.pie-chart]
            [test.open-company-web.components.user-selector]
            [test.open-company-web.components.login]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests 
  'test.open-company-web.components.comment
  'test.open-company-web.components.currency-picker
  'test.open-company-web.components.link
  'test.open-company-web.components.pie-chart
  'test.open-company-web.components.user-selector
  'test.open-company-web.components.login)