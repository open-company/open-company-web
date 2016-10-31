(ns test.open-company-web.components.growth.growth-edit
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.growth.growth-edit :refer [growth-edit]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {:initial-focus "test"
                :growth-data {"2015-10test" {:period "2015-10" :value 0 :slug "test"}}
                :metrics {"test" {
                                  :slug "test"
                                  :name "Test"
                                  :interval "monthly"
                                  :unit "users"}}
                :editing-cb #()
                :show-first-edit-tip false})

(deftest test-growth-edit-component
  (testing "Growth edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root growth-edit app-state {:target c})
          growth-edit-node (sel1 c [:div.composed-section-edit.growth-body.edit])]
      (is (not (nil? growth-edit-node)))
      (tu/unmount! c))))