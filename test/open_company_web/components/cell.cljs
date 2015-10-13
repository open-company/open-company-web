(ns test.open-company-web.components.cell
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.cell :refer [cell]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :value 1234
  :placeholder "placeholder"
  :prefix "$"
  :cell-state :new
  :draft-cb #()})


(deftest test-cell-component
  (testing "Cell component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root cell app-state {:target c})
          new-node (sel1 c [:div.state-new])
          new-span (sel1 c [:span])]
      (is (not (nil? new-node)))
      (is (not (nil? new-span)))
      (is (= (.-innerHTML new-span) "placeholder"))
      (sim/click new-node nil)
      (om.core/render-all)
      (let [editing-node (sel1 c [:input])]
        (is (not (nil? editing-node)))
        (is (= (.-value editing-node) "1234"))
        (sim/change editing-node {:target {:value "54321"}})
        (sim/key-down editing-node {:key "Enter"})
        (om.core/render-all)
        (let [draft-node (sel1 c [:div.state-draft])
              draft-span (sel1 c [:span])]
          (is (not (nil? draft-node)))
          (is (not (nil? draft-span)))))
      (tu/unmount! c))))