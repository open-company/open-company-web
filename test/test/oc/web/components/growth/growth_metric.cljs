(ns test.oc.web.components.growth.growth-metric
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [oc.web.components.growth.growth-metric :refer [growth-metric]]
              [om.dom :as dom :include-macros true]
              [oc.web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def metric-slug "test-metric")

(def test-atom {
  :metric-info {
    :slug metric-slug
    :name "Test metric"
    :interval "monthly"
    :unit "test"}
  :metric-data [{
    :period "2015-10"
    :slug metric-slug
    :value 123456}
   {:period "2015-11"
    :slug metric-slug
    :value 654321}]})

(deftest test-growth-metric-component
  (testing "Growth metric component"
    (router/set-route! ["companies" "buffer" "growth"]
                       {:org "buffer" :topic "growth"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root growth-metric app-state {:target c})
          growth-metric-node (sel1 c [:div.growth-metric])]
      (is (not (nil? growth-metric-node)))
      (tu/unmount! c))))