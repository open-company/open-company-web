(ns test.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.comment]
            [test.open-company-web.components.compensation]
            [test.open-company-web.components.currency-picker]
            [test.open-company-web.components.finances]
            [test.open-company-web.components.headcount]
            [test.open-company-web.components.link]
            [test.open-company-web.components.list-companies]
            [test.open-company-web.components.page]
            [test.open-company-web.components.page-not-found]
            [test.open-company-web.components.pie-chart]
            [test.open-company-web.components.report]
            [test.open-company-web.components.report-line]
            [test.open-company-web.components.navbar]
            [test.open-company-web.components.sidebar]))

(enable-console-print!)

(doo-tests 'test.open-company-web.components.comment
           'test.open-company-web.components.currency-picker
           'test.open-company-web.components.link
           'test.open-company-web.components.list-companies
           'test.open-company-web.components.pie-chart
           'test.open-company-web.components.report
           'test.open-company-web.components.report-line
           'test.open-company-web.components.finances
           'test.open-company-web.components.headcount
           'test.open-company-web.components.compensation
           'test.open-company-web.components.page-not-found
           'test.open-company-web.components.page
           'test.open-company-web.components.navbar
           'test.open-company-web.components.sidebar)
