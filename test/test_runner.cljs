(ns test.test-runner
  (:require [cljs.test :as tt]
            [doo.runner :refer-macros [doo-tests]]
            [test.open-company-web.tests]))

(doo-tests 'test.open-company-web.tests)
