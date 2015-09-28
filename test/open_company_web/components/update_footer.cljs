(ns test.open-company-web.components.update-footer
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.update-footer :refer [update-footer]]
              [om.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :updated-at "2015-09-14T20:49:19Z"
  :author {
    :name "Stuart Levinson"
    :slack_id "U06SQLDFT"
    :image "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015.png"
  }
})


(deftest test-update-footer-component
  (testing "Update footer component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root update-footer app-state {:target c})
          timeago-node (sel1 c [:p.timeago])
          image-node (sel1 c [:img.author-image])]
      (is (not (nil? timeago-node)))
      (is (not (nil? image-node)))
      (tu/unmount! c))))