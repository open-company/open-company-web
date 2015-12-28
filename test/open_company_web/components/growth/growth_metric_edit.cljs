(ns test.open-company-web.components.growth.growth-metric-edit
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.growth.growth-metric-edit :refer [growth-metric-edit]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:metric-info {:slug "test"
                              :name "Test"
                              :interval "monthly"
                              :unit "users"
                              :target "increase"}
                :metric-count 1
                :metrics {"test" {
                                  :slug "test"
                                  :name "Test"
                                  :interval "monthly"
                                  :unit "users"
                                  :target "increase"}}
                :new-metric false
                :next-cb #()
                :delete-metric-cb #()
                :cancel-cb #()
                :change-growth-metric-cb #()})

(deftest test-growth-metric-edit-component
  (testing "Growth metric edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root growth-metric-edit app-state {:target c})
          growth-metric-edit-node (sel1 c [:div.growth-metric-edit])]
      (is (not (nil? growth-metric-edit-node)))
      (tu/unmount! c))))