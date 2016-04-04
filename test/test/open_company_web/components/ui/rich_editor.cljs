(ns test.open-company-web.components.ui.rich-editor
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.ui.rich-editor :refer [rich-editor]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def ^:private body
  "<h3>Recruiting</h3><p>We're continuing our fast pace of growing the team, and we'd love your help to spread the word about all our <a>current open positions</a>.</p>")

(def test-atom {
  :editing false
  :section :test
  :body-counter 0
  :read-only false
  :body "test body"
  :placeholder "Placeholder text"
})


(deftest test-rich-editor-component
  (testing "Rich editor component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root rich-editor app-state {:target c})
          rich-editor-node (sel1 c [:div.rich-editor-container])]
      (is (not (nil? rich-editor-node)))
      (tu/unmount! c))))