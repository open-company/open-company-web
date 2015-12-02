(ns test.open-company-web.components.editable-title
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :editing false
  :read-only false
  :title "Test editable title"
  :section "test"
  :start-editing-cb #()
  :change-cb #()
  :cancel-cb #()
  :cancel-if-needed-cb #()
  :save-cb #()
})

(deftest test-editable-title-component
  (testing "Editable title component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root editable-title app-state {:target c})
          textarea-node (sel1 c [:textarea.editable-title])]
      (is (not (nil? textarea-node)))
      (tu/unmount! c))))