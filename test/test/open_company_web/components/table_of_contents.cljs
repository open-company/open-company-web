(ns test.open-company-web.components.table-of-contents
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.table-of-contents :refer [table-of-contents]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :sections {
    :test_a ["test_a_1" "test_a_2"]
    :test_b ["test_b_1" "test_b_2"]}})

(deftest test-table-of-contents-component
  (testing "Table of contents component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root table-of-contents app-state {:target c})
          table-of-contents-node (sel1 c [:div.table-of-contents])]
      (is (not (nil? table-of-contents-node)))
      (tu/unmount! c))))