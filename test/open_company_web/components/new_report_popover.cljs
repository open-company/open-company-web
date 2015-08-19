(ns test.open-company-web.components.new-report-popover
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.new-report-popover :refer [new-report-popover]]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  ; basics
  :offsetTop 0
  :offsetLeft 0
})

(deftest test-new-report-popover-component
  (testing "New report popover component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root new-report-popover app-state {:target c})
          new-report-year-node (sel1 c [:select#new-report-year])
          new-report-month-node (sel1 c [:select#new-report-month])]
      (is (not (nil? new-report-year-node)))
      (is (not (nil? new-report-month-node)))
      (tu/unmount! c))))