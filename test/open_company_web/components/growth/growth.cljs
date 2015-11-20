(ns test.open-company-web.components.growth.growth
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.growth.growth :refer [growth]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section "test"
  :section-data {
    :title "Test"
    :notes {
      :body "Test body"}
    :metrics [
      {:slug "test-metric"
       :name "Test metric"
       :target "high"
       :interval "monthly"
       :unit "tests"}]
    :data [
        {:period "2015-10"
         :slug "test-metric"
         :target 123456
         :value 123456}
        {:period "2015-11"
         :slug "test-metric"
         :target 654321
         :value 654321}]}})

(deftest test-growth-component
  (testing "Growth component"
    (router/set-route! ["companies" "buffer" "growth"]
                       {:slug "buffer" :section "growth"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root growth app-state {:target c})
          growth-node (sel1 c [:div.growth])]
      (is (not (nil? growth-node)))
      (tu/unmount! c))))