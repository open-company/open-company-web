(ns test.oc.web.components.ui.org-avatar
  (:require [cljs-react-test.utils :as tu]
            [cljs-react-test.simulate :as sim]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [cljs.test :refer-macros [deftest async testing is are use-fixtures]]
            [oc.web.rum-utils :as ru]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]))

(enable-console-print!)

; dynamic mount point for components
(def ^:dynamic c)

(def empty-logo
  {:logo-url nil
   :logo-width 0
   :logo-height 0})

(def with-logo
  {:logo-url "http://example.com/image.png"
   :logo-width 100
   :logo-height 100})

(def org-data {
  :org-data {
    :name "test"
    :slug "test"}})

(deftest test-org-avatar-component
  (testing "Org avatar component"
    (testing "with link and empty logo"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge empty-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) true)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (not (nil? (sel1 c [:div.org-avatar :a.org-link]))))
        (is (nil? (sel1 c [:div.org-avatar :img.org-avatar-img])))
        (is (not (nil? (sel1 c [:div.org-avatar :span.org-name]))))
        (tu/unmount! c)))
    (testing "without link and not empty logo"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge with-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) false)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (nil? (sel1 c [:div.org-avatar :a.org-link])))
        (is (not (nil? (sel1 c [:div.org-avatar :img.org-avatar-img]))))
        (is (nil? (sel1 c [:div.org-avatar :span.org-name])))
        (tu/unmount! c)))
    (testing "with logo with name always"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge with-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) false :always)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (nil? (sel1 c [:div.org-avatar :a.org-link])))
        (is (not (nil? (sel1 c [:div.org-avatar :img.org-avatar-img]))))
        (is (not (nil? (sel1 c [:div.org-avatar :span.org-name]))))
        (tu/unmount! c)))
    (testing "without logo with name always"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge empty-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) false :always)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (nil? (sel1 c [:div.org-avatar :a.org-link])))
        (is (nil? (sel1 c [:div.org-avatar :img.org-avatar-img])))
        (is (not (nil? (sel1 c [:div.org-avatar :span.org-name]))))
        (tu/unmount! c)))
    (testing "with logo with name never"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge with-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) false :never)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (nil? (sel1 c [:div.org-avatar :a.org-link])))
        (is (not (nil? (sel1 c [:div.org-avatar :img.org-avatar-img]))))
        (is (nil? (sel1 c [:div.org-avatar :span.org-name])))
        (tu/unmount! c)))
    (testing "without logo with name never"
      (let [c (tu/new-container!)
            test-atom (update-in org-data [:org-data] merge empty-logo)]
        (ru/drv-root {:state test-atom
                      :drv-spec (dis/drv-spec test-atom (atom {}))
                      :target c
                      :component #(org-avatar (:org-data test-atom) false :never)})
        (is (not (nil? (sel1 c [:div.org-avatar]))))
        (is (nil? (sel1 c [:div.org-avatar :a.org-link])))
        (is (nil? (sel1 c [:div.org-avatar :img.org-avatar-img])))
        (is (nil? (sel1 c [:div.org-avatar :span.org-name])))
        (tu/unmount! c)))))