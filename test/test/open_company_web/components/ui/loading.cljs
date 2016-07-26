(ns test.open-company-web.components.ui.loading
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.ui.loading :refer (loading)]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:loading true})

(deftest test-loading-component
  (testing "Loading component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root loading app-state {:target c})
          loading-node (sel1 c [:div.oc-loading])
          loading-internal-node (sel1 [:i.fa.fa-circle-o-notch])]
      (is (not (nil? loading-node)))
      (is (not (nil? loading-internal-node)))
      (tu/unmount! c))))