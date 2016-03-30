(ns test.open-company-web.lib.utils
  (:require [cljs.test :refer-macros [deftest async testing is]]
            [open-company-web.lib.utils :as utils]))

(deftest test-utils
  (testing "Metric prefix functions"
    (is (= "1"     (utils/with-metric-prefix 1.00123456)))
    (is (= "1.2"   (utils/with-metric-prefix 1.20123456)))
    (is (= "1.23"  (utils/with-metric-prefix 1.23456789)))
    (is (= "10"    (utils/with-metric-prefix 10.0123456)))
    (is (= "10.2"  (utils/with-metric-prefix 10.2345678)))
    (is (= "100"   (utils/with-metric-prefix 100.000000)))
    (is (= "100"   (utils/with-metric-prefix 100.123456)))
    (is (= "1K"    (utils/with-metric-prefix 1000.12345)))
    (is (= "1.2K"  (utils/with-metric-prefix 1200.12345)))
    (is (= "1.23K" (utils/with-metric-prefix 1230.12345)))
    (is (= "10K"   (utils/with-metric-prefix 10000.3456)))
    (is (= "10.1K" (utils/with-metric-prefix 10120.3456)))
    (is (= "1M"    (utils/with-metric-prefix 1000000.12)))
    (is (= "1.2M"  (utils/with-metric-prefix 1200000.12)))
    (is (= "1.23M" (utils/with-metric-prefix 1230000.12)))
    (is (= "10M"   (utils/with-metric-prefix 10000000.1)))
    (is (= "10.1M" (utils/with-metric-prefix 10123456.7)))
    (is (= "100M"  (utils/with-metric-prefix 100123456)))))