(ns test.open-company-web.components.compensation
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.compensation :refer [compensation readonly-compensation]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :headcount {}
  :compensation {}
  :currency "USD"
})

(deftest test-compensation-component
  (testing "Compensation component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root compensation app-state {:target c})
          container-node (sel1 c [:div.compensation])]
      (is (not (nil? container-node)))
      (tu/unmount! c))))

(deftest test-readonly-compensation-component
  (testing "Readonly compensation component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root readonly-compensation app-state {:target c})
          container-node (sel1 c [:.report-list])]
      (is (not (nil? container-node)))
      (tu/unmount! c))))