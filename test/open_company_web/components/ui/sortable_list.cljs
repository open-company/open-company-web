(ns test.open-company-web.components.ui.sortable-list
    (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros (sel1 sel)]
              [open-company-web.components.ui.sortable-list :refer (sortable-list)]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(defn temp-list-node []
  (om/component
    (dom/div {} "Test")))

(def test-atom {
  :sort ["a" "b" "c" "d"]
  :items {
    :a {:a 1}
    :b {:b 2}
    :c {:c 3}
    :d {:d 4}}
  :item temp-list-node})

(deftest test-sortable-list-component
  (testing "Sortable list component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root sortable-list app-state {:target c})
          sortable-list-node (sel1 c [:ul.sortable])
          list-nodes (sel sortable-list-node [:li])]
      (is (not (nil? sortable-list-node)))
      (is (= (count list-nodes) (count (:sort test-atom))))
      (tu/unmount! c))))