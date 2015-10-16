(ns test.open-company-web.components.finances.cash-flow
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances.cash-flow :refer [cash-flow]]
              [om.dom :as dom :include-macros true]
              [test.open-company-web.data.finances :refer [finances]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(deftest test-cash-flow-component
  (testing "Cash flow component"
    (let [c (tu/new-container!)
          app-state (atom finances)
          _ (om/root cash-flow app-state {:target c})
          cash-flow-node (sel1 c [:div.section.cash-flow])]
      (is (not (nil? cash-flow-node)))
      (tu/unmount! c))))