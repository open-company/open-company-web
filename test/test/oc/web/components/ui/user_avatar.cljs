(ns test.oc.web.components.ui.user-avatar
  (:require [cljs-react-test.utils :as tu]
            [cljs-react-test.simulate :as sim]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [oc.web.rum-utils :as ru]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.components.ui.user-avatar :refer [user-avatar]]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom (atom {}))

(deftest test-user-avatar-component
  (testing "User avatar component"
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component #(om/component (user-avatar))})
      (is (not (nil? (sel1 c [:button.user-avatar-button]))))
      (tu/unmount! c))))