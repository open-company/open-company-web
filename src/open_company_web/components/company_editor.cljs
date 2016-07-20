(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

(defn create-company-clicked [owner e]
  (utils/event-stop e)
  (when-not (om/get-state owner :loading)
    (let [data         (om/get-props owner)
          company-name (-> data :company-editor :name)]
      (if (clojure.string/blank? company-name)
        (js/alert "Please insert a company name")
        (do
          (om/set-state! owner :loading true)
          (dis/dispatch! [:company-submit]))))))

(defcomponent company-editor [data owner]

  (init-state [_]
    {:loading false})

  (did-mount [_]
    (utils/update-page-title "OpenCompany - Setup Your Company")
    (when-not (-> data :company-editor :name)
      ;; using utils/after here because we can't dispatch inside another dispatch.
      ;; ultimately we should switch to some event-loop impl that works like a proper queue
      ;; and does not have these limitations
      (utils/after 1 #(dis/dispatch! [:input [:company-editor :name] (jwt/get-key :org-name)]))))

  (render-state [_ {:keys [loading]}]
    (dom/div {:class "company-editor"
              :style {:border-top (str "4px solid " (occ/get-color-by-kw :yellow))}}
      (dom/div {:class "col-12 p3"}
        (dom/button {:on-click #(dis/dispatch! [:logout])
                     :class "btn-reset btn-outline"}
          "Back to Opencompany.com"))
      (dom/div {:class "container"}
        (dom/div {:class "col-md-7 col-md-offset-2 p0"}
          (dom/h2 {:class "domine mb3"} "Get Started"))
        (dom/div {:class "col-md-7 col-md-offset-2 bg-gray p3"}
          (dom/form {:on-submit (partial create-company-clicked owner)}
            (dom/div {:class "form-group"}
              (dom/label {:class "small-caps h6 bold block"} "Company Name")
              (dom/input {:type "text"
                          :class "domine h4 p2 bg-white-05 md-col-9 border-none"
                          :placeholder "Simple name without the Inc., LLC, etc."
                          :value (-> data :company-editor :name)
                          :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])}))

            ;; Slug preview/input
            #_(dom/div {:class "md-col-9 py2 overflow-scroll"}
                (dom/label {:class "caps h6 bold block"} "Slug")
                (dom/span {:class "domine h4 pl2 py2 bg-white-05"} "opencompany.com/")
                (dom/input {:type "text"
                            :class "domine h4 pr2 py2 bg-white-05 border-none"
                            :placeholder "..."
                            :value (or (-> data :company-editor :slug) (-> data :company-editor :name))
                            :on-change #(dis/dispatch! [:input [:company-editor :slug] (.. % -target -value)])})))
          ;; Description
          #_(dom/div {:class "form-group"}
              (dom/label {:class "col"} "Description")
              (dom/input {:type "text"
                          :class "form-control"
                          :placeholder "Simple company description or tagline (e.g. 'A messaging app for teams')"
                          :value (-> data :company-editor :description)
                          :on-change #(dis/dispatch! [:input [:company-editor :description] (.. % -target -value)])}))
            (dom/button {:class "btn-reset btn-solid"
                         :on-click (partial create-company-clicked owner)}
                        (when loading
                          (loading/small-loading {:class "left mt1"}))
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "Setup Your Company")))))))
