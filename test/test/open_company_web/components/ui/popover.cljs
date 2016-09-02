(ns test.open-company-web.components.ui.popover
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.utils :as tu]
              [rum.core :as rum]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.ui.popover :refer [popover]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-data {
  :container-id "test-popover"
  :message "Test."
  :success-title "Test"
  :success-cb #(prn "test")})

(deftest test-popover-component
  (testing "Popover component"
    (let [c (tu/new-container!)
          app-state (atom test-data)
          _ (rum/mount (popover test-data) c)
          popover-node (sel1 c [:div.oc-popover])]
      (is (not (nil? popover-node)))
      (rum/unmount c))))