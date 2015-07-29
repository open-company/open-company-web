(ns test.open-company-web.components.report
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.report :refer [report]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :ticker "OPEN"
  :year "2015"
  :period "Q4"
  :OPEN {
    :report-OPEN-2014-Q4 {

    }
  }
})

(deftest test-report-component
  (testing "Report component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root report app-state {:target c})
          report-node (sel1 c [:div.report-container])]
      (is (not (nil? report-node)))
      (tu/unmount! c))))