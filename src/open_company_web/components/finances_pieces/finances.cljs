(ns open-company-web.components.finances-pieces.finances
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.finances-pieces.cash :refer (cash)]
            [open-company-web.components.finances-pieces.revenue :refer (revenue)]
            [open-company-web.components.finances-pieces.costs :refer (costs)]
            [open-company-web.components.finances-pieces.burn-rate :refer (burn-rate)]
            [open-company-web.components.finances-pieces.runway :refer (runway)]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.cell :refer [cell]]))

(defcomponent finances [data owner]
  (init-state [_]
    {:focus (:tab @router/path)
     :hover false})
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          slug (:slug @router/path)
          company-data ((keyword slug) data)
          finances-data (:finances company-data)
          cash-classes (str classes (when (= focus "cash") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          burn-rate-classes (str classes (when (= focus "burn-rate") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          partial-link (str "/companies/" slug "/finances/")]
      (dom/div {:class "row"}
        (dom/div {:class "finances"}
          (dom/h2 {} "Finances")
          (dom/div {:class "link-bar"}
            (om/build link {:class cash-classes :href (str partial-link "cash") :name "Cash"})
            (om/build link {:class revenue-classes :href (str partial-link "revenue") :name "Revenue"})
            (om/build link {:class costs-classes :href (str partial-link "costs") :name "Costs"})
            (om/build link {:class burn-rate-classes :href (str partial-link "burn-rate") :name "Burn Rate"})
            (om/build link {:class runway-classes :href (str partial-link "runway") :name "Runway"}))
          (dom/div {:class (utils/class-set {:finances-body true :editable (om/get-state owner :hover)})
                    :on-mouse-over #(om/update-state! owner :hover (fn [_] true))
                    :on-mouse-out #(om/update-state! owner :hover (fn [_] false))}
            (case focus

              "cash"
              (om/build cash company-data)

              "revenue"
              (om/build revenue company-data)

              "costs"
              (om/build costs company-data)

              "burn-rate"
              (om/build burn-rate company-data)

              "runway"
              (om/build runway company-data))
            (om/build update-footer {:updated-at (:updated-at finances-data)
                                     :author (:author finances-data)})
            (om/build rich-editor (:commentary finances-data))))))))

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
    (let [slug (:slug @router/path)
          finances-data (:finances ((keyword slug) data))
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:finances data)))
          rows-data (map #(merge {:prefix cur-symbol} %) (:data finances-data))
          cur-period (utils/current-period)]
      (if (:loading data)
        ; loading
        (dom/h4 {} "Loading data...")
        ; real component
        (dom/div {:class "row"}
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
                                          (router/nav! (str "/companies/" slug "/finances/cash")))} "Cancel")))))))))
  