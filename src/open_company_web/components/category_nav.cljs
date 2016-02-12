(ns open-company-web.components.category-nav
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [goog.events :as events]
            [goog.style :as gstyle])
  (:import [goog.events EventType]))

(def max-scroll-top (atom -1))

(defn check-scroll [owner]
  (events/listen
    js/window
    EventType.SCROLL
    (fn[e]
      (when-let [cat-node (om/get-ref owner "category-nav")]
        (let [nav (sel1 :nav.navbar)
              scroll-top (.-scrollTop (sel1 :body))
              fix-top-margin-scrolling (sel1 :.fix-top-margin-scrolling)
              win-width (.-offsetWidth js/window)]
          (when (= @max-scroll-top -1)
            (let [initial-offset-top (utils/offset-top cat-node)
                  nav-height (.-offsetHeight nav)]
              (reset! max-scroll-top (- initial-offset-top nav-height))))
          (let [actual-position (.-position (.-style cat-node))
                next-position (if (>= scroll-top @max-scroll-top) "fixed" "relative")
                will-change (not= actual-position next-position)
                cat-node-style (.-style cat-node)]
            (if (>= scroll-top @max-scroll-top)
              (do
                (gstyle/setStyle fix-top-margin-scrolling #js {:margin-top "44px"})
                (gstyle/setStyle cat-node #js {:position "fixed"
                                               :top "50px"
                                               :width (str win-width "px")}))
              (do
                (gstyle/setStyle fix-top-margin-scrolling #js {:margin-top "0px"})
                (gstyle/setStyle cat-node #js {:position "relative"
                                               :top "0px"
                                               :width (str win-width "px")})))
            (when will-change
              (gstyle/setStyle cat-node #js {:transform "translate3d(0px,0px,0px)"}))))))))

(defn category-click [data category-name e]
  ;; prevent the route reload
  (reset! open-company-web.core/prevent-route-dispatch true)
  ;; call the switch tab callback
  ((:switch-tab-cb data) category-name)
  ;; prevent the anchor element from reload the route
  (.preventDefault e)
  ;; change the window.location.hash
  (set! (.-hash (.-location js/window)) category-name)
  ;; fix the scroll to the first section if necessary
  (when (and (not= @max-scroll-top -1)
             (>= (.-scrollTop (sel1 :body)) @max-scroll-top))
    (set! (.-scrollTop (sel1 :body)) @max-scroll-top))
  ;; reactivate the url change handler
  (reset! open-company-web.core/prevent-route-dispatch false))

(defn setup-body-min-height []
  (when-let [app-div (sel1 :div#app)]
    (let [app-height (.-offsetHeight app-div)
          cur-min-height (.parseInt js/window (.-minHeight (.-style (sel1 :body))))
          fix-cur-min-height (if (js/isNaN cur-min-height) 0 cur-min-height)]
      (when (> app-height fix-cur-min-height)
        (gstyle/setStyle (sel1 :body) #js {:minHeight (str app-height "px")})))))

(defcomponent category-nav [data owner]

  (did-mount [_]
    (.setTimeout js/window #(check-scroll owner) 1000))

  (render [_]
    (let [slug (:slug @router/path)
          company-data (:company-data data)
          categories (:categories company-data)
          active-category (:active-category data)]
      (setup-body-min-height)
      (dom/div #js {:className "row category-nav" :ref "category-nav"}
        (for [category categories]
          (let [category-name (name category)
                category-class (utils/class-set {:category true
                                                 :active (= active-category category-name)})]
            (dom/a {:class "oc-header"
                    :href (str "/companies/" slug "/dashboard#" category-name)
                    :on-click (partial category-click data category-name)}
              (dom/div {:class category-class}
                (dom/div {:class "category-label"}
                  (utils/camel-case-str category-name))))))))))