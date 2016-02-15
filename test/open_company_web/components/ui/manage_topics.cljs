(ns test.open-company-web.components.ui.manage-topics
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.ui.manage-topics :refer [manage-topics]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-manage-topics-component
  (testing "Manage topics component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root manage-topics app-state {:target c})
          manage-topics-node (sel1 c [:div.manage-topics])]
      (is (not (nil? manage-topics-node)))
      (tu/unmount! c))))