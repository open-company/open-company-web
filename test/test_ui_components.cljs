(ns test.test-ui-components
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.ui.currency-picker]
            [test.open-company-web.components.ui.link]
            [test.open-company-web.components.ui.charts]
            [test.open-company-web.components.ui.user-selector]
            [test.open-company-web.components.login]
            [test.open-company-web.components.ui.login-button]
            [test.open-company-web.components.ui.editable-title]
            [test.open-company-web.components.ui.cell]
            [test.open-company-web.components.ui.rich-editor]
            [test.open-company-web.components.update-footer]
            [test.open-company-web.components.ui.utility-components]
            [test.open-company-web.components.section-footer]
            [test.open-company-web.components.ui.uncontrolled-content-editable]
            [test.open-company-web.components.popover]
            [test.open-company-web.components.ui.headline]
            [test.open-company-web.components.ui.sortable-list]
            [test.open-company-web.components.ui.manage-topics]
            [test.open-company-web.components.ui.loading]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests
  'test.open-company-web.components.ui.currency-picker
  'test.open-company-web.components.ui.link
  'test.open-company-web.components.ui.charts
  'test.open-company-web.components.ui.user-selector
  'test.open-company-web.components.login
  'test.open-company-web.components.ui.login-button
  'test.open-company-web.components.ui.editable-title
  'test.open-company-web.components.ui.cell
  'test.open-company-web.components.ui.rich-editor
  'test.open-company-web.components.update-footer
  'test.open-company-web.components.ui.utility-components
  'test.open-company-web.components.section-footer
  'test.open-company-web.components.ui.uncontrolled-content-editable
  'test.open-company-web.components.popover
  'test.open-company-web.components.ui.headline
  'test.open-company-web.components.ui.sortable-list
  'test.open-company-web.components.ui.manage-topics
  'test.open-company-web.components.ui.loading)
