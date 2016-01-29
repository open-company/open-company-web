(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (put!)]
            [open-company-web.lib.utils :as utils]))

(defn change-category [category-name]
  (let [ch (utils/get-channel "change-category")]
    (put! ch category-name)))

(defn check-scroll [owner]
  (when-let [cat-node (om/get-ref owner "category-nav")]
    (let [$cat-node (.$ js/window cat-node)
          $nav (.$ js/window "nav.navbar")
          initial-offset-top (.-top (.offset $cat-node))
          nav-height (.height $nav)
          $win (.$ js/window js/window)
          max-scroll-top (- initial-offset-top nav-height)]
      (.scroll $win (fn [e]
                      (let [scroll-top (.scrollTop $win)]
                        (if (>= scroll-top max-scroll-top)
                          ;; top scroll reached, fix the bar and don't let it scroll
                          (do
                            (.css (.$ js/window ".topic-list") #js {"margin-top" "44px"})
                            (.css $cat-node #js {"position" "fixed" "top" "50px"}))
                          ;; let the bar move free with the scroller
                          (do
                            (.css $cat-node #js {"position" "relative" "top" "0px"})
                            (.css (.$ js/window ".topic-list") #js {"margin-top" "0px"})))))))))

(defcomponent category-nav [data owner]

  (did-mount [_]
    (.setTimeout js/window #(check-scroll owner) 500))

  (render [_]
    (let [company-data (:company-data data)
          categories (:categories company-data)
          active-category (:active-category data)]
      (dom/div #js {:className "row category-nav" :ref "category-nav"}
        (for [category categories]
          (let [category-name (name category)
                category-class (str "col-xs-4 category" (if (= active-category category-name) " active" ""))]
            (dom/a {:on-click #(change-category category-name)}
              (dom/div {:class category-class}
                (dom/div {:class "category-label"}
                  (utils/camel-case-str category-name))))))))))