(ns open-company-web.components.profile-components.company
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [om-bootstrap.panel :as p]
            [om-bootstrap.input :as i]))

(def months ["January" "February" "March" "April" "May" "June" "July" "August" "September" "October" "November" "December"])
(def years ["2015" "2014" "2013" "2012" "2011" "2010"])

(defcomponent option [data owner]
  (render [_]
    (dom/option {} (:text data))))

(defcomponent company [data owner]
  (render [_]
    (p/panel {:header (dom/h3 "Basics")}
      (dom/div {:class "panel-body"}
        (dom/div {:class "row"}
          (dom/form {:class "form-horizontal"}
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Open Symbol")
              (dom/div {:class "col-sm-1"}
                (dom/input {
                  :type "text"
                  :value (:symbol data)
                  :id "symbol"
                  :class "form-control"
                  :maxLength 5
                  :placeholder ""}))
              (dom/p {:class "help-block"} "1 to 5 alpha-numeric characters, eg. OPEN, BUFFR, 2XTR"))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Company name")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "text"
                  :id "name"
                  :value (:name data)
                  :class "form-control"}))
              (dom/p {:class "help-block"} "Casual company name (leave out Inc., LLC, etc.)"))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Open company since")
              (dom/div {:class "col-sm-1"}
                (dom/select {
                  :id "open-month"
                  :value (:open-month data)
                  :class "form-control"}
                  (for [month months]
                    (om/build option {:text month}))))
              (dom/div {:class "col-sm-1"}
                (dom/select {
                  :id "open-year"
                  :value (:open-year data)
                  :class "form-control"}
                  (for [year years]
                    (om/build option {:text year}))))
              (dom/p {:class "help-block"} ""))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Tagline")
              (dom/div {:class "col-sm-6"}
                (dom/input {
                  :type "text"
                  :id "tagline"
                  :value (:tagline data)
                  :placeholder "optional"
                  :class "form-control"}))
              (dom/p {:class "help-block"} ""))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Logo")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "file"
                  :id "logo"
                  :value (:logo data)
                  :class "form-control"}))
              (dom/p {:class "help-block"} "Optional small logo file (PNG, JPG, GIF)"))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Location(s)")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "text"
                  :id "location"
                  :value (:location data)
                  :class "form-control"
                  :placeholder "eg. Paris, France"})
                (dom/a {:on-click #(.preventDefault %)}
                  (dom/i {:class "fa fa-plus"})
                  "Add location"))
              (dom/p {:class "help-block"} "Each company location"))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Descriptive tag(s)")
              (dom/div {:class "col-sm-6"}
                (dom/input {
                  :type "text"
                  :id "tags"
                  :value (:tags data)
                  :class "form-control"
                  :placeholder "eg. B2B, mobile, finance, internet of things, security, blockchain"}))
              (dom/p {:class "help-block"} ""))
            (dom/div {:class "form-group"}
              (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Get in touch")
              (dom/div {:class "col-sm-3"}
                (dom/input {
                  :type "text"
                  :id "email"
                  :value (:email data)
                  :class "form-control"
                  :placeholder "eg. hello@startup.com"}))
              (dom/p {:class "help-block"} "Best way to contact the company (email, URL, phone number, etc.)"))))))))
