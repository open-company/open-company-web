(ns open-company-web.components.company-profile
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [om-bootstrap.nav :as n]
            [om-bootstrap.panel :as p]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [cljs.core.async :refer [put! chan <!]]
            [open-company-web.dispatcher :refer [app-state]]
            [open-company-web.lib.iso4217 :refer [iso4217 sorted-iso4217]]))

(defn- save-company-data [company-data logo]
  (let [slug (:slug @router/path)]
    (api/patch-company slug {:name (:name company-data)
                             :slug slug
                             :currency (:currency company-data)
                             :logo logo})))

(defn- check-image [url owner cb]
  (let [img (new js/Image)]
    (set! (.-onload img) (fn [e] (cb owner true)))
    (set! (.-onerror img) (fn [e] (cb owner false)))
    (set! (.-src img) url)))

(defn- check-img-cb [owner result]
  (let [slug (:slug @router/path)
        company-data ((keyword slug) @app-state)]
    (if result
      (save-company-data company-data (om/get-state owner :logo))
      (js/alert "Invalid image url"))))

(defcomponent currency-option [data owner]
  (render [_]
    (dom/option {
      :value (or (:value data) (:text data))
      :disabled (and (contains? :value data) (= (count (:value data)) 0))}
      (:text data))))

(defcomponent company-profile [data owner]

  (init-state [_]
    (let [save-chan (chan)]
      (utils/add-channel "save-company" save-chan))
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      {:initial-logo (:logo company-data)
       :logo (:logo company-data)}))

  (will-receive-props [_ next-props]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) next-props)]
      (om/set-state! owner :initial-logo (:logo company-data))))

  (will-mount [_]
    (om/set-state! owner :selected-tab 1)
    (let [save-change (utils/get-channel "save-company")]
        (go (loop []
          (let [change (<! save-change)]
            (let [slug (:slug @router/path)
                  company-data ((keyword slug) data)
                  logo (om/get-state owner :logo)]
              (if (not= logo (om/get-state owner :initial-logo))
                (if (clojure.string/blank? logo)
                  (save-company-data company-data "")
                  (check-image logo owner check-img-cb))
                (save-company-data company-data (:logo company-data)))
              (recur)))))))

  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "profile-container"}
        (case (om/get-state owner :selected-tab)

          ;; Company
          1 (dom/div {:class "row"}
              (dom/form {:class "form-horizontal"}
                (dom/h2 {} (:name company-data))

                ;; Company name
                (dom/div {:class "form-group"}
                  (dom/label {:for "name" :class "col-sm-2 control-label"} "Company name")
                  (dom/div {:class "col-sm-3"}
                    (dom/input {
                      :type "text"
                      :id "name"
                      :value (:name company-data)
                      :on-change #(utils/change-value company-data % :name)
                      :on-blur #(utils/save-values "save-company")
                      :class "form-control"}))
                  (dom/p {:class "help-block"} "Casual company name (leave out Inc., LLC, etc.)"))

                ; Slug
                (dom/div {:class "form-group"}
                  (dom/label {:for "slug" :class "col-sm-2 control-label"} "Company slug")
                  (dom/div {:class "col-sm-2"}
                    (dom/input {
                      :type "text"
                      :value (:slug company-data)
                      :id "slug"
                      :class "form-control"
                      :disabled true}))
                  (dom/p {:class "help-block"} "1 to 5 alpha-numeric characters, eg. OPEN, BUFFR, 2XTR"))

                ;; Currency
                (dom/div {:class "form-group"}
                  (dom/label {:for "currency" :class "col-sm-2 control-label"} "Currency")
                  (dom/div {:class "col-sm-2"}
                    (dom/select {
                      :type "file"
                      :id "currency"
                      :value (:currency company-data)
                      :on-change (fn [e] (utils/change-value company-data e :currency) (utils/save-values "save-company"))
                      :class "form-control"}
                        (for [currency (sorted-iso4217)]
                          (let [symbol (:symbol currency)
                                display-symbol (or symbol (:code currency))
                                label (str (:text currency) " " display-symbol)]
                            (om/build currency-option {:value (:code currency) :text label})))))
                  (dom/p {:class "help-block"} "Currency business uses for its finances")))

                ;; Company logo
                (dom/div {:class "form-group"}
                  (dom/label {:for "logo" :class "col-sm-2 control-label"} "Logo")
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
                  (dom/p {:class "help-block"} "Company logo url"))))))))