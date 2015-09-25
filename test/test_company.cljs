(ns test.test-company
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.profile]
            [test.open-company-web.components.report.percentage-switch]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests 
  'test.open-company-web.components.profile
  'test.open-company-web.components.report.percentage-switch)