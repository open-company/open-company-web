(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.footer :refer (footer)]
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
    (dom/div {:class "company-editor"}
      (dom/div {:class (str "group fullscreen-page "  (if (jwt/jwt) "with-small-footer" "with-footer"))}
        (dom/div {:class "col-md-7 col-md-offset-2 p0"}
          (dom/h2 {:class "domine mb3 ml3"} "Company Setup"))
        (dom/div {:class "col-md-7 col-md-offset-2 bg-gray p3 group"}
          (dom/form {:on-submit (partial create-company-clicked owner)}
            (dom/div {:class "form-group"}
              (dom/label {:class "small-caps h6 bold block"} "COMPANY NAME")
              (dom/input {:type "text"
                          :class "domine h4 p2 bg-white-05 md-col-9 border-none"
                          :style #js {:width "100%"}
                          :placeholder "Simple name without the Inc., LLC, etc."
                          :value (-> data :company-editor :name)
                          :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])})))
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click (partial create-company-clicked owner)}
                        (when loading
                          (loading/small-loading {:class "left mt1"}))
                        (dom/label {:class (str "mt1" (when loading " ml2"))} "NEXT →"))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer {:footer-width (responsive/total-layout-width-int card-width columns-num)})))))