(ns test.open-company-web.components.expanded-topic
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [open-company-web.components.expanded-topic :refer [expanded-topic]]
              [om.dom :as dom :include-macros true]
              [open-company-web.router :as router]
              [open-company-web.data.company :refer (company)]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :loading false
  :section "update"
  :section-data (:update company-data)})

(deftest test-expanded-topic-component
  (testing "Expanded topic component"
    (router/set-route! ["companies" :test]
                       {:slug :test})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root expanded-topic app-state {:target c})
          expanded-topic-node (sel1 c [:div.topic-expanded])]
      (is (not (nil? expanded-topic-node)))
      (tu/unmount! c))))