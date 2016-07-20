(ns test.open-company-web.components.ui.login-button
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [rum.core :as rum]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.ui.login-button :refer [login-button]]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(deftest test-login-button-component
  (testing "Login button component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          _ (rum/mount (login-button {}) c)
          login-button-node (sel1 c [:button])]
      (is (not (nil? login-button-node)))
      (tu/unmount! c))))