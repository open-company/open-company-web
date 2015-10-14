(ns test.open-company-web.components.profile
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.profile :refer [profile]]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.finances :as finances-data]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  ; basics
  :offsetTop 0
  :offsetLeft 0
  :buffer finances-data/finances
})

(deftest test-profile-component
  (testing "Profile component"
    (router/set-route! ["companies" "buffer" "finances" "cash"]
                       {:slug "buffer" :section "finances" :tab "cash"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root profile app-state {:target c})
          profile-node (sel1 c [:div.profile-container])]
      (is (not (nil? profile-node)))
      (tu/unmount! c))))