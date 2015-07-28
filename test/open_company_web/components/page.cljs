(ns test.open-company-web.components.page
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.page :refer [company]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :ticker "OPEN"
  :OPEN {
    :symbol "OPEN"
    :links [
      {
        :rel "report"
        :href "/v1/companies/OPEN/2015/Q1"
      }
    ]
  }
})

(deftest test-company-component
  (testing "Company component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root company app-state {:target c})
          company-node (sel1 c [:div.company-container])
          link-nodes (sel c [:div.report-link])]
      (is (not (nil? company-node)))
      (is (= (count link-nodes) (count (:links (:OPEN test-atom)))))
      (tu/unmount! c))))
