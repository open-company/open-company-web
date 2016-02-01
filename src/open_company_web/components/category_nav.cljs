(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (put!)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defn change-category [category-name]
  (let [slug (:slug @router/path)]
    (.setToken open-company-web.core/history (str "/companies/" slug "/dashboard#" category-name))))

(def max-scroll-top (atom -1))

(defn check-scroll [owner]
  (let [$win (.$ js/window js/window)]
    (.scroll $win
             (fn [e]
               (when-let [cat-node (om/get-ref owner "category-nav")]
                 (let [$cat-node (.$ js/window cat-node)
                       $nav (.$ js/window "nav.navbar")
                       scroll-top (.scrollTop $win)
                       $topic-list (.$ js/window ".topic-list")]
                   (when (= @max-scroll-top -1)
                     (let [initial-offset-top (.-top (.offset $cat-node))
                           nav-height (.height $nav)
                           tmp-scroll-top (- initial-offset-top nav-height)]
                       (reset! max-scroll-top tmp-scroll-top)))
                   (let [actual-position (.-position (.-style cat-node))
                         next-position (if (>= scroll-top @max-scroll-top) "fixed" "relative")
                         will-change (not= actual-position next-position)]
                     (if (>= scroll-top @max-scroll-top)
                       ;; top scroll reached, fix the bar and don't let it scroll
                       (do
                         (.css $topic-list #js {"margin-top" "44px"})
                         (.css $cat-node #js {"position" "fixed" "top" "50px"}))
                       ;; let the bar move free with the scroller
                       (do
                         (.css $cat-node #js {"position" "relative" "top" "0px"})
                         (.css $topic-list #js {"margin-top" "0px"})))
                     (when will-change
                       (set! (.-display (.-style cat-node)) "none")
                       (.-offsetHeight cat-node)
                       (set! (.-display (.-style cat-node)) "")))))))))

(defcomponent category-nav [data owner]

  (did-mount [_]
    (.setTimeout js/window #(check-scroll owner) 1000))

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