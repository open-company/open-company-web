(ns test.open-company-web.tests
    (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
              [cljs-react-test.simulate :as sim]
              [cljs-react-test.utils :as tu]
              [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [dommy.core :as dommy :refer-macros [sel1 sel]]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(defn test-component [data owner]
  (om/component
    (dom/div nil
      (dom/p nil "Enter your name:")
      (dom/input
       #js {:onChange #(om/update! data :name (.. % -target -value))
            :value (:name data)})
      (dom/p nil (str "Your name is: " (:name data))))))

(deftest name-component
  (testing "The initial state is displayed"
    (let [c (tu/new-container!)]
      (let [app-state (atom {:name "Arya"})
          _ (om/root test-component app-state {:target c})
          display-node (second (sel c [:p]))
          input-node (sel1 c [:input])]
        (is (re-find #"Arya" (.-innerHTML display-node)))
        (testing "and when there is new input, it changes the state"
          (sim/change input-node {:target {:value "Nymeria"}})
          (om.core/render-all)
          (is (= "Nymeria" (:name @app-state)))
          (is (re-find #"Nymeria" (.-innerHTML display-node))))
        (tu/unmount! c)))))
