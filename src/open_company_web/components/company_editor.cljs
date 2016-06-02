(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

(defcomponent company-editor [data owner]
  (did-mount [_]
    (utils/update-page-title "OpenCompany - Setup Your Company")
    (swap! dis/app-state assoc-in [:company-editor :name] (jwt/get-key :org-name)))
  (render [_]
    (dom/div {:class "company-editor"}
      (dom/div {:class "col-12 p3"}
        (dom/a {:href "/" :class "btn-reset btn-outline"} "Back to Opencompany.com"))
      (dom/div {:class "container"}
        (dom/div {:class "col-md-7 col-md-offset-2 p0"}
          (dom/h2 {:class "domine mb3"} "Get Started"))
        (dom/div {:class "col-md-7 col-md-offset-2 bg-gray p3"}
          (dom/form {}
            (dom/div {:class "form-group"}
              (dom/label {:class "caps h6 bold block"} "Company Name")
              (dom/input {:type "text"
                          :class "domine h4 p2 bg-white-05 md-col-9 border-none"
                          :placeholder "Simple name without the Inc., LLC, etc."
                          :value (-> data :company-editor :name)
                          :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])})

              ;; Slug preview/input
              #_(dom/div {:class "md-col-9 py2 overflow-scroll"}
                  (dom/label {:class "caps h6 bold block"} "Slug")
                  (dom/span {:class "domine h4 pl2 py2 bg-white-05"} "opencompany.com/")
                  (dom/input {:type "text"
                              :class "domine h4 pr2 py2 bg-white-05 border-none"
                              :placeholder "..."
                              :value (or (-> data :company-editor :slug) (-> data :company-editor :name))
                              :on-change #(dis/dispatch! [:input [:company-editor :slug] (.. % -target -value)])})))
            (dom/div {:class "right-align pt3"}
              (dom/button {:class "btn-reset btn-solid"
                           :on-click #(do (.preventDefault %) (dis/dispatch! [:company-submit]))}
                "Setup Your Company"))))))))