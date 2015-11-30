(ns test.open-company-web.components.finances.finances
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.finances.finances :refer [finances]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]
              [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-data (utils/fix-sections company))

(def test-atom {:section-data (:finances company-data)
                :section :finances
                :actual-as-of (:updated-at (:finances company-data))
                :revisions-navigation-cb #()
                :read-only false})

(deftest test-finances-component
  (testing "Finances component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root finances app-state {:target c})]
      (is (not (nil? (sel1 c [:div.finances]))))
      ; editable click
      (sim/click (sel1 c [:i.editable-pen]) nil)
      (om.core/render-all)
      (is (not (nil? (sel1 c [:div.save-section]))))
      ; cancel click
      (sim/click (sel1 c [:div.save-section :button.oc-cancel]) nil)
      (om.core/render-all)
      (is (nil? (sel1 c [:div.save-section])))
      ; editable again
      (sim/focus (sel1 c [:textarea.editable-title]) nil)
      (om.core/render-all)
      (is (not (nil? (sel1 c [:div.save-section]))))
      ; save click
      (sim/click (sel1 c [:div.save-section :button.oc-success]) nil)
      (om.core/render-all)
      (is (nil? (sel1 c [:div.save-section])))
      (tu/unmount! c))))