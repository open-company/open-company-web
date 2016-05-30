(ns test.open-company-web.components.login
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.login :refer [login]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :auth-settings {
    :auth-url "https://slack.com/oauth/authorize?client_id=&redirect_uri=/slack-oauth&state=open-company-auth&scope=identify,read,post"
    :redirectURI "/slack-oauth"
    :state "open-company-auth"
    }
})

(deftest test-login-component
  (testing "Login component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root login app-state {:target c})
          h1-node (sel1 c [:h1])]
      (is (not (nil? h1-node)))
      (tu/unmount! c))))