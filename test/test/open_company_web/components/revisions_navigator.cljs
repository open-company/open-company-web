(ns test.open-company-web.components.revisions-navigator
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.revisions-navigator :refer [revisions-navigator]]
              [om.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]
              [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :section :test
  :section-data {
    :revisions [
      {
        :rel "revision",
        :method "GET",
        :href "/companies/test/finances?as-of=2015-10-13T20:42:47Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-10-13T20:42:47Z"
      }
      {
        :rel "revision"
        :method "GET"
        :href "/companies/test/finances?as-of=2015-10-13T20:42:12Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-10-13T20:42:12Z"
      }
      {
        :rel "revision"
        :method "GET"
        :href "/companies/test/finances?as-of=2015-10-13T17:45:08Z"
        :type "application/vnd.open-company.section.v1+json"
        :updated-at "2015-10-13T17:45:08Z"
      }
    ]
    :updated-at "2015-10-13T20:42:47Z"
    :as-of "2015-10-13T20:42:47Z"
  }
  :loading false
  :navigate-cb nil})

(deftest test-revisions-navigator-component
  (testing "Revisions navigator component"
    (router/set-route! ["companies" "cool"] {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root revisions-navigator app-state {:target c})
          revisions-navigator-node (sel1 c [:div.revisions-navigator])
          first-rev (sel1 c [:a.rev-double-prev])
          prev-rev  (sel1 c [:a.rev-single-prev])
          next-rev  (sel1 c [:a.rev-single-next])
          last-rev  (sel1 c [:a.rev-double-next])]
      (is (not (nil? revisions-navigator-node)))
      (is (nil? first-rev))
      (is (not (nil? prev-rev)))
      (is (nil? next-rev))
      (is (nil? last-rev))
      (tu/unmount! c))))