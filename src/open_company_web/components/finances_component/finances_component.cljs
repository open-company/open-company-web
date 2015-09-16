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
            [open-company-web.components.finances-component.runaway :refer (runaway)]))

(defcomponent finances [data owner]
  (init-state [_]
    {:focus (:tab @router/path)})
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          cash-classes (str classes (when (= focus "cash") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          burn-rate-classes (str classes (when (= focus "burn-rate") " active"))
          runaway-classes (str classes (when (= focus "runaway") " active"))]
      (if (:loading data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "finances"}
          (dom/h2 {} "Finances")
          (dom/div {:class "link-bar"}
            (om/build link {:class cash-classes :href "/finances/cash" :name "Cash"})
            (om/build link {:class revenue-classes :href "/finances/revenue" :name "Revenue"})
            (om/build link {:class costs-classes :href "/finances/costs" :name "Costs"})
            (om/build link {:class burn-rate-classes :href "/finances/burn-rate" :name "Burn Rate"})
            (om/build link {:class runaway-classes :href "/finances/runaway" :name "Runaway"}))
          (dom/div {:class "finances-body"}
            (case focus
              
              "cash"
              (om/build cash (:finances data))
              
              "revenue"
              (om/build revenue data)
              
              "costs"
              (om/build costs data)
              
              "burn-rate"
              (om/build burn-rate data)
              
              "runaway"
              (om/build runaway data))))))))