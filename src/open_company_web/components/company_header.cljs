(ns open-company-web.components.company-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [goog.events :as events]
            [goog.style :as gstyle]
            [goog.events.EventType :as EventType])
  (:import [goog.events EventType]))

(defonce company-header-pt (atom 0))

(defn watch-scroll [owner]
  (when-let [company-name-container (om/get-ref owner "company-name-container")]
    (let [category-nav (sel1 [:div.category-nav])
          company-header (om/get-ref owner "company-header")
          company-description-container (om/get-ref owner "company-description-container")
          topic-list (sel1 [:div.topic-list])
          company-name-offset-top (.-offsetTop company-name-container)]
      (events/listen
        js/window
        EventType/SCROLL
        (fn [e]
          (let [scroll-top (.-scrollTop (.-body js/document))]
            (when (zero? @company-header-pt)
              (let [company-name-container-height (.-clientHeight company-name-container)
                    category-nav-height (.-clientHeight category-nav)
                    company-header-height (.-clientHeight company-header)
                    category-nav-pivot (+ (- company-header-height category-nav-height company-name-container-height) 7)]
                (reset! company-header-pt category-nav-pivot)))
            (if (> scroll-top company-name-offset-top)
              (do
                (gstyle/setStyle company-name-container #js {:position "fixed"})
                (gstyle/setStyle company-description-container #js {:marginTop "50px"}))
              (do
                (gstyle/setStyle company-name-container #js {:position "relative"})
                (gstyle/setStyle company-description-container #js {:marginTop "0px"})))
            (if (> scroll-top @company-header-pt)
              (do
                (gstyle/setStyle category-nav #js {:position "fixed"
                                                   :top "50px"
                                                   :left "0"})
                (gstyle/setStyle topic-list #js {:margin-top "50px"}))
              (do
                (gstyle/setStyle category-nav #js {:position "relative"
                                                   :top "0"
                                                   :left "0"})
                (gstyle/setStyle topic-list #js {:margin-top "0px"})))
            (gstyle/setStyle category-nav #js {:webkitTransform "translate3d(0,0,0)"})
            (gstyle/setStyle company-name-container #js {:webkitTransform "translate3d(0,0,0)"})
            (gstyle/setStyle company-description-container #js {:webkitTransform "translate3d(0,0,0)"})))))))

(defcomponent company-header [{:keys [company-data navbar-editing] :as data} owner]

  (did-mount [_]
    (.setTimeout js/window #(watch-scroll owner) 500))
 
  (render [_]
    (let [company-data (:company-data data)]
      (dom/div #js {:className "company-header"
                    :ref "company-header"}

        (dom/div #js {:className "company-header-top group"}
          ;; Company logo
          (dom/div {:class "company-logo-container"}
            (dom/img {:src (:logo company-data)
                      :class "company-logo"
                      :title (:name company-data)}))
          ;; Buttons
          (dom/div {:class "buttons-container"}
            (dom/button {:class "oc-btn bullhorn"}
              (dom/img {:src (str "/img/bullhorn.png?" ls/deploy-key)}))
            (dom/button {:class "oc-btn 3dots"}
              (dom/img {:src (str "/img/3dots.png?" ls/deploy-key)}))))

        (when-not navbar-editing
          (dom/div {}
            ;; Company name
            (dom/div #js {:className "company-name-container oc-header"
                          :ref "company-name-container"}
              (dom/div {:class "company-name"} (:name company-data)))

            ;; Company description
            (dom/div #js {:className "container oc-header"
                          :ref "company-description-container"}
              (dom/div {:class "company-description"} (:description company-data)))))

        ;; Category navigation
        (om/build category-nav (assoc data :navbar-editing navbar-editing))))))