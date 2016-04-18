(ns open-company-web.components.company-profile
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.components.navbar :refer (navbar)]
            [om-bootstrap.nav :as n]
            [om-bootstrap.panel :as p]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [cljs.core.async :refer (put! chan <!)]
            [open-company-web.dispatcher :refer (app-state)]
            [open-company-web.lib.iso4217 :refer (iso4217 sorted-iso4217)]))

(defn- save-company-data [company-data logo logo-width logo-height]
  (let [slug (:slug @router/path)]
    (api/patch-company slug {:name (:name company-data)
                             :slug slug
                             :currency (:currency company-data)
                             :description (:description company-data)
                             :logo-width (js/parseInt logo-height)
                             :logo-height (js/parseInt logo-height)
                             :logo logo})))

(defn- check-image [url owner cb]
  (let [img (new js/Image)]
    (set! (.-onload img) (fn [e] (cb owner img true)))
    (set! (.-onerror img) (fn [e] (cb owner img false)))
    (set! (.-src img) url)))

(defn- check-img-cb [owner img result]
 (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (js/alert "Invalid image url")
    (let [slug (:slug @router/path)
          company-data ((keyword slug) @app-state)]
      (save-company-data company-data (om/get-state owner :logo) (.-width img) (.-height img)))))

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
       :name (:name data)
       :description (:description data)})

  (will-receive-props [_ next-props]
    (om/set-state! owner :initial-logo (:logo data)))

  (will-mount [_]
    (let [save-change (utils/get-channel "save-company")]
      (go (loop []
        (let [change (<! save-change)]
          (let [slug (:slug @router/path)
                company-data ((keyword slug) @app-state)
                logo (om/get-state owner :logo)]
            (if (not= logo (om/get-state owner :initial-logo))
              (if (clojure.string/blank? logo)
                (save-company-data company-data "" 0 0)
                (check-image logo owner check-img-cb))
              (save-company-data company-data (:logo company-data) (:logo-width company-data) (:logo-height company-data)))
            (recur)))))))

  (render [_]
    (let [slug (keyword (:slug @router/path))]

      (utils/update-page-title (str "OpenCompany - " (:name data)))

      (dom/div {:class "profile-container"}
        ;; Company
        (dom/div {:class "row"}
          (dom/form {:class "form-horizontal"}

            ;; Company name
            (dom/div {:class "form-group"}
              (dom/label {:for "name" :class "col-sm-3 control-label oc-header"} "Company name")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "text"
                  :id "name"
                  :value (om/get-state owner :name)
                  :on-change #(om/set-state! owner :name (.. % -target -value))
                  :on-blur (fn [e]
                             (utils/handle-change data (om/get-state owner :name) :name)
                             (utils/save-values "save-company"))
                  :class "form-control"}))
              (dom/p {:class "help-block"} "Casual company name (leave out Inc., LLC, etc.)"))

            ; Slug
            (dom/div {:class "form-group"}
              (dom/label {:for "slug" :class "col-sm-3 control-label oc-header"} "Company slug")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "text"
                  :value (:slug data)
                  :id "slug"
                  :class "form-control"
                  :disabled true}))
              (dom/p {:class "help-block"} "https://opencompany.com/bago"))

            ;; Currency
            (dom/div {:class "form-group"}
              (dom/label {:for "currency" :class "col-sm-3 control-label oc-header"} "Currency")
              (dom/div {:class "col-sm-5"}
                (dom/select {
                  :type "file"
                  :id "currency"
                  :value (:currency data)
                  :on-change (fn [e]
                               (utils/change-value data e :currency)
                               (utils/save-values "save-company"))
                  :class "form-control"}
                    (for [currency (sorted-iso4217)]
                      (let [symbol (:symbol currency)
                            display-symbol (or symbol (:code currency))
                            label (str (:text currency) " " display-symbol)]
                        (om/build currency-option {:value (:code currency) :text label})))))
              (dom/p {:class "help-block"} "Currency for company finances"))

            ;; Company logo
            (dom/div {:class "form-group"}
              (dom/label {:for "logo" :class "col-sm-3 control-label oc-header"} "Logo")
              (dom/div {:class "col-sm-5"}
                (dom/input {
                  :type "text"
                  :value (om/get-state owner :logo)
                  :id "logo"
                  :class "form-control"
                  :maxLength 255
                  :on-change #(om/set-state! owner :logo (.. % -target -value))
                  :on-blur #(utils/save-values "save-company")
                  :placeholder "http://example.com/logo.png"}))
              (dom/p {:class "help-block"} "URL to company logo"))

            ;; Company description
            (dom/div {:class "form-group"}
              (dom/label {:for "logo" :class "col-sm-3 control-label oc-header"} "Description")
              (dom/div {:class "col-sm-5"}
                (dom/textarea {
                  :value (om/get-state owner :description)
                  :id "description"
                  :class "form-control"
                  :max-length 250
                  :on-change #(om/set-state! owner :description (.. % -target -value))
                  :on-blur (fn [e]
                             (utils/handle-change data (om/get-state owner :description) :description)
                             (utils/save-values "save-company"))}))
              (dom/p {:class "help-block"} "Description of the company"))))))))

(defcomponent company-profile [data owner]

  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (get data slug)]

      (when (:read-only company-data)
        (utils/redirect! (str "/" (name slug))))

      (dom/div {:class "company-container container"}

        ;; Company / user header
        (om/build navbar data)

        (dom/div {:class "navbar-offset container-fluid"}

          ;; White space
          (dom/div {:class "col-md-1"})

          (dom/div {:class "col-md-7 main"}

            (if (:loading data)
              
              ;; The data is still loading
              (dom/div (dom/h4 "Loading data..."))

              ;; Company profile
              (om/build company-profile-form company-data))))))))