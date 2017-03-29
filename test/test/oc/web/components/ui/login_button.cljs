(ns test.oc.web.components.ui.login-button
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [oc.web.rum-utils :as ru]
            [oc.web.components.ui.login-button :refer [login-button]]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)
; Test atom
(def test-atom (atom {}))

(deftest test-login-button-component
  (testing "Login button component"
    (router/set-route! [:test] {:org :test})
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component #(om/component (login-button))})
      (is (not (nil? (sel1 c [:button]))))
      (tu/unmount! c))))