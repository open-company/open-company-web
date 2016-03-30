(ns test.open-company-web.components.popover
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.popover :refer [popover]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-all-sections-component
  (testing "All sections component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root popover app-state {:target c})
          popover-node (sel1 c [:div.oc-popover])]
      (is (not (nil? popover-node)))
      (tu/unmount! c))))