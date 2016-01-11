(ns test.open-company-web.components.login-button
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.login-button :refer [login-button]]
            [om.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {})

(deftest test-login-button-component
  (testing "Login button component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root login-button app-state {:target c})
          login-button-node (sel1 c [:a])]
      (is (not (nil? login-button-node)))
      (tu/unmount! c))))