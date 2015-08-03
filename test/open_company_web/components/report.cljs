(ns test.open-company-web.components.report
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.report :refer [report]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-symbol "OPEN")
(def year "2015")
(def period "Q4")

(def test-atom {
  :symbol company-symbol
  :year year
  :period period
  :OPEN {
    :report-OPEN-2014-Q4 {

    }
  }
})

(deftest test-report-component
  (testing "Report component"
    (router/set-route! [company-symbol year period] {:symbol company-symbol :year year :period period})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root report app-state {:target c})
          report-node (sel1 c [:div.report-container])]
      (is (not (nil? report-node)))
      (tu/unmount! c))))