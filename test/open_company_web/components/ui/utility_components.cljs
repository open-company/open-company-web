(ns test.open-company-web.components.ui.utility-components
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.ui.utility-components :refer [editable-pen]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.users :refer [users]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-editable-pen-selector
  (testing "Editable pen component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root editable-pen app-state {:target c})
          editable-pen-node (sel1 c [:i.editable-pen])]
      (is (not (nil? editable-pen-node)))
      (tu/unmount! c))))