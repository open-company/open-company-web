(ns test.open-company-web.lib.utils
  (:require [cljs.test :refer-macros [deftest async testing is]]
            [open-company-web.lib.utils :as utils]))

(def values {
  "1"      1.0012345
  "1.1"    1.1012345
  "10"     10.001234
  "10.1"   10.101234
  "100"    100.00123
  "100.1"  100.10123
  "1K"     1001.1234
  "1.1K"   1101.1234
  "1M"     1001234
  "1.1M"   1101234
  "10M"    10001234
  "10.1M"  10101234
})

(deftest test-utils
  (testing "Metric prefix functions"
    (doseq [v (vals values)]
      (is (= v (get values (utils/with-metric-prefix v)))))))