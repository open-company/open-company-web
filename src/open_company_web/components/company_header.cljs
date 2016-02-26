(ns open-company-web.components.company-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [goog.events :as events]
            [goog.style :as gstyle]
            [goog.events.EventType :as EventType])
  (:import [goog.events EventType]))

(defonce company-header-pt (atom 0))

(defn watch-scroll [owner]
  (when-let [company-header (om/get-ref owner "company-header")]
    (let [company-name-offset-top (.-offsetTop (om/get-ref owner "company-name-container"))
          category-nav (sel1 [:div.category-nav])
          topic-list (sel1 [:div.topic-list])]
      (events/listen
        js/window
        EventType/SCROLL
        (fn [e]
          (let [scroll-top (.-scrollTop (.-body js/document))
                company-name-container (om/get-ref owner "company-name-container")
                company-description-container (om/get-ref owner "company-description-container")]
            (when (zero? @company-header-pt)
              (let [company-name-container-height (.-clientHeight company-name-container)
                    category-nav-height (.-clientHeight category-nav)
                    company-header-height (.-clientHeight company-header)
                    category-nav-pivot (- company-header-height
                                          category-nav-height
                                          company-name-container-height)]
                (reset! company-header-pt category-nav-pivot)))
            ; fix company name and move the company description relatively when
            ; the scroll hit the company name
            (when (and company-name-container company-description-container)
              (if (> scroll-top company-name-offset-top)
                (do
                  (gstyle/setStyle company-name-container #js {:position "fixed"})
                  (gstyle/setStyle company-description-container #js {:marginTop "50px"}))
                (do
                  (gstyle/setStyle company-name-container #js {:position "relative"})
                  (gstyle/setStyle company-description-container #js {:marginTop "0px"}))))
            ; fix the category navigation bar and move the topic list relatively when
            ; the scroll hit the category navigation max top
            (when (and category-nav topic-list)
              (if (> scroll-top @company-header-pt)
                (do
                  (gstyle/setStyle category-nav #js {:position "fixed"
                                                     :top "50px"
                                                     :left "0"})
                  (gstyle/setStyle topic-list #js {:marginTop "60px"}))
                (do
                  (gstyle/setStyle category-nav #js {:position "relative"
                                                     :top "0"
                                                     :left "0"})
                  (gstyle/setStyle topic-list #js {:marginTop "10px"}))))
            (when category-nav
              (gstyle/setStyle category-nav #js {:webkitTransform "translate3d(0,0,0)"}))
            (when company-name-container
              (gstyle/setStyle company-name-container #js {:webkitTransform "translate3d(0,0,0)"}))
            (when company-description-container
              (gstyle/setStyle company-description-container #js {:webkitTransform "translate3d(0,0,0)"}))))))))

(defcomponent company-header [{:keys [company-data navbar-editing] :as data} owner]

  (did-mount [_]
    (.setTimeout js/window #(watch-scroll owner) 500))
 
  (render [_]
    (let [company-data (:company-data data)]
      (dom/div #js {:className "company-header"
                    :ref "company-header"}
        (if navbar-editing

          (dom/div {:class "navbar-editing"
                    :key "navbar-editing"}
            (dom/button {:class "save-bt oc-btn oc-link"
                         :on-click (fn [e]
                                    (when-let [ch (utils/get-channel "save-bt-navbar")]
                                     (put! ch {:click true :event e})))} "Save")
            (dom/button {:class "cancel-bt oc-btn oc-link"
                         :on-click (fn [e]
                                    (when-let [ch (utils/get-channel "cancel-bt-navbar")]
                                     (put! ch {:click true :event e})))} "Cancel"))

          (dom/div {:class "company-header-internal"
                    :key "company-header-internal"}
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

            ;; Company name
            (dom/div #js {:className "company-name-container"
                          :ref "company-name-container"}
              (dom/div {:class "company-name"} (:name company-data)))

            ;; Company description
            (dom/div #js {:className "company-description-container"
                          :ref "company-description-container"}
              (dom/div {:class "company-description"} (:description company-data)))))

        ;; Category navigation
        (om/build category-nav (assoc data :navbar-editing navbar-editing))))))