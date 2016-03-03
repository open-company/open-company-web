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

(defonce company-name-offset-top (atom 0))

(defonce scroll-listener-key (atom nil))

(defn listener [e owner]
  (let [company-header (om/get-ref owner "company-header")
        category-nav (sel1 [:div.category-nav])
        topic-list (sel1 [:div.topic-list])
        scroll-top (.-scrollTop (.-body js/document))
        company-name-container (om/get-ref owner "company-name-container")
        company-description-container (om/get-ref owner "company-description-container")]
    (when (zero? @company-header-pt)
      (let [company-name-container-height (.-clientHeight company-name-container)
            category-nav-height (.-clientHeight category-nav)
            company-header-height (.-clientHeight company-header)
            category-nav-pivot (- company-header-height
                                  category-nav-height
                                  company-name-container-height
                                  3)]
        (reset! company-header-pt category-nav-pivot)))
    ; fix company name and move the company description relatively when
    ; the scroll hit the company name
    (when (and company-name-container company-description-container)
      (if (> scroll-top @company-name-offset-top)
        (do
          (gstyle/setStyle company-name-container #js {:position "fixed"})
          (gstyle/setStyle company-description-container #js {:marginTop "46px"}))
        (do
          (gstyle/setStyle company-name-container #js {:position "relative"})
          (gstyle/setStyle company-description-container #js {:marginTop "0px"}))))
    ; fix the category navigation bar and move the topic list relatively when
    ; the scroll hit the category navigation max top
    (when (and category-nav topic-list)
      (if (or (> scroll-top @company-header-pt) (om/get-props owner :navbar-editing))
        (do
          (gstyle/setStyle category-nav #js {:position "fixed"
                                             :top "46px"
                                             :left "0"})
          (gstyle/setStyle topic-list #js {:marginTop "72px"}))
        (do
          (gstyle/setStyle category-nav #js {:position "relative"
                                             :top "0"
                                             :left "0"})
          (gstyle/setStyle topic-list #js {:marginTop "5px"}))))
    (when category-nav
      (gstyle/setStyle category-nav #js {:webkitTransform "translate3d(0,0,0)"}))
    (when company-name-container
      (gstyle/setStyle company-name-container #js {:webkitTransform "translate3d(0,0,0)"}))
    (when company-description-container
      (gstyle/setStyle company-description-container #js {:webkitTransform "translate3d(0,0,0)"}))))

(defn watch-scroll [owner]
  (if (utils/is-mobile)
    (when (nil? @scroll-listener-key)
      (when-let [company-header (om/get-ref owner "company-header")]
        (let [name-offset-top (.-offsetTop (om/get-ref owner "company-name-container"))
              category-nav (sel1 [:div.category-nav])
              topic-list (sel1 [:div.topic-list])]
          (when (zero? @company-name-offset-top)
            (reset! company-name-offset-top name-offset-top))
          (reset! scroll-listener-key (events/listen
                                       js/window
                                       EventType/SCROLL
                                       #(listener % owner))))))
    (events/unlistenByKey @scroll-listener-key)))

(defcomponent company-header [{:keys [company-data navbar-editing] :as data} owner]

  (render [_]
    ;; add the scroll listener if the logo is not present
    (when (clojure.string/blank? (:logo company-data))
      (.setTimeout js/window #(watch-scroll owner) 500))
    (dom/div #js {:className "company-header"
                  :ref "company-header"}
      (if navbar-editing

        (dom/div {:class "navbar-editing"
                  :key "navbar-editing"}
          (dom/button {:class "save-bt oc-btn oc-link"
                       :disabled (not (:save-bt-active data))
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
                        :on-load #(watch-scroll owner) ;; add scroll listener when the logo is loaded
                        :on-error #(watch-scroll owner) ;; or it errors on loading
                        :class "company-logo"
                        :title (:name company-data)}))
            ;; Buttons
            (dom/div {:class (utils/class-set {:buttons-container true
                                               :hidden (not (utils/is-mobile))})}
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

      (when-not (:editing-topic data)
        ;; Category navigation
        (om/build category-nav data)))))