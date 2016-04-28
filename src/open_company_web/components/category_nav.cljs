(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.style :as gstyle])
  (:import [goog.fx.dom Scroll]))

(def max-scroll-top 133)

(defn scroll-to-top!
  "Scroll to top of the page while ensuring body is not shrinking to avoid jump"
  []
  (gstyle/setStyle (sel1 :body) "min-height" "9999px")
  (doto (Scroll. (.-body js/document)
                 #js [0 (.-scrollTop (.-body js/document))]
                 #js [0 0]
                 utils/oc-animation-duration)
    (events/listen
     EventType/FINISH
     #(gstyle/setStyle (sel1 :body) "min-height" 0))
    (.play)))

(defn category-click [data category-name e]
  ;; prevent the route reload
  (reset! prevent-route-dispatch true)
  ;; call the switch tab callback
  ((:switch-category-cb data) category-name)
  ;; prevent the anchor element from reload the route
  (.preventDefault e)
  (set! js/window.location.hash category-name)
  (when (utils/is-mobile) (scroll-to-top!))
  ;; reactivate the url change handler
  (reset! prevent-route-dispatch false))

(defn get-categories [categories is-editing]
  (if (or (utils/is-mobile) is-editing)
    categories
    (vec (concat ["all"] categories))))

(defcomponent category-nav [data owner]

  (render [_]
    (let [navbar-editing (:navbar-editing data)
          scroll-top (.-scrollTop (sel1 :body))
          company-data (:company-data data)
          categories (if (:company-data data) (get-categories (:categories company-data) navbar-editing) [])
          active-category (:active-category data)
          sections (keys (:sections company-data))
          navbar-style (if (and (utils/is-mobile)
                                (or navbar-editing ; is editing style
                                ; or the scroll pivot has been initialized and
                                ; the scroll is higher than the header
                                (>= scroll-top max-scroll-top)))
                         #js {:position "fixed"
                              :top "50px"}
                         #js {:position "relative"
                              :top "0px"})]
      (dom/div #js {:className "row category-nav"
                    :ref "category-nav"
                    :style navbar-style}
        (dom/div #js {:className "category-nav-internal"
                      :style #js {:width (if (utils/is-mobile)
                                           "100%"
                                           (str (* (inc (count sections)) 150) "px"))}}
          (for [category categories]
            (let [category-name (name category)
                  category-class (utils/class-set {:category true
                                                   :active (= active-category category-name)})]
              (dom/a {:class ""
                      :href (oc-urls/company-category category-name)
                      :on-click (partial category-click data category-name)}
                (dom/div {:class category-class}
                  (dom/div {:class "category-label"}
                    (utils/camel-case-str category-name)))))))))))
