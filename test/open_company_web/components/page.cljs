(ns test.open-company-web.components.page
    (:require [cljs.test :refer-macros [deftest testing is]]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.page :refer [company]]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-symbol "OPEN")

(def test-atom {
  :OPEN {
    :symbol company-symbol
    :links [
      {
        :rel "report"
        :href "/companies/OPEN/reports/2015/Q1"
      }
    ]
  }
})

(deftest test-company-component
  (testing "Company component"
    (router/set-route! [company-symbol] {:ticker company-symbol})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root company app-state {:target c})
          company-node (sel1 c [:div.company-container])]
      (is (not (nil? company-node)))
      (tu/unmount! c))))