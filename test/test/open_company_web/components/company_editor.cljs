(ns test.open-company-web.components.company-editor
  (:require [cljs.test :refer-macros [deftest testing is are use-fixtures]]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.company-editor :refer (company-editor)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:loading false})

(deftest test-company-editor-component
  (testing "Company editor component"
    (router/set-route! ["create-company"] {})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root company-editor app-state {:target c})
          company-editor-node (sel1 c [:div.company-editor])]
      (is (not (nil? company-editor-node)))
      (tu/unmount! c))))