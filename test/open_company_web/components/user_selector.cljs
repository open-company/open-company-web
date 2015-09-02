(ns test.open-company-web.components.user-selector
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.user-selector :refer [user-selector]]
              [om.dom :as dom :include-macros true]
              [open-company-web.lib.utils :refer [users]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :users users
  :value "U06STCKLN"
})

(deftest test-user-selector
  (testing "User selector component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root user-selector app-state {:target c})
          select-node (sel1 c [:select])]
      (is (not (nil? select-node)))
      (tu/unmount! c))))