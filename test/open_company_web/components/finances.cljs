(ns test.open-company-web.components.finances
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances :refer [finances readonly-finances calc-run-away]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def base-atom {
  :period "M1"
  :currency "USD"})

(def positive-atom (merge base-atom {
  :finances {
    :cash 1000
    :revenue 200
    :costs 100}}))

(def break-even-atom (merge base-atom {
  :finances {
    :cash 1000
    :revenue 100
    :costs 100}}))

(def negative-atom (merge base-atom {
  :finances {
    :cash 1000
    :revenue 100
    :costs 200}}))

(deftest test-calc-run-away
  (testing "Run away calculation"
    (is (calc-run-away "M1" 1000 100) 300)
    (is (calc-run-away "Q1" 1000 100) 1200)))

(deftest test-finances-component
  (testing "Finances component | Positive run-away"
    (let [c (tu/new-container!)
          app-state (atom positive-atom)
          _ (om/root finances app-state {:target c})
          finances-node (sel1 c [:div.finances])
          run-away (sel1 c [:label.run-away])]
      (is (not (nil? finances-node)))
      (is (nil? run-away))
      (tu/unmount! c)))

  (testing "Finances component | Break even run-away"
    (let [c (tu/new-container!)
          app-state (atom break-even-atom)
          _ (om/root finances app-state {:target c})
          finances-node (sel1 c [:div.finances])
          run-away (sel1 c [:label.run-away])]
      (is (not (nil? finances-node)))
      (is (nil? run-away))
      (tu/unmount! c)))

  (testing "Finances component | Negative run-away"
    (let [c (tu/new-container!)
          app-state (atom negative-atom)
          _ (om/root finances app-state {:target c})
          finances-node (sel1 c [:div.finances])
          run-away (sel1 c [:label.run-away])]
      (is (not (nil? finances-node)))
      (is (not (nil? run-away)))
      (tu/unmount! c))))

(deftest test-readonly-finances-component
  (testing "Readonly finances component"
    (let [c (tu/new-container!)
          app-state (atom positive-atom)
          _ (om/root readonly-finances app-state {:target c})
          finances-node (sel1 c [:.report-list])]
      (is (not (nil? finances-node)))
      (tu/unmount! c))))