(ns test.open-company-web.components.report.finances-section
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
          [cljs-react-test.simulate :as sim]
          [cljs-react-test.utils :as tu]
          [om.core :as om :include-macros true]
          [dommy.core :as dommy :refer-macros [sel1 sel]]
          [open-company-web.components.report.finances-section :refer [finances-section]]
          [om.dom :as dom :include-macros true]
          [open-company-web.lib.utils :refer [thousands-separator]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :label "Cash on hand"
  :key-name :cash
  :help-block "Cash and cash equivalents"
  :prefix "$"
  :placeholder "US Dollars"
  :cursor {
    :cash 10000
  }
})

(deftest test-finances-section-component
  (testing "Finances section component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances-section app-state {:target c})
          finances-node (sel1 c [:input#cash])]
      (is (not (nil? finances-node)))
      (is (= (.-value finances-node) (thousands-separator (:cash (:cursor test-atom)))))
      (tu/unmount! c))))