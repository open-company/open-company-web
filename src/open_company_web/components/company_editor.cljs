(ns open-company-web.components.company-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.navbar :refer (navbar)]
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
    (dom/div {:class "company-editor"}
      (dom/div {:class "fullscreen-page group"}
        (om/build navbar {:hide-right-menu true :show-navigation-bar true})
        (dom/div {:class "company-editor-box group navbar-offset"}
          (dom/form {:on-submit (partial create-company-clicked owner)}
            (dom/div {:class "form-group"}
              (dom/label {:class "company-editor-message"} "Welcome!")
              (dom/label {:class "company-editor-message"} "What's the name of your company?")
              (dom/input {:type "text"
                          :class "company-editor-input domine h4"
                          :style #js {:width "100%"}
                          :placeholder "Simple name without the Inc., LLC, etc."
                          :value (-> data :company-editor :name)
                          :on-change #(dis/dispatch! [:input [:company-editor :name] (.. % -target -value)])})))
            (dom/div {:class "center"}
              (dom/button {:class "btn-reset btn-solid get-started-button"
                           :on-click (partial create-company-clicked owner)}
                          (when loading
                            (loading/small-loading {:class "left mt1"}))
                          (dom/label {:class (str "pointer mt1" (when loading " ml2"))} "GET STARTED"))))))))