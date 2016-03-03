(ns test.open-company-web.components.topic
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.topic :refer (topic)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :loading false
  :section "update"
  :section-data (:update company-data)
  :active-category "progress"})

(deftest test-topic-component
  (testing "Topic component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topic app-state {:target c})
          topic-node (sel1 c [:div.topic])]
      (is (not (nil? topic-node)))
      (tu/unmount! c))))