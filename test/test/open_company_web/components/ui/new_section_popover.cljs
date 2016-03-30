(ns test.open-company-web.components.ui.new-section-popover
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.ui.new-section-popover :refer [new-section-popover]]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-new-section-popover-component
  (testing "New section popover component"
    (let [c (tu/new-container!)
          app-state (atom test-atom) 
          _ (om/root new-section-popover app-state {:target c})
          new-section-popover-node (sel1 c [:div.new-section-popover])]
      (is (not (nil? new-section-popover-node)))
      (tu/unmount! c))))