(ns test.open-company-web.components.simple-section
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.simple-section :refer [simple-section]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section-data {
    :body "Simple section text"
    :updated-at "2015-09-14T20:49:19Z"
    :author {
      :name "Stuart Levinson"
      :user-id "U06SQLDFT"
      :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
    }
    :section :update
  }
  :read-only false
  :section :update
})

(deftest test-simple-section-component
  (testing "Simple section component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root simple-section app-state {:target c})
          section-node (sel c [:div.simple-section])]
      (is (not (nil? section-node)))
      (tu/unmount! c))))