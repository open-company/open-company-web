(ns test.open-company-web.components.charts
    (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros (sel1 sel)]
              [open-company-web.components.charts :refer (column-chart)]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-column-chart-component
  (testing "Column chart component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root column-chart app-state {:target c})
          chart-node (sel1 c [:div.column-chart.chart-container])]
      (is (not (nil? chart-node)))
      (tu/unmount! c))))