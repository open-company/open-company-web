(ns test.test-ui-components
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.ui.link]
            [test.open-company-web.components.ui.login-button]
            [test.open-company-web.components.ui.cell]
            [test.open-company-web.components.ui.utility-components]
            [test.open-company-web.components.section-footer]
            [test.open-company-web.components.ui.popover]
            [test.open-company-web.components.ui.headline]
            [test.open-company-web.components.ui.loading]
            [test.open-company-web.lib.utils]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests
  'test.open-company-web.components.ui.link
  'test.open-company-web.components.ui.login-button
  'test.open-company-web.components.ui.cell
  'test.open-company-web.components.ui.utility-components
  'test.open-company-web.components.section-footer
  'test.open-company-web.components.ui.popover
  'test.open-company-web.components.ui.headline
  'test.open-company-web.components.ui.loading
  'test.open-company-web.lib.utils)
