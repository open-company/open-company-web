(ns test.open-company-web.components.manage-topic
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.manage-topic :refer [manage-topic]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-manage-topic-component
  (testing "Manage topic component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root manage-topic app-state {:target c})
          manage-topic-node (sel1 c [:div.manage-topic])]
      (is (not (nil? manage-topic-node)))
      (tu/unmount! c))))