(ns test.open-company-web.components.rich-editor
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.rich-editor :refer [rich-editor]]
              [om.dom :as dom :include-macros true]
              [open-company-web.data.finances :as finances-data]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def ^:private body
  "<h3>Recruiting</h3><p>We're continuing our fast pace of growing the team, and we'd love your help to spread the word about all our <a>current open positions</a>.</p>")

(def test-atom {
  :body body
  :section "update"
  :updated-at "2015-09-14T20:49:19Z"
  :author {
    :name "Stuart Levinson"
    :user-id "U06SQLDFT"
    :image "https://avatars.slack-edge.com/2015-10-16/12647678369_79b4fbf15439d29d5457_192.jpg"
  }
})


(deftest test-rich-editor-component
  (testing "Rich editor component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root rich-editor app-state {:target c})
          rich-editor-node (sel1 c [:div.rich-editor])]
      (is (not (nil? rich-editor-node)))
      (tu/unmount! c))))