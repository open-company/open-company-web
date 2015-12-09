(ns test.open-company-web.components.finances.finances-edit
    (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros (sel1 sel)]
              [open-company-web.components.finances.finances-edit :refer (finances-edit)]
              [open-company-web.components.finances.utils :as finances-utils]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]
              [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def fixed-company-data (utils/fix-sections company))

(def map-finances-data (finances-utils/map-placeholder-data (:data (:finances fixed-company-data))))

(def test-atom {
  :finances-data map-finances-data
  :change-finances-cb #()})

(deftest test-finances-edit-component
  (testing "Finances edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances-edit app-state {:target c})
          finances-node (sel1 c [:div.finances])
          rows (sel c [:table :tbody :tr])]
      (is (not (nil? finances-node)))
      (is (count rows) (inc (count (vals map-finances-data))))
      (tu/unmount! c))))