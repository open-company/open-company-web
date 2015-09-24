(ns open-company-web.components.finances-component.finances-component
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.finances-component.cash :refer (cash)]
            [open-company-web.components.finances-component.revenue :refer (revenue)]
            [open-company-web.components.finances-component.costs :refer (costs)]
            [open-company-web.components.finances-component.burn-rate :refer (burn-rate)]
            [open-company-web.components.finances-component.runway :refer (runway)]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer [cell]]))

(defcomponent finances [data owner]
  (init-state [_]
    {:focus (:tab @router/path)
     :hover false})
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          finances-data (:finances data)
          cash-classes (str classes (when (= focus "cash") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          burn-rate-classes (str classes (when (= focus "burn-rate") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))]
      (if (:loading data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "row"}
          (om/build navbar finances-data)
          (dom/div {:class "finances"}
            (dom/h2 {} "Finances")
            (dom/div {:class "link-bar"}
              (om/build link {:class cash-classes :href "/finances/cash" :name "Cash"})
              (om/build link {:class revenue-classes :href "/finances/revenue" :name "Revenue"})
              (om/build link {:class costs-classes :href "/finances/costs" :name "Costs"})
              (om/build link {:class burn-rate-classes :href "/finances/burn-rate" :name "Burn Rate"})
              (om/build link {:class runway-classes :href "/finances/runway" :name "Runway"}))
            (dom/div {:class (utils/class-set {:finances-body true :editable (om/get-state owner :hover)})
                      :on-mouse-over #(om/update-state! owner :hover (fn [_] true))
                      :on-mouse-out #(om/update-state! owner :hover (fn [_] false))}
              (case focus

                "cash"
                (om/build cash finances-data)

                "revenue"
                (om/build revenue finances-data)

                "costs"
                (om/build costs finances-data)

                "burn-rate"
                (om/build burn-rate finances-data)

                "runway"
                (om/build runway (:finances data)))
              (om/build update-footer {:updated-at (:updated-at (:oc:finances finances-data))
                                       :author (:author (:oc:finances finances-data))})
              (om/build rich-editor (:commentary (:oc:finances finances-data))))))))))

(defcomponent finances-edit-row [data owner]
  (render [_]
    (let [prefix (:prefix data)
          is-new (:new data)]
      (dom/tr {}
        (dom/td {:class "no-cell"} (utils/period-string (:period data) (when is-new :force-year)))
        (dom/td {}
          (om/build cell {:value (:cash data)
                          :placeholder (if is-new "at month end" "")
                          :prefix prefix
                          :cell-state (if is-new :new :display)}))
        (dom/td {}
          (om/build cell {:value (:revenue data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state (if is-new :new :display)}))
        (dom/td {}
          (om/build cell {:value (:costs data)
                          :placeholder (if is-new "entire month" "")
                          :prefix prefix
                          :cell-state (if is-new :new :display)}))
        (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
                (if is-new "calculated" (:burn-rate data)))
        (dom/td {:class (utils/class-set {:no-cell true :new-row-placeholder is-new})}
                (if is-new "calculated" (:runway data)))))))

(defcomponent finances-edit [data owner]
  (render [_]
    (let [finances-data (:oc:finances (:finances data))
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:finances data)))
          rows-data (map #(merge {:prefix cur-symbol} %) (:data finances-data))
          cur-period (utils/current-period)]
      (if (:loading data)
        ; loading
        (dom/h4 {} "Loading data...")
        ; real component
        (dom/div {:class "row"}
          (om/build navbar (:finances data))
          (dom/div {:class "finances"}
            (dom/h2 {} "Finances")
            (dom/div {:class "finances-body edit"}
              (dom/table {:class "table table-striped"}
                (dom/thead {}
                  (dom/tr {}
                    (dom/th {} "")
                    (dom/th {} "Cash")
                    (dom/th {} "Revenue")
                    (dom/th {} "Costs")
                    (dom/th {} "Burn")
                    (dom/th {} "Runway")))
                (dom/tbody {}
                  (when-not (utils/period-exists cur-period (:data finances-data))
                    (om/build finances-edit-row {:prefix cur-symbol
                                                 :period cur-period
                                                 :new true}))
                  (om/build-all finances-edit-row rows-data)))
              (dom/div {:class "finances-edit-buttons"}
                (dom/button {:class "btn btn-success"
                             :on-click #(println "Save with api!")} "Save")
                (dom/button {:class "btn btn-default cancel"
                             :on-click #(do
                                          (-> % .preventDefault)
                                          (router/nav! "/finances/cash"))} "Cancel")))))))))