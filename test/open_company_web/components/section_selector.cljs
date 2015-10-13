(ns test.open-company-web.components.section-selector
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.section-selector :refer [section-selector]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :read-only false
  :section "update"
  :data {}
})

(deftest test-section-selector-component
  (testing "Section selector component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root section-selector app-state {:target c})
          section-selector-node (sel1 c [:div.section-selector])]
      (is (not (nil? section-selector-node)))
      (tu/unmount! c))))