(ns test.open-company-web.components.topic-overlay-edit
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.topic-overlay-edit :refer [topic-overlay-edit]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :topic "values"
  :topic-data (:values company)
  :focus nil
  :currency "USD"})

(deftest test-topic-overlay-edit-component
  (testing "Topic overlay edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topic-overlay-edit app-state {:target c})
          topic-overlay-edit-node (sel1 c [:div.fullscreen-topic-edit])]
      (is (not (nil? topic-overlay-edit-node)))
      (tu/unmount! c))))