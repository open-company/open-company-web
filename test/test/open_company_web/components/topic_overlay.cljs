(ns test.open-company-web.components.topic-overlay
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.topic-overlay :refer [topic-overlay topic-overlay-internal topic-overlay-edit]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.company :refer (company)]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section "values"
  :section-data (:values company)
  :selected-metric nil
  :currency "USD"})

(deftest test-topic-overlay-component
  (testing "Topic overlay component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root topic-overlay app-state {:target c})
          topic-overlay-node (sel1 c [:div.topic-overlay])
          topic-overlay-internal-node (sel1 c [:div.topic-overlay-internal])]
      (is (not (nil? topic-overlay-node)))
      (is (not (nil? topic-overlay-internal-node)))
      (tu/unmount! c)))

  (testing "Topic overlay edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom (assoc test-atom :force-editing true))
          _ (om/root topic-overlay app-state {:target c})
          topic-overlay-node (sel1 c [:div.topic-overlay])
          topic-overlay-edit-node (sel1 c [:div.topic-overlay-edit])]
      (is (not (nil? topic-overlay-node)))
      (is (not (nil? topic-overlay-edit-node)))
      (tu/unmount! c))))