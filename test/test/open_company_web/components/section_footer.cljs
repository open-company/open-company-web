(ns test.open-company-web.components.section-footer
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.section-footer :refer (section-footer)]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :edting false
  :cancel-cb #()
  :save-cb #()
  :is-new-section false
  :save-disabled true
})

(deftest test-editable-title-component
  (testing "Section footer component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root section-footer app-state {:target c})
          save-button (sel1 c [:button.oc-success])
          cancel-button (sel1 c [:button.oc-cancel])]
      (is (not (nil? save-button)))
      (is (= (.-innerHTML save-button) "SAVE"))
      (is (not (nil? cancel-button)))
      (is (= (.-innerHTML cancel-button) "CANCEL"))
      (tu/unmount! c)))
  (testing "Section footer component for new section"
    (let [c (tu/new-container!)
          app-state (atom (merge test-atom {:save-disabled true
                                            :is-new-section true}))
          _ (om/root section-footer app-state {:target c})
          save-button (sel1 c [:button.oc-success])
          cancel-button (sel1 c [:button.oc-cancel])]
      (is (not (nil? save-button)))
      (is (true? (.-disabled save-button)))
      (is (= (.-innerHTML save-button) "CREATE"))
      (is (not (nil? cancel-button)))
      (is (= (.-innerHTML cancel-button) "DELETE"))
      (tu/unmount! c))))