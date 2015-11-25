(ns test.open-company-web.components.table-of-contents-item
  (:require [cljs.test :refer-macros (deftest async testing is are use-fixtures)]
            [cljs-react-test.simulate :as sim]
            [cljs-react-test.utils :as tu]
            [om.core :as om :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.components.table-of-contents-item :refer (table-of-contents-item)]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def test-atom {
  :category "Category"
  :section "section"
  :title "Section title"
  :updated-at "2015-09-14T20:49:19Z"
  :show-popover #()
})

(deftest test-table-of-contents-item-component
  (testing "Table of contents item component"
    (let [c (tu/new-container!)
          app-state (atom test-atom)
          _ (om/root table-of-contents-item app-state {:target c})
          section-close (sel1 c [:div.category-section-close])
          section-link (sel1 c [:div.category-section :a])
          section-title (sel1 c [:p.section-title])
          section-date (sel1 c [:p.section-date])
          category-section-sortable (sel1 c [:div.category-section-sortable])]
      (is (not (nil? section-close)))
      (is (not (nil? section-link)))
      (is (not (nil? section-title)))
      (is (= (.-innerHTML section-title) (:title test-atom)))
      (is (not (nil? section-date)))
      (is (not (empty? (.-innerHTML section-date))))
      (is (not (nil? category-section-sortable)))
      (tu/unmount! c))))