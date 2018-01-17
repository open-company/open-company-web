(ns test.oc.web.components.user-profile
  (:require [cljs-react-test.utils :as tu]
            [cljs-react-test.simulate :as sim]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [oc.web.rum-utils :as ru]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions :as actions]
            [oc.web.components.user-profile :refer (user-profile)]))

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
                    :component user-profile})
      (is (not (nil? (sel1 c [:div.user-profile]))))
      (tu/unmount! c))))