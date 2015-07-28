(ns test.open-company-web.components.report-line
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.report-line :refer [report-editable-line report-line]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-report-editable-line-component
  (testing "Report editable line component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root report-editable-line app-state {:target c})
          report-node (sel1 c [:div.report-editable-line])]
      (is (not (nil? report-node)))
      (tu/unmount! c))))

(deftest test-report-line-component
  (testing "Report line component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root report-line app-state {:target c})
          report-node (sel1 c [:span.report-line])]
      (is (not (nil? report-node)))
      (tu/unmount! c))))
