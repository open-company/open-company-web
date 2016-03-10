(ns test.open-company-web.components.topic-body
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.topic-body :refer (topic-body)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section "update"
  :section-data (:update company-data)
  :active-category "progress"})

(deftest test-topic-body-component
  (testing "Topic body component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topic-body app-state {:target c})
          topic-body-node (sel1 c [:div.topic-body])]
      (is (not (nil? topic-body-node)))
      (tu/unmount! c))))