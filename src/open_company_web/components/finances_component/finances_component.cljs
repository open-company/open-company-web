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
            [open-company-web.lib.utils :as utils]))

(defcomponent finances [data owner]
  (init-state [_]
    {:focus (:tab @router/path)})
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          finances-data (:finances data)
          cash-classes (str classes (when (= focus "cash") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          burn-rate-classes (str classes (when (= focus "burn-rate") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          author (:author (:oc:finances finances-data))
          updated-at (:updated-at (:oc:finances finances-data))]
      (if (:loading data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "finances"}
          (dom/h2 {} "Finances")
          (dom/div {:class "link-bar"}
            (om/build link {:class cash-classes :href "/finances/cash" :name "Cash"})
            (om/build link {:class revenue-classes :href "/finances/revenue" :name "Revenue"})
            (om/build link {:class costs-classes :href "/finances/costs" :name "Costs"})
            (om/build link {:class burn-rate-classes :href "/finances/burn-rate" :name "Burn Rate"})
            (om/build link {:class runway-classes :href "/finances/runway" :name "Runway"}))
          (dom/div {:class "finances-body"}
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
            (dom/div {:class "author"}
              (dom/p {:class "timeago"} (utils/time-since updated-at))
              (dom/img {:src (:image author) :alt (:name author) :class "author-image"}))))))))

