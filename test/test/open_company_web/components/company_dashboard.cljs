(ns test.open-company-web.components.company-dashboard
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.company-dashboard :refer (company-dashboard)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom company)

(deftest test-company-dashboard-component
  (testing "Company dashboard component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root company-dashboard app-state {:target c})
          company-dashboard-node (sel1 c [:div.company-dashboard])]
      (is (not (nil? company-dashboard-node)))
      (tu/unmount! c))))