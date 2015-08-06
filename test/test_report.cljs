(ns test.test-report
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.report]
            [test.open-company-web.components.report-line]
            [test.open-company-web.components.compensation]
            [test.open-company-web.components.finances]
            [test.open-company-web.components.headcount]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests 
  'test.open-company-web.components.report
  'test.open-company-web.components.report-line
  'test.open-company-web.components.finances
  'test.open-company-web.components.headcount
  'test.open-company-web.components.compensation)