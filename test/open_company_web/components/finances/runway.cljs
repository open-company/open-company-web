(ns test.open-company-web.components.finances.runway
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances.runway :refer [runway]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section-data (:finances (utils/fix-sections company))
  :read-only false
  :currency "USD"
  :start-editing-cb #()})

(deftest test-runway-component
  (testing "Runway component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root runway app-state {:target c})
          chart-node (sel1 c [:div.section.runway])]
      (is (not (nil? chart-node)))
      (tu/unmount! c))))