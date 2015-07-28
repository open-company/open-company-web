(ns test.open-company-web.components.currency-picker
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :currency "USD"
})

(deftest test-currency-picker-component
  (testing "Currency picker component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root currency-picker app-state {:target c})
          currency-picker-node (sel1 c [:#currency-dropdown])]
      (is (not (nil? currency-picker-node)))
      (tu/unmount! c))))
