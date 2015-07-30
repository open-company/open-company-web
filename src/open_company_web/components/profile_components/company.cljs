(ns open-company-web.components.profile-components.company
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [om-bootstrap.panel :as p]
            [om-bootstrap.input :as i]
            [open-company-web.lib.iso4217 :refer [iso4217 sorted-iso4217]]
            [open-company-web.lib.utils :refer [handle-change get-channel]]
            [cljs.core.async :refer [put!]]))

(def months [
  ["01" "January"]
  ["02" "February"]
  ["03" "March"]
  ["04" "April"]
  ["05" "May"]
  ["06" "June"]
  ["07" "July"]
  ["08" "August"]
  ["09" "September"]
  ["10" "October"]
  ["11" "November"]
  ["12" "December"]])

(def years ["2015" "2014" "2013" "2012" "2011" "2010"])

(def stages ["Idea" "Prototype" "Closed beta" "Open beta" "Launched" "Pivoting" "Exited" "Closed"])

(defn change-value [cursor e key]
  (handle-change cursor (.. e -target -value) key))

(defn save-values []
  (let [save-channel (get-channel "save-company")]
    (put! save-channel 1)))

(defn founded-changed [owner data]
  (let [founded-month-el (om/get-ref owner "founded-month")
        founded-month-value (.-value (.getDOMNode founded-month-el))
        founded-year-el (om/get-ref owner "founded-year")
        founded-year-value (.-value (.getDOMNode founded-year-el))
        value (str founded-year-value "-" founded-month-value)]
    (handle-change data value :founded)
    (save-values)))

(defn open-since-changed [owner data]
  (let [open-month-el (om/get-ref owner "open-month")
        open-month-value (.-value (.getDOMNode open-month-el))
        open-year-el (om/get-ref owner "open-year")
        open-year-value (.-value (.getDOMNode open-year-el))
        value (str open-year-value "-" open-month-value)]
    (handle-change data value :open-since)
    (save-values)))

(defn save-new-web-link [owner data]
  (let [web-label-el (om/get-ref owner "new-web-label")
        web-url-el (om/get-ref owner "new-web-url")
        web-label-value (.-value (.getDOMNode web-label-el))
        web-url-value (.-value (.getDOMNode web-url-el))]
    (when (and (> (count web-label-value) 0) (> (count web-url-value) 0))
      (handle-change data web-url-value [:web (keyword web-label-value)])
      (save-values))))

(defn remove-web-link [data key]
  (let [web-links (:web data)
        web-links-clean (dissoc web-links key)]
    (handle-change data web-links-clean :web)
    (save-values)))

(defcomponent option [data owner]
  (render [_]
    (dom/option {
      :value (or (:value data) (:text data))
      :disabled (and (contains? :value data) (= (count (:value data)) 0))}
      (:text data))))

(defcomponent basics [data owner]
  (render [_]
    (let [splitted-founded (clojure.string/split (:founded data) "-")
          founded-month (second splitted-founded)
          founded-year (first splitted-founded)
          splitted-open-since (clojure.string/split (:open-since data) "-")
          open-month (second splitted-open-since)
          open-year (first splitted-open-since)]
      (p/panel {:header (dom/h3 "Basics")}
        (dom/div {:class "panel-body"}
          (dom/div {:class "row"}
            (dom/form {:class "form-horizontal"}

              ;; Company symbol
              (dom/div {:class "form-group"}
                (dom/label {:for "symbol" :class "col-sm-2 control-label"} "Open Symbol")
                (dom/div {:class "col-sm-1"}
                  (dom/input {
                    :type "text"
                    :value (:symbol data)
                    :id "symbol"
                    :class "form-control"
                    :maxLength 5
                    :on-change #(change-value data % :symbol)
                    :on-blur #(save-values)
                    :placeholder ""}))
                (dom/p {:class "help-block"} "1 to 5 alpha-numeric characters, eg. OPEN, BUFFR, 2XTR"))

              ;; Company name
              (dom/div {:class "form-group"}
                (dom/label {:for "name" :class "col-sm-2 control-label"} "Company name")
                (dom/div {:class "col-sm-3"}
                  (dom/input {
                    :type "text"
                    :id "name"
                    :value (:name data)
                    :on-change #(change-value data % :name)
                    :on-blur #(save-values)
                    :class "form-control"}))
                (dom/p {:class "help-block"} "Casual company name (leave out Inc., LLC, etc.)"))

              ;; Stage
              (dom/div {:class "form-group"}
                (dom/label {:for "stage" :class "col-sm-2 control-label"} "Stage")
                (dom/div {:class "col-sm-2"}
                  (dom/select #js {
                    :id "stage"
                    :value (:stage data)
                    :className "form-control"
                    :onChange (fn [e] (change-value data e :stage) (save-values))}
                    (for [stage stages]
                      (om/build option {:text stage}))))
                  (dom/p {:class "help-block"} "Which of the following best describes your progress?"))

              ;; Founded when
              (dom/div {:class "form-group"}
                (dom/label {:class "col-sm-2 control-label"} "Founded")
                (dom/div {:class "col-sm-1"}
                  (dom/select #js {
                    :ref "founded-month"
                    :id "founded-month"
                    :value founded-month
                    :className "form-control"
                    :onChange #(founded-changed owner data)}
                    (for [month months]
                      (om/build option {:text (second month) :value (first month)}))))
                (dom/div {:class "col-sm-1"}
                  (dom/select #js {
                    :ref "founded-year"
                    :id "founded-year"
                    :value founded-year
                    :className "form-control"
                    :onChange #(founded-changed owner data)}
                    (for [year years]
                      (om/build option {:text year}))))
                (dom/p {:class "help-block"} ""))

              ;; Open company since
              (dom/div {:class "form-group"}
                (dom/label {:class "col-sm-2 control-label"} "Open company since")
                (dom/div {:class "col-sm-1"}
                  (dom/select #js {
                    :ref "open-month"
                    :id "open-month"
                    :value open-month
                    :className "form-control"
                    :onChange #(open-since-changed owner data)}
                    (for [month months]
                      (om/build option {:text (second month) :value (first month)}))))
                (dom/div {:class "col-sm-1"}
                  (dom/select #js {
                    :ref "open-year"
                    :id "open-year"
                    :value open-year
                    :className "form-control"
                    :onChange #(open-since-changed owner data)}
                    (for [year years]
                      (om/build option {:text year}))))
                (dom/p {:class "help-block"} ""))

              ;; Tagline
              (dom/div {:class "form-group"}
                (dom/label {:for "tagline" :class "col-sm-2 control-label"} "Tagline")
                (dom/div {:class "col-sm-6"}
                  (dom/input {
                    :type "text"
                    :id "tagline"
                    :value (:tagline data)
                    :placeholder "optional"
                    :on-change #(change-value data % :tagline)
                    :on-blur #(save-values)
                    :class "form-control"}))
                (dom/p {:class "help-block"} ""))

              ;; Logo
              (dom/div {:class "form-group"}
                (dom/label {:for "logo" :class "col-sm-2 control-label"} "Logo")
                (dom/div {:class "col-sm-3"}
                  (dom/input {
                    :type "file"
                    :id "logo"
                    :value (:logo data)
                    :class "form-control"}))
                (dom/p {:class "help-block"} "Optional small logo file (PNG, JPG, GIF)"))

              ;; Currency
              (dom/div {:class "form-group"}
                (dom/label {:for "currency" :class "col-sm-2 control-label"} "Currency")
                (dom/div {:class "col-sm-2"}
                  (dom/select {
                    :type "file"
                    :id "currency"
                    :value (:currency data)
                    :on-change (fn [e] (change-value data e :currency) (save-values))
                    :class "form-control"}
                      (for [currency (sorted-iso4217)]
                        (let [symbol (:symbol currency)
                              display-symbol (or symbol (:code currency))
                              label (str (:text currency) " " display-symbol)]
                          (om/build option {:value (:code currency) :text label})))))
                (dom/p {:class "help-block"} "Currency business uses for its finances"))

              ;; Locations
              (dom/div {:class "form-group"}
                (dom/label {:for "location" :class "col-sm-2 control-label"} "Location(s)")
                (dom/div {:class "col-sm-3"}
                  (dom/input {
                    :type "text"
                    :id "location"
                    :value (:location data)
                    :class "form-control"
                    :on-change #(change-value data % :location)
                    :on-blur #(save-values)
                    :placeholder "eg. Paris, France"})
                  (dom/a {:on-click #(.preventDefault %)}
                    (dom/i {:class "fa fa-plus"})
                    " add location"))
                (dom/p {:class "help-block"} "Each company location"))

              ;; Tags
              (dom/div {:class "form-group"}
                (dom/label {:for "tags" :class "col-sm-2 control-label"} "Descriptive tag(s)")
                (dom/div {:class "col-sm-6"}
                  (dom/input {
                    :type "text"
                    :id "tags"
                    :value (:tags data)
                    :class "form-control"
                    :on-change #(change-value data % :tags)
                    :on-blur #(save-values)
                    :placeholder "eg. B2B, mobile, finance, internet of things, security, blockchain"}))
                (dom/p {:class "help-block"} ""))

              ;; Get in touch
              (dom/div {:class "form-group"}
                (dom/label {:for "email" :class "col-sm-2 control-label"} "Get in touch")
                (dom/div {:class "col-sm-3"}
                  (dom/input {
                    :type "text"
                    :id "email"
                    :value (:email data)
                    :class "form-control"
                    :on-change #(change-value data % :email)
                    :on-blur #(save-values)
                    :placeholder "eg. hello@startup.com"}))
                (dom/p {:class "help-block"} "Best way to contact the company (email, URL, phone number, etc.)")))))))))


(defcomponent mission [data owner]
  (render [_]
    (p/panel {:header (dom/h3 "Mission")}
      (dom/div {:class "panel-body"}
        (dom/div {:class "row"}
          (dom/form {:class "form-horizontal"}

            ;; Description
            (dom/div {:class "form-group"}
              (dom/label {:for "description" :class "col-sm-2 control-label"} "Brief business description")
              (dom/div {:class "col-sm-9"}
                (dom/textarea {
                  :class "form-control"
                  :id "description"
                  :rows "5"
                  :value (:description data)
                  :on-change #(change-value data % :description)
                  :on-blur #(save-values)
                  :placeholder "Explain your business, what market do you serve, what problems do you solve, who are your customers, why do they like you, what is distinct about your approach?"})))

            ;; Mission
            (dom/div {:class "form-group"}
              (dom/label {:for "mission" :class "col-sm-2 control-label"} "Why founded?")
              (dom/div {:class "col-sm-9"}
                (dom/textarea {
                  :class "form-control"
                  :id "mission"
                  :rows "5"
                  :value (:mission data)
                  :on-change #(change-value data % :mission)
                  :on-blur #(save-values)
                  :placeholder "Why did you start this particular business? Why are you the best founding team to start it?"})))))))))

(defcomponent on-the-web [data owner]
  (render [_]
    (let [remaining-web-links (apply dissoc (:web data) [:company :about :twitter :facebook :linkedin :github :angellist])]
      (p/panel {:header (dom/h3 "On the Web")}
        (dom/div {:class "panel-body"}
          (dom/div {:class "row"}
            (dom/form {:class "form-horizontal"}

              ;; Web page
              (dom/div {:class "form-group"}
                (dom/label {:for "company" :class "col-sm-2 control-label"} "Web page")
                (dom/div {:class "col-sm-4"}
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "company"
                    :value (:company (:web data))
                    :on-change #(change-value data % [:web :company])
                    :on-blur #(save-values)
                    :placeholder "http://startup.com/"}))
                (dom/span {:class "help-block"} ""))

              ;; About page
              (dom/div {:class "form-group"}
                (dom/label {:for "about" :class "col-sm-2 control-label"} "About page")
                (dom/div {:class "col-sm-4"}
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "about"
                    :value (:about (:web data))
                    :on-change #(change-value data % [:web :about])
                    :on-blur #(save-values)
                    :placeholder "http://startup.com/about"}))
                (dom/p {:class "help-block"} "Optional"))

              ;; Twitter
              (dom/div {:class "form-group"}
                (dom/label {:for "twitter" :class "col-sm-2 control-label"} "Company Twitter handle")
                (dom/div {:class "input-group col-sm-4"}
                  (dom/div {:class "input-group-addon"} "@")
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "twitter"
                    :on-change #(change-value data % [:web :twitter])
                    :on-blur #(save-values)
                    :value (:twitter (:web data))}))
                (dom/p {:class "help-block"} "Optional"))

              ;; Facebook
              (dom/div {:class "form-group"}
                (dom/label {:for "facebook" :class "col-sm-2 control-label"} "Facebook company page")
                (dom/div {:class "input-group col-sm-4"}
                  (dom/div {:class "input-group-addon"} "www.facebook.com/")
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "facebook"
                    :on-change #(change-value data % [:web :facebook])
                    :on-blur #(save-values)
                    :value (:facebook (:web data))}))
                (dom/p {:class "help-block"} "Optional"))

              ;; LinkedIn
              (dom/div {:class "form-group"}
                (dom/label {:for "linkedin" :class "col-sm-2 control-label"} "LinkedIn company page")
                (dom/div {:class "input-group col-sm-4"}
                  (dom/div {:class "input-group-addon"} "www.linkedin.com/company/")
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "linkedin"
                    :on-change #(change-value data % [:web :linkedin])
                    :on-blur #(save-values)
                    :value (:linkedin (:web data))}))
                (dom/p {:class "help-block"} "Optional"))

              ;; GitHub
              (dom/div {:class "form-group"}
                (dom/label {:for "github" :class "col-sm-2 control-label"} "GitHub organization")
                (dom/div {:class "input-group col-sm-4"}
                  (dom/div {:class "input-group-addon"} "www.github.com/")
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "github"
                    :on-change #(change-value data % [:web :github])
                    :on-blur #(save-values)
                    :value (:github (:web data))}))
                (dom/p {:class "help-block"} "Optional"))

              ;; AngelList
              (dom/div {:class "form-group"}
                (dom/label {:for "angellist" :class "col-sm-2 control-label"} "AngelList page")
                (dom/div {:class "input-group col-sm-4"}
                  (dom/div {:class "input-group-addon"} "angel.co/")
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :id "angellist"
                    :on-change #(change-value data % [:web :angellist])
                    :on-blur #(save-values)
                    :value (:angellist (:web data))}))
                (dom/p {:class "help-block"} "Optional"))

              ;; Remaining custom keys
              (for [[key value] remaining-web-links]
                (dom/div {:class "form-group"}
                  (dom/label {:for key :class "col-sm-2 control-label"} (name key))
                  (dom/div {:class "input-group col-sm-4"}
                    (dom/input {
                      :type "text"
                      :class "form-control"
                      :id (name key)
                      :on-change #(change-value data % [:web key])
                      :on-blur #(save-values)
                      :value value})
                    (dom/a {:on-click (fn [e]
                                        (.preventDefault e)
                                        (remove-web-link data key))}
                      (dom/i {:class "fa fa-remove"})
                      " remove URL"))
                  (dom/p {:class "help-block"} "Optional")))

              ;; Other
              (dom/div {:class "form-group"}
                (dom/div {:class "input-group col-sm-1 col-sm-offset-1"}
                  (dom/input #js {
                    :type "text"
                    :className "form-control"
                    :id "new-web-label"
                    :ref "new-web-label"
                    :placeholder "Other"}))
                  (dom/div {:class "col-sm-4"}
                    (dom/input #js {
                      :type "text"
                      :className "form-control"
                      :id "new-web-url"
                      :ref "new-web-url"
                      :placeholder "URL"})
                    (dom/a {
                      :on-click (fn [e]
                                  (.preventDefault e)
                                  (save-new-web-link owner data))}
                      (dom/i {:class "fa fa-plus"})
                      " add URL"))))))))))
