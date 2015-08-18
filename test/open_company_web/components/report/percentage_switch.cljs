(ns test.open-company-web.components.report.percentage-switch
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
          [cljs-react-test.simulate :as sim]
          [cljs-react-test.utils :as tu]
          [om.core :as om :include-macros true]
          [dommy.core :as dommy :refer-macros [sel1 sel]]
          [open-company-web.components.report.percentage-switch :refer [percentage-switch]]
          [om.dom :as dom :include-macros true]
          [open-company-web.lib.utils :refer [thousands-separator]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :compensation {
    :percentage true
  }
  :currency "USD"
})

(deftest test-percentage-switch-component
  (testing "Percentage switch component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root percentage-switch app-state {:target c})
          percentage-active-node (sel1 c [:button.percentage.active])]
      (is (not (nil? percentage-active-node)))
      (tu/unmount! c))))