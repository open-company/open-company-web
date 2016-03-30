(ns test.open-company-web.components.user-profile
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.user-profile :refer [user-profile]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-user-profile-component
  (testing "User profile component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root user-profile app-state {:target c})
          user-profile-node (sel1 c [:div.user-profile])]
      (is (not (nil? user-profile-node)))
      (tu/unmount! c))))