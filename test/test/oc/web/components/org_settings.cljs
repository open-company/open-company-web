(ns test.oc.web.components.org-settings
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [oc.web.rum-utils :as ru]
            [oc.web.dispatcher :as dis]
            [oc.web.components.org-settings :as cs]
            [oc.web.data.company :refer (company)]
            [oc.web.router :as router]))

(enable-console-print!)

(def test-atom
  (atom {:offsetTop 0
         :offsetLeft 0
         :buffer company
         :jwt {:real-name "Ada Lovelace"}}))

(deftest test-org-settings-component
  (testing "Org Settings component"
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom (atom {}))
                    :target c
                    :component cs/org-settings})
      (is (not (nil? (sel1 c [:div.settings-container]))))
      (tu/unmount! c))))