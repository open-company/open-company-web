(ns test.open-company-web.components.ui.headline
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.ui.headline :refer [headline]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :headline "aaa"
  :section "headline-test"})

(deftest test-company-avatar-component
  (testing "Company avatar component"
    (router/set-route! ["companies" "test"]
                       {:slug "test"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root headline app-state {:target c})
          headline-node (sel1 c [:div.headline])]
      (is (not (nil? headline-node)))
      (tu/unmount! c))))