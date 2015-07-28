(ns test.open-company-web.components.finances
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances :refer [finances readonly-finances]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :finances {
    :cash 12000
    :ravenue 2000
    :costs 1000
  }
})

(deftest test-finances-component
  (testing "Finances component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances app-state {:target c})
          finances-node (sel1 c [:.report-list])]
      (is (not (nil? finances-node)))
      (tu/unmount! c))))

(deftest test-readonly-finances-component
  (testing "Readonly finances component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root readonly-finances app-state {:target c})
          finances-node (sel1 c [:.report-list])]
      (is (not (nil? finances-node)))
      (tu/unmount! c))))
