(ns test.oc.web.components.topics-list
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [oc.web.components.topics-list :refer (topics-list)]
            [om.dom :as dom :include-macros true]
            [oc.web.data.company :refer (company)]
            [oc.web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :loading false
  :company-data company
  :topics-data company
  :columns-num 1
  :card-width "100px"
  :topics ["update" "finances"]})

(deftest test-topics-list-component
  (testing "Topic list component"
    (router/set-route! ["companies" "buffer"]
                       {:org "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topics-list app-state {:target c})
          topics-list-node (sel1 c [:div.topic-list])]
      (is (not (nil? topics-list-node)))
      (tu/unmount! c))))