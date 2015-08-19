(ns test.open-company-web.components.report.headcount-section
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
          [cljs-react-test.simulate :as sim]
          [cljs-react-test.utils :as tu]
          [om.core :as om :include-macros true]
          [dommy.core :as dommy :refer-macros [sel1 sel]]
          [open-company-web.components.report.headcount-section :refer [headcount-section]]
          [om.dom :as dom :include-macros true]
          [open-company-web.lib.utils :refer [thousands-separator]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :key-name :founders
  :label "Founders"
  :help-block "Currently employed founders"
  :cursor {
    :founders 2
  }
})

(deftest test-headcount-section-component
  (testing "Headcount section component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root headcount-section app-state {:target c})
          founders-node (sel1 c [:input#founders])]
      (is (not (nil? founders-node)))
      (is (= (.-value founders-node) (thousands-separator (:founders (:cursor test-atom)))))
      (tu/unmount! c))))