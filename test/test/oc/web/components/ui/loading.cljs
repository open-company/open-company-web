(ns test.oc.web.components.ui.loading
  (:require [cljs-react-test.utils :as tu]
            [cljs-react-test.simulate :as sim]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [oc.web.rum-utils :as ru]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.components.ui.loading :refer (loading rloading)]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:loading true})

(deftest test-loading-component
  (testing "Loading Om component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root loading app-state {:target c})
          loading-node (sel1 c [:div.oc-loading])]
      (is (not (nil? loading-node)))
      (tu/unmount! c)))
  (testing "Loading Rum component"
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component #(om/component (rloading))})
      (is (not (nil? (sel1 c [:div.oc-loading]))))
      (tu/unmount! c))))