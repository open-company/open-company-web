(ns test.open-company-web.components.growth.growth
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.growth.growth :refer [growth]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]
              [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-data (utils/fix-sections company))

(def test-atom {:section-data (:growth company-data)
                :section :growth
                :actual-as-of (:updated-at (:growth company-data))
                :revisions-navigation-cb #()
                :read-only false})

(deftest test-growth-component
  (testing "Growth component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root growth app-state {:target c})]
      (is (not (nil? (sel1 c [:div.growth]))))
      ; editable click
      (sim/click (sel1 c [:h2.editable-title]) nil)
      (om.core/render-all)
      (is (not (nil? (sel1 c [:div.save-section]))))
      ; cancel click
      (sim/click (sel1 c [:div.save-section :button.oc-cancel]) nil)
      (om.core/render-all)
      (is (nil? (sel1 c [:div.save-section])))
      ; editable again
      (sim/click (sel1 c [:h2.editable-title]) nil)
      (om.core/render-all)
      (is (not (nil? (sel1 c [:div.save-section]))))
      ; save click
      (sim/click (sel1 c [:div.save-section :button.oc-success]) nil)
      (om.core/render-all)
      (is (nil? (sel1 c [:div.save-section])))
      (tu/unmount! c))))