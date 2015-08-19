(ns test.open-company-web.components.profile-components.company
  (:require [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [open-company-web.components.profile-components.company :refer [basics mission on-the-web]]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  ; basics
  :currency "USD"
  :name "Transparency, LLC"
  :symbol "OPEN"
  :tagline "asd asdjas sks pw"
  :founded "2015-04"

  ; mission
  :mission "asd"
  :description "asd"

  ; web
  :web {
    :company "https://opencompany.io"
    :asd "asd"
  }
})

(deftest test-basics-component
  (testing "Basics component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root basics app-state {:target c})
          name-node (sel1 c [:input#name])
          symbol-node (sel1 c [:input#symbol])
          founded-year-node (sel1 c [:select#founded-year])
          founded-month-node (sel1 c [:select#founded-month])]
      (is (not (nil? name-node)))
      (is (not (nil? symbol-node)))
      (is (not (nil? founded-year-node)))
      (is (not (nil? founded-month-node)))
      (is (= (.-value name-node) (:name test-atom)))
      (is (= (.-value symbol-node) (:symbol test-atom)))
      (is (= (str (.-value founded-year-node) "-" (.-value founded-month-node)) (:founded test-atom)))
      (tu/unmount! c))))

(deftest test-mission-component
  (testing "Mission component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root mission app-state {:target c})
          description-node (sel1 c [:textarea#description])
          mission-node (sel1 c [:textarea#mission])]
      (is (not (nil? description-node)))
      (is (not (nil? mission-node)))
      (is (= (.-value description-node) (:description test-atom)))
      (is (= (.-value mission-node) (:mission test-atom)))
      (tu/unmount! c))))

(deftest test-on-the-web-component
  (testing "On-the-web component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root on-the-web app-state {:target c})
          company-node (sel1 c [:input#company])
          asd-node (sel1 c [:input#custom-asd])]
      (is (not (nil? company-node)))
      (is (not (nil? asd-node)))
      (is (= (.-value company-node) (:company (:web test-atom))))
      (is (= (.-value asd-node) (:asd (:web test-atom))))
      (tu/unmount! c))))