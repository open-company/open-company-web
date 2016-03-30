(ns test.open-company-web.components.ui.link
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.ui.link :refer [link]]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :href "/"
  :name "asd"
})

(deftest test-link-component
  (testing "Link component"
    (let [c (tu/new-container!)
          app-state (atom test-atom) 
          _ (om/root link app-state {:target c})
          link-node (sel1 c [:a])]
      (is (not (nil? link-node)))
      (tu/unmount! c))))