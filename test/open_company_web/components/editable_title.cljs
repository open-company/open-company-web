(ns test.open-company-web.components.editable-title
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.editable-title :refer [editable-title]]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section-data {
    :title "Test editable title"
  }
})

(deftest test-editable-title-component
  (testing "Editable title component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root editable-title app-state {:target c})
          h2-node (sel1 c [:h2.editable-title])]
      (is (not (nil? h2-node)))
      (sim/click h2-node nil)
      (om.core/render-all)
      (let [input-node (sel1 c [:input])]
        (is (not (nil? input-node)))
        (sim/change input-node {:target {:value "Another value"}})
        (sim/key-down input-node {:key "Enter"})
        (om.core/render-all)
        (let [h2-node-after (sel1 c [:h2.editable-title])]
          (is (not (nil? h2-node-after)))
          (is (= (.-innerHTML h2-node-after) "Another value"))))
      (tu/unmount! c))))