(ns open-company-web.components.company-profile
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.footer :refer (footer)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.urls :as oc-urls]
            [open-company-web.api :as api]
            [cljs.core.async :refer (put! chan <!)]
            [open-company-web.dispatcher :as dis :refer (app-state)]
            [open-company-web.lib.iso4217 :refer (iso4217 sorted-iso4217)]))

(defn- save-company-data [company-data logo logo-width logo-height]
  (let [slug (router/current-company-slug)
        fixed-logo (or logo "")
        fixed-logo-width (or logo-width 0)
        fixed-logo-height (or logo-height 0)]
    (api/patch-company slug {:name (:name company-data)
                             :slug slug
                             :currency (:currency company-data)
                             :description (:description company-data)
                             :logo-width (js/parseInt fixed-logo-width)
                             :logo-height (js/parseInt fixed-logo-height)
                             :logo fixed-logo})))

(defn- check-image [url owner data cb]
  (let [img (new js/Image)]
    (set! (.-onload img) (fn [e] (cb owner data img true)))
    (set! (.-onerror img) (fn [e] (cb owner data img false)))
    (set! (.-src img) url)))

(defn- check-img-cb [owner data img result]
 (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (do
      (js/alert "Invalid image url")
      (om/set-state! owner :logo (om/get-state owner :initial-logo))
      (om/set-state! owner :loading false))
    (save-company-data data (om/get-state owner :logo) (.-width img) (.-height img))))

(defcomponent currency-option [data owner]
  (render [_]
    (dom/option {
      :value (or (:value data) (:text data))
      :disabled (and (contains? :value data) (= (count (:value data)) 0))}
      (:text data))))

(defcomponent company-profile-form [data owner]

  (init-state [_]
    (let [save-chan (chan)]
      (utils/add-channel "save-company" save-chan))
      {:initial-logo (:logo data)
       :logo (:logo data)
       :company-name (:name data)
       :loading false
       :description (:description data)})

  (will-receive-props [_ next-props]
    (om/set-state! owner :loading false)
    (om/set-state! owner :initial-logo (:logo data)))

  (will-mount [_]
    (let [save-change (utils/get-channel "save-company")]
      (go (loop []
        (let [change (<! save-change)]
          (let [logo (om/get-state owner :logo)
                company-data (om/get-props owner)]
            (om/set-state! owner :loading true)
            ; if the log has changed
            (if (not= logo (om/get-state owner :initial-logo))
              ; and it's empty
              (if (clojure.string/blank? logo)
                ; save the data w/o a logo
                (save-company-data company-data "" 0 0)
                ; else check the logo
                (check-image logo owner company-data check-img-cb))
              ; else save the company datas
              (save-company-data company-data (:logo company-data) (:logo-width company-data) (:logo-height company-data)))
            (recur)))))))

  (render-state [_ {:keys [company-name logo description loading]}]
    (let [slug (keyword (router/current-company-slug))]

      (utils/update-page-title (str "OpenCompany - " company-name))

      (dom/div {:class "profile-container group"}
        (dom/div {:class "company-settings"} "Company Settings")
        ;; Company
        (dom/div {:class "company-form"}

          ;; Company name
          (dom/div {:class "company-name-title"} "COMPANY NAME")
          (dom/input {:class "company-name"
                      :type "text"
                      :id "name"
                      :value company-name
                      :on-change #(om/set-state! owner :company-name (.. % -target -value))
                      :on-blur (fn [e]
                                 (utils/handle-change data company-name :company-name)
                                 (utils/save-values "save-company"))})

          ; Slug
          (dom/div {:class "company-slug-title"} "COMPANY SLUG")
          (dom/div {:class "company-slug"} (name slug))

          ;; Currency
          (dom/div {:class "company-currency-title"} "DISPLAY CURRENCY IN")
          (dom/select {:id "currency"
                       :value (:currency data)
                       :on-change (fn [e]
                                   (utils/change-value data e :currency)
                                   (utils/save-values "save-company"))
                       :class "company-currency form-control"}
            (for [currency (sorted-iso4217)]
              (let [symbol (:symbol currency)
                    display-symbol (or symbol (:code currency))
                    label (str (:text currency) " " display-symbol)]
                (om/build currency-option {:value (:code currency) :text label}))))

          ;; Company logo
          (dom/div {:class "company-logo-title"} "LOGO (180x180px)")
          (dom/input {:type "text"
                      :value logo
                      :id "logo"
                      :class "company-logo"
                      :maxLength 255
                      :on-change #(om/set-state! owner :logo (.. % -target -value))
                      :on-blur #(utils/save-values "save-company")
                      :placeholder "http://example.com/logo.png"})

          ;; Company description
          (dom/div {:class "company-description-title"} "DESCRIPTION")
          (dom/textarea {:value description
                         :id "description"
                         :class "company-description"
                         :max-length 250
                         :on-change #(om/set-state! owner :description (.. % -target -value))
                         :on-blur (fn [e]
                                   (utils/handle-change data description :description)
                                   (utils/save-values "save-company"))})
          (dom/div {:class "save-button-container"}
            (dom/button {:class "save-button"} "SAVE")))))))

(defcomponent company-profile [data owner]

  (render [_]
    (let [company-data (dis/company-data data)]

      (when (:read-only company-data)
        (router/redirect! (oc-urls/company)))

      (dom/div {:class "company-profile fullscreen-page"}

        (om/build back-to-dashboard-btn {})

        (if (:loading data)
              
          ;; The data is still loading
          (dom/div (dom/h4 "Loading data..."))

          ;; Company profile
          (om/build company-profile-form company-data))

        (om/build footer data)))))


