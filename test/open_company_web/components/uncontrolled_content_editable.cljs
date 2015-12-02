(ns test.open-company-web.components.uncontrolled-content-editable
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.uncontrolled-content-editable :refer (uncontrolled-content-editable)]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :html "test UCE body"
  :editing false
  :body-counter 0
  :class "rich-editor"
  :placeholder "Placeholder"
})

(deftest test-uncontrolled-content-editable-component
  (testing "Uncontrolled content editable component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root uncontrolled-content-editable app-state {:target c})
          div (sel1 c [:div.rich-editor])]
      (is (not (nil? div)))
      (is (= (.-innerHTML div) (:html test-atom)))
      (tu/unmount! c))))