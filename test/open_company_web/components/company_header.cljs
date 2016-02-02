(ns test.open-company-web.components.company-header
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.company-header :refer (company-header)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :loading false
  :company-data company
  :active-category "progress"})

(deftest test-company-header-component
  (testing "Company header component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root company-header app-state {:target c})
          company-header-node (sel1 c [:div.company-header])]
      (is (not (nil? company-header-node)))
      (tu/unmount! c))))