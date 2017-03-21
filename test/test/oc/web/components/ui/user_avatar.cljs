(ns test.oc.web.components.ui.user-avatar
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.rum-utils :as ru]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [oc.web.components.ui.user-avatar :refer [user-avatar]]
            [oc.web.dispatcher :as dis]
            [oc.web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom (atom {}))

(deftest test-user-avatar-component
  (testing "User avatar component"
    (router/set-route! ["companies" "test"]
                       {:org "test"})
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component #(om/component (user-avatar))})
      (is (not (nil? (sel1 c [:button.user-avatar-button]))))
      (tu/unmount! c))))