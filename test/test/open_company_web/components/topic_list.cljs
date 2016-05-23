(ns test.open-company-web.components.topic-list
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.topic-list :refer (topic-list)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :loading false
  :company-data company
  :columns-num 1
  :card-width "100px"
  :topics ["update" "finances"]
  :active-category "progress"})

(deftest test-topic-list-component
  (testing "Topic list component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topic-list app-state {:target c})
          topic-list-node (sel1 c [:div.topic-list])]
      (is (not (nil? topic-list-node)))
      (tu/unmount! c))))