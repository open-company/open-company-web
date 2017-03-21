(ns test.oc.web.components.user-profile
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [oc.web.components.user-profile :refer [user-profile]]
            [om.dom :as dom :include-macros true]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.rum-utils :as ru]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom (atom {}))

(deftest test-user-profile-component
  (testing "User profile component"
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component #(om/component (user-profile))})
      (is (not (nil? (sel1 c [:div.user-profile]))))
      (tu/unmount! c))))