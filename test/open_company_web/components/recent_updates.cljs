(ns test.open-company-web.components.recent-updates
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.recent-updates :refer [recent-updates]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-recent-updates-component
  (testing "Recent updates component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root recent-updates app-state {:target c})
          recent-updates-node (sel1 c [:div.recent-updates])]
      (is (not (nil? recent-updates-node)))
      (tu/unmount! c))))