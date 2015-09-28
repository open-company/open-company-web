(ns test.open-company-web.components.sidebar
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.sidebar :refer [sidebar]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def company-slug "buffer")

(def test-atom {
  :buffer {
    :slug company-slug
    :links [
      {
        :rel "report"
        :href "/companies/OPEN/reports/2015/Q1"
      }
    ]
  }
})

(deftest test-sidebar-component
  (testing "Sidebar component"
    (router/set-route! ["companies" company-slug]
                       {:slug company-slug})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root sidebar app-state {:target c})
          sidebar-node (sel c [:div.sidebar])]
      (is (not (nil? sidebar-node)))
      (tu/unmount! c))))