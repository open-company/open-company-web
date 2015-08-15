(ns test.open-company-web.components.comment
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.comment :refer [comment-component comment-readonly-component]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :cursor {
    :comment "This is a comment"
  }
  :key :comment
  :disabled true
})

(deftest test-comment-component
  (testing "Comment component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root comment-component app-state {:target c})
          textarea-node (sel1 c [:textarea])]
      (is (not (nil? textarea-node)))
      (is (= (.-value textarea-node) (:comment (:cursor test-atom))))
      (tu/unmount! c))))

(deftest test-comment-readonly-component
  (testing "Comment readonly component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root comment-readonly-component app-state {:target c})
          textarea-node (sel1 c [:textarea])]
      (is (= (.-value textarea-node) (:comment (:cursor test-atom))))
      (testing "and when the textarea is disabled and the user change the content it is not transmitted to the atom"
        (sim/change textarea-node {:target {:value "test"}})
        (om.core/render-all)
        (is (= "test" (.-value textarea-node)))
        (is (not (= "test" (:comment (:cursor @app-state))))))
      (tu/unmount! c))))