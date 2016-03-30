(ns test.open-company-web.components.page-not-found
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.page-not-found :refer [page-not-found]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-page-not-found-component
  (testing "Page not found component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root page-not-found app-state {:target c})
          h1-node (sel1 c [:h1])]
      (is (not (nil? h1-node)))
      (tu/unmount! c))))