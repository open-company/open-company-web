(ns open-company-web.components.company-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [cljs.core.async :refer (put!)]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.category-nav :refer (category-nav)]
            [open-company-web.router :as router]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [goog.events :as events]
            [goog.style :as gstyle]
            [goog.events.EventType :as EventType]
            [om-bootstrap.button :as b])
  (:import [goog.events EventType]))

(def company-header-pt (atom 0))

(def company-name-offset-top (atom 0))

(def scroll-listener-key (atom nil))

(defn scroll-listener [e owner]
  (let [company-header (om/get-ref owner "company-header")
        category-nav (sel1 [:div.category-nav])
        topic-list (sel1 [:div.topic-list])
        scroll-top (.-scrollTop (.-body js/document))
        company-name-container (sel1 [:div.company-name-container])
        company-description-container (sel1 [:div.company-description-container])]
    (when (and (zero? @company-header-pt) company-name-container company-header category-nav)
      (let [company-name-container-height (.-clientHeight company-name-container)
            company-header-height (.-clientHeight company-header)
            category-nav-height (.-clientHeight category-nav)
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
          (gstyle/setStyle company-name-container #js {:position "fixed" :top "0px" :left "0px"})
          (gstyle/setStyle company-description-container #js {:marginTop "61px"}))
        (do
          (gstyle/setStyle company-name-container #js {:position "relative"})
          (gstyle/setStyle company-description-container #js {:marginTop "15px"}))))
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
    (when-not @scroll-listener-key
      (when-let [company-header (om/get-ref owner "company-header")]
        (let [company-name-container (om/get-ref owner "company-name-container")
              name-offset-top (.-offsetTop company-name-container)
              category-nav (sel1 [:div.category-nav])
              topic-list (sel1 [:div.topic-list])]
          (when (zero? @company-name-offset-top)
            (reset! company-name-offset-top name-offset-top))
          (reset! scroll-listener-key (events/listen
                                       js/window
                                       EventType/SCROLL
                                       #(scroll-listener % owner))))))
    (events/unlistenByKey @scroll-listener-key)))

(defn menu-click [owner]
  (if (cook/get-cookie :jwt)
    (do ; Logout
      (cook/remove-cookie! :jwt)
      (utils/redirect! "/"))
    ; redirect to login
    (utils/redirect! "/login")))

(def logo-max-height 100)

(defn logo-on-load [owner]
  (when-not (utils/is-mobile)
    (when-let [logo (om/get-ref owner "company-logo")]
      (let [logo-height (.-height logo)
            height-diff (- logo-max-height logo-height)]
        (when (pos? height-diff)
          (gstyle/setStyle logo #js {:marginTop (str (quot height-diff 2) "px")})))))
  (watch-scroll owner))

(defcomponent company-header [{:keys [company-data navbar-editing stakeholder-update] :as data} owner options]

  (render [_]
    ;; add the scroll listener if the logo is not present and not stakeholder update
    (when (and (not stakeholder-update) company-data (clojure.string/blank? (:logo company-data)))
      (.setTimeout js/window #(watch-scroll owner) 500))

    (dom/div #js {:className "company-header"
                  :ref "company-header"}
      (let [link-url (str "/" (:slug company-data) (when-not stakeholder-update "/updates"))]
        
        ; topic editing
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
        ; topic list
        (dom/div {:class "company-header-internal"
                  :key "company-header-internal"}
          (dom/div #js {:className "company-header-top group"}
            ;; Company logo
            (dom/div {:class "company-logo-container"}
              (dom/img #js {:src (:logo company-data)
                            ;; add scroll listener when the logo is loaded unless stakeholder update
                            :onLoad (when-not stakeholder-update #(logo-on-load owner))
                            ;; or add listener if logo errors on loading unless stakeholder update
                            :onError (when-not stakeholder-update #(watch-scroll owner))
                            :className "company-logo"
                            :title (:name company-data)
                            :ref "company-logo"}))
            ;; Buttons
            (dom/div {:class (utils/class-set {:buttons-container true
                                               :hidden (not (utils/is-mobile))})}
              (let [icon-url (if stakeholder-update "/img/dashboard.svg" "/img/digest.svg")]
                (dom/button {:type "button" :class "btn btn-link digest-button"}
                  (dom/img {:src icon-url})))
              (b/dropdown {:class "oc-btn vert-ellipse" :bs-size "30px" :title "" :pull-right? true}
                (b/menu-item {:key 1
                              :on-click #(menu-click owner)} (if (cook/get-cookie :jwt) "Logout" "Sign In/Sign Up")))))

          ;; Company name
          (dom/div #js {:className "company-name-container"
                        :ref "company-name-container"}
            (dom/div {:class "company-name"} (:name company-data)))

          ;; Company description
          (dom/div #js {:className "company-description-container"
                        :ref "company-description-container"}
            (dom/div {:class "company-description"} (:description company-data))
            ;; View navigation
            (let [link-name (if stakeholder-update "Dashboard View" "Digest View")]
                (dom/a {:class "nav-link" :href link-url} link-name)))))

      (when-not (:editing-topic data)
        ;; Category navigation
        (om/build category-nav data)))))