(ns test.open-company-web.components.headcount
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.headcount :refer [headcount readonly-headcount]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :headcount {
    :founders 2
    :executives 1
    :pt-employees 3
    :ft-contractors 4
  }
})

(deftest test-headcount-component
  (testing "Headcount component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root headcount app-state {:target c})
          headcount-node (sel1 c [:.report-list])]
      (is (not (nil? headcount-node)))
      (tu/unmount! c))))

(deftest test-readonly-headcount-component
  (testing "Readonly headcount component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root readonly-headcount app-state {:target c})
          headcount-node (sel1 c [:.report-list])]
      (is (not (nil? headcount-node)))
      (tu/unmount! c))))