(ns test.open-company-web.components.simple-section
    (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros (sel1 sel)]
              [open-company-web.components.simple-section :refer (simple-section)]
              [open-company-web.lib.utils :as utils]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-data (utils/fix-sections company))

(def test-atom {:section-data (:challenges company-data)
                :section :challenges
                :actual-as-of (:updated-at (:challenges company-data))
                :revisions-navigation-cb #()
                :read-only false})

(deftest test-simple-section-component
  (testing "Simple section component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root simple-section app-state {:target c})]
      (is (not (nil? (sel c [:div.simple-section]))))
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
      ; edit title
      (sim/change (sel1 c [:div.editable-title-container :input.editable-title]) {:target {:value "Test title"}})
      (om.core/render-all)
      (is (= (.-value (sel1 c [:div.editable-title-container :input.editable-title])) "Test title"))
      ; save click
      (sim/click (sel1 c [:div.save-section :button.oc-success]) nil)
      (om.core/render-all)
      (is (nil? (sel1 c [:div.save-section])))
      (tu/unmount! c))))