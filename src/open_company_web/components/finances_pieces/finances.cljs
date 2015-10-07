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
            [open-company-web.components.cell :refer [cell]]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab))))

(defcomponent finances [data owner]
  (init-state [_]
    {:focus "cash"
     :hover false
     :read-only (:read-only data)})
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          slug (:slug @router/path)
          company-data (:company-data data)
          finances-data (:finances company-data)
          commentary-data (:commentary finances-data)
          cash-classes (str classes (when (= focus "cash") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          burn-rate-classes (str classes (when (= focus "burn-rate") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          read-only (:read-only data)
          subsection-data {:company-data company-data
                           :read-only read-only
                           :editable-click-callback (:editable-click-callback data)}]
      (dom/div {:class "row"}
        (dom/div {:class "finances"}
          (dom/h2 {:class (utils/class-set {:finances-title true
                                            :linked (om/get-state owner :read-only)})
                   :on-click #(when (om/get-state owner :read-only)
                                (-> % .preventDefault)
                                (router/nav! (str "/companies/" slug "/finances")))}
                  "Finances")
          (dom/div {:class "link-bar"}
            (dom/a {:href "#"
                    :class cash-classes
                    :title "Cash"
                    :data-tab "cash"
                    :on-click #(subsection-click % owner)} "Cash")
            (dom/a {:href "#"
                    :class revenue-classes
                    :title "Revenue"
                    :data-tab "revenue"
                    :on-click #(subsection-click % owner)} "Revenue")
            (dom/a {:href "#"
                    :class costs-classes
                    :title "Costs"
                    :data-tab "costs"
                    :on-click #(subsection-click % owner)} "Costs")
            (dom/a {:href "#"
                    :class burn-rate-classes
                    :title "Burn rate"
                    :data-tab "burn-rate"
                    :on-click #(subsection-click % owner)} "Burn rate")
            (dom/a {:href "#"
                    :class runway-classes
                    :title "Runway"
                    :data-tab "runway"
                    :on-click #(subsection-click % owner)} "Runway"))
          (dom/div {:class (utils/class-set {:finances-body true
                                             :editable (and (not (om/get-state owner :read-only))
                                                            (om/get-state owner :hover))})
                    :on-mouse-over #(om/update-state! owner :hover (fn [_] true))
                    :on-mouse-out #(om/update-state! owner :hover (fn [_] false))}
            (case focus

              "cash"
              (om/build cash subsection-data)

              "revenue"
              (om/build revenue subsection-data)

              "costs"
              (om/build costs subsection-data)

              "burn-rate"
              (om/build burn-rate subsection-data)

              "runway"
              (om/build runway company-data))
            (om/build update-footer {:updated-at (:updated-at finances-data)
                                     :author (:author finances-data)
                                     :section :finances})
            (om/build rich-editor {:read-only read-only
                                   :section-data commentary-data
                                   :section :finances})
            (om/build revisions-navigator {:revisions (:revisions finances-data)
                                           :actual-revision (utils/link-for (:links finances-data) "self" "GET")
                                           :section :finances})))))))

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
          company-data (:company-data data)
          finances-data (:finances company-data)
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
                             :on-click (:cancel-edit-callback data)} "Cancel")))))))))
  