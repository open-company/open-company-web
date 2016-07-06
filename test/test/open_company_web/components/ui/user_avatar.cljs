(ns test.open-company-web.components.ui.user-avatar
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [rum.core :as rum]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.ui.user-avatar :refer [user-avatar]]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(deftest test-user-avatar-component
  (testing "User avatar component"
    (router/set-route! ["companies" "test"]
                       {:slug "test"})
    (let [c (tu/new-container!)
          _ (rum/mount (user-avatar nil) c)
          user-avatar-node (sel1 c [:div.user-avatar])]
      (is (not (nil? user-avatar-node)))
      (tu/unmount! c))))