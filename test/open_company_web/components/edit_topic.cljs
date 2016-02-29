(ns test.open-company-web.components.edit-topic
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.edit-topic :refer (edit-topic)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section-data {
    :title "title"
    :headline "headline"
    :body "body"}})

(def options {
  :navbar-editing-cb #()})

(deftest test-edit-topic-component
  (testing "Edit topic component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root edit-topic app-state {:target c :opts options})
          edit-topic-node (sel1 c [:div.edit-topic])
          title-node (sel1 c [:input.edit-topic-title-input])
          headline-node (sel1 c [:input.edit-topic-headline-input])
          body-node (sel1 c [:div.body-editor])]
      (is (not (nil? edit-topic-node)))
      (is (not (nil? title-node)))
      (is (= (.-value title-node) (:title (:section-data test-atom))))
      (is (not (nil? headline-node)))
      (is (= (.-value headline-node) (:headline (:section-data test-atom))))
      (is (not (nil? body-node)))
      (is (= (.-innerHTML body-node) (:body (:section-data test-atom))))
      (tu/unmount! c))))