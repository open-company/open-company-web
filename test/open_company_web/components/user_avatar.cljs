(ns test.open-company-web.components.user-avatar
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.user-avatar :refer [user-avatar]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-user-avatar-component
  (testing "User avatar component"
    (router/set-route! ["companies" "test"]
                       {:slug "test"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root user-avatar app-state {:target c})
          user-avatar-node (sel1 c [:div.user-avatar])]
      (is (not (nil? user-avatar-node)))
      (tu/unmount! c))))