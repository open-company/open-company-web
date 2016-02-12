(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

(defcomponent company-editor [data owner]
  (render [_]
    (utils/update-page-title "OPENcompany - Create your company")
    (dom/div {:class "company-editor container"}
      (om/build navbar data)
      (dom/div {:class "container-fluid"}
        (dom/div {:class "col-md-6 col-md-offset-3 main"}
          (dom/form {:style {:margin-top "3em"}}
            (dom/div {:class "form-group"}
              (dom/label "Company Name")
              (dom/input {:type "text"
                          :class "form-control"
                          :placeholder "e.g. Apple"
                          :value (-> data :company-editor :name)
                          :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])}))
            (dom/div {:class "form-group"}
              (dom/label {:class "col"} "Tagline")
              (dom/input {:type "text"
                          :class "form-control"
                          :placeholder "e.g. Think different."
                          :value (-> data :company-editor :description)
                          :on-change #(dis/dispatch! [:input [:company-editor :description] (.. % -target -value)])}))
            (dom/button {:class "btn btn-primary"
                         :on-click #(do (.preventDefault %) (dis/dispatch! [:company-submit]))}
                        "Create Company")))))))