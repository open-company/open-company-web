(ns test.open-company-web.lib.oc-lib
  (:require [cljs.test :refer-macros [deftest async testing is]]
            [open-company-web.lib.oc-lib :as oc-lib]))

(deftest test-oc-lib
  "About returning contiguous periods"

  (testing "degenerate cases"
    (is (thrown? js/Error (oc-lib/contiguous [] :yearly)))
    (is (thrown? js/Error (oc-lib/contiguous [] 42)))
    (is (thrown? js/Error (oc-lib/contiguous [] "foos")))
    (is (thrown? js/Error (oc-lib/contiguous {} :weekly)))
    (is (thrown? js/Error (oc-lib/contiguous "" :monthly))))

  (testing "when periods are weeks"
    (is (= (oc-lib/contiguous [] "weekly") []))
    (is (= (oc-lib/contiguous [] :weekly) []))
    (is (= (oc-lib/contiguous ["2016-10-10"] :weekly) ["2016-10-10"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-10-03"] :weekly) ["2016-10-10" "2016-10-03"]))
    (is (= (oc-lib/contiguous ["2016-10-03" "2016-10-10"] "weekly") ["2016-10-10" "2016-10-03"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-09-26"] :weekly) ["2016-10-10"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-10-03" "2016-09-19" "2016-09-26"] :weekly)
           ["2016-10-10" "2016-10-03" "2016-09-26" "2016-09-19"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-10-03" "2016-09-12" "2016-09-26"] :weekly)
           ["2016-10-10" "2016-10-03" "2016-09-26"]))
    (is (= (oc-lib/contiguous ["2016-10-17" "2016-10-03" "2016-09-12" "2016-09-26"] :weekly) ["2016-10-17"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-10-03" "2016-09-12" "2016-09-19"] :weekly)
           ["2016-10-10" "2016-10-03"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2016-09-12" "2016-09-26" "2016-08-29"] :weekly) ["2016-10-10"]))
    (is (= (oc-lib/contiguous ["2016-10-10" "2015-10-03" "2013-09-19" "2014-09-26"] :weekly) ["2016-10-10"])))

  (testing "when periods are months"
    (is (= (oc-lib/contiguous [] "monthly") []))
    (is (= (oc-lib/contiguous [] :monthly) []))
    (is (= (oc-lib/contiguous ["2016-10"]) ["2016-10"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-09"] :monthly) ["2016-10" "2016-09"]))
    (is (= (oc-lib/contiguous ["2016-09" "2016-10"] "monthly") ["2016-10" "2016-09"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-08"] :monthly) ["2016-10"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-09" "2016-11" "2016-08"])
           ["2016-11" "2016-10" "2016-09" "2016-08"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-09" "2016-11" "2016-07"])
           ["2016-11" "2016-10" "2016-09"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-09" "2016-12" "2016-08"]) ["2016-12"]))
    (is (= (oc-lib/contiguous ["2016-10" "2016-07" "2016-11" "2016-08"]) ["2016-11" "2016-10"]))
    (is (= (oc-lib/contiguous ["2016-12" "2016-06" "2016-08" "2016-10"]) ["2016-12"]))
    (is (= (oc-lib/contiguous ["2014-02" "2013-03" "2016-05" "2015-04"]) ["2016-05"])))

  (testing "when periods are quarters"
    (is (= (oc-lib/contiguous [] "quarterly") []))
    (is (= (oc-lib/contiguous [] :quarterly) []))
    (is (= (oc-lib/contiguous ["2016-07"] :quarterly) ["2016-07"]))
    (is (= (oc-lib/contiguous ["2016-07" "2016-04"] :quarterly) ["2016-07" "2016-04"]))
    (is (= (oc-lib/contiguous ["2016-04" "2016-07"] "quarterly") ["2016-07" "2016-04"]))
    (is (= (oc-lib/contiguous ["2016-07" "2016-01"] :quarterly) ["2016-07"]))
    (is (= (oc-lib/contiguous ["2016-04" "2016-07" "2016-01" "2016-10"] :quarterly)
           ["2016-10" "2016-07" "2016-04" "2016-01"]))
    (is (= (oc-lib/contiguous ["2016-04" "2016-07" "2015-10" "2016-10"] :quarterly)
           ["2016-10" "2016-07" "2016-04"]))
    (is (= (oc-lib/contiguous ["2016-04" "2016-01" "2015-10" "2016-10"] :quarterly) ["2016-10"]))
    (is (= (oc-lib/contiguous ["2015-10" "2016-01" "2016-07" "2016-10"] :quarterly) ["2016-10" "2016-07"]))
    (is (= (oc-lib/contiguous ["2016-04" "2015-04" "2015-10" "2016-10"] :quarterly) ["2016-10"]))
    (is (= (oc-lib/contiguous ["2014-04" "2015-07" "2013-01" "2016-10"] :quarterly) ["2016-10"])))
  )