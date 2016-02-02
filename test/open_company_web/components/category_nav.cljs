(ns test.open-company-web.components.category-nav
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [om.dom :as dom :include-macros true]
            [open-company-web.data.company :refer (company)]
            [open-company-web.router :as router]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :company-data company
  :active-category "progress"})

(deftest test-category-nav-component
  (testing "Category nav component"
    (router/set-route! ["companies" "buffer"]
                       {:slug "buffer"})
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root category-nav app-state {:target c})
          category-nav-node (sel1 c [:div.category-nav])
          categories (sel c [:div.category])]
      (is (not (nil? category-nav-node)))
      (is (= (count categories) (count (:categories (:company-data test-atom)))))
      (tu/unmount! c))))