(ns test.open-company-web.components.fullscreen-topic-edit
    (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros (sel1 sel)]
              [open-company-web.components.fullscreen-topic-edit :refer (fullscreen-topic-edit)]
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

(deftest test-fullscreen-topic-edit-component
  (testing "Fullscreen topic edit component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root fullscreen-topic-edit app-state {:target c})
          fullscreen-topic-edit-node (sel1 c [:div.fullscreen-topic-edit])]
      (is (not (nil? fullscreen-topic-edit-node)))
      (tu/unmount! c))))