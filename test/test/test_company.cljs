(ns test.test-company
  (:require [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.company-profile]
            [test.open-company-web.components.company-editor]
            [test.open-company-web.components.ui.revisions-navigator]
            [test.open-company-web.components.ui.company-avatar]
            [test.open-company-web.components.ui.user-avatar]
            [test.open-company-web.components.user-profile]
            [test.open-company-web.components.finances.cash]
            [test.open-company-web.components.finances.cash-flow]
            [test.open-company-web.components.finances.costs]
            [test.open-company-web.components.finances.runway]
            [test.open-company-web.components.finances.finances-edit]
            [test.open-company-web.components.growth.growth-metric]
            [test.open-company-web.components.growth.growth-edit]
            [test.open-company-web.components.growth.growth-metric-edit]
            [test.open-company-web.components.company-dashboard]
            [test.open-company-web.components.company-header]
            [test.open-company-web.components.category-nav]
            [test.open-company-web.components.topic-list]
            [test.open-company-web.components.topic]
            [test.open-company-web.components.edit-topic]
            [test.open-company-web.components.topic-body]
            [test.open-company-web.components.expanded-topic]
            [test.open-company-web.components.fullscreen-topic-edit]))

(enable-console-print!)

;; Break tests into somewhat logical groupings of 1/2 dozen or less tests so
;; we don't run out of memory on CI server

(doo-tests
  'test.open-company-web.components.company-profile
  'test.open-company-web.components.company-editor
  'test.open-company-web.components.ui.revisions-navigator
  'test.open-company-web.components.ui.company-avatar
  'test.open-company-web.components.ui.user-avatar
  'test.open-company-web.components.user-profile
  'test.open-company-web.components.finances.cash
  'test.open-company-web.components.finances.cash-flow
  'test.open-company-web.components.finances.costs
  'test.open-company-web.components.finances.runway
  'test.open-company-web.components.finances.finances-edit
  'test.open-company-web.components.growth.growth-metric
  'test.open-company-web.components.growth.growth-edit
  'test.open-company-web.components.growth.growth-metric-edit
  'test.open-company-web.components.company-dashboard
  'test.open-company-web.components.company-header
  'test.open-company-web.components.category-nav
  'test.open-company-web.components.topic-list
  'test.open-company-web.components.topic
  'test.open-company-web.components.edit-topic
  'test.open-company-web.components.topic-body
  'test.open-company-web.components.expanded-topic
  'test.open-company-web.components.fullscreen-topic-edit)