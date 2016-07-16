(ns open-company-web.components.company-settings
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.footer :refer (footer)]
            [open-company-web.components.stripe :as stripe]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.urls :as oc-urls]
            [open-company-web.api :as api]
            [open-company-web.local-settings :as ls]
            [cljs.core.async :refer (put! chan <!)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.iso4217 :refer (iso4217 sorted-iso4217)]))

(defn- save-company-data [company-data logo logo-width logo-height]
  (let [slug (router/current-company-slug)
        fixed-logo (or logo "")
        fixed-logo-width (or logo-width 0)
        fixed-logo-height (or logo-height 0)]
    (api/patch-company slug {:name (:name company-data)
                             :slug slug
                             :currency (:currency company-data)
                             :logo-width (js/parseInt fixed-logo-width)
                             :logo-height (js/parseInt fixed-logo-height)
                             :logo fixed-logo})))

(defn- check-img-cb [owner data img result]
 (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (do
      (js/alert "Invalid image url")
      (om/set-state! owner :logo (om/get-state owner :initial-logo))
      (om/set-state! owner :loading false))
    (save-company-data data (om/get-state owner :logo) (.-width img) (.-height img))))

(defn- check-image [url owner data]
  (let [img (new js/Image)]
    (set! (.-onload img) #(check-img-cb owner data img true))
    (set! (.-onerror img) #(check-img-cb owner data img false))
    (set! (.-src img) url)))

(defn save-company-clicked [owner]
  (let [logo         (om/get-state owner :logo)
        old-company-data (dis/company-data (om/get-props owner))
        new-company-data {:name (om/get-state owner :company-name)
                          :currency (om/get-state owner :currency)}]
    (om/set-state! owner :loading true)
    ; if the log has changed
    (if (not= logo (om/get-state owner :initial-logo))
      ; and it's empty
      (if (clojure.string/blank? logo)
        ; save the data w/o a logo
        (save-company-data new-company-data "" 0 0)
        ; else check the logo
        (check-image logo owner new-company-data))
      ; else save the company datas
      (save-company-data new-company-data (:logo old-company-data) (:logo-width old-company-data) (:logo-height old-company-data)))))

(defcomponent currency-option [data owner]
  (render [_]
    (dom/option {:value (or (:value data) (:text data))
                 :disabled (and (contains? :value data) (= (count (:value data)) 0))}
      (:text data))))

(defn get-state [data current-state]
  (let [company-data (dis/company-data data)]
    {:initial-logo (:logo data)
     :logo (or (:logo current-state) (:logo company-data))
     :company-name (or (:company-name current-state) (:name company-data))
     :currency (or (:currency current-state) (:currency company-data))
     :loading false}))

(defcomponent company-settings-form [data owner]

  (init-state [_]
    (get-state data nil))

  (will-receive-props [_ next-props]
    (om/set-state! owner (get-state next-props nil)))

  (render-state [_ {:keys [company-name logo currency description loading]}]
    (let [slug (keyword (router/current-company-slug))]

      (utils/update-page-title (str "OpenCompany - " company-name))
      (dom/div {:class "lg-col-5 md-col-7 col-11 mx-auto mt4 mb4 settings-container group"}
        (dom/div {:class "company-settings"}
          (dom/span {} "Company Settings")
          (when-not company-name
            (loading/small-loading)))
        ;; Company
        (dom/div {:class "company-form p3"}

          ;; Company name
          (dom/div {:class "small-caps bold mb1"} "COMPANY NAME")
          (dom/input {:class "npt col-8 p1 mb3"
                      :type "text"
                      :id "name"
                      :value company-name
                      :on-change #(om/set-state! owner :company-name (.. % -target -value))})
          ; Slug
          (dom/div {:class "small-caps bold mb1"} "DASHBOARD URL")
          (dom/div {:class "npt npt-disabled col-8 p1 mb3"} (str ls/web-server "/" (name slug)))
          ;; Currency
          (dom/div {:class "small-caps bold mb1"} "DISPLAY CURRENCY IN")
          (dom/select {:id "currency"
                       :value currency
                       :on-change #(om/set-state! owner :currency (.. % -target -value))
                       :class "npt col-8 p1 mb3 company-currency"}
            (for [currency (sorted-iso4217)]
              (let [symbol (:symbol currency)
                    display-symbol (or symbol (:code currency))
                    label (str (:text currency) " " display-symbol)]
                (om/build currency-option
                          {:value (:code currency) :text label}
                          {:react-key (:code currency)}))))

          ;; Company logo
          (dom/div {:class "small-caps bold mb1"} "SQUARE COMPANY LOGO URL (approx. 180x180px)")
          (dom/input {:type "text"
                      :value logo
                      :id "logo"
                      :class "npt col-10 p1 mb3"
                      :maxLength 255
                      :on-change #(om/set-state! owner :logo (.. % -target -value))
                      :placeholder "http://example.com/logo.png"})
          (dom/div {:class "mt2 right-align"}
            (dom/button {:class "btn-reset btn-solid"
                         :on-click #(save-company-clicked owner)}
              (if loading
                (loading/small-loading)
                "SAVE"))))))))

(defcomponent company-settings [data owner]

  (render [_]
    (let [company-data (dis/company-data data)]

      (when (:read-only company-data)
        (router/redirect! (oc-urls/company)))

      (dom/div {:class "main-company-settings fullscreen-page"}

        (back-to-dashboard-btn {})

        (if (:loading data)

          ;; The data is still loading
          (dom/div (dom/h4 "Loading data..."))

          ;; Company profile
          (dom/div {:class "company-settings-container"}
            (om/build company-settings-form data)))

        (om/build footer data)))))