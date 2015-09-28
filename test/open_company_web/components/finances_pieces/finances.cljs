(ns test.open-company-web.components.finances-pieces.finances
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances-pieces.finances :refer [finances finances-edit]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.finances :as finances-data]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:buffer finances-data/finances})


(deftest test-finances-component
  (testing "Finances component"
    (router/set-route! ["companies" "buffer" "finances" "cash"]
                       {:slug "buffer" :section "finances" :tab "cash"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances app-state {:target c})
          finances-node (sel1 c [:div.finances])]
      (is (not (nil? finances-node)))
      (tu/unmount! c))))

(deftest test-finances-edit-component
  (testing "Finances edit component"
    (router/set-route! ["companies" "buffer" "finances" "edit"]
                       {:slug "buffer" :section "finances" :tab "edit"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances-edit app-state {:target c})
          finances-node (sel1 c [:div.finances])]
      (is (not (nil? finances-node)))
      (tu/unmount! c))))