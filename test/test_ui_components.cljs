(ns test.test-ui-components
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.currency-picker]
            [test.open-company-web.components.link]
            [test.open-company-web.components.charts]
            [test.open-company-web.components.user-selector]
            [test.open-company-web.components.editable-title]
            [test.open-company-web.components.cell]
            [test.open-company-web.components.rich-editor]
            [test.open-company-web.components.simple-section]
            [test.open-company-web.components.update-footer]
            [test.open-company-web.components.utility-components]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests
  'test.open-company-web.components.currency-picker
  'test.open-company-web.components.link
  'test.open-company-web.components.charts
  'test.open-company-web.components.user-selector
  'test.open-company-web.components.editable-title
  'test.open-company-web.components.cell
  'test.open-company-web.components.rich-editor
  'test.open-company-web.components.simple-section
  'test.open-company-web.components.update-footer
  'test.open-company-web.components.utility-components)