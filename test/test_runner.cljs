(ns test.test-runner
  (:require [cljs.test :as tt]
            [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.components.comment]
            [test.open-company-web.components.compensation]))

(enable-console-print!)

(doo-tests 'test.open-company-web.components.comment
           'test.open-company-web.components.compensation)
