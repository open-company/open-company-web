(ns test.oc.web.components.ui.org-avatar
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [oc.web.components.ui.org-avatar :refer [org-avatar]]
            [om.dom :as dom :include-macros true]
            [oc.web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :org-data {
    :name "test"
    :slug "test"}})

(deftest test-org-avatar-component
  (testing "Org avatar component"
    (router/set-route! ["companies" "test"]
                       {:org "test"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root org-avatar app-state {:target c})
          org-avatar-node (sel1 c [:div.org-avatar])]
      (is (not (nil? org-avatar-node)))
      (tu/unmount! c))))