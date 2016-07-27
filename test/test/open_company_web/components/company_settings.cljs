(ns test.open-company-web.components.company-settings
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om]
            [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.rum-utils :as ru]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.company-settings :as cs]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

(def test-atom
  (atom {:offsetTop 0
         :offsetLeft 0
         :buffer company
         :jwt {:real-name "Ada Lovelace"}}))

(deftest test-company-settings-component
  (testing "Company Settings component"
    (let [c (tu/new-container!)]
      (ru/drv-root {:state test-atom
                    :drv-spec (dis/drv-spec test-atom)
                    :target c
                    :component cs/company-settings})
      (is (not (nil? (sel1 c [:div.settings-container]))))
      (tu/unmount! c))))