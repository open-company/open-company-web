(ns test.open-company-web.components.list-companies
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.list-companies :refer [list-companies]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :companies [
    {
      :symbol "OPEN"
      :name "Open Company Inc."
    }
    {
      :symbol "BUFF"
      :name "Buffer Inc."
    }
  ]
})

(deftest test-list-companies-component
  (testing "List companies component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root list-companies app-state {:target c})
          companies-node (sel1 c [:ul])]
      (is (not (nil? companies-node)))
      (tu/unmount! c))))
